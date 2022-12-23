package com.apt_x.app.views.activity.card;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityCardDetailNewBinding;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.base.BaseActivity;

public class CardDetailNewActivity extends BaseActivity {

    ActivityCardDetailNewBinding binding;
    Context context = CardDetailNewActivity.this;
    Activity activity = CardDetailNewActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_card_detail_new);
        initializeViews();
    }

    @Override
    public void initializeViews() {

        binding.changepinbtn.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.changepinbtn:
                startActivity(new Intent(CardDetailNewActivity.this, ChangePinActivity.class));
               // finish();
                break;

        }

    }
}