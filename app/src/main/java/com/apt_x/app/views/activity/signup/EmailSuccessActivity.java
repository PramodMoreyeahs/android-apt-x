package com.apt_x.app.views.activity.signup;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityEmailSuccessBinding;
import com.apt_x.app.views.base.BaseActivity;

public class EmailSuccessActivity extends BaseActivity {
    ActivityEmailSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_email_success);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_success);
        initializeViews();
    }

    @Override
    public void initializeViews() {
      /*  Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomanimation);
        binding.successimg.startAnimation(animation);
*/

        final ValueAnimator anim = ValueAnimator.ofFloat(1f, 0.3f);
        anim.setDuration(800);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                binding.successimg.setScaleX((Float) animation.getAnimatedValue());
                binding.successimg.setScaleY((Float) animation.getAnimatedValue());

            }
        });
        anim.setRepeatCount(1);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.start();



        binding.tvContinue.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvContinue:
                startActivity(new Intent(EmailSuccessActivity.this, CaptureImageActivity.class));

                break;

        }

    }
}