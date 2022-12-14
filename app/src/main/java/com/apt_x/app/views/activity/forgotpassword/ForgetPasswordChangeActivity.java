package com.apt_x.app.views.activity.forgotpassword;

import android.annotation.SuppressLint;
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
import com.apt_x.app.databinding.ForgetPasswordChangeActivityBinding;
import com.apt_x.app.model.ChangePasswordResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.changepassword.ChangePasswordViewModel;
import com.apt_x.app.views.activity.login.LoginActivity;
import com.apt_x.app.views.base.BaseActivity;

public class ForgetPasswordChangeActivity extends BaseActivity {
    private ForgetPasswordChangeActivityBinding binding;
    ChangePasswordViewModel viewModel;
    public ApiCalls apiCalls;

    Context context = ForgetPasswordChangeActivity.this;
    Activity activity = ForgetPasswordChangeActivity.this;
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
        binding = DataBindingUtil.setContentView(this, R.layout.forget_password_change_activity);
        super.onCreate(savedInstanceState);
        initializeViews();
    }

    @Override
    public void initializeViews() {
        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        apiCalls = ApiCalls.getInstance(ForgetPasswordChangeActivity.this);
        viewModel.response_validator.observe(this,resetObserver);
        viewModel.validator.observe(this,observer);
        binding.ivBack.setOnClickListener(this);
        binding.tvContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
        }else if (view==binding.tvContinue){
            validate();



        }
    }

    private void validate() {

        if(binding.etNew.getText().toString().isEmpty())
        {
            Utils.showToast(getApplicationContext(),getString(R.string.enter_password));
        }
        else if(binding.etConfNew.getText().toString().isEmpty())
        {
            Utils.showToast(getApplicationContext(),getString(R.string.enter_confirm_password));
        }
        else if(!binding.etNew.getText().toString().equals(binding.etConfNew.getText().toString()))
        {
            Utils.showToast(getApplicationContext(),getString(R.string.confirm_password_match));
        }
        else if(!(binding.etNew.getText().toString().length()>=8))
        {
            Utils.showToast(getApplicationContext(),getString(R.string.password_should_be_8_character));
        }
        else
        {
            Utils.showDialog(this, getString(R.string.please_wait));
            String email=getIntent().getStringExtra("email");
            viewModel.changeForgetPassword(binding,apiCalls,email);
        }
    }

    Observer<ChangePasswordResponse> resetObserver= new Observer<ChangePasswordResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable ChangePasswordResponse changePasswordBean) {
            if(changePasswordBean.getStatus())
            {
                Utils.showToast(getApplicationContext(),changePasswordBean.getMessage());
                startActivity(new Intent(ForgetPasswordChangeActivity.this, LoginActivity.class));


            }
            else
            {
                Utils.showToast(getApplicationContext(),changePasswordBean.getMessage());
            }


        }
    };
    Observer observer = (Observer<Integer>) value -> {
        switch (value) {
            case AppUtils.CONFIRMPASSWORD:
                Utils.hideProgressDialog();
                Utils.showToast(getApplicationContext(), getString(R.string.confirm_password_match));
                break;

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