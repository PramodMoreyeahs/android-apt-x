package com.apt_x.app.views.activity.sendMoney;

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
import com.apt_x.app.databinding.ActivityAddNewRecipientSendingToBinding;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;

public class SendingActivity extends BaseActivity {
    private ActivityAddNewRecipientSendingToBinding binding;
    String name="",email="",userId="",user_wallet_id="",existing="";
    Context context = SendingActivity.this;
    Activity activity = SendingActivity.this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_recipient_sending_to);
        //setContentView(R.layout.activity_new_transaction);
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
        binding.llUpper.ivBack.setOnClickListener(this);
        binding.llUpper.tvTitle.setText(R.string.sending_to);
        //binding.rlAddNew.setOnClickListener(this);
        binding.tvContinue.setOnClickListener(this);
        binding.tvWallet.setText("$ "+MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.WALLET_BALANCE));
        if(getIntent()!=null)
        {
            name=getIntent().getStringExtra("name");
            email=getIntent().getStringExtra("email");
            userId=getIntent().getStringExtra("userID");
            user_wallet_id=getIntent().getStringExtra("user_wallet_id");
            existing=getIntent().getStringExtra("existing")!=null?getIntent().getStringExtra("existing")
            :"";

        }
        binding.tvName.setText(name);
        binding.tvEmail.setText(email);


    }

    @Override
    public void onClick(View view) {
        if (view == binding.tvContinue) {
            if(binding.etAmount.getText().toString().isEmpty())
            {
                Utils.showToast(getApplicationContext(),getResources().getString(R.string.please_enter_amou));
            }
            else if(Integer.parseInt(binding.etAmount.getText().toString())==0)
            {
                Utils.showToast(getApplicationContext(),getResources().getString(R.string.please_enter_valid_amount));
            }
            else
            {
                startActivity(new Intent(this, ReviewActivity.class)
                        .putExtra("name",name)
                        .putExtra("email",email)
                        .putExtra("amount",binding.etAmount.getText().toString().trim())
                        .putExtra("userID",userId)
                        .putExtra("existing",existing)
                        .putExtra("user_wallet_id",user_wallet_id)
                );
            }

        }/*else if(view == binding.rlWithdraw) {
            startActivity(new Intent(this, SendingActivity2.class));
        }*/
        else if(view ==binding.llUpper.ivBack)
        {
            onBackPressed();
        }
        if (view == binding.tv10) {
            binding.etAmount.setText("10");
        }
        if (view == binding.tv20) {
            binding.etAmount.setText("20");
        }
        if (view == binding.tv50) {
            binding.etAmount.setText("50");
        }

        if (view == binding.tv100) {
            binding.etAmount.setText("100");
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