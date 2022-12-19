package com.apt_x.app.views.activity.kyc;


import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityKycfailedBinding;
import com.apt_x.app.views.activity.forgotpassword.ForgotPasswordActivity;
import com.apt_x.app.views.activity.login.LoginActivity;
import com.apt_x.app.views.base.BaseActivity;

public class KYCfailedActivity extends BaseActivity {

   private ActivityKycfailedBinding binding;

   Context context = KYCfailedActivity.this;
   Activity activity = KYCfailedActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kycfailed);
      //  setContentView(R.layout.activity_kycfailed);
        initializeViews();

    }

    @Override
    public void initializeViews() {
     binding.mailtext2.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent =new Intent(Intent.ACTION_SENDTO);
             intent.setData(Uri.parse("mailto:Support@aptpay.com"));
             startActivity(intent);
         }
     });

     binding.calltext2.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent =new Intent(Intent.ACTION_DIAL);
             intent.setData(Uri.parse("tel:1.866.303.3955"));
             startActivity(intent);
         }
     });


     binding.tvContinue.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             startActivity(new Intent(KYCfailedActivity.this, LoginActivity.class));
             finish();
         }
     });

    }

    @Override
    public void onClick(View view) {

    }
}