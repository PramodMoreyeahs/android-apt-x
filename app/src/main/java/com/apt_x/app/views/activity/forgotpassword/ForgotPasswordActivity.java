package com.apt_x.app.views.activity.forgotpassword;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.model.ResendOtpResponses;
import com.apt_x.app.model.bean.ForgotPasswordResponseBean;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ActivityForgotPasswordBinding;
import com.apt_x.app.views.customview.Link;

public class ForgotPasswordActivity extends BaseActivity {

    ActivityForgotPasswordBinding binding;
    ForgotViewModel viewModel;
    ApiCalls apiCalls;
    Context context = ForgotPasswordActivity.this;
    Activity activity = ForgotPasswordActivity.this;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        initializeViews();
        context = this;
    }

    @Override
    public void initializeViews() {
        apiCalls = ApiCalls.getInstance(ForgotPasswordActivity.this);
        viewModel = ViewModelProviders.of(this).get(ForgotViewModel.class);
        viewModel.response_validator_resend.observe(this, response_observer_resend);
        viewModel.validator.observe(this,observer);
        binding.ivBack.setOnClickListener(this);
     Utils.addLink(binding.tvBackToSignIn,getString(R.string.sign_in),true,
             getResources().getColor(R.color.colorAccent)).setOnClickListener(new Link.OnClickListener() {
         @Override
         public void onClick(String clickedText) {
             onBackPressed();
         }
     });
     binding.tvContinue.setOnClickListener(this);

        //    viewModel = ViewModelProviders.of(this).get(ForgotViewModel.class);
        // binding.setViewModel(viewModel);
//        viewModel.validator.observe(this, observer);
//        viewModel.response_validator.observe(this, response_observer);
    }

    Observer observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.empty_id:
                    Utils.showToast(context, getString(R.string.enter_employeeid));
                    break;

                case AppUtils.invalid_mail:
                    Utils.showToast(context, getString(R.string.enter_valid_employeeid));
                    break;
            }
        }
    };


    Observer<ForgotPasswordResponseBean> response_observer = new Observer<ForgotPasswordResponseBean>() {

        @Override
        public void onChanged(@Nullable ForgotPasswordResponseBean loginBean) {

            if (loginBean == null) {
                Utils.showAlert(context, getString(R.string.error), getString(R.string.enter_employeeid), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
            }

        }
    };

    @Override
    public void onClick(View view) {
        if (view == binding.tvContinue) {
           if(viewModel.checkEmail(binding.etEmail.getText().toString())){
               viewModel.resendOtp(binding.etEmail.getText().toString(),apiCalls);
           }



        }
        if(view == binding.ivBack){
            onBackPressed();
        }
    }
    Observer<ResendOtpResponses> response_observer_resend = new Observer<ResendOtpResponses>() {

        @Override
        public void onChanged(@Nullable ResendOtpResponses loginBean) {
            if (loginBean!=null) {
                startActivity(new Intent(ForgotPasswordActivity.this, ForgotPasswordVerificationActivity.class)
                        .putExtra("email",binding.etEmail.getText().toString()));
                //Utils.showAlert(getApplicationContext(), "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);

            }
            else {
                  Utils.showToast(getApplicationContext(),"error");
            }
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
