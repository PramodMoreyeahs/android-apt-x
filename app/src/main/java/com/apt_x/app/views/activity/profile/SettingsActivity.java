package com.apt_x.app.views.activity.profile;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.login.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.apt_x.app.R;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.views.activity.ChangePasswordActivity;
import com.apt_x.app.views.activity.SendingFromActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.SettingsActivityBinding;

public class SettingsActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{
    private SettingsActivityBinding binding;
    private GoogleApiClient googleApiClient;
    Context context = SettingsActivity.this;
    Activity activity = SettingsActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.settings_activity);
        super.onCreate(savedInstanceState);
        initializeViews();
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
    public void initializeViews() {
        binding.ivBack.setOnClickListener(this);
        binding.lhPassword.setOnClickListener(this);
        binding.lhSending.setOnClickListener(this);
        binding.llLogout.setOnClickListener(this);
        binding.lhAppLanguage.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
        }
        if (view == binding.lhPassword) {
            goToPassword();
        }
        if (view == binding.lhSending) {
            goToSendingFrom();
        }
        if (view == binding.lhAppLanguage) {
            goToAppLanguage();
        }
        if (view == binding.llLogout) {
            logout();
        }
    }

    private void goToAppLanguage() {
        startActivity(new Intent(this, ChangeLanguageActivity.class));

    }
    private void logout() {
        logoutGoogle();
        MyPref.getInstance(this).clearPrefs();

        Pref.setAccessToken(this,"");
        Pref.setBoolean(this, false, Pref.REMEMBER_ME);
        Pref.setBoolean(this, false, Pref.IS_LOGIN);
        Pref.setBoolean(this, false, Pref.IS_ADDRESS_FILLED);
        startActivity(new Intent(this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
        );
        finish();

    }

    void logoutGoogle() {
        LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@io.reactivex.annotations.NonNull Status status) {
                        if (status.isSuccess()) {
                            // do something
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.session), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void goToSendingFrom() {
        startActivity(new Intent(this, SendingFromActivity.class));
    }

    private void goToPassword() {
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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