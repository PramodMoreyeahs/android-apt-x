package com.apt_x.app.views.activity.account;

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
import com.apt_x.app.databinding.ActivityAddNewAccountBinding;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.model.PorfilePictureUrlResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.activity.profile.ProfileViewModel;
import com.apt_x.app.views.activity.verification.AddAddressActivity;
import com.apt_x.app.views.base.BaseActivity;

public class AddNewAccountActivity extends BaseActivity {
    private ActivityAddNewAccountBinding binding;
    ProfileViewModel viewModel;
    ApiCalls apicalls;
    String name,firstName,lastName,mobile;
    String  profilePicture;
    Context context = AddNewAccountActivity.this;
    Activity activity = AddNewAccountActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_account);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        super.onCreate(savedInstanceState);
        initializeViews();
    }

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
    public void initializeViews() {
        apicalls = ApiCalls.getInstance(this);
        viewModel.response_validator.observe(this, response_observer);
        viewModel.response_validator_picture.observe(this, response_observer_picture);
        viewModel.response_validator_update_profile.observe(this, response_observer_update_picture);

        viewModel.getProfile(apicalls);
    }
    Observer<GetProfileResponse> response_observer = new Observer<GetProfileResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetProfileResponse countriesResponse) {
            assert countriesResponse != null;

        }
    };
    Observer<PorfilePictureUrlResponse> response_observer_picture = new Observer<PorfilePictureUrlResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable PorfilePictureUrlResponse countriesResponse) {
            if (countriesResponse.getData().getFlag()){
                profilePicture=countriesResponse.getData().getData().getFilepath();

            }

        }
    };
    Observer<GetProfileResponse> response_observer_update_picture = new Observer<GetProfileResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetProfileResponse countriesResponse) {
            assert countriesResponse != null;
                startActivity(new Intent(AddNewAccountActivity.this, HomeActivity.class));
                finish();
        }
    };



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvUpdate:
                updateProfile();
                break;
            case R.id.etAddress:
                goToAddress();
                break;

        }
    }

    private void goToAddress() {
        startActivity(new Intent(this, AddAddressActivity.class)
                .putExtra("comeFrom","Profile"));
    }

    private void updateProfile() {
        name=binding.etName.getText().toString();
        String[] splited = name.split(" ");
        firstName = splited[0];
        lastName =splited[1];
       // mobile=binding.etMobile.getText().toString();
        viewModel.doUpdateProfile(firstName,lastName,mobile,profilePicture,apicalls);

    }
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