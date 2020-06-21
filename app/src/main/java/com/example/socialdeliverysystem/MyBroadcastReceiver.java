package com.example.socialdeliverysystem;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static int packageCount = 0;
    private String channelId;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        channelId = createNotificationChannel(context);
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "new_package_service":
                    sendNotification(context,"Social Delivery!", "You received a new package: " + intent.getSerializableExtra("parcelId") );
                    break;
                case "no_internet_connection":
                    sendNotification(context,"Social Delivery! no internet connection", "Check your connection" );
                    break;
            }
        }
    }

    private void sendNotification(Context context, String title, String text){
        notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(packageCount++, notificationBuilder.build());
    }

    public static String createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "ChannelId";
            CharSequence channelName = "Social Delivery";
            String channelDescription = "Social Delivery Alert";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(true);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
            return channelId;
        } else {
            return null;
        }
    }
}