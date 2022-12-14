package com.apt_x.app.views.fragment.setting;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apt_x.app.R;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.views.base.BaseFragment;
import com.apt_x.app.databinding.FragmentSettingLayoutBinding;

/**
 * Created by shivanivani on 22/4/21
 */

public class SettingFragment extends BaseFragment implements DialogClickListener {

    SettingViewModel viewModel;
    FragmentSettingLayoutBinding binding;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_layout, container, false);
        viewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        initializeViews(binding.getRoot());
        return binding.getRoot();
    }
    @Override
    public void initializeViews(View rootView) {


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDialogClick(int which, int requestCode) {

    }
}