package com.apt_x.app.views.activity.signup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.acuant.acuantfacecapture.FaceCaptureActivity;
import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityCaptureImageBinding;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.views.activity.kyc.KYCActivity;
import com.apt_x.app.views.base.BaseActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CaptureImageActivity extends BaseActivity {

    ActivityCaptureImageBinding binding;
    String capturedurl;
    public String email = "";
    private Bitmap capturedSelfieImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_capture_image);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_capture_image);
        initializeViews();

        // System.out.println("Captured Url" + FaceCaptureActivity.OUTPUT_URL);
    }

    @Override
    public void initializeViews() {

        if (getIntent() != null) {
            email = getIntent().getStringExtra(Keys.EMAIL);
            MyPref.getInstance(this).writePrefs(MyPref.USER_EMAIL, email);
        } else {
            email = MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.USER_EMAIL);

        }

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
                break;
            case R.id.tvContinue:
                capturedurl = "";
                startActivity(new Intent(getApplicationContext(), KYCActivity.class)
                        .putExtra(Keys.EMAIL, email));

                break;
            case R.id.tvcapture:
                startActivity(new Intent(CaptureImageActivity.this, FaceCaptureActivity.class)
                        .putExtra("captureselfie", "true"));
                break;
            case R.id.retry:
                System.out.println("Captured Url2" + FaceCaptureActivity.OUTPUT_URL);
                startActivityForResult(new Intent(CaptureImageActivity.this, FaceCaptureActivity.class)
                        .putExtra("captureselfie", "true"), 99);

        }

    }


}