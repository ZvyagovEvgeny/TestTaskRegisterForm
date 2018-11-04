package com.belina.registration.ui.registration;

import android.content.Context;
import android.databinding.ObservableField;

import com.belina.registration.dagger.component.ApplicationComponent;
import com.belina.registration.R;
import com.belina.registration.model.EmailStatus;
import com.belina.registration.model.EmailCheckerV2;
import com.belina.registration.model.EmailValidationResult;
import com.belina.registration.model.ErrorType;
import com.belina.registration.model.Field;
import com.belina.registration.model.PasswordCheckResult;
import com.belina.registration.model.PasswordChecker;
import com.belina.registration.ui.base.viewmodel.ObservableLiveData;
import com.belina.registration.ui.base.viewmodel.StoredViewModel;
import com.belina.registration.ui.base.viewmodel.ViewModelBase;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class RegistrationViewModel extends ViewModelBase implements StoredViewModel {
    private Context context; // application context

    private final int MIN_PASSWORD_LENGTH = 5;
    private final int MAX_PASSWORD_LENGTH = 10;

    public ObservableLiveData<Field>  emailField = new ObservableLiveData<>(new Field());
    public ObservableLiveData<Field> passwordField = new ObservableLiveData<>(new Field());

    public ObservableField<String> message = new ObservableField<>();

    public PublishSubject<Message> messagePublishSubject = PublishSubject.create();

    public Observable<Message> getMessagePublishSubject() {
        return messagePublishSubject;
    }

    private EmailCheckerV2 emailChecker;
    private PasswordChecker passwordChecker = new PasswordChecker(
            MIN_PASSWORD_LENGTH,
            MAX_PASSWORD_LENGTH);


    private List<String> allowedDomainNames;

    public RegistrationViewModel(Context context, ApplicationComponent applicationComponent){

        this.context = context.getApplicationContext();
        allowedDomainNames = new ArrayList<>( applicationComponent.getConstants().getDomains());
        emailChecker = new EmailCheckerV2(allowedDomainNames);

    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void onSendButtonClicked(){

        if(!checkPassword(true))return;

        if(checkEmail(true)){
            EmailValidationResult checkResult = emailChecker.check(emailField.getValue().string.get());
            if(checkResult.emailStatus == EmailStatus.UNKNOWN_DOMAIN_NAME){
                messagePublishSubject.onNext(
                        new Message(getStringRecource(R.string.check_domain_name), emailField.getValue().string.get(),
                                getStringRecource(R.string.registration),getStringRecource(R.string.cancel),
                        this::sendUserData));
            }else{
                sendUserData();
            }
        }
    }


    private void sendUserData(){
        Timber.d("Send");
    }

    public boolean checkEmail(boolean printErrorIfFieldIsEmpty){

        String userEmail = emailField.getValue().string.get();
        ObservableField<String> errorField = emailField.getValue().errorMessage;

        EmailValidationResult validationResult =  emailChecker.check(userEmail);

        printEmailValidationResult(validationResult,errorField,userEmail,printErrorIfFieldIsEmpty);

        if(validationResult.emailStatus == EmailStatus.UNKNOWN_DOMAIN_NAME){
            emailField.getValue().errorType.set(ErrorType.WARNING);
            return true;
        }

        if(validationResult.emailStatus==EmailStatus.OK)
            return true;

        emailField.getValue().errorType.set(ErrorType.ERROR);

        return false;
    }

    public boolean checkPassword(boolean printErrorIfFieldIsEmpty){
        String password = passwordField.getValue().string.get();

        PasswordCheckResult passwordCheckResult = passwordChecker.check(password);
        printPasswordError(passwordField.getValue(),passwordCheckResult,printErrorIfFieldIsEmpty);

        return passwordCheckResult == PasswordCheckResult.OK;
    }

    private void printPasswordError(Field password, PasswordCheckResult checkResult,
                                    boolean printErrorIfFieldIsEmpty){

        switch (checkResult){
            case OK:
                password.errorMessage.set("");
                break;
            case EMPTY:
                if(printErrorIfFieldIsEmpty)
                    password.errorMessage.set(getStringRecource(R.string.this_field_is_required));
                else
                    password.errorMessage.set("");
                break;
            case PASSWORD_IS_LONG:
                password.errorMessage.set(getPasswordsRestrictions());
                break;
            case PASSWORD_IS_SHORT:
                password.errorMessage.set(getPasswordsRestrictions());
                break;
        }

    }

    private String getPasswordsRestrictions(){
        Object [] objects = new Object[]{MIN_PASSWORD_LENGTH,MAX_PASSWORD_LENGTH};
        String msg =  MessageFormat.format(getStringRecource(R.string.password_length_restrictions),objects);
        return msg;
    }

    private void printEmailValidationResult(EmailValidationResult validationResult, ObservableField<String> errorField,
                                            String email, boolean printErrorIfFieldIsEmpty){
        switch (validationResult.emailStatus){
            case OK:
                errorField.set("");
                break;
            case EMPTY:
                if(printErrorIfFieldIsEmpty)
                    errorField.set(getStringRecource(R.string.this_field_is_required));
                else{
                    errorField.set("");
                }
                break;
            case AT_SYMBOL_OCCURS_SEVERAL_TIMES:
                errorField.set(getStringRecource(R.string.symbol_at_occurs_more_than_once));
                break;
            case NO_AT_SYMBOL:
                errorField.set(getStringRecource(R.string.symbol_at_is_present));
                break;
            case NOT_VALID:
                if(validationResult.indexOfError >= email.length())
                {
                    errorField.set("Некорректный email");
                }
                else{
                    errorField.set(getStringRecource(R.string.incorrect_email_check_symbol)+
                            email.charAt(validationResult.indexOfError));
                }
                break;
            case UNKNOWN_DOMAIN_NAME:
                errorField.set("Неизвестное доменное имя");
                break;
                default:
                   break;

        }
    }


    private String getStringRecource(int id){
        return context.getResources().getString(id);
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

    private void onNetworkError(Throwable t){
        Timber.d(t);

    }
}
