package com.apt_x.app.views.activity.loadfund;

import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityLoadFundMethodBinding;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;

public class LoadFundSecondActivity extends BaseActivity {
    ActivityLoadFundMethodBinding binding;
    Context context = LoadFundSecondActivity.this;
    Activity activity = LoadFundSecondActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_load_fund_method);
        initializeViews();
    }
    @Override
    public void initializeViews() {
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.llInterac:
                startActivity(new Intent(this, InteracETransferActivity.class).putExtra("Amount" ,
                        getIntent().getStringExtra("Amount")));
                finish();
                break;
            case R.id.llBillPay:
                startActivity(new Intent(this, InteracETransferActivity.class).putExtra("Amount" ,
                        getIntent().getStringExtra("Amount")));
                finish();
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