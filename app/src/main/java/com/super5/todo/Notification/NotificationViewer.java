package com.super5.todo.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.super5.todo.Activity.ViewTodoActivity;
import com.super5.todo.R;
import com.super5.todo.Service.AlarmService;

public class NotificationViewer {

    Context context;
    int alarmID;
    String title;
    String time;

    int icon;

    String contentText;

    public NotificationViewer(Context context, int alarmID, String title, String time) {
        this.context = context;
        this.alarmID = alarmID;
        this.title = title;
        this.time = time;

        contentText = "You missed an alarm at "+time;

        icon = R.drawable.icon;

        callNotification();
    }

    private void callNotification(){
        String CHANNEL_ID = "todo_channel";// The id of the channel.
        CharSequence name = "todo_channel";// The user-visible name of the channel.


        int icon = R.drawable.icon;


        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            sendOreoNotification();
        }else{
            sendNotification();
        }
    }


    private void sendOreoNotification() {

        Intent intent = new Intent(context, ViewTodoActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarmID, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(context);

        Notification.Builder builder = oreoNotification.getOreoNotification(title,contentText,pendingIntent,defaultSound, String.valueOf(icon));

        oreoNotification.getManager().notify(alarmID, builder.build());

    }

    private void sendNotification() {

        Intent intent = new Intent(context, ViewTodoActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarmID, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);


        noti.notify(alarmID, builder.build());


    }



}
