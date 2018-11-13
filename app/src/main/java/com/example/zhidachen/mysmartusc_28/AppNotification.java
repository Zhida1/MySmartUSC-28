package com.example.zhidachen.mysmartusc_28;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Notification.Builder;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

public class AppNotification extends ContextWrapper {

    private final String CHANNEL_ID = "MySmartSC_Notification";
    private final String CHANNEL_Name = "MySmartSC";
    private final int NOTIFICATION_ID = 001;
    private NotificationManager notificationManager;

    public AppNotification(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_Name, importance);
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            getManager().createNotificationChannel(notificationChannel);
        }
    }

    public NotificationManager getManager() {
        if(notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public Notification.Builder getAppChannelNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_more_black_24dp);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}
