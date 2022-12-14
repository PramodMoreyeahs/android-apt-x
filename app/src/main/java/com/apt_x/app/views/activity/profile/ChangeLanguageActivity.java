package com.apt_x.app.views.activity.profile;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityChangeLanguageBinding;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.base.BaseActivity;

public class ChangeLanguageActivity extends BaseActivity {

    Context context = ChangeLanguageActivity.this;
    Activity activity = ChangeLanguageActivity.this;
    ActivityChangeLanguageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_language);
        initializeViews();

    }

    @Override
    public void initializeViews() {
        binding.llEnglish.setOnClickListener(this);
        binding.llFrench.setOnClickListener(this);
        binding.ivBackArrow.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llEnglish:
          /*      LocaleHelper.setLocale(ChangeLanguageActivity.this, "en");
               startActivity(new Intent(ChangeLanguageActivity.this, WelcomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.LANG_TYPE_PREF,"en");
                finishAffinity();*/
                LocaleHelper.setLocale(ChangeLanguageActivity.this, "en");
                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.LANG_TYPE_PREF, "en");
                startActivity(new Intent(this, MyProfileActivity.class));
                finish();

               /* Intent intent = getIntent();
                finish();
                startActivity(intent);*/

               /* Configuration newConfig = new Configuration();             newConfig.locale = Locale.ENGLISH;
               onConfigurationChanged(newConfig);*/
                //  recreate();

                //  finish();

                break;
            case R.id.llFrench:
                LocaleHelper.setLocale(ChangeLanguageActivity.this, "fr");
                /*startActivity(new Intent(ChangeLanguageActivity.this, WelcomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.LANG_TYPE_PREF,"fr");
                finishAffinity();*/
                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.LANG_TYPE_PREF, "fr");
                startActivity(new Intent(this, MyProfileActivity.class));
                finish();
                /*Intent intent1 = getIntent();
                finish();
                startActivity(intent1);*/

                //   finish();

                break;
            case R.id.iv_back_arrow:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onConfigurationChanged(@NonNull @io.reactivex.annotations.NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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