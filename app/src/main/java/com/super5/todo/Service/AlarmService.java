package com.super5.todo.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

import com.super5.todo.Notification.NotificationViewer;
import com.super5.todo.BroadcastReceiver.AlarmReceiver;
import com.super5.todo.Model.TodoModel;
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
                        new NotificationViewer(this, todoModel.getAlarmId(),todoModel.getTitle(),todoModel.getTime());

                    }
                }

            } else {
                AlarmReceiver.setAlarm(getApplicationContext(),cal,todoModel.getAlarmId(), false);
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




}
