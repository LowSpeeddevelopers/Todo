package com.nexttech.todo.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.nexttech.todo.Activity.AlarmActivity;
import com.nexttech.todo.Service.AlarmService;

import java.util.Calendar;


public class AlarmReceiver extends BroadcastReceiver {
    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if(intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Intent serviceIntent = new Intent(context, AlarmService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        } else {
            int alarmID = intent.getIntExtra("alarm_id", 0);
            Intent i = new Intent(context, AlarmActivity.class);
            i.putExtra("alarm_id",alarmID);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
    public static void setAlarm(Context mContext, Calendar calender, int alarmID, boolean alarmstatus){
        AlarmManager alarmManager=(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent ine = new Intent(mContext, AlarmReceiver.class);
        ine.putExtra("alarm_id",alarmID);
        PendingIntent pi;
        if(alarmstatus){
            pi=PendingIntent.getBroadcast(mContext,alarmID,ine,PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            pi=PendingIntent.getBroadcast(mContext,alarmID,ine,0);
        }
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),pi);
        }else {
            Toast.makeText(mContext,"Internal Error!",Toast.LENGTH_LONG).show();
        }
    }
    public static void cancelAlarm(Context mContext, int alarmId){
        Intent i = new Intent(mContext, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, alarmId, i, 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(sender);
        }else {
            Toast.makeText(mContext,"Internal Error!",Toast.LENGTH_LONG).show();
        }
    }
}
