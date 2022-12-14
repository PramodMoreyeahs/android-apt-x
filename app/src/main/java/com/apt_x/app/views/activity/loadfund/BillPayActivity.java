package com.apt_x.app.views.activity.loadfund;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityInteraceEtransferBinding;
import com.apt_x.app.model.AddFundResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.google.gson.Gson;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class BillPayActivity extends BaseActivity {

    ActivityInteraceEtransferBinding binding;
    ApiCalls apiCalls;
    Context context = BillPayActivity.this;
    Activity activity = BillPayActivity.this;


    @Override
    public void initializeViews() {
        apiCalls=ApiCalls.getInstance(this);
       binding.tvAppId.setText(MyPref.getInstance(this).readPrefs(MyPref.APPID));
       binding.tvEmail.setText(MyPref.getInstance(this).readPrefs(MyPref.EMAILINTERAC));
       binding.tvCopyEmail.setOnClickListener(this);
       binding.tvContinue.setOnClickListener(this);
       binding.tvCopyId.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_interace_etransfer);
        initializeViews();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvCopyEmail:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(binding.tvEmail.getText().toString());
                Utils.showToast(this, binding.tvEmail.getText().toString() + " Copied");
                break;
            case R.id.tvCopyId:
                ClipboardManager clipboardManager1 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager1.setText(binding.tvAppId.getText().toString());
                Utils.showToast(this, binding.tvAppId.getText().toString() + " Copied");
                break;
            case R.id.tvContinue:
              //  apiCalls.adFund(getIntent().getStringExtra("Amount"),MyPref.getInstance(this).readPrefs(MyPref.APPID),disposableObserver);
                apiCalls.addDisburesement(getIntent().getStringExtra("Amount"),
                        "CARD",
                 "CAD",
                        "1234123412341234",
                        "2024-04",
                        disposableObserver
                        );
                break;
        }
    }

    DisposableObserver<AddFundResponse> disposableObserver = new DisposableObserver<AddFundResponse>() {
        @Override
        public void onNext(@NonNull AddFundResponse getCompanyConstantResponse) {
            Utils.hideProgressDialog();
            System.out.println("Response ::" + new Gson().toJson(getCompanyConstantResponse));
            Utils.showToast(BillPayActivity.this, "Success");
            Intent intent = new Intent(BillPayActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            Utils.showToast(BillPayActivity.this, e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };

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