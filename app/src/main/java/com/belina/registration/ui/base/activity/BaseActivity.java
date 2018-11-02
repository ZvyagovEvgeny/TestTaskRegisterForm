package com.belina.registration.ui.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.belina.registration.ui.base.viewmodel.StoredViewModel;
import com.belina.registration.ui.base.viewmodel.ViewModelFactory;

import timber.log.Timber;

public abstract class BaseActivity<T extends StoredViewModel> extends AppCompatActivity  {

    private T viewModel;

    private static final int LOADER_ID = 101;
    private static String LOG_TAG = "BaseActivity";




    private int loaderId(){
        return LOADER_ID;
    }

    static void log(String d,String method){
        Timber.d(d+": "+method);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initDagger();
        super.onCreate(savedInstanceState);

        Loader<T> loader = LoaderManager.getInstance(this).getLoader(loaderId());
        if (loader == null) {
            initLoader();
        } else {
            this.viewModel = ((ViewModelLoader<T>) loader).getPresenter();
            this.viewModel.onAttached(getContext());
            onViewModelCreatedOrRestored(viewModel);
        }

    }

    private void initLoader() {

        LoaderManager.getInstance(this).initLoader(loaderId(),null,callbacks);
    }

    LoaderManager.LoaderCallbacks<T> callbacks = new LoaderManager.LoaderCallbacks<T>(){

        @Override
        public final Loader<T> onCreateLoader(int id, Bundle args) {
           log("onCreateLoader","onCreateLoader");
            return new ViewModelLoader<>(BaseActivity.this,
                    getViewModelFactory(), tag());
        }

        @Override
        public final void onLoadFinished(Loader<T> loader, T viewModel) {
            BaseActivity.this.viewModel = viewModel;;
            viewModel.onAttached(getContext());
            onViewModelCreatedOrRestored(viewModel);
        }

        @Override
        public final void onLoaderReset(Loader<T> loader) {
            BaseActivity.this.viewModel = null;
        }
    };

    /**
     * String tag use for log purposes.
     */
    @NonNull
    protected String tag(){
        return LOG_TAG;
    }

    /**
     * Instance of {@link ViewModelFactory} use to create a ViewModel when needed. This instance should
     * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
     */
    @NonNull
    protected abstract ViewModelFactory<T> getViewModelFactory();

    /**
     * Hook for subclasses that deliver the {@link StoredViewModel} before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    protected abstract void onViewModelCreatedOrRestored(@NonNull T viewModel);

    @Override
    protected void onDestroy() {

        if(viewModel!=null){
            if(isFinishing()){
                viewModel.onActivityDetached();
                viewModel.onActivityDestroyed();
            }
            else {
                viewModel.onActivityDetached();
            }
        }
        super.onDestroy();
    }

    protected Context getContext(){
        return this;
    }

    protected void initDagger(){

    }


}

