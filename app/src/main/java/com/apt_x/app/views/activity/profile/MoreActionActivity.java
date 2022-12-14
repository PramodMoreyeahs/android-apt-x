package com.apt_x.app.views.activity.profile;

import static com.apt_x.app.utils.Utils.showCustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.apt_x.app.privacy.PrivacyPolicy;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.AboutActivity;
import com.apt_x.app.views.activity.SupportActivity;
import com.apt_x.app.views.activity.TermsAndConditionsActivity;
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
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ActivityMoreActionBinding;

public class MoreActionActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private ActivityMoreActionBinding binding;
    Context context = MoreActionActivity.this;
    Activity activity = MoreActionActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_more_action);
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
        binding.llLogout.setOnClickListener(this);
        binding.llprivacy.setOnClickListener(this);
        binding.llTerms.setOnClickListener(this);
        binding.llcardAgree.setOnClickListener(this);
        binding.llFAQ.setOnClickListener(this);
        binding.llCloseAccounr.setOnClickListener(this);

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
      /*  if (view == binding.ivBack) {
            onBackPressed();
        }
        if (view == binding.llLogout) {
            logout1(MoreActionActivity.this);
        }*/

        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.llprivacy:
                startActivity(new Intent(MoreActionActivity.this, PrivacyPolicy.class).putExtra("type", "privacy"));
                break;
            case R.id.llTerms:
                startActivity(new Intent(MoreActionActivity.this, PrivacyPolicy.class).putExtra("type", "terms"));
                break;
            case R.id.llcardAgree:
                // startActivity(new Intent(MoreActionActivity.this, TermsAndConditionsActivity.class));
                Utils.showToast(getApplicationContext(), getString(R.string.coming_soon));
                break;
            case R.id.llFAQ:
                Utils.showToast(getApplicationContext(), getString(R.string.coming_soon));
                break;
            case R.id.llCloseAccounr:
                Utils.showToast(getApplicationContext(), getString(R.string.coming_soon));
                break;
            case R.id.llLogout:
                logout1(MoreActionActivity.this);
                break;

            case R.id.ll_contact:
                startActivity(new Intent(MoreActionActivity.this, SupportActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(MoreActionActivity.this, AboutActivity.class));
                break;


        }
    }

    public void logout1(Context activity) {
        final Dialog dialog = showCustomDialog(activity, R.layout.logout_dialog);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView ok = (TextView) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(v -> {
            logout();
        });
        TextView cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void logout() {

        logoutGoogle();
        MyPref.getInstance(this).clearPrefs();
        Pref.setAccessToken(this, "");
        Pref.setBoolean(this, false, Pref.REMEMBER_ME);
        Pref.setBoolean(this, false, Pref.IS_LOGIN);
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

    @Override
    public void onConnectionFailed(@NonNull @io.reactivex.annotations.NonNull ConnectionResult connectionResult) {

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