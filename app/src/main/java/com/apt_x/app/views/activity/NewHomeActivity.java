package com.apt_x.app.views.activity;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.profile.MyProfileActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ActivityNewHomeBinding;

public class NewHomeActivity extends BaseActivity {
    private ActivityNewHomeBinding binding;

    Context context = NewHomeActivity.this;
    Activity activity = NewHomeActivity.this;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }


    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_home);
        super.onCreate(savedInstanceState);
        initializeViews();
    }

    @Override
    public void initializeViews() {
        binding.ivUser.setOnClickListener(this);
        //replaceFragment(false, new HomeFragment(), R.id.mainContent);

    }

    @Override
    public void onClick(View view) {
        if (view == binding.ivUser) {
            goToUserProfile();
        }
    }

    private void goToUserProfile() {
        startActivity(new Intent(this, MyProfileActivity.class));
    }

  @Override
    public void doLogout() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Timeout");
                builder.setMessage("Sorry this Session Timeout");
                builder.setCancelable(false);
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
                AlertDialog dialog = builder.show();
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context, R.color.blue));
            }
        });
    }

}