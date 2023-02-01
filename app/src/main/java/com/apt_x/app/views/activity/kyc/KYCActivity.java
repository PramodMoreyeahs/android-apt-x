package com.apt_x.app.views.activity.kyc;


import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.acuant.acuantfacecapture.FaceCaptureActivity;
import com.apt_x.app.R;
import com.apt_x.app.authsdk.verifyAucant.MainActivity;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.forgotpassword.ForgotPasswordVerificationActivity;
import com.apt_x.app.views.activity.signup.CaptureIdActivity;
import com.apt_x.app.views.activity.signup.CaptureImageActivity;
import com.apt_x.app.views.activity.signup.PasteLinkActivity;
import com.apt_x.app.views.activity.signup.VerifyPhoneActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ActivityKYCBinding;

public class KYCActivity extends BaseActivity {

    ActivityKYCBinding binding;
    String email = "";
    Context context = KYCActivity.this;
    Activity activity = KYCActivity.this;
    @Override
    public void initializeViews() {
        if (getIntent() != null) {
            email = getIntent().getStringExtra(Keys.EMAIL);
            System.out.println("email in KYC1" + email);
          //  MyPref.getInstance(this).writePrefs(MyPref.USER_EMAIL, email);

        } else {
            System.out.println("email in KYC2" + email);
            email = MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.USER_EMAIL);
        }
        email=MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.USER_EMAIL);
        System.out.println("email in KYC3" + email);

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_k_y_c);
        binding.ivBack.setOnClickListener(this);
        initializeViews();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPassport:
                startActivity(new Intent(this, MainActivity.class)
                        .putExtra(Keys.detail, Keys.passport));
            break;
            case R.id.llDrivingL:
                startActivity(new Intent(this, MainActivity.class)
                        .putExtra(Keys.detail, Keys.driving));
                break;
            case R.id.llPermanentR:
                startActivity(new Intent(this, MainActivity.class)
                        .putExtra(Keys.detail, Keys.permanentR));
                break;
            case R.id.llIdentityCard:
                startActivity(new Intent(this, CaptureIdActivity.class)
                        .putExtra(Keys.detail, Keys.identity));
                break;
            case R.id.llCitizenCard:
                startActivity(new Intent(this, CaptureIdActivity.class)
                        .putExtra(Keys.detail, Keys.citizen));
                break;
            case R.id.llSecure:
                startActivity(new Intent(this, CaptureIdActivity.class)
                        .putExtra(Keys.detail, Keys.secure));
                break;
            case R.id.ivBack:
                FaceCaptureActivity.Companion.setAbsolutePath("");
                startActivity(new Intent(this, CaptureImageActivity.class)
                        .putExtra(Keys.EMAIL, email));

                break;

        }
    }

  /*  @Override
    public void onBackPressed() {
        Utils.exitApp(this);
    }*/

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