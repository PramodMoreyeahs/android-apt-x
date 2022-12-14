package com.apt_x.app.views.activity.card;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityViewPinBinding;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.base.BaseActivity;

public class ViewPinActivity extends BaseActivity {
    ActivityViewPinBinding binding;
    Context context = ViewPinActivity.this;
    Activity activity = ViewPinActivity.this;


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
        binding= DataBindingUtil.setContentView(this,R.layout.activity_view_pin);
        initializeViews();
    }

     public void initializeViews() {


        binding.llUpper.ivBack.setOnClickListener(this);
         binding.tvchangePin.setOnClickListener(this);
         binding.llUpper.tvTitle.setText(getString(R.string.view_pin));

         binding.llView.requestFocus();
         InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.showSoftInput( binding.llView, InputMethodManager.SHOW_IMPLICIT);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvContinue:
                startActivity(new Intent(ViewPinActivity.this, HomeActivity.class));
                finish();
                break;
            case R.id.tvchange_pin:
                startActivity(new Intent(ViewPinActivity.this,ChangePinActivity.class));
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