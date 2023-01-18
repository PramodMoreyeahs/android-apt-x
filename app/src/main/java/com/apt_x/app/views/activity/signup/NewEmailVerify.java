package com.apt_x.app.views.activity.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityNewEmailVerifyBinding;
import com.apt_x.app.privacy.PrivacyPolicy;
import com.apt_x.app.views.base.BaseActivity;

public class NewEmailVerify extends BaseActivity {
 ActivityNewEmailVerifyBinding binding;

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
                case R.id.tvContinue:
                    startActivity(new Intent(NewEmailVerify.this, PasteLinkActivity.class));
                break;

        }

    }
}