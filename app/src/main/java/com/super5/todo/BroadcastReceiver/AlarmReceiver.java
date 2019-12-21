package com.super5.todo.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.super5.todo.Activity.AlarmActivity;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Wake Up!",Toast.LENGTH_SHORT).show();
        Log.e("alarm","Alarm called");

        if(intent.hasExtra("alarm_id")){
            Log.e("status","true");
        }else {
            Log.e("status","false");
        }
        int alarmID = intent.getIntExtra("alarm_id", 0);

        Log.e("alarmireceive", String.valueOf(alarmID));

        Intent i = new Intent(context, AlarmActivity.class);


        i.putExtra("alarm_id",alarmID);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Log.e("receiver called","true");
        context.startActivity(i);
    }


    public static void setAlarm(Context mContext, Calendar calender, int alarmID){
        AlarmManager alarmManager=(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent ine = new Intent(mContext, AlarmReceiver.class);

        ine.putExtra("alarm_id",alarmID);
        PendingIntent pi = PendingIntent.getBroadcast(mContext,alarmID,ine,PendingIntent.FLAG_UPDATE_CURRENT|Intent.FILL_IN_DATA);

//        if(alarmstatus){
//            pi=PendingIntent.getBroadcast(mContext,alarmID,ine,PendingIntent.FLAG_UPDATE_CURRENT|Intent.FILL_IN_DATA);
//        }else {
//            pi=PendingIntent.getBroadcast(mContext,alarmID,ine,0);
//        }



//        if(calender.before(Calendar.getInstance())){
//            Log.e("time","before");
//        } else {
//            Log.e("time","after");

            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),pi);
//        }


        Log.e("current time",String.valueOf(Calendar.getInstance().getTimeInMillis()));
        Log.e("current date",String.valueOf(Calendar.getInstance().getTime()));
        Log.e("set time",String.valueOf(calender.getTime()));
        Log.e("alarm","Alarm has been saved");

    }


    public static void cancelAlarm(Context mContext, int alarmId){
        Intent i = new Intent(mContext, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, alarmId, i, 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        Log.e("cancel", "Alarm Cancel");
    }
}
