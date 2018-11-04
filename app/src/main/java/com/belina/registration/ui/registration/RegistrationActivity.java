package com.belina.registration.ui.registration;

import android.content.DialogInterface;
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
import com.belina.registration.ui.base.viewmodel.ViewModelFactory;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class RegistrationActivity extends BaseActivity<RegistrationViewModel> {

    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding databinding;
    private ApplicationComponent component;

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

        compositeDisposable.add(registrationViewModel.messagePublishSubject.subscribe(this::showDilog));
        component =  App.create(this).getMoviesDBComponent();
        initDataBinding();
    }

    private void showDilog(Message message){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(message.getTitle())
                .setMessage(message.getMessage())
                .setPositiveButton(message.getOkButton(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        message.callOkCallback();
                        // continue with delete
                    }
                })
                .setNegativeButton(message.getCancelButton(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();

    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
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

    }

    View.OnFocusChangeListener focusChangeListener =  new View.OnFocusChangeListener() {

        private boolean oldFocus = false;

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            registrationViewModel.checkEmail(false);
            registrationViewModel.checkPassword(false);
        }
    };

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
