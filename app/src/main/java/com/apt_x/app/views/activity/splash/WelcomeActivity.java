package com.apt_x.app.views.activity.splash;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.acuant.acuantfacecapture.FaceCaptureActivity;
import com.apt_x.app.R;
import com.apt_x.app.authsdk.verifyAucant.MainActivity;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.activity.login.LoginActivity;
import com.apt_x.app.views.activity.verification.AddAddressActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ActivityWelcomeBinding;
import com.bumptech.glide.Glide;

/**
 * Created by shivanivani on 22/4/21.
 */
public class WelcomeActivity extends BaseActivity {


    ActivityWelcomeBinding binding;
    boolean isClicked=false;
    Context context = WelcomeActivity.this;
    Activity activity = WelcomeActivity.this;

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
    public void initializeViews() {
       // binding.tvStarted.setOnClickListener(this);
        //binding.tvSignIn.setOnClickListener(this);

        Glide.with(this)
                .load(R.drawable.latgif).into(binding.ivAnimation);
       /* binding.ivAnimation.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        initializeViews();
        context = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isClicked){
                    if (MyPref.getInstance(getApplicationContext()).readBooleanPrefs(Pref.IS_LOGIN)) {
                        if (!MyPref.getInstance(getApplicationContext()).readBooleanPrefs(Pref.IS_ADDRESS_FILLED)) {
                            startActivity(new Intent(context, AddAddressActivity.class)
                                    .putExtra("isFirsttime", "true"));
                            Log.e("TAGHome", "AddAddressActivity " );
                        }
                        else if (!MyPref.getInstance(getApplicationContext()).readBooleanPrefs(Pref.IS_KYC_FILLED)) {
                            startActivity(new Intent(context, MainActivity.class));
                            Log.e("TAGHome", "MainActivity " );
                        }
                        else {
                            Pref.IS_FIRST = "1";

                            startActivity(new Intent(context, HomeActivity.class));
                            Log.e("TAGHome", "Home activity " );
                        }
                    }  else {
                        System.out.println("IS_DOB in splash" + Pref.IS_DOB);
                        System.out.println("SHARED DOB" +    MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.DOBSHARED));
                        startActivity(new Intent(context, LoginActivity.class));
                        Log.e("TAGHome", "LoginActivity " );
                    }
                    finish();

                }
            }
        }, 5000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStarted:
            case R.id.tvSignIn:
                isClicked=true;
                if (Pref.getBoolean(getApplicationContext(), Pref.IS_LOGIN)) {
                    startActivity(new Intent(context, HomeActivity.class));
                } else {
                    startActivity(new Intent(this, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }

                finish();
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
