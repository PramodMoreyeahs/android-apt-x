package com.apt_x.app.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.apt_x.app.application.MyApp;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.R;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("response", new Gson().toJson(remoteMessage.getData()));

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getNotification() != null) {
                //handle the data message here
                String title = remoteMessage.getNotification().getTitle();
                String body = remoteMessage.getNotification().getBody();
                String imgUrl = remoteMessage.getData().get("ImageURL");

                sendNotification(title, body);
                Log.e("notification title", "" + title);
                Log.e("notification body", "" + body);
                Log.e("notification image", "" + imgUrl);
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    public static void sendNotification(String title, String body) {
        try {

          /*  final RemoteViews remoteViews = new RemoteViews(MyApp.getInstance().getPackageName(), R.layout.remote_layout);
            remoteViews.setImageViewResource(R.id.remoteview_notification_icon, R.mipmap.ic_launcher2);
            remoteViews.setTextViewText(R.id.remoteview_notification_headline, pushDataBean.getTitle());
            remoteViews.setTextViewText(R.id.remoteview_notification_short_message, pushDataBean.getMessage());
          */
            NotificationManager notificationManager = (NotificationManager) MyApp.getInstance().getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationId = 1;
            String channelId = "consumer";
            String channelName = "Test";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }

            long when = System.currentTimeMillis();
            NotificationManager mNotificationManager = (NotificationManager) MyApp.getInstance().getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = null;
            Intent notificationIntent = new Intent(MyApp.getInstance().getApplicationContext(), HomeActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            contentIntent = PendingIntent.getActivity(MyApp.getInstance().getApplicationContext(),
                    (int) when, notificationIntent, 0);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(MyApp.getInstance().getApplicationContext(), channelId)
                            .setSmallIcon(getNotificationIconnew())
                            .setLargeIcon(BitmapFactory.decodeResource(MyApp.getInstance().getApplicationContext().getResources(), R.drawable.app_logo))
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setColor(Color.BLACK)
                            .setSound(defaultSoundUri)
                            .setContentIntent(contentIntent);
            Notification notification = notificationBuilder.
                    setStyle(new NotificationCompat.BigTextStyle().
                            bigText(body)).build();
            //  notification.bigContentView = remoteViews;

            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_ALL;
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            mNotificationManager.notify(m, notification);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendBigNotification(String title, String body, Bitmap image) {
        try {
            NotificationManager notificationManager = (NotificationManager) MyApp.getInstance().getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "consumer";
            String channelName = "Test";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }

            long when = System.currentTimeMillis();
            NotificationManager mNotificationManager = (NotificationManager) MyApp.getInstance().getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = null;
            Intent notificationIntent = new Intent(MyApp.getInstance().getApplicationContext(), HomeActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            contentIntent = PendingIntent.getActivity(MyApp.getInstance().getApplicationContext(),
                    (int) when, notificationIntent, 0);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(MyApp.getInstance().getApplicationContext(), channelId)
                            .setSmallIcon(getNotificationIconnew())
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setColor(Color.BLACK)
                            .setSound(defaultSoundUri)
                            .setContentIntent(contentIntent);

            NotificationCompat.BigPictureStyle bpStyle = new NotificationCompat.BigPictureStyle();
            bpStyle.bigPicture(image).build();

            Notification notification = notificationBuilder.
                    setStyle(bpStyle).build();

            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_ALL;
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            mNotificationManager.notify(m, notification);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getNotificationIconnew() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.app_logo : R.drawable.app_logo;
    }

}