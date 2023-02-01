package com.apt_x.app.views.activity.signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.acuant.acuantfacecapture.FaceCaptureActivity;
import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityCaptureImageBinding;
import com.apt_x.app.model.NewImageResponseModel;
import com.apt_x.app.model.PorfilePictureUrlResponse;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.kyc.KYCActivity;
import com.apt_x.app.views.activity.profile.MyProfileActivity;
import com.apt_x.app.views.activity.profile.ProfileViewModel;
import com.apt_x.app.views.base.BaseActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CaptureImageActivity extends BaseActivity {

    ActivityCaptureImageBinding binding;
    ProfileViewModel viewModel;
    String capturedurl;
    ApiCalls apicalls;
    public String email = "";
    public String profilePicture = "";
    private Bitmap capturedSelfieImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_capture_image);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_capture_image);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        apicalls = ApiCalls.getInstance(CaptureImageActivity.this);
        initializeViews();


        // System.out.println("Captured Url" + FaceCaptureActivity.OUTPUT_URL);
    }

    @Override
    public void initializeViews() {
        viewModel.response_validator_picture2.observe(this, response_observer_picture2);
        if (getIntent() != null) {

        /*    if(getIntent().getStringExtra(Keys.EMAIL) != null || !getIntent().getStringExtra(Keys.EMAIL).isEmpty()){
                email = getIntent().getStringExtra(Keys.EMAIL);
                MyPref.getInstance(this).writePrefs(MyPref.USER_EMAIL, email);
                System.out.println("SharedPref" + MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.USER_EMAIL));
            }else if(getIntent().getStringExtra("email") != null || !getIntent().getStringExtra("email").isEmpty()){
                email = getIntent().getStringExtra("email");
                MyPref.getInstance(this).writePrefs(MyPref.USER_EMAIL, email);
                System.out.println("SharedPref in email" + MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.USER_EMAIL));
            }*/

            email = getIntent().getStringExtra(Keys.EMAIL);
            MyPref.getInstance(this).writePrefs(MyPref.USER_EMAIL, email);
            System.out.println("SharedPref" + MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.USER_EMAIL));

        } else {
            System.out.println("email in CaptureImage linkelse" + email);
            email = MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.USER_EMAIL);

        }
        viewModel.response_validator_picture.observe(this, response_observer_picture);
        binding.tvContinue.setOnClickListener(this);
        binding.tvcapture.setOnClickListener(this);
        binding.retry.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);

        capturedurl = FaceCaptureActivity.Companion.getAbsolutePath();

        System.out.println("Captured Url callback1" + capturedurl);

        if (capturedurl != null && !capturedurl.isEmpty()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(capturedurl);

            binding.capturedimg.setImageBitmap(myBitmap);
            binding.tvContinue.setVisibility(View.VISIBLE);
            binding.retry.setVisibility(View.VISIBLE);
            binding.tvcapture.setVisibility(View.GONE);
        } else {
            binding.retry.setVisibility(View.GONE);
            binding.tvContinue.setVisibility(View.GONE);
            binding.tvcapture.setVisibility(View.VISIBLE);

        }


    }

    Observer<NewImageResponseModel> response_observer_picture2 = new Observer<NewImageResponseModel>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable NewImageResponseModel countriesResponse) {

            if(countriesResponse.getStatus()){

                System.out.println("Location in edit profile" + countriesResponse.getData().getMessage().toString());
                profilePicture =  countriesResponse.getData().getMessage();
                MyPref.getInstance(getApplicationContext())
                        .writePrefs(MyPref.USER_SELFI, profilePicture);
                startActivity(new Intent(getApplicationContext(), KYCActivity.class)
                        .putExtra(Keys.EMAIL, email));
            }

        }
    };


    Observer<PorfilePictureUrlResponse> response_observer_picture = new Observer<PorfilePictureUrlResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable PorfilePictureUrlResponse captureimageres) {

          //  MyPref.getInstance(getContext()).writePrefs(MyPref.USER_EMAIL, email);

            System.out.println("email in api call" + MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.USER_EMAIL));
            startActivity(new Intent(getApplicationContext(), KYCActivity.class)
                    .putExtra(Keys.EMAIL, email));
       /*     if (!captureimageres.getData().getMessage().isEmpty()){
                System.out.println("Capture Image Uploaded succesfully");
                capturedurl = "";
                Utils.hideProgressDialog();
                                startActivity(new Intent(getApplicationContext(), KYCActivity.class)
                        .putExtra(Keys.EMAIL, email));
            }
*/
        }
    };

    private void newtest() {

        String url = capturedurl;

        byte[] bytes = readFromFile(url);

        capturedSelfieImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }


    private byte[] readFromFile(String fileUri) {     //java code for readfrom file

        File file = new File(fileUri);
        int size = Math.toIntExact(file.length());

        byte[] bytes = new byte[size];
        System.out.println("READFROMFILE" + bytes.length);

        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;

    }


/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99 && resultCode == RESULT_OK) {

            System.out.println("OnAcrivity listened");
        }
    }
*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                startActivity(new Intent(this, PasteLinkActivity.class)
                        .putExtra(Keys.EMAIL, email));
                finish();
                break;
            case R.id.tvContinue:
           File capturedfile = new File(capturedurl);
                System.out.println("captured file" + capturedfile);
                Utils.showDialog(this, "Loading");
               // viewModel.uploadProfile(capturedfile,apicalls);
                viewModel.uploadProfile2(capturedfile,apicalls);

                break;
            case R.id.tvcapture:
                startActivity(new Intent(CaptureImageActivity.this, FaceCaptureActivity.class)
                        .putExtra("captureselfie", "true")
                        .putExtra("email", email));
                break;
            case R.id.retry:
                System.out.println("Captured Url2" + FaceCaptureActivity.OUTPUT_URL);
                startActivityForResult(new Intent(CaptureImageActivity.this, FaceCaptureActivity.class)
                        .putExtra("captureselfie", "true")
                        .putExtra("email", email),99);

        }

    }


}