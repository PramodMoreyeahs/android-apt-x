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
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityCardPinBinding;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.base.BaseActivity;

public class CardPinActivity extends BaseActivity {
    ActivityCardPinBinding binding;
    Context context = CardPinActivity.this;
    Activity activity = CardPinActivity.this;


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
        binding= DataBindingUtil.setContentView(this,R.layout.activity_card_pin);
        initializeViews();
    }

     public void initializeViews() {
         try {
             if(getIntent()!=null)
             {
                 if (getIntent().getStringExtra("block").equals("blockCard")) {
                     binding.llUpper.tvTitle.setText(getResources().getString(R.string.block_card));
                     binding.tvContinue.setVisibility(View.VISIBLE);
                     binding.tvtitle.setText(getResources().getString(R.string.please_enter_pin_block));
                 } else {
                     binding.tvContinue.setVisibility(View.GONE);
                     binding.llUpper.tvTitle.setText(R.string.enter_card_pin);
                     binding.tvtitle.setText(getResources().getString(R.string.please_enter_pin));
                     binding.llUpper.tvTitle.setText(getResources().getString(R.string.enter_card_pin));
                 }
             }
             else
             {
                 //binding.llUpper.tvTitle.setText(getResources().getString(R.string.block_card));
             }

         } catch (Exception e){
             e.printStackTrace();

         }

        binding.llUpper.ivBack.setOnClickListener(this);
         binding.tvContinue.setOnClickListener(this);

         binding.llView.requestFocus();
         InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.showSoftInput( binding.llView, InputMethodManager.SHOW_IMPLICIT);

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
                         try {
                             if(getIntent().getStringExtra("block")==null)
                             {
                                 startActivity(new Intent(CardPinActivity.this,CardDetailActivity.class));
                                 finish();
                             }


                         }
                         catch (Exception e){
                             e.printStackTrace();
                         }

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
            case R.id.tvContinue:
                startActivity(new Intent(CardPinActivity.this, HomeActivity.class));
                finish();
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