package com.belina.registration.ui.registration;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.belina.registration.R;
import com.belina.registration.ui.base.activity.BaseActivity;
import com.belina.registration.databinding.ActivityRegistrationBinding;
import com.belina.registration.ui.base.activity.SoftKeyboard;
import com.belina.registration.ui.base.viewmodel.ViewModelFactory;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RegistrationActivity extends BaseActivity<RegistrationViewModel> {

    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding databinding;



    SoftKeyboard softKeyboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        compositeDisposable.dispose();
    }


    @NonNull
    @Override
    protected ViewModelFactory<RegistrationViewModel> getViewModelFactory() {
        return ()->new RegistrationViewModel(getContext());
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

       /* ScrollView mainLayout = databinding.registerFormLayout; // You must use the layout root
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

        softKeyboard = new SoftKeyboard(mainLayout, im);
        Disposable d = softKeyboard.keyboardListener()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((keyboarIsShowing)->databinding.logo.setVisibility(keyboarIsShowing?View.GONE:View.VISIBLE));
        compositeDisposable.add(d);*/
    }



}
