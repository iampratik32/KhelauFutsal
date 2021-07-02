package com.example.khelaufutsal.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class OreoNotification extends ContextWrapper {
    private static final String CHANNEL_ID = "com.example.final_sathi";
    private static final String CHANNEL_NAME = "Sathi";

    private NotificationManager notificationManager;

    public OreoNotification(Context base){
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel(){
        NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(false);
        channel1.enableVibration(true);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager(){
        if(notificationManager==null){
            notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public Notification.Builder getOreoNotification(String title, String body, PendingIntent pendingIntent, Uri soundUri, String icon){
        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentIntent(pendingIntent).setContentTitle(title).setContentText(body).setSmallIcon(Integer.parseInt(icon))
                .setSound(soundUri).setAutoCancel(true);
    }

}
