package com.belina.registration.ui.registration;

import android.content.Context;
import android.databinding.ObservableField;

import com.belina.registration.model.EmailChecker;
import com.belina.registration.model.Field;
import com.belina.registration.ui.base.viewmodel.ObservableLiveData;
import com.belina.registration.ui.base.viewmodel.StoredViewModel;
import com.belina.registration.ui.base.viewmodel.ViewModelBase;

public class RegistrationViewModel extends ViewModelBase implements StoredViewModel {
    private Context context; // application context

    public ObservableLiveData<Field>  emailField = new ObservableLiveData<>(new Field());

    public ObservableField<String> password = new ObservableField<>();
    public ObservableField<String> message = new ObservableField<>();

    public EmailChecker emailChecker;


    public void onSendButtonClicked(){

    }

    public RegistrationViewModel(Context context){
        this.context = context.getApplicationContext();
        emailField.getValue().setFieldChecker(new EmailChecker());

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
