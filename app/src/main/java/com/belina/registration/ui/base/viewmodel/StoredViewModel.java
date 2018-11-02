package com.belina.registration.ui.base.viewmodel;

import android.content.Context;

public interface StoredViewModel {

    void onActivityDetached();
    void onActivityDestroyed();
    void onAttached(Context context);

}
