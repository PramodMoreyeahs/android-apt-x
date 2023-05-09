package com.apt_x.app.views.activity.signup;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.webkit.PermissionRequest;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.apt_x.app.R;

public class MyForegroundService extends Service {
    PermissionRequest request;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Log.e("Service", "Foreground Service is running...");
                            try {
                                Thread.sleep(2000);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                      //  requestPermissions(PERM_CAMERA, MY_CAMERA_REQUEST_CODE);

                                    } else {
                                        System.out.println("Granted  permission inside foreground services");
                                        String[] PERMISSIONS = {
                                                PermissionRequest.RESOURCE_VIDEO_CAPTURE,};
                                        //request.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
                                      // webView.loadUrl(verifylink);
                                    }
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();

        final String CHANNELID = "Foreground Service ID";
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    CHANNELID,
                    CHANNELID,
                    NotificationManager.IMPORTANCE_LOW
            );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getSystemService(NotificationManager.class).createNotificationChannel(channel);
            }
        }
        Notification.Builder notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNELID)
                    .setContentText("Activity Recognition is running....")
                    .setContentTitle("Apt-X Pay")
                    .setSmallIcon(R.mipmap.ic_launcher);
        }

        startForeground(1001, notification.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
