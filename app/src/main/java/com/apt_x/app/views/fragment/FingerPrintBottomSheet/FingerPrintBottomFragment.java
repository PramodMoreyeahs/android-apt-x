package com.apt_x.app.views.fragment.FingerPrintBottomSheet;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;

import androidx.databinding.DataBindingUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.apt_x.app.R;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.databinding.FragmentBottomSheetBinding;

import io.reactivex.annotations.NonNull;


/**
 * Created by shivanivani on 22/4/21
 */

public class FingerPrintBottomFragment  extends BottomSheetDialogFragment
        implements DialogClickListener {

    FragmentBottomSheetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false);
        initializeViews();
        return binding.getRoot();
    }

    public void initializeViews() {

        binding.tvClose.setOnClickListener(v -> dismiss());
        binding.tvEnable.setOnClickListener(v -> {
            MyPref.getInstance(getActivity()).writeBooleanPrefs(MyPref.USE_FINGER_PRINT,true);
            dismiss();
        });
        if (getDialog() != null) {
            getDialog().setOnShowListener(dialog -> {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                FrameLayout bottomSheetLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                bottomSheetDialog.setCancelable(false);
                if (bottomSheetLayout == null) {
                    return;
                }
                bottomSheetLayout.setBackground(null);
            });
        }

    }



    @Override
    public void onDialogClick(int which, int requestCode) {

    }


}