package com.apt_x.app.views.activity.signup;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityCaptureIdBinding;
import com.apt_x.app.model.GetCompanyConstantResponse;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.kyc.KYCDetailsActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;


public class CaptureIdActivity extends BaseActivity {

    ActivityCaptureIdBinding binding;
    ApiCalls apiCalls;
    Utils utils;
    String moveTo = "";

    byte[] byteArray = null;
    Context context = CaptureIdActivity.this;
    Activity activity = CaptureIdActivity.this;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void initializeViews() {
        apiCalls = ApiCalls.getInstance(this);
        utils = new Utils();
        binding.mainIdPassport.setOnClickListener(this);
        binding.tvRetry.setOnClickListener(this);
        binding.header.tvTitle.setText(getString(R.string.capture_id));
        if (getIntent() != null) {
            moveTo = getIntent().getStringExtra(Keys.detail);
        }
        switch (moveTo) {
            case Keys.passport:
                binding.tvHeader.setText(getString(R.string.capture_id_) + getString(R.string.passport));
                binding.mainIdPassport.setText(getString(R.string.capture_id_) + getString(R.string.passport));
                break;
            case Keys.driving:
                binding.tvHeader.setText(getString(R.string.capture_id_) + getString(R.string.driving_license));
                binding.mainIdPassport.setText(getString(R.string.capture_id_) + getString(R.string.driving_license));
                break;
            case Keys.permanentR:
                binding.tvHeader.setText(getString(R.string.capture_id_) + getString(R.string.permanent_resident_card));
                binding.mainIdPassport.setText(getString(R.string.capture_id_) + getString(R.string.permanent_resident_card));
                break;
            case Keys.identity:
                binding.tvHeader.setText(getString(R.string.capture_id_) + getString(R.string.identity_card));
                binding.mainIdPassport.setText(getString(R.string.capture_id_) + getString(R.string.identity_card));
                break;
            case Keys.citizen:
                binding.tvHeader.setText(getString(R.string.capture_id_) + getString(R.string.citizenship_card));
                binding.mainIdPassport.setText(getString(R.string.capture_id_) + getString(R.string.citizenship_card));
                break;
            case Keys.secure:
                binding.tvHeader.setText(getString(R.string.capture_id_) + getString(R.string.secure_certificate_of_indian_status));
                binding.mainIdPassport.setText(getString(R.string.capture_id));
                break;
        }
    }

