package com.apt_x.app.views.base;

import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;

/**
 * Created by shivanivani on 22/4/21
 */

public class BaseViewModel<N> extends ViewModel {


    private WeakReference<N> mNavigator;

    public BaseViewModel() {

    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }

    public WeakReference<N> getmNavigator() {
        return mNavigator;
    }
}
