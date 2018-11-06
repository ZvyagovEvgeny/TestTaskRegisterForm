package com.belina.registration.ui.registration;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.belina.registration.R;
import com.belina.registration.application.App;
import com.belina.registration.dagger.component.ApplicationComponent;
import com.belina.registration.ui.base.activity.BaseActivity;
import com.belina.registration.databinding.ActivityRegistrationBinding;

import com.belina.registration.ui.base.adapter.CustomFilterAdapter;
import com.belina.registration.ui.base.events.Message;
import com.belina.registration.ui.base.events.ProgressBarMessage;
import com.belina.registration.ui.base.viewmodel.ViewModelFactory;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class RegistrationActivity extends BaseActivity<RegistrationViewModel> {

    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding databinding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected ViewModelFactory<RegistrationViewModel> getViewModelFactory() {
        return ()->new RegistrationViewModel(getContext(),App.create(this).getMoviesDBComponent());
    }

    @Override
    protected void onViewModelCreatedOrRestored(@NonNull RegistrationViewModel viewModel) {
        this.registrationViewModel = viewModel;
       initDataBinding();
    }

    private android.databinding.Observable.OnPropertyChangedCallback onMessageChangedCallback =
            new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                    showDialog(registrationViewModel.message.get());
                }
            };

    private android.databinding.Observable.OnPropertyChangedCallback onProgressBarChangedCallback =
            new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                    showProgressBar(registrationViewModel.progressBar.get());
                }
            };

    private void initDataBinding() {
       databinding = DataBindingUtil.setContentView(this,R.layout.activity_registration);
        databinding.setViewModel(registrationViewModel);

        CustomFilterAdapter customFilterAdapter = new CustomFilterAdapter(
                this,
                android.R.layout.simple_list_item_1,new ArrayList<>(
               registrationViewModel.getTrustedDomains()));

        databinding.emailTextView.setAdapter(customFilterAdapter);

        registrationViewModel.message.addOnPropertyChangedCallback(onMessageChangedCallback);
        registrationViewModel.progressBar.addOnPropertyChangedCallback(onProgressBarChangedCallback);
        showDialog(registrationViewModel.message.get());
        showProgressBar(registrationViewModel.progressBar.get());

        databinding.emailTextView.setOnFocusChangeListener(focusChangeListener);
        databinding.passwordTV.setOnFocusChangeListener(focusChangeListener);

        databinding.registerFormLayout.setListener(this::onSoftKeyboardShown);

    }

    private void showProgressBar(ProgressBarMessage progressBarMessage){
        if(progressBarMessage==null)return;
        if(progressDialog==null)
            progressDialog = new ProgressDialog(this,
                  R.style.AppCompatAlertDialogStyle);

        if(!progressBarMessage.isShow()){
            progressDialog.dismiss();
            return;
        }
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(progressBarMessage.getMessage());
        progressDialog.show();
        Timber.d("Before showing");
    }

    public void onSoftKeyboardShown(boolean isShowing) {
        if (isShowing) {
            databinding.logo.setVisibility(View.GONE);

        } else {
            databinding.logo.setVisibility(View.VISIBLE);
        }
    }

    private View.OnFocusChangeListener focusChangeListener =  new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            registrationViewModel.checkEmail(false);
            registrationViewModel.checkPassword(false);
        }
    };

    private void showDialog(Message message){
        if(message==null)return;
        if(!message.isShow()){return;}
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(this);
        }


        builder.setTitle(message.getTitle())
                .setMessage(message.getMessage())
                .setPositiveButton(message.getOkButton(), (dialog,which)-> {
                    message.setShow(false);
                    message.callOkCallback();
                })
                .setNegativeButton(message.getCancelButton(), (dialog,which)->{
                    message.setShow(false);
                    message.callCancelCallback();
                })
                .show();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        compositeDisposable.dispose();
        compositeDisposable = new CompositeDisposable();
        registrationViewModel.message.removeOnPropertyChangedCallback(onMessageChangedCallback);
        registrationViewModel.progressBar.addOnPropertyChangedCallback(onProgressBarChangedCallback);
    }
}
