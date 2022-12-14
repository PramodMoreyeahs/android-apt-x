package com.apt_x.app.views.activity.newTransactions;

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
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ActivityRecipientAddressBinding;

public class RecipientAddressActivity extends BaseActivity {
    private ActivityRecipientAddressBinding binding;
    Context context = RecipientAddressActivity.this;
    Activity activity = RecipientAddressActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipient_address);
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

    }

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
        }
        if (view == binding.tvContinue) {
            validate();

        }

    }

    private void validate() {

        if(binding.etAddress.getText().toString().isEmpty())
        {
            Utils.showToast(getApplicationContext(),
                    getResources().getString(R.string.please_enter_address));
        }
        else if (binding.etHouse.getText().toString().isEmpty()){
            Utils.showToast(getApplicationContext(),
                    getResources().getString(R.string.please_enter_apartment_code));
        }
        else  if(binding.etZipCode.getText().toString().isEmpty())
        {
            Utils.showToast(getApplicationContext(),
                    getResources().getString(R.string.please_enter_postal_code));
        }

        else  if(binding.etCity.getText().toString().isEmpty())
        {
            Utils.showToast(getApplicationContext(),
                    getResources().getString(R.string.please_enter_city));
        }
        else
        {
            startActivity(new Intent(RecipientAddressActivity.this,SenderInfoActivity.class));
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