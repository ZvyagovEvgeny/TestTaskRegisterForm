package com.belina.registration.model;

import android.databinding.ObservableField;

import io.reactivex.Observable;

public abstract class FieldChecker {

    abstract public void check(Field field);
}