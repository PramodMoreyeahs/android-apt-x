package com.apt_x.app.views.activity.signup;

import static com.acuant.acuantfacecapture.FaceCaptureActivity.RESPONSE_SUCCESS_CODE_CAPTUREIMG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.acuant.acuantfacecapture.FaceCaptureActivity;
import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityCaptureImageBinding;
import com.apt_x.app.views.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;

public class CaptureImageActivity extends BaseActivity {

    ActivityCaptureImageBinding binding;

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
        binding.tvContinue.setOnClickListener(this);

        String capturedurl = FaceCaptureActivity.Companion.getAbsolutePath();

        System.out.println("Captured Url callback1" + capturedurl);

        if(capturedurl != null && !capturedurl.isEmpty()){
            File imgFile = new File(capturedurl);

            if(imgFile.getAbsolutePath() != null){
                System.out.println("Captured Url imgFile" + imgFile.getAbsolutePath());
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

               // binding.capturedimg.setImageBitmap(myBitmap);
                 Picasso.with(this).load(imgFile.getAbsolutePath()).into(binding.capturedimg);
                System.out.println("Captured Url callback2" + capturedurl);
                capturedurl="";

            }else{
                System.out.println("Captured Url imgFile else" + imgFile.getAbsolutePath());
            }





        }else{
            System.out.println("captured is empty");
        }


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.tvContinue:
                System.out.println("Captured Url2" + FaceCaptureActivity.OUTPUT_URL);
                startActivity(new Intent(CaptureImageActivity.this, FaceCaptureActivity.class)
                        .putExtra("captureselfie", "true"));

        }

    }


}