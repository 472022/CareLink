package com.example.carelink;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "my_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("notification_title");
        String message = intent.getStringExtra("notification_message");

        Intent notificationIntent = new Intent(context, fragment_subscription.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {
            builder = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
