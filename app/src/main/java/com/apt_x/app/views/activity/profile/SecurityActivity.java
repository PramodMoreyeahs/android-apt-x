package com.apt_x.app.views.activity.profile;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.FingerPrintEnable;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.ChangePasswordActivity;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ActivitySecurityBinding;

public class SecurityActivity extends BaseActivity implements FingerPrintEnable.onResult {
    private ActivitySecurityBinding binding;
    FingerprintManager fingerprintManager;
    FingerPrintEnable fingerPrintEnable;
    Context context = SecurityActivity.this;
    Activity activity = SecurityActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_security);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initializeViews();
        }
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initializeViews() {
        binding.lhPassword.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);

        fingerprintManager=(FingerprintManager) SecurityActivity.this.getSystemService(Context.FINGERPRINT_SERVICE);

        if(fingerprintManager!=null&& fingerprintManager.isHardwareDetected())
        {
            binding.llBio.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.llBio.setVisibility(View.GONE);
        }
        if(MyPref.getInstance(getApplicationContext()).readBooleanPrefs(MyPref.USE_FINGER_PRINT) || Pref.IS_BIONEED){
            binding.biobt.setChecked(true);
        }
        fingerPrintEnable=new FingerPrintEnable(SecurityActivity.this,false,this);


        binding.biobt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    fingerPrintEnable.enableFingerPrint();
                    MyPref.getInstance(getApplicationContext()).writeBooleanPrefs(Pref.IS_BOIMETRIC,true);
                    Pref.IS_FIRST = "0";
                    Pref.IS_BIONEED = true;
                    }
                else
                {
                    MyPref.getInstance(getApplicationContext()).writeBooleanPrefs(MyPref.USE_FINGER_PRINT,false);
                    MyPref.getInstance(getApplicationContext()).writeBooleanPrefs(Pref.IS_BOIMETRIC,false);
                    Pref.IS_BIONEED = false;
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == binding.lhPassword) {
            goToPassword();
        }
        if (view == binding.ivBack) {
            onBackPressed();
        }


    }

    private void goToPassword() {
       // Utils.showToast(getApplicationContext(),"Coming soon");
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    @Override
    public void onclick(Boolean status) {
        if(status)
        {
            binding.biobt.setChecked(true);
        }
        else
        {
            binding.biobt.setChecked(false);
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