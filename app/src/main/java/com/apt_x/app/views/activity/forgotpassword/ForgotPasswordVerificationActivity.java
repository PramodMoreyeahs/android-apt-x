package com.apt_x.app.views.activity.forgotpassword;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.model.ResendOtpResponses;
import com.apt_x.app.model.VerifyOtpResponse;
import com.apt_x.app.model.bean.ForgotPasswordResponseBean;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.ExtendedTextWatcher;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ForgotPasswordBinding;
import com.apt_x.app.views.customview.Link;

public class ForgotPasswordVerificationActivity extends BaseActivity {

    ForgotPasswordBinding binding;
    ForgotViewModel viewModel;
    public ApiCalls apiCalls;
    Intent intent;
    String email ="";
    String storeemail ="";
    boolean isTimerOn = true;
    CountDownTimer countDownTimer;
    Context context = ForgotPasswordVerificationActivity.this;
    Activity activity = ForgotPasswordVerificationActivity.this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        context = this;
    }

    @Override
    public void initializeViews() {
        startTimer();
        binding = DataBindingUtil.setContentView(this, R.layout.forgot_password);
        binding.tvContinue.setOnClickListener(this);
        viewModel = ViewModelProviders.of(this).get(ForgotViewModel.class);
        apiCalls = ApiCalls.getInstance(ForgotPasswordVerificationActivity.this);

        //        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator_otp.observe(this, response_observer_resend);
        viewModel.response_validator_resend.observe(this, response_observer_resend2);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        email = bundle.getString("email");
       // String text="<font color=#676767>"+getString(R.string.enter_ver)+"</font> <font color=#2FB1F8>"+email+"</font>";
        String text="<font color=#676767>"+getString(R.string.enter_ver)+ " " +email;

        binding.tvVerifyCode.setText(Html.fromHtml(text));
        binding.ivBack.setOnClickListener(this);
        binding.etOtp.addTextChangedListener(otpWatcher);
    }

    ExtendedTextWatcher otpWatcher = new ExtendedTextWatcher() {
        @Override
        public void onTextChange(CharSequence s, int start, int before, int count) {
            char[] cArr = s.toString().trim().toCharArray();
            binding.tvCodeOne.setText("-");
            binding.tvCodeTwo.setText("-");
            binding.tvCodeThree.setText("-");
            binding.tvCodeFour.setText("-");
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
        }
    };

    Observer observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.empty_id:
                    Utils.showToast(context, getString(R.string.enter_employeeid));
                    break;

                case AppUtils.empty_password:
                    Utils.showToast(context, getString(R.string.enter_password));
                    break;
            }
        }
    };


    Observer<ForgotPasswordResponseBean> response_observer = new Observer<ForgotPasswordResponseBean>() {

        @Override
        public void onChanged(@Nullable ForgotPasswordResponseBean loginBean) {

          if(loginBean.getSuccess())
          {

          }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvContinue:
               /* startActivity(new Intent(ForgotPasswordVerificationActivity.this, LoginActivity.class));
                finish();*/
                if (!binding.etOtp.getText().toString().isEmpty()) {
                    countDownTimer.cancel();
                    viewModel.verifyOtp(binding.etOtp.getText().toString(),email,apiCalls);
                   /* startActivity(new Intent(ForgotPasswordVerificationActivity.this, ChangePasswordActivity.class));
                    finish();*/
                }else {
                    Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    Observer<VerifyOtpResponse> response_observer_resend = new Observer<VerifyOtpResponse>() {

        @Override
        public void onChanged(@Nullable VerifyOtpResponse loginBean) {
          if(loginBean.getStatus())
          {
              startActivity(new Intent(ForgotPasswordVerificationActivity.this, ForgetPasswordChangeActivity.class)
                      .putExtra("email",email));
          }
          else
          {
              if(loginBean.getMessage().equals("otp did not match"))
              {
                  Utils.showToast(ForgotPasswordVerificationActivity.this,getString(R.string.otp_not_match));
              }
              else if(loginBean.getMessage().equals("User not registered"))
              {
                  Utils.showToast(ForgotPasswordVerificationActivity.this,getString(R.string.user_not_register));
              }
              else
              {
                  Utils.showToast(ForgotPasswordVerificationActivity.this,loginBean.getMessage());
              }
          }
        }
    };

    Observer<ResendOtpResponses> response_observer_resend2 = new Observer<ResendOtpResponses>() {

        @Override
        public void onChanged(@Nullable ResendOtpResponses loginBean) {
            if(loginBean.isStatus())
            {
                Utils.showToast(getApplicationContext(),loginBean.getMessage()+" to "+email);
                startTimer();
            }
        }
    };
    public void startTimer() {
        isTimerOn = true;
        countDownTimer = new CountDownTimer(60000, 1000) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                isTimerOn = true;
                long remaingTime = millisUntilFinished / 1000;
                binding.txtResend.setText(" " + getString(R.string.txt_resend_in) + " " + "("+remaingTime+")");
                //here you can have your logic to set text to edittext
                String pattern = " " + getString(R.string.txt_resend_in) + " " ;
                Utils.addLink(binding.txtResend, pattern, true, ContextCompat.getColor(getApplicationContext(), R.color.text_gray));
            }

            public void onFinish() {
                isTimerOn = false;
                binding.txtResend.setText(getString(R.string.txt_not_recieve) + " " + getString(R.string.txt_send_otp));
                String pattern = " " + getString(R.string.txt_send_otp);

                Utils.addLink(binding.txtResend, pattern, true, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)).
                        setOnClickListener(new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                if (!isTimerOn) {
                                    Utils.showDialog(ForgotPasswordVerificationActivity.this, getString(R.string.please_wait));
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
