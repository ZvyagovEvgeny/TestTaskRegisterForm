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

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class RegistrationActivity extends BaseActivity<RegistrationViewModel> {

    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding databinding;
    private ApplicationComponent component;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component =  App.create(this).getMoviesDBComponent();
    }

    @NonNull
    @Override
    protected ViewModelFactory<RegistrationViewModel> getViewModelFactory() {

        return ()->new RegistrationViewModel(getContext(),App.create(this).getMoviesDBComponent());
    }

    @Override
    protected void onViewModelCreatedOrRestored(@NonNull RegistrationViewModel viewModel) {
        this.registrationViewModel = viewModel;


        component =  App.create(this).getMoviesDBComponent();

        compositeDisposable.add(registrationViewModel
                .getMessageSubject().subscribe(this::showDialog));
        compositeDisposable.add(registrationViewModel
                .getProgressBarSubjet().subscribe(this::showProgressBar));

        initDataBinding();
    }

    private void initDataBinding() {
        databinding = DataBindingUtil.setContentView(this,R.layout.activity_registration);
        databinding.setViewModel(registrationViewModel);

        CustomFilterAdapter customFilterAdapter = new CustomFilterAdapter(
                this,
                android.R.layout.simple_list_item_1,new ArrayList<>(
                component.getConstants().getDomains()));

        databinding.emailTextView.setAdapter(customFilterAdapter);
        databinding.emailTextView.setOnFocusChangeListener(focusChangeListener);
        databinding.passwordTV.setOnFocusChangeListener(focusChangeListener);



        databinding.registerFormLayout.setListener(this::onSoftKeyboardShown);

    }

    private void showProgressBar(ProgressBarMessage progressBarMessage){
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

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle(message.getTitle())
                .setMessage(message.getMessage())
                .setPositiveButton(message.getOkButton(), (dialog,which)->message.callOkCallback())
                .setNegativeButton(message.getCancelButton(), (dialog,which)->{})
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
    }
}
