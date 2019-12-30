package com.super5.todo.BroadcastReceiver;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import com.super5.todo.Activity.AlarmActivity;
import com.super5.todo.Activity.ViewTodoActivity;
import com.super5.todo.Adapter.TodoAdapter;
import com.super5.todo.Service.AlarmService;

import java.util.Calendar;
import java.util.List;


public class AlarmReceiver extends BroadcastReceiver {

    boolean isActivityFound;

    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

        Log.e("alarm", "Receive");

        if(intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            Log.e("alarm", "Boot Completed Called");

            Intent serviceIntent = new Intent(context, AlarmService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }



        } else {



            Log.e("alarm", "Boot not Completed Called");

            //Toast.makeText(context,"Wake Up!",Toast.LENGTH_SHORT).show();
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
    }


    public static void setAlarm(Context mContext, Calendar calender, int alarmID, boolean alarmstatus){
        AlarmManager alarmManager=(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent ine = new Intent(mContext, AlarmReceiver.class);

        ine.putExtra("alarm_id",alarmID);
        PendingIntent pi;

        //PendingIntent.getBroadcast(mContext,alarmID,ine,PendingIntent.FLAG_UPDATE_CURRENT|Intent.FILL_IN_DATA);

        if(alarmstatus){
            pi=PendingIntent.getBroadcast(mContext,alarmID,ine,PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            pi=PendingIntent.getBroadcast(mContext,alarmID,ine,0);
        }



//        if(calender.before(Calendar.getInstance())){
//            Log.e("time","before");
//        } else {
//            Log.e("time","after");

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),pi);
        }else {
            Toast.makeText(mContext,"Internal Error!",Toast.LENGTH_LONG).show();
        }
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
        if (alarmManager != null) {
            alarmManager.cancel(sender);
        }else {
            Toast.makeText(mContext,"Internal Error!",Toast.LENGTH_LONG).show();
        }

        Log.e("cancel", "Alarm Cancel");
    }


}
