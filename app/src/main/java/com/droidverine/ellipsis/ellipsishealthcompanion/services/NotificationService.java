package com.droidverine.ellipsis.ellipsishealthcompanion.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.droidverine.ellipsis.ellipsishealthcompanion.Activities.MainActivity;
import com.droidverine.ellipsis.ellipsishealthcompanion.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;




public class NotificationService extends FirebaseMessagingService {
        NotificationManager manager;
    public static final String Techxter_notification_channel_id="siesgst.techxter.com.techxter";
    public static final String Techxter_notification_channel_name="TechXter";
        private static final String TAG = "MyFirebaseMsgService";

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
//FOR OREO NOTIFICATION

                    //if API VERSION is OREO AND ABOVE THEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CreateChannels();
                Notification.Builder builder = getTECHXTERNOT(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                getManager().notify(new Random().nextInt(), builder.build());
                Log.d(TAG, "FROM:" + remoteMessage.getFrom());
            }
            Log.d(TAG, "FROM:" + remoteMessage.getFrom());
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data: " + remoteMessage.getData());
            }
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Mesage body:" + remoteMessage.getNotification().getBody());
                sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
            }
}


       private void sendNotification(String body, String title) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           intent.putExtra("noti","noti");
           PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_chat)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(notificationSound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
        }
//ALL BELOW OREO NOTIFICATION
    public NotificationManager getManager() {
        if(manager==null)
        {
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public void CreateChannels( )
    {
Log.d("notchannel","notchannel");
        NotificationChannel TECHXTER=new NotificationChannel(Techxter_notification_channel_id,Techxter_notification_channel_name, NotificationManager.IMPORTANCE_HIGH);
        TECHXTER.enableLights(true);
        TECHXTER.enableVibration(true);
        TECHXTER.setLightColor(Color.RED);

        TECHXTER.setShowBadge(true);
        TECHXTER.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(TECHXTER);

    }
    public Notification.Builder getTECHXTERNOT(String title, String data)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("noti","noti");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);

        return new Notification.Builder(getApplicationContext(),Techxter_notification_channel_id)
                .setContentTitle(title).setContentText(data).setSmallIcon(R.drawable.ic_chat).setAutoCancel(true).setContentIntent(pendingIntent);
    }
}
