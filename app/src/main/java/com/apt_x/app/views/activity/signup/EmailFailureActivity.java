package com.apt_x.app.views.activity.signup;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityEmailFailureBinding;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.views.base.BaseActivity;

public class EmailFailureActivity extends BaseActivity {

    ActivityEmailFailureBinding binding;
    String email = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_email_failure);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_failure);
        initializeViews();
    }

    @Override
    public void initializeViews() {
        binding.tvContinue.setOnClickListener(this);
        if (getIntent() != null) {
            email = getIntent().getStringExtra(Keys.EMAIL);

        } else {

        }

        final ValueAnimator anim = ValueAnimator.ofFloat(1f, 0.3f);
        anim.setDuration(900);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                binding.failedimage.setScaleX((Float) animation.getAnimatedValue());
                binding.failedimage.setScaleY((Float) animation.getAnimatedValue());
            }
        });
        anim.setRepeatCount(1);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.start();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvContinue:
                startActivity(new Intent(EmailFailureActivity.this, PasteLinkActivity.class)
                        .putExtra(Keys.EMAIL, email));
                break;

        }
    }
}