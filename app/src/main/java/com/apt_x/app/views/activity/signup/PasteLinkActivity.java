package com.apt_x.app.views.activity.signup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityPasteLinkBinding;
import com.apt_x.app.model.LinkVerifyModel;
import com.apt_x.app.model.VerifyOtpResponse;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.kyc.KYCActivity;
import com.apt_x.app.views.base.BaseActivity;

public class PasteLinkActivity extends BaseActivity {

    ActivityPasteLinkBinding binding;
    SignUpViewModel viewModel;
    public ApiCalls apiCalls;
    String email = "";
    Context context = PasteLinkActivity.this;
    Activity activity = PasteLinkActivity.this;

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
        //setContentView(R.layout.activity_paste_link);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_paste_link);
        initializeViews();
    }

    @Override
    public void initializeViews() {
        binding.ivBack.setOnClickListener(this);
        binding.tvContinue.setOnClickListener(this);
        binding.Resendlink.setOnClickListener(this);
        apiCalls = ApiCalls.getInstance(PasteLinkActivity.this);

        if (getIntent() != null) {
            email = getIntent().getStringExtra(Keys.EMAIL);

        } else {

        }

        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        viewModel.response_linkverify.observe(this, response_observer);
        viewModel.response_resendlink.observe(this, response_observer_link);

    }

    Observer<LinkVerifyModel> response_observer = new Observer<LinkVerifyModel>() {

        @Override
        public void onChanged(@Nullable LinkVerifyModel loginBean) {
            if (loginBean == null) {
                Utils.showAlert(getApplicationContext(), "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            if (loginBean.getStatus()) {
                startActivity(new Intent(PasteLinkActivity.this, EmailSuccessActivity.class));

                finish();
            } else {
                startActivity(new Intent(PasteLinkActivity.this, EmailFailureActivity.class)
                        .putExtra(Keys.EMAIL, email));



            }
        }
    };

    Observer<LinkVerifyModel> response_observer_link = new Observer<LinkVerifyModel>() {

        @Override
        public void onChanged(@Nullable LinkVerifyModel loginBean) {
            if (loginBean == null) {
                Utils.showAlert(getApplicationContext(), "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            if (loginBean.getStatus()) {

                Toast.makeText(context, "Link Sent", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();

            }
        }
    };


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.Resendlink:
                viewModel.ResendLink(email,apiCalls);
                break;
            case R.id.tvContinue:
                navfun();

                break;

        }
    }

    private void navfun() {
        System.out.println("Email success clicked");
        viewModel.verifyLink(binding.etEmail.getText().toString(),email,apiCalls);
    }
}