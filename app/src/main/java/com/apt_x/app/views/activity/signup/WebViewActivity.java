package com.apt_x.app.views.activity.signup;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityWebViewBinding;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.views.base.BaseActivity;

public class WebViewActivity extends BaseActivity {

    ActivityWebViewBinding binding;
    String verifylink = "";
    private String TAG = "TEST";

    String[] permissions = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS};
    private static final String[] PERM_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };

    private PermissionRequest mPermissionRequest;
    ValueCallback<Uri> uploadMsg;
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private int requestCode = 1;
    WebView webView;



    private static final int MY_CAMERA_REQUEST_CODE = 1;
    private static final int MY_REQUEST_VIDEO_CAPTURE = 2;
    private final ActivityResultLauncher<String[]> locationPermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        if (result.containsValue(false)) {
            System.out.println("New location permission" + result);
            //one of the permissions is not granted, use result[0] to get result for individual permissions
        } else {
            System.out.println("New location permission" + result);

            //all permissions granted do something
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        initializeViews();
        foregraoundservicess();
    }

    private void foregraoundservicess() {

 /*       Intent i = new Intent(this, MyForegroundService.class);
        this.startService(i);
      //  Intent notificationIntent = new Intent(this, YourService.class);*/

        Intent serviceIntent = new Intent(this, MyForegroundService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }

    }

    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(MyForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }




    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initializeViews() {


        if (getIntent() != null) {
           verifylink = getIntent().getStringExtra(Keys.VERIFYLINK);

            // verifylink = "https://verifypro.sandbox.aptpay.com/?reference=QX2AKH3PQV7";
            //  verifylink = "https://cordova-rtc.github.io/cordova-plugin-iosrtc-sample/index.html";
            System.out.println("verifylink in new flow" + verifylink);
        }
        webView = this.binding.webView1;
        WebViewSetup();
        isPermissionGranted();
        // End setWebChromeClient








//        isPermissionGranted();





      /*  webView.setWebChromeClient(new WebChromeClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                                                     *//*      runOnUiThread(() -> {
                                                             System.out.println("package manager"*//**//* + PackageManager.PERMISSION_GRANTED*//**//* + request + Build.VERSION_CODES.M + Build.VERSION.SDK_INT);

                                                             final String[] requestedResources = request.getResources();
                                                             request.grant(requestedResources);
                                                         });*//*







         *//*     webkitPermissionRequest = request;
                final String[] requestedResources = request.getResources();
                for (String r : requestedResources) {
                    if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                        Permission();

                        //    demandForPermission(request.getOrigin().toString(), Manifest.permission.RECORD_AUDIO, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                    }
                }*//*



         *//*

                String permission = Manifest.permission.CAMERA;
                int grant = ContextCompat.checkSelfPermission(WebViewActivity.this, permission);
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    String[] permission_list = new String[1];
                    permission_list[0] = permission;
                    ActivityCompat.requestPermissions(WebViewActivity.this, permission_list, 1);
                }
*//*



         *//*       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

                        }else{
                            System.out.println("Granted");
                        }
                    }*//*
                //  }
            }

        });*/


    }

    private void isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERM_CAMERA, MY_CAMERA_REQUEST_CODE);

            } else {
                System.out.println("Granted  permission");
                webView.loadUrl(verifylink);
            }
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void WebViewSetup() {

        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setUseWideViewPort(true);
        webView.setInitialScale(1);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDatabaseEnabled(true);
        webSettings.setSupportZoom(true);
        webView.setLayerType(View.LAYER_TYPE_NONE, null);

        webView.setWebChromeClient(new WebChromeClient() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                final String[] requestedResources = request.getResources();
                String[] PERMISSIONS = {
                        PermissionRequest.RESOURCE_VIDEO_CAPTURE,};
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(PERM_CAMERA, MY_CAMERA_REQUEST_CODE);

                } else {
                    System.out.println("Granted  permission" + "Orgin::" + request.getOrigin().toString());
                    for (String r : requestedResources) {
                        if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                     /* //  requestPermissions(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE, PermissionRequest.RESOURCE_AUDIO_CAPTURE}, MY_REQUEST_VIDEO_CAPTURE);
                       // locationPermissions.launch(requestedResources);
                        request.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
                       // request.getResources();*/

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WebViewActivity.this)
                                    .setTitle("Allow Permission to camera")
                                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            request.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
                                            // isPermissionGranted();
                                            Log.d(TAG,"Granted inside dialog");
                                        }
                                    })
                                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            request.deny();
                                            Log.d(TAG,"Denied in dialog");
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();



                            break;
                        } else {
                            System.out.println("webChromeClient:: " + request);
                        }
                    }

                }
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String[] PERMISSIONS = {
                            PermissionRequest.RESOURCE_VIDEO_CAPTURE,};
                    locationPermissions.launch(PERMISSIONS);

                    final String[] requestedResources = request.getResources();
                    // request.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});


                    request.grant(requestedResources);

                    requestPermissions(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE, PermissionRequest.RESOURCE_AUDIO_CAPTURE}, MY_REQUEST_VIDEO_CAPTURE);

                }*/
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
               // super.onPermissionRequestCanceled(request);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //super.onPageFinished(view, url);
                System.out.println("Onpage finished" + url);
            }
        });


        //  webView.loadUrl("http://www.tutorialspoint.com");
//       openUrlInChrome(verifylink);

        /*webView.loadUrl(verifylink);*/


    }

    @Override
    protected void onStop() {
        super.onStop();
        /**
         * When the application falls into the background we want to stop the media stream
         * such that the camera is free to use by other apps.
         */
        webView.evaluateJavascript("if(window.localStream){window.localStream.stop();}", null);
    }

    /* private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    System.out.println("isGranted" + isGranted);
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });*/

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE || requestCode == MY_REQUEST_VIDEO_CAPTURE) {
            System.out.println("GrantResult" + grantResults.length);

            if (hasAllPermissionsGranted(grantResults)) {
                System.out.println("GrantResult True");
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {
                System.out.println("GrantResult false");
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }


       /*     if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            //if (Arrays.binarySearch(grantResults, -1) >= 0)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            }

            else{
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }*/
        } else {
            Toast.makeText(this, "camera request code" + requestCode, Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onClick(View view) {

    }

    void openUrlInChrome(String url) {
        try {
            try {
                Uri uri = Uri.parse("googlechrome://navigate?url=" + url);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } catch (ActivityNotFoundException e) {
                Uri uri = Uri.parse(url);
                // Chrome is probably not installed
                // OR not selected as default browser OR if no Browser is selected as default browser
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        } catch (Exception ex) {
            // Timber.e(ex, null);
        }
    }
    // Return here when file selected from camera or from SDcard

}


