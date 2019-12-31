package com.nexttech.todo.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.nexttech.todo.Activity.ViewTodoActivity;
import com.nexttech.todo.R;

public class NotificationViewer {
    private Context context;
    private int alarmID;
    private String title;
    private int icon;
    private String contentText;
    public NotificationViewer(Context context, int alarmID, String title, String time) {
        this.context = context;
        this.alarmID = alarmID;
        this.title = title;
        contentText = "You missed an alarm at "+time;
        icon = R.drawable.icon;
        callNotification();
    }
    private void callNotification(){
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
        if (noti != null) {
            noti.notify(alarmID, builder.build());
        }
    }
}