    private void navigation(GetCompanyConstantResponse getCompanyConstantResponse) {
        switch (moveTo) {
            case Keys.passport:
                try {
                    startActivity(new Intent(CaptureIdActivity.this, KYCDetailsActivity.class)
                            .putExtra(Keys.sanboxApiKey, getCompanyConstantResponse.getAPTPAY_Sandbox_API())
                            .putExtra(Keys.ApiKey, getCompanyConstantResponse.getAPI_Key())
                            .putExtra(Keys.LiveApiKey, getCompanyConstantResponse.getLIVE_API_Endpoint())
                            .putExtra(Keys.KYCURL, getCompanyConstantResponse.getAdd_kyc())
                            .putExtra(Keys.detail, Keys.passport)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("catch exception" + e.getMessage());
                }

                break;
            case Keys.driving:
                startActivity(new Intent(CaptureIdActivity.this, KYCDetailsActivity.class)
                        .putExtra(Keys.sanboxApiKey, getCompanyConstantResponse.getAPTPAY_Sandbox_API())
                        .putExtra(Keys.ApiKey, getCompanyConstantResponse.getAPI_Key())
                        .putExtra(Keys.LiveApiKey, getCompanyConstantResponse.getLIVE_API_Endpoint())
                        .putExtra(Keys.KYCURL, getCompanyConstantResponse.getAdd_kyc())
                        .putExtra(Keys.detail, Keys.driving)
                );
                break;
            case Keys.permanentR:
                startActivity(new Intent(CaptureIdActivity.this, KYCDetailsActivity.class)
                        .putExtra(Keys.sanboxApiKey, getCompanyConstantResponse.getAPTPAY_Sandbox_API())
                        .putExtra(Keys.ApiKey, getCompanyConstantResponse.getAPI_Key())
                        .putExtra(Keys.LiveApiKey, getCompanyConstantResponse.getLIVE_API_Endpoint())
                        .putExtra(Keys.KYCURL, getCompanyConstantResponse.getAdd_kyc())
                        .putExtra(Keys.detail, Keys.permanentR)
                );

                break;
            case Keys.identity:
                startActivity(new Intent(CaptureIdActivity.this, KYCDetailsActivity.class)
                        .putExtra(Keys.sanboxApiKey, getCompanyConstantResponse.getAPTPAY_Sandbox_API())
                        .putExtra(Keys.ApiKey, getCompanyConstantResponse.getAPI_Key())
                        .putExtra(Keys.LiveApiKey, getCompanyConstantResponse.getLIVE_API_Endpoint())
                        .putExtra(Keys.KYCURL, getCompanyConstantResponse.getAdd_kyc())
                        .putExtra(Keys.detail, Keys.identity)
                );

                break;
            case Keys.citizen:
                startActivity(new Intent(CaptureIdActivity.this, KYCDetailsActivity.class)
                        .putExtra(Keys.sanboxApiKey, getCompanyConstantResponse.getAPTPAY_Sandbox_API())
                        .putExtra(Keys.ApiKey, getCompanyConstantResponse.getAPI_Key())
                        .putExtra(Keys.LiveApiKey, getCompanyConstantResponse.getLIVE_API_Endpoint())
                        .putExtra(Keys.KYCURL, getCompanyConstantResponse.getAdd_kyc())
                        .putExtra(Keys.detail, Keys.citizen)
                );

                break;
            case Keys.secure:
                startActivity(new Intent(CaptureIdActivity.this, KYCDetailsActivity.class)
                        .putExtra(Keys.sanboxApiKey, getCompanyConstantResponse.getAPTPAY_Sandbox_API())
                        .putExtra(Keys.ApiKey, getCompanyConstantResponse.getAPI_Key())
                        .putExtra(Keys.LiveApiKey, getCompanyConstantResponse.getLIVE_API_Endpoint())
                        .putExtra(Keys.KYCURL, getCompanyConstantResponse.getAdd_kyc())
                        .putExtra(Keys.detail, Keys.secure)
                );

                break;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_capture_id);
        initializeViews();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_id_passport:
                if (binding.mainIdPassport.getText().toString().equalsIgnoreCase(getString(R.string.continue_with))) {
                    apiCall();
                } else {
                    (((TedPermission.Builder) TedPermission.with(this).setPermissionListener(permissionlistener)).setDeniedMessage((CharSequence) getString(R.string.txt_permission_message))).setPermissions("android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").check();
                }
                break;
            case R.id.tvRetry:
                (((TedPermission.Builder) TedPermission.with(this).setPermissionListener(permissionlistener)).setDeniedMessage((CharSequence) getString(R.string.txt_permission_message))).setPermissions("android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").check();
                break;
        }
    }

    PermissionListener permissionlistener = new PermissionListener() {
        public void onPermissionGranted() {
            openImageFromGallery();
        }

        public void onPermissionDenied(ArrayList<String> arrayList) {
        }
    };

    public void openImageFromGallery() {
        Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(this);
        startActivityForResult(intent, 201);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 201) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == -1) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    binding.ivCapture.setImageBitmap(bitmap);
                    String bitmapString = BitMapToString(bitmap);
                    MyPref.getInstance(this).writePrefs(MyPref.CaptureIdBitmap, bitmapString);
                    binding.mainIdPassport.setText(getString(R.string.continue_with));
                    binding.llCapture.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_bg_capture_id));
                    Utils.showToast(this, getString(R.string.ImageCaptured));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == 204) {
                result.getError();
            }
        }

    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void apiCall() {
        try {
            Utils.showDialog(this, "Uploading");
            apiCalls.getCompanyConstant(getCompanyConstantDes, Keys.KEYID);
        } catch (Exception e) {
            Utils.showToast(this, getString(R.string.something_went_wrong));
            e.printStackTrace();
        }

    }

    DisposableObserver<GetCompanyConstantResponse> getCompanyConstantDes = new DisposableObserver<GetCompanyConstantResponse>() {
        @Override
        public void onNext(@NonNull GetCompanyConstantResponse getCompanyConstantResponse) {
            Utils.hideProgressDialog();
            System.out.println("Response ::" + new Gson().toJson(getCompanyConstantResponse));
            if (getCompanyConstantResponse.getStatus()) {
                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.EMAILINTERAC, getCompanyConstantResponse.getAptPayEmailofrPayment());
                navigation(getCompanyConstantResponse);
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            Utils.showToast(CaptureIdActivity.this, e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };
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