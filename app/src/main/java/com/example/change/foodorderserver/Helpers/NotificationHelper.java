package com.example.change.foodorderserver.Helpers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.example.change.foodorderserver.R;

public class NotificationHelper extends ContextWrapper {
    private static final String CHANGE_CHANNEL_ID = "com.example.change.foodorderserver";
    private static final String CHANGE_CHANNEL_NAME = "FoodOrder";
    NotificationManager manager;




    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel();
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel channel = new NotificationChannel(CHANGE_CHANNEL_ID,
                CHANGE_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);





    }

    public NotificationManager getManager() {

        if (manager == null)
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }


    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getChanneledNotofication(String title, String body, PendingIntent contentIntent, Uri soundUri){
        return   new Notification.Builder(getApplicationContext(),CHANGE_CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setSound(soundUri)
                .setAutoCancel(false).setStyle(new Notification.BigTextStyle());

    }
}
