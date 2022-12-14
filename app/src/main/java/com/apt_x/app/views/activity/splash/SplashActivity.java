package com.apt_x.app.views.activity.splash;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.authsdk.verifyAucant.MainActivity;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.login.LoginActivity;
import com.apt_x.app.views.activity.waitlist.WaitListActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.apt_x.app.R;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.activity.verification.AddAddressActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ActivitySplashBinding;

/**
 * Created by shivanivani on 22/4/21.
 */
public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;
    boolean isClicked=false;
    Context context = SplashActivity.this;
    Activity activity = SplashActivity.this;


    @Override
    public void initializeViews() {
//        LocaleHelper.setLocale(SplashActivity.this, "en");

        Glide.with(SplashActivity.this).asGif().load(R.drawable.newios)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }
            @Override
            public boolean onResourceReady(final GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                resource.setLoopCount(1);
                resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        //do whatever after specified number of loops complete
                        if (Pref.getBoolean(getApplicationContext(), Pref.IS_LOGIN)) {
                            startActivity(new Intent(SplashActivity.this, WaitListActivity.class));
                            finish();
                        }
                        else {
                            binding.tvStarted.setVisibility(View.VISIBLE);
                            binding.tvStarted.setEnabled(true);
                        }

                        ((GifDrawable)binding.ivSplash.getDrawable()).stop();
                    }
                });
                return false;
            }
        }).into(binding.ivSplash);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initializeViews();
        context = this;

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
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isClicked){
                    if (MyPref.getInstance(getApplicationContext()).readBooleanPrefs(Pref.IS_LOGIN)) {
                        if (!MyPref.getInstance(getApplicationContext()).readBooleanPrefs(Pref.IS_ADDRESS_FILLED)) {
                            startActivity(new Intent(context, AddAddressActivity.class));
                            Log.e("TAGHome", "AddAddressActivity " );
                        }
                        else if (!MyPref.getInstance(getApplicationContext()).readBooleanPrefs(Pref.IS_KYC_FILLED)) {
                            startActivity(new Intent(context, MainActivity.class));
                            Log.e("TAGHome", "MainActivity " );
                        }
                        else {
                            startActivity(new Intent(context, HomeActivity.class));
                            Log.e("TAGHome", "Home activity " + Pref.IS_FIRST);
                        }
                    }  else {
                        startActivity(new Intent(context, LoginActivity.class));
                        Log.e("TAGHome", "LoginActivity " );
                    }
                    finish();
                }
            }
        }, 4000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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
