package com.belina.registration.ui.base.activity;

import android.content.Context;
import android.support.v4.content.Loader;

import com.belina.registration.ui.base.viewmodel.StoredViewModel;
import com.belina.registration.ui.base.viewmodel.ViewModelFactory;

import timber.log.Timber;

public class ViewModelLoader<T extends StoredViewModel> extends Loader<T> {

    private final ViewModelFactory<T> factory;
    private final String tag;
    private T viewModel;

    public ViewModelLoader(Context context, ViewModelFactory<T> factory, String tag) {
        super(context);
        this.factory = factory;
        this.tag = tag;
    }

    @Override
    protected void onStartLoading() {
        Timber.d("onStartLoading-"+tag);
        // if we already own a presenter instance, simply deliver it.
        if (viewModel != null) {
            deliverResult(viewModel);
            return;
        }

        // Otherwise, force a load
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        Timber.d("onForceLoad-"+tag);
        // Create the Presenter using the Factory
        viewModel = factory.create();

        // Deliver the result
        deliverResult(viewModel);
    }

    @Override
    public void deliverResult(T data) {
        super.deliverResult(data);
        Timber.d("deliverResult-"+tag);
    }

    @Override
    protected void onStopLoading() {
        Timber.d("onStopLoading-"+tag);
    }

    @Override
    protected void onReset() {
        Timber.d("onReset-"+tag);
        if (viewModel != null) {
            viewModel.onActivityDestroyed();
            viewModel = null;
        }
    }

    public T getPresenter() {
        return viewModel;
    }
}
