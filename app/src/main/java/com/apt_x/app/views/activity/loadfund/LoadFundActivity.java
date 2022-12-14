package com.apt_x.app.views.activity.loadfund;

import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityLoadFundBinding;
import com.apt_x.app.utils.NumberTextWatcherForThousand;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.verification.AddAddressActivity;
import com.apt_x.app.views.base.BaseActivity;

public class LoadFundActivity extends BaseActivity {

    ActivityLoadFundBinding binding;
    Context context = LoadFundActivity.this;
    Activity activity = LoadFundActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_load_fund);
        initializeViews();
    }

    @Override
    public void initializeViews() {
        binding.ivBack.setOnClickListener(this);


    }
    public void showDialog() {
        final Dialog dialog = Utils.showCustomDialog(this, R.layout.load_fund_dialog);
        TextView yes = dialog.findViewById(R.id.btn_ok);
        TextView cancel = dialog.findViewById(R.id.btn_cancel);
        yes.setOnClickListener(v -> {
            startActivity(new Intent(this, AddAddressActivity.class)
                    .putExtra("comeFrom","Home"));
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        try {


            if (v.getId() == R.id.tvContinue) {


                if (TextUtils.isEmpty(binding.etAmount.getText().toString().trim())) {
                    Utils.showToast(this, getString(R.string.please_enter_amou));
                } else if (Integer.parseInt(binding.etAmount.getText().toString()) < 100) {
                    Utils.showToast(this, getString(R.string.amount_should_be_));
                } else if (Integer.parseInt(binding.etAmount.getText().toString()) > 25000) {
                    Utils.showToast(this, getString(R.string.amount_should_be_));
                } else {
                    startActivity(new Intent(this, LoadFundSecondActivity.class)
                            .putExtra("Amount", binding.etAmount.getText().toString().replaceAll(",", ""))
                    );

                }
            }
            if (v == binding.ivBack) {
                onBackPressed();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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