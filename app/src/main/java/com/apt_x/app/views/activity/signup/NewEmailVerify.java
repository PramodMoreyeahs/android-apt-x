package com.apt_x.app.views.activity.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityNewEmailVerifyBinding;
import com.apt_x.app.privacy.PrivacyPolicy;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.base.BaseActivity;

public class NewEmailVerify extends BaseActivity {
 ActivityNewEmailVerifyBinding binding;
    public ApiCalls apiCalls;
    String email = "";
    SignUpViewModel viewModel;

    Context context = NewEmailVerify.this;
    Activity activity = NewEmailVerify.this;

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
    //    setContentView(R.layout.activity_new_email_verify);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_email_verify);
        initializeViews();
    }

    @Override
    public void initializeViews() {
        binding.ivBack.setOnClickListener(this);
        binding.tvContinue.setOnClickListener(this);

        apiCalls = ApiCalls.getInstance(NewEmailVerify.this);
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        if (getIntent() != null) {
            email = getIntent().getStringExtra(Keys.EMAIL);
            System.out.println("Email in new flow" + email);

        } else {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
                case R.id.tvContinue:
                    startActivity(new Intent(NewEmailVerify.this, PasteLinkActivity.class)
                            .putExtra(Keys.EMAIL, email));
                break;

        }

    }
}