package com.apt_x.app.views.activity.signup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.apt_x.app.R;
import com.apt_x.app.model.ResendOtpResponses;
import com.apt_x.app.model.VerifyOtpResponse;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.kyc.KYCActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.views.customview.Link;
import com.apt_x.app.databinding.ActivityVerifyPhoneBinding;

public class VerifyPhoneActivity extends BaseActivity {

    ActivityVerifyPhoneBinding binding;
    boolean isTimerOn = true;
    CountDownTimer countDownTimer;
    SignUpViewModel viewModel;
    public ApiCalls apiCalls;
      String email = "";

    Context context = VerifyPhoneActivity.this;
    Activity activity = VerifyPhoneActivity.this;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void initializeViews() {
        apiCalls = ApiCalls.getInstance(VerifyPhoneActivity.this);

        if (getIntent() != null) {
            email = getIntent().getStringExtra(Keys.EMAIL);

        } else {

        }
        binding.tvemail.setText(getString(R.string.enter_ver) + " " + email);
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator_otp.observe(this, response_observer);
        viewModel.response_validator_resend.observe(this, response_observer_resend);

        startTimer();
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

    }

    Observer observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {
                case AppUtils.invalid_otp:
                    Utils.showToast(getApplicationContext(), getString(R.string.enter_valid_otp));
                    break;
                case AppUtils.empty_otp:
                    Utils.showToast(getApplicationContext(), getString(R.string.enter_otp));
                    break;
                case AppUtils.NO_INTERNET:
                    Utils.showToast(getApplicationContext(), getString(R.string.internet_connection));
                    break;
                case AppUtils.SERVER_ERROR:
                    Utils.showToast(getApplicationContext(), getString(R.string.server_error));
                    break;
            }
        }
    };

    Observer<VerifyOtpResponse> response_observer = new Observer<VerifyOtpResponse>() {

        @Override
        public void onChanged(@Nullable VerifyOtpResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(getApplicationContext(), "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            if (loginBean.getStatus()) {
                countDownTimer.cancel();
                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.ACCESS_TOKEN, loginBean.getToken());
                Pref.setAccessToken(VerifyPhoneActivity.this, "Bearer " + loginBean.getToken());
                Pref.setBoolean(VerifyPhoneActivity.this, true, Pref.REMEMBER_ME);
                Pref.setBoolean(VerifyPhoneActivity.this, true, Pref.IS_LOGIN);
                startActivity(new Intent(getApplicationContext(), KYCActivity.class)
                        .putExtra(Keys.EMAIL, email)
                );
                finish();
            } else {
                if (loginBean.getMessage().equals("otp did not match")) {
                    Utils.showToast(VerifyPhoneActivity.this, getString(R.string.otp_not_match));
                } else if (loginBean.getMessage().equals("User not registered")) {
                    Utils.showToast(VerifyPhoneActivity.this, getString(R.string.user_not_register));
                } else {
                    Utils.showToast(VerifyPhoneActivity.this, loginBean.getMessage());
                }

            }
        }
    };


    Observer<ResendOtpResponses> response_observer_resend = new Observer<ResendOtpResponses>() {

        @Override
        public void onChanged(@Nullable ResendOtpResponses loginBean) {
            if (loginBean.isStatus()) {
                Utils.showToast(getApplicationContext(), loginBean.getMessage() + " to " + email);
                startTimer();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_phone);
        initializeViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvContinue:
                if (viewModel.validateOtp(binding)) {
                    Utils.showDialog(this, getString(R.string.loading));

                    countDownTimer.cancel();
//                    MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.ACCESS_TOKEN, loginBean.getToken());
//                    Pref.setAccessToken(VerifyPhoneActivity.this, "Bearer "+loginBean.getToken());
                    Pref.setBoolean(VerifyPhoneActivity.this, true, Pref.REMEMBER_ME);
                    Pref.setBoolean(VerifyPhoneActivity.this, true, Pref.IS_LOGIN);
                    //  startActivity(new Intent(getApplicationContext(), AddAddressActivity.class));
                    //  finish();
                    viewModel.verifyOtp(binding.etOtp.getText().toString(), email,
                            apiCalls);
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    public void startTimer() {
        isTimerOn = true;
        countDownTimer = new CountDownTimer(60000, 1000) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                isTimerOn = true;
                long remaingTime = millisUntilFinished / 1000;
                binding.txtResend.setText( " " + getString(R.string.txt_resend_in) + " " +"("+remaingTime+")");
                //here you can have your logic to set text to edittext
                String pattern = " " + getString(R.string.txt_resend_in) + " ";
                Utils.addLink(binding.txtResend, pattern, true, ContextCompat.getColor(getApplicationContext(), R.color.text_gray));
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                isTimerOn = false;
                binding.txtResend.setText(" " + getString(R.string.txt_resend_in));
                String pattern = " " + getString(R.string.txt_resend_in);

                Utils.addLink(binding.txtResend, pattern, true, ContextCompat.getColor(getApplicationContext(), R.color.text_gray)).
                        setOnClickListener(new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                if (!isTimerOn) {
                                    Utils.showDialog(VerifyPhoneActivity.this, getString(R.string.please_wait));
                                    binding.tvCodeOne.setText("");
                                    binding.tvCodeTwo.setText("");
                                    binding.tvCodeThree.setText("");
                                    binding.tvCodeFour.setText("");
                                    binding.etOtp.setText("");
                                    viewModel.resendOtp(email, apiCalls);
                                }

                            }
                        });
            }

        }.start();

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