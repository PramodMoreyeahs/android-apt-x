package com.apt_x.app.views.activity.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.PersonalProfileBinding;
import com.bumptech.glide.Glide;

public class PersonalDetailActivity extends BaseActivity {
    private PersonalProfileBinding binding;
    ProfileViewModel viewModel;
    ApiCalls apicalls;
    Context context = PersonalDetailActivity.this;
    Activity activity = PersonalDetailActivity.this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.personal_profile);
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
        viewModel.getProfile(apicalls);
        binding.ivBack.setOnClickListener(this);
        binding.ivEditProfile.setOnClickListener(this);

    }

    Observer<GetProfileResponse> response_observer = new Observer<GetProfileResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetProfileResponse countriesResponse) {
            assert countriesResponse != null;
            if (countriesResponse.getUser() != null) {
                if(countriesResponse.getUser().getCity()!=null)
                {
                    binding.tvCity.setVisibility(View.VISIBLE);
                    binding.ivLocation.setVisibility(View.VISIBLE);
                    binding.etAddress.setText(countriesResponse.getUser().getStreet()!=null?countriesResponse.getUser().getStreet()+", "
                            +MyProfileActivity.streetaddress2
                            +countriesResponse.getUser().getZip()+", "
                            +countriesResponse.getUser().getCity()+", "
                            +countriesResponse.getUser().getState():"");
                    binding.tvCity.setText(countriesResponse.getUser().getStreet()+", "
                            +MyProfileActivity.streetaddress2
                            +countriesResponse.getUser().getZip()+", "
                            +countriesResponse.getUser().getCity()+", "
                            +countriesResponse.getUser().getState());
                }
                else{
                    binding.tvCity.setVisibility(View.GONE);
                    binding.ivLocation.setVisibility(View.GONE);
                }


                if(countriesResponse.getUser().getFirstName()!=null)
                binding.tvName.setText(countriesResponse.getUser().getFirstName()+" "+countriesResponse.getUser().getLastName());
                if(countriesResponse.getUser().getMobile()!=null)
                binding.tvNumber.setText(countriesResponse.getUser().getMobile());
                if(countriesResponse.getUser().getEmail()!=null)
                binding.tvEMail.setText(countriesResponse.getUser().getEmail());
                if (countriesResponse.getUser().getProfilePicture()!=null){
                    Glide
                            .with(PersonalDetailActivity.this)
                            .asBitmap()
                            .load(countriesResponse.getUser().getProfilePicture())
                            //.placeholder()
                            .into(binding.ivProfile);
                }
                else {

                    byte[] b = Base64.decode(MyPref.getInstance(PersonalDetailActivity.this).readPrefs(MyPref.USER_SELFI1), Base64.DEFAULT);
                    Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
                    binding.ivProfile.setImageBitmap(bitmapImage);
                }
                /*binding.tvCity.setText(countriesResponse.getUser().getStreet()+", "
                        +countriesResponse.getUser().getStreet_line_2()+", "
                        +countriesResponse.getUser().getCity()+", "
                        +countriesResponse.getUser().getState());*/
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
        }
        if (view == binding.ivEditProfile) {
            goToEditProfile();
        }

    }

    private void goToEditProfile() {
        startActivity(new Intent(this, EditProfileActivity.class));
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