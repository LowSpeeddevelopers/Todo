package com.super5.todo.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.super5.todo.R;
import com.super5.todo.Service.AlarmService;

public class NotificationViewer {

    Context context;
    int alarmID;
    String title;
    String time;

    public NotificationViewer(Context context, int alarmID, String title, String time) {
        this.context = context;
        this.alarmID = alarmID;
        this.title = title;
        this.time = time;

        callNotification();
    }

    private void callNotification(){
        String CHANNEL_ID = "todo_channel";// The id of the channel.
        CharSequence name = "todo_channel";// The user-visible name of the channel.


        int icon = R.drawable.icon;
        String contentText = "You missed an alarm at "+time;

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O){



            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, String.valueOf(alarmID));

            mBuilder.setSmallIcon(icon);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(contentText);

           // Intent resultIntent = new Intent(context, AlarmService.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(AlarmService.class);
            Intent i = new Intent(context,ViewTodoActivity.class);


            stackBuilder.addNextIntentWithParentStack(i);

            stackBuilder.addNextIntent(i);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(alarmID,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (mNotificationManager != null) {
                mNotificationManager.notify(alarmID, mBuilder.build());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            android.app.Notification notification = new android.app.Notification.Builder(context, String.valueOf(alarmID))
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(contentText)
                    .setChannelId(CHANNEL_ID)
                    .build();

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

// Issue the notification.
            mNotificationManager.notify(alarmID , notification);

        }
    }



}
