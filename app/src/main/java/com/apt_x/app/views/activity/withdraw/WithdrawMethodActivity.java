package com.apt_x.app.views.activity.withdraw;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import android.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityWithrawMethodBinding;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.profile.PaymentActivity;
import com.apt_x.app.views.activity.profile.PaymentCardActivity;
import com.apt_x.app.views.base.BaseActivity;

public class WithdrawMethodActivity extends BaseActivity {

    ActivityWithrawMethodBinding binding;
    Context context = WithdrawMethodActivity.this;
    Activity activity = WithdrawMethodActivity.this;

    @Override
    public void initializeViews() {
        binding.llEFT.setOnClickListener(this);
        binding.llAptSend.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withraw_method);

        initializeViews();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_EFT:
                startActivity(new Intent(this, PaymentActivity.class)
                        .putExtra(Keys.detail, Keys.passport));
                break;
            case R.id.ll_AptSend:
                startActivity(new Intent(this, PaymentCardActivity.class)
                        .putExtra(Keys.detail, Keys.driving));
                break;
            case R.id.ll_tbd:
             /*   startActivity(new Intent(this, PaymentCardActivity.class)
                        .putExtra(Keys.detail, Keys.driving));*/
                Utils.showToast(getApplicationContext(),getResources().getString(R.string.coming_soon));
                break;

            case R.id.ivBack:
                onBackPressed();
                break;


        }
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