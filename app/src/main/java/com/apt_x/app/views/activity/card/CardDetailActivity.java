package com.apt_x.app.views.activity.card;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityCardDetailBinding;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;

public class CardDetailActivity extends BaseActivity {
    ActivityCardDetailBinding binding;
    Context context = CardDetailActivity.this;
    Activity activity = CardDetailActivity.this;

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
        binding= DataBindingUtil.setContentView(this,R.layout.activity_card_detail);
        initializeViews();
    }

     public void initializeViews() {
        binding.ivBack.setOnClickListener(this);
        //binding.tvchangePin.setOnClickListener(this);
        //binding.tvremove.setOnClickListener(this);
         binding.tvTotalBalance.setText("$ "+ MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.WALLET_BALANCE));




    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tvchange_pin:
               startActivity(new Intent(CardDetailActivity.this,ChangePinActivity.class));
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