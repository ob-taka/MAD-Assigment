package com.health.anytime;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    private String title, des;
    private int id;

    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("title");
        des = intent.getStringExtra("des");
        id = intent.getIntExtra("id", 0);

        NotificationHelper notificationHelper = new NotificationHelper(context);

        Intent launch_intent = new Intent(context, MainActivity.class);

        PendingIntent pt = PendingIntent.getActivity(context, id, launch_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nb = notificationHelper.getChannelNotification()
                .setContentIntent(pt)
                .setContentTitle(title)
                .setContentText(des);

        notificationHelper.getManager().notify(id, nb.build());
    }
}
