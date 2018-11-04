package com.belina.registration.ui.registration;

import android.content.Context;
import android.databinding.ObservableField;

import com.belina.registration.dagger.component.ApplicationComponent;
import com.belina.registration.R;
import com.belina.registration.data.balinasoft.api.model.Account;
import com.belina.registration.data.balinasoft.api.model.AccountBuilder;
import com.belina.registration.data.balinasoft.api.model.BalinasoftResponce;
import com.belina.registration.data.balinasoft.api.model.BalingSoftResponseError;
import com.belina.registration.data.balinasoft.api.model.ErrorCode;
import com.belina.registration.data.balinasoft.api.model.StatusCode;
import com.belina.registration.data.balinasoft.api.model.Valid;
import com.belina.registration.model.email.EmailStatus;
import com.belina.registration.model.email.EmailChecker;
import com.belina.registration.model.email.EmailValidationResult;
import com.belina.registration.ui.base.editableField.ErrorType;
import com.belina.registration.ui.base.editableField.Field;
import com.belina.registration.model.password.PasswordCheckResult;
import com.belina.registration.model.password.PasswordChecker;
import com.belina.registration.ui.base.events.Message;
import com.belina.registration.ui.base.events.ProgressBarMessage;
import com.belina.registration.ui.base.viewmodel.ObservableLiveData;
import com.belina.registration.ui.base.viewmodel.StoredViewModel;
import com.belina.registration.ui.base.viewmodel.ViewModelBase;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import timber.log.Timber;

public class RegistrationViewModel extends ViewModelBase implements StoredViewModel {
    private Context context; // application context

    private final int MIN_PASSWORD_LENGTH = 8;
    private final int MAX_PASSWORD_LENGTH = 20;

    public ObservableLiveData<Field>  emailField = new ObservableLiveData<>(new Field());
    public ObservableLiveData<Field> passwordField = new ObservableLiveData<>(new Field());

    public ObservableField<String> message = new ObservableField<>();

    private BehaviorSubject<Message> messageSubject = BehaviorSubject.create().create();
    private BehaviorSubject<ProgressBarMessage> progressDialogSubject
            = BehaviorSubject.create();

    private ApplicationComponent applicationComponent;

    public Observable<Message> getMessageSubject() {
        return messageSubject;
    }
    public Observable<ProgressBarMessage> getProgressBarSubjet(){
        return progressDialogSubject;
    }

    private EmailChecker emailChecker;
    private PasswordChecker passwordChecker = new PasswordChecker(
            MIN_PASSWORD_LENGTH,
            MAX_PASSWORD_LENGTH);

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public RegistrationViewModel(Context context, ApplicationComponent applicationComponent){

        this.context = context.getApplicationContext();
        this.applicationComponent = applicationComponent;
        List<String> allowedDomainNames =
                new ArrayList<>( applicationComponent.getConstants().getDomains());
        emailChecker = new EmailChecker(allowedDomainNames);

    }

    public void onSendButtonClicked() {

        if (!checkPassword(true)) {
            checkEmail(true);
            return;
        }

        if(checkEmail(true)){
            EmailValidationResult checkResult =
                    emailChecker.check(emailField.getValue().string.get());
            if(checkResult.emailStatus == EmailStatus.UNKNOWN_DOMAIN_NAME){
                messageSubject.onNext(
                        new Message(
                                getStringRecource(R.string.check_domain_name),
                                emailField.getValue().string.get(),
                                getStringRecource(R.string.registration),
                                getStringRecource(R.string.cancel),
                                this::sendUserData,
                                null));
            }else{
                sendUserData();
            }
        }
    }

    private void sendUserData(){

        String email = emailField.getValue().string.get();
        String password = passwordField.getValue().string.get();

        String login = emailChecker.getEmailLokcalPart(email);
        login = login.toLowerCase();

        Timber.d("Send");
        Account account = new AccountBuilder()
                .setEmail(login)
                .setPassword(password)
                .createAccount();

        compositeDisposable.add(applicationComponent.balinaSoftApi().signup(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( this::onSignUp,this::onNetworkError));
        progressDialogSubject.onNext(
                new ProgressBarMessage(getStringRecource(R.string.registration_in_progress)));

    }

    private void onSignUp(BalinasoftResponce signUpResult){
        if(signUpResult.getStatus() == StatusCode.SUCCESS.getCode()){
            progressDialogSubject.onNext(new ProgressBarMessage(false));
            messageSubject.onNext(new Message(
                    getStringRecource(R.string.registration_short),
                    getStringRecource(R.string.registration_successful),
                    getStringRecource(R.string.OK),
                    null));
        }else {
            Timber.d("Status not 200:"+signUpResult.getStatus());
        }
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
        return  MessageFormat.format(
                getStringRecource(R.string.password_length_restrictions),
                objects);
    }

    private void printEmailValidationResult(EmailValidationResult validationResult,
                                            ObservableField<String> errorField,
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
                    errorField.set(getStringRecource(R.string.incorrect_email_in_end));
                }
                else{
                    errorField.set(getStringRecource(R.string.incorrect_email_check_symbol)+
                            email.charAt(validationResult.indexOfError));
                }
                break;
            case UNKNOWN_DOMAIN_NAME:
                errorField.set(getStringRecource(R.string.unknown_domain_name));
                break;
                default:
                   break;

        }
    }


    private String getStringRecource(int id){
        return context.getResources().getString(id);
    }



    private void onNetworkError(Throwable t){
        Timber.d(t);
        progressDialogSubject.onNext(new ProgressBarMessage(false));
        if(t instanceof HttpException){

            HttpException httpException = (HttpException)t;

            try{
                String body =  httpException.response().errorBody().string();
                Gson gson = new Gson();
                BalingSoftResponseError responseError =
                        gson.fromJson(body,BalingSoftResponseError.class);

                if(responseError!=null){
                    if(responseError.getError().equals(ErrorCode.VALIDATION_ERROR.getError())){

                        Valid firstValid = responseError.getValid().get(0);
                        String message = firstValid.getField() + "\n"+ firstValid.getMessage();
                        showMessage(getStringRecource(R.string.validation_error),message);
                        return;
                    }
                    if(responseError.getError().equals(ErrorCode.USER_ALREADY_EXISTS.getError())){
                        showMessage(getStringRecource(R.string.registration_error),
                                getStringRecource(R.string.user_with_this_login_exists));
                        return;
                    }

                    showMessage(getStringRecource(R.string.registration_error),
                            getStringRecource(R.string.unknown_error));
                }

            }catch (IOException e){
                Timber.d(e);
            }
        }
        else {
            showMessage(getStringRecource(R.string.registration_error),
                    getStringRecource(R.string.unknown_error));
        }
    }

    private void showMessage(String title,String message){
        messageSubject.onNext(
                new Message(title,
                        message,
                        getStringRecource(R.string.OK),
                null));
    }

    @Override
    public void onActivityDetached() {

    }

    @Override
    public void onActivityDestroyed() {
        compositeDisposable.dispose();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onAttached(Context context) {

    }
}
