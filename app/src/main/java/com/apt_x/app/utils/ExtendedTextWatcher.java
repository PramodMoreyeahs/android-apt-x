package com.apt_x.app.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 🔥 MVP-I Architecture and Android Jetpack 🔥
 * 🍴 Focused on Clean Architecture
 * Created by 🔱 Pratik Kataria 🔱 on 16-07-2021.
 */
public abstract class ExtendedTextWatcher implements TextWatcher {

    public abstract void onTextChange(CharSequence s, int start, int before, int count);

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onTextChange(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
