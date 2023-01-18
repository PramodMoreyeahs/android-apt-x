package com.apt_x.app.views.activity.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityPasteLinkBinding;
import com.apt_x.app.views.base.BaseActivity;

public class PasteLinkActivity extends BaseActivity {

    ActivityPasteLinkBinding binding;

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

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvContinue:
                navfun();

                break;

        }
    }

    private void navfun() {
        System.out.println("Email success clicked");
        startActivity(new Intent(PasteLinkActivity.this, EmailSuccessActivity.class));
    }
}