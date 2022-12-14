package com.apt_x.app.views.activity;


import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;

import android.content.DialogInterface;
import android.content.res.Configuration;

import android.os.AsyncTask;
import android.os.Bundle;


import android.view.View;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityTermsAndConditionsBinding;
import com.gun0912.tedpermission.PermissionListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class TermsAndConditionsActivity extends BaseActivity {

    ActivityTermsAndConditionsBinding binding;



    Context context = TermsAndConditionsActivity.this;
    Activity activity = TermsAndConditionsActivity.this;
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
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    public void initializeViews() {


        WebSettings settings = binding.pdfView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        binding.pdfView.setWebChromeClient(new WebChromeClient());
      String url="https://4879-103-15-67-130.ngrok.io/api/auth/getAdd?type=policyFR";
        binding.pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);

        binding.pdfView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


   /*     new RetrivePDFfromUrl().execute(BuildConfig.BASE_URL+"auth/getAdd?type=policyFR");
        Log.e("PDF URL **********",BuildConfig.BASE_URL+"auth/getAdd?type=policyFR");*/



    }
    PermissionListener permissionlistener = new PermissionListener() {
        public void onPermissionGranted() {
            Utils.showToast(TermsAndConditionsActivity.this,"Success");
        }

        public void onPermissionDenied(ArrayList<String> arrayList) {
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_terms_and_conditions);

        initializeViews();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        v.getId();
    }


    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            //pdfView.fromStream(inputStream).load();
        }
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