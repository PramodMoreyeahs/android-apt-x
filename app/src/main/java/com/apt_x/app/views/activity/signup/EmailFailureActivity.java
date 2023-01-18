package com.apt_x.app.views.activity.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityEmailFailureBinding;
import com.apt_x.app.views.base.BaseActivity;

public class EmailFailureActivity extends BaseActivity {

    ActivityEmailFailureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_email_failure);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_email_failure);
        initializeViews();
    }

    @Override
    public void initializeViews() {
binding.tvContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvContinue:
                startActivity(new Intent(EmailFailureActivity.this, CaptureImageActivity.class));
                break;

        }
    }
}