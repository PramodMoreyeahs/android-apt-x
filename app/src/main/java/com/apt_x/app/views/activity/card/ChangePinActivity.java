package com.apt_x.app.views.activity.card;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityChangePinBinding;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.base.BaseActivity;

public class ChangePinActivity extends BaseActivity {
    ActivityChangePinBinding binding;
    Context context = ChangePinActivity.this;
    Activity activity = ChangePinActivity.this;
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
        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_pin);
        initializeViews();
    }

     public void initializeViews() {
        binding.ivBack.setOnClickListener(this);
        binding.changepinbtn.setOnClickListener(this);
     //   binding.TextViewTitle.setText(getResources().getString(R.string.change_pin));
        binding.titlecgpn.setText(getResources().getString(R.string.change_pin));

         binding.etOtp.addTextChangedListener(new TextWatcher() {
             public void afterTextChanged(Editable s) {
                 char[] cArr = s.toString().trim().toCharArray();
                 binding.tvCodeOne.setText("");
                 binding.tvCodeTwo.setText("");
                 binding.tvCodeThree.setText("");
                 binding.tvCodeFour.setText("");
                 for (int i = 0; i < s.length(); i++) {
                     if (i == 0) {
                         binding.tvCodeOne.setText(String.valueOf(cArr[i]));
                     }
                     if (i == 1) {
                         binding.tvCodeTwo.setText(String.valueOf(cArr[i]));
                     }
                     if (i == 2) {
                         binding.tvCodeThree.setText(String.valueOf(cArr[i]));
                     }
                     if (i == 3) {
                         binding.tvCodeFour.setText(String.valueOf(cArr[i]));
                     }
                 }
               /* if (binding.etOtp.getText().toString().length() == 4) {
                    binding.tvContinue.setEnabled(true);
                    binding.tvContinue.setBackground(ContextCompat.getDrawable(VerifyPhoneActivity.this, R.drawable.btn_bg));
                } else {
                    binding.tvContinue.setEnabled(false);
                    binding.tvContinue.setBackground(ContextCompat.getDrawable(VerifyPhoneActivity.this, R.drawable.btn_bg));
                }*/
             }


             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             public void onTextChanged(CharSequence s, int start, int before, int count) {
             }
         });

         binding.etOtp2.addTextChangedListener(new TextWatcher() {
             public void afterTextChanged(Editable s) {
                 char[] cArr = s.toString().trim().toCharArray();
                 binding.tvnCodeOne.setText("");
                 binding.tvnCodeTwo.setText("");
                 binding.tvnCodeThree.setText("");
                 binding.tvnCodeFour.setText("");
                 for (int i = 0; i < s.length(); i++) {
                     if (i == 0) {
                         binding.tvnCodeOne.setText(String.valueOf(cArr[i]));
                     }
                     if (i == 1) {
                         binding.tvnCodeTwo.setText(String.valueOf(cArr[i]));
                     }
                     if (i == 2) {
                         binding.tvnCodeThree.setText(String.valueOf(cArr[i]));
                     }
                     if (i == 3) {
                         binding.tvnCodeFour.setText(String.valueOf(cArr[i]));


                     }
                 }
               /* if (binding.etOtp.getText().toString().length() == 4) {
                    binding.tvContinue.setEnabled(true);
                    binding.tvContinue.setBackground(ContextCompat.getDrawable(VerifyPhoneActivity.this, R.drawable.btn_bg));
                } else {
                    binding.tvContinue.setEnabled(false);
                    binding.tvContinue.setBackground(ContextCompat.getDrawable(VerifyPhoneActivity.this, R.drawable.btn_bg));
                }*/
             }


             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             public void onTextChanged(CharSequence s, int start, int before, int count) {
             }
         });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ivBack:
                onBackPressed();
                break;
            case  R.id.changepinbtn:
                startActivity(new Intent(ChangePinActivity.this, HomeActivity.class));
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