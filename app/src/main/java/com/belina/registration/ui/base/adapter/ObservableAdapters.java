package com.belina.registration.ui.base.adapter;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.design.widget.TextInputLayout;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.EditText;

import com.belina.registration.R;
import com.belina.registration.ui.base.editableField.ErrorType;

public class ObservableAdapters {

    @BindingAdapter({"app:binding"})
    public static void bindEditText(EditText view,
                                    final ObservableField<String> observableField) {

        Pair<ObservableField<String>, TextWatcherAdapter> pair =
                (Pair) view.getTag(R.id.bound_observable);
        if (pair == null || pair.first != observableField) {
            if (pair != null) {
                view.removeTextChangedListener(pair.second);
            }
            TextWatcherAdapter watcher = new TextWatcherAdapter() {
                public void onTextChanged(CharSequence s,
                                          int start, int before, int count) {
                    observableField.set(s.toString());
                }
            };
            view.setTag(R.id.bound_observable,
                    new Pair<>(observableField, watcher));
            view.addTextChangedListener(watcher);
        }
        String newValue = observableField.get();
        if (!view.getText().toString().equals(newValue)) {
            view.setText(newValue);
        }
    }

    @BindingAdapter({"app:errorString"})
    public static void bindingField(TextInputLayout textInputLayout, String error){
       if(error.isEmpty())textInputLayout.setError(null);
       else textInputLayout.setError(error);

    }


    @BindingAdapter("visibility")
    public static void visibilityAdapter(View view,Boolean visibility){
        int i = visibility?View.VISIBLE:View.INVISIBLE;
        view.setVisibility(i);
    }

    @BindingAdapter("errorTextAppearance")
    public static void visibilityAdapter(TextInputLayout view,ErrorType errorType){
        if(errorType == ErrorType.ERROR)
            view.setErrorTextAppearance(R.style.error_appearance);
        if(errorType == ErrorType.WARNING)
            view.setErrorTextAppearance(R.style.warning_appearance);

    }





}
