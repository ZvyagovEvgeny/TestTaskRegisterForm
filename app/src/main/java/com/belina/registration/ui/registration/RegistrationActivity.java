package com.belina.registration.ui.registration;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component =  App.create(this).getMoviesDBComponent();
    }



    ApplicationComponent component;

    @NonNull
    @Override
    protected ViewModelFactory<RegistrationViewModel> getViewModelFactory() {

        return ()->new RegistrationViewModel(getContext(),component);
    }

    @Override
    protected void onViewModelCreatedOrRestored(@NonNull RegistrationViewModel viewModel) {
        this.registrationViewModel = viewModel;
        initDataBinding();
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

    }

    View.OnFocusChangeListener focusChangeListener =  new View.OnFocusChangeListener() {

        private boolean oldFocus = false;

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            registrationViewModel.checkEmail();
//            if(oldFocus && !hasFocus){
//                registrationViewModel.checkEmail();
//            }
//            oldFocus = hasFocus;
        }
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        compositeDisposable.dispose();
    }





}
