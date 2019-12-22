package com.super5.todo.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.super5.todo.BroadcastReceiver.AlarmReceiver;
import com.super5.todo.Model.TodoModel;
import com.super5.todo.R;
import com.super5.todo.db.TodoDb;
import java.util.ArrayList;
import java.util.Calendar;


public class AlarmService extends Service {

    ArrayList<TodoModel> myRemainders;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.e("started", "Service Started");

        TodoDb todoDb = new TodoDb(getApplicationContext());

        myRemainders = todoDb.getData();

        for (int i = 0; i<myRemainders.size(); i++){
            TodoModel todoModel = myRemainders.get(i);

            Calendar cal = dateAndTimeParse(todoModel.getDate(),todoModel.getTime());

            if (cal.before(Calendar.getInstance())){

                if (!todoModel.isAlarmRes()){
                    todoModel.setAlarmRes(true);

                    if (todoDb.updateData(todoModel) > 0){
                        sendNotification(todoModel.getAlarmId(),todoModel.getTitle(),todoModel.getTime());
                    }
                }



            } else {
                AlarmReceiver.setAlarm(getApplicationContext(),cal,todoModel.getAlarmId());
            }

        }


    }

    private Calendar dateAndTimeParse(String date, String time){
        Calendar calendar = Calendar.getInstance();
        String[] strDate=date.split("/",3);
        int day=Integer.parseInt(strDate[0]);
        int month=Integer.parseInt(strDate[1]);
        int year=Integer.parseInt(strDate[2]);
        String[] strTime=time.split(":",2);
        int hour=Integer.parseInt(strTime[0]);
        int minute=Integer.parseInt(strTime[1]);
        month--;
        calendar.set(year,month,day,hour,minute,0);
        return calendar;
    }

    private void sendNotification(int alarmID, String title, String time){

        String CHANNEL_ID = "todo_channel";// The id of the channel.
        CharSequence name = "todo_channel";// The user-visible name of the channel.


        int icon = R.drawable.icon;
        String contentText = "You missed an alarm at "+time;

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, String.valueOf(alarmID));

            mBuilder.setSmallIcon(icon);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(contentText);

            Intent resultIntent = new Intent(this, AlarmService.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(AlarmService.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(alarmID,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (mNotificationManager != null) {
                mNotificationManager.notify(alarmID, mBuilder.build());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            Notification notification = new Notification.Builder(getApplicationContext(), String.valueOf(alarmID))
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(contentText)
                    .setChannelId(CHANNEL_ID)
                    .build();

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

// Issue the notification.
            mNotificationManager.notify(alarmID , notification);
        }



    }



}
