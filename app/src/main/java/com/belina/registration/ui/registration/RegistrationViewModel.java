package com.belina.registration.ui.registration;

import android.content.Context;
import android.databinding.ObservableField;

import com.belina.registration.dagger.component.ApplicationComponent;
import com.belina.registration.data.api.IpApi;
import com.belina.registration.data.api.model.IpApiQueryResult;
import com.belina.registration.model.CheckResult;
import com.belina.registration.model.EmailChecker;
import com.belina.registration.model.EmailCheckerV2;
import com.belina.registration.model.Field;
import com.belina.registration.ui.base.viewmodel.ObservableLiveData;
import com.belina.registration.ui.base.viewmodel.StoredViewModel;
import com.belina.registration.ui.base.viewmodel.ViewModelBase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.belina.registration.data.api.IpApi.STATUS_SUCCESS;
import static io.reactivex.annotations.SchedulerSupport.IO;

public class RegistrationViewModel extends ViewModelBase implements StoredViewModel {
    private Context context; // application context

    public ObservableLiveData<Field>  emailField = new ObservableLiveData<>(new Field());

    public ObservableField<String> password = new ObservableField<>();
    public ObservableField<String> message = new ObservableField<>();

    public EmailCheckerV2 emailChecker;

    private ApplicationComponent applicationComponent;

    List<String> popularDomains;

    public RegistrationViewModel(Context context, ApplicationComponent applicationComponent){

        this.context = context.getApplicationContext();
        this.applicationComponent = applicationComponent;
        popularDomains = applicationComponent.getConstants().getDomains();
        emailChecker = new EmailCheckerV2(popularDomains);

    }


    private void onNetworkError(Throwable t){
        Timber.d(t);

    }
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void onSendButtonClicked(){


    }

    public Observable<IpApiQueryResult> checkDomain(String email){

        String domain = emailChecker.getEmailDomain(emailField.getValue().string.get());
        return applicationComponent.ipApi().checkDomainName(domain)
                .subscribeOn( Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void checkEmail(){

        String userEmail = emailField.getValue().string.get();

        CheckResult checkResult =  emailChecker.check(userEmail);

        switch (checkResult){
            case UNKNOWN_DOMAIN_NAME:
                String domainName = emailChecker.getEmailDomain(userEmail);
                Disposable d =  checkDomain(userEmail).subscribe(
                        (data)->{
                            if(data.getStatus().equals(IpApi.STATUS_SUCCESS)){
                                emailField.getValue().errorMessage.set("");
                                popularDomains.add(domainName);
                            }else{
                                emailField.getValue().errorMessage.set("домееное имя не существует");
                            }
                            },
                        this::onNetworkError);
                compositeDisposable.add(d);
                emailField.getValue().errorMessage.set("Проверка доменного имени...");
                break;
            case OK:
                emailField.getValue().errorMessage.set("");
                break;
            case EMPTY:
                emailField.getValue().errorMessage.set("");
                break;
            case NOT_VALID:
                emailField.getValue().errorMessage.set("Некорректный email");
                break;
        }
    }


    @Override
    public void onActivityDetached() {

    }

    @Override
    public void onActivityDestroyed() {

    }

    @Override
    public void onAttached(Context context) {

    }
}
