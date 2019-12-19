package com.super5.todo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.super5.todo.Model.TodoModel;
import com.super5.todo.R;
import com.super5.todo.db.TodoDb;

public class AlarmActivity extends AppCompatActivity {

    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        Intent intent = getIntent();

        final int alarmID = intent.getIntExtra("alarm_id",0);

        final String title, note, date, time;

        Log.e("AlarmID", alarmID+"");

        if (alarmID == 0) {
            Log.e("alarm","Alarm ID Null set");
        }

        TodoModel model = new TodoDb(this).getDataByAlarmID(alarmID);

        title = model.getTitle();
        note = model.getNote();
        date = model.getDate();
        time = model.getTime();

        TextView tvTitle, tvNote, tvDate, tvTime, btnDismiss;

        tvTitle = findViewById(R.id.tv_title);
        tvNote = findViewById(R.id.tv_note);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        btnDismiss = findViewById(R.id.btn_dismiss);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                closeActivity();

                sendNotification(alarmID, title, time);
            }
        }, 60000);

        tvTitle.setText(title);
        tvNote.setText(note);
        tvDate.setText(date);
        tvTime.setText(time);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        player = MediaPlayer.create(this, notification);
        player.setLooping(true);
        player.start();

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });
    }

    private void closeActivity(){
        player.stop();
        finish();
    }

    private void sendNotification(int alarmID, String title, String time){

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.O){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setSmallIcon(R.drawable.icon);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText("You missed an alarm at "+time);

            Intent resultIntent = new Intent(this, AlarmActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(AlarmActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            mNotificationManager.notify(alarmID, mBuilder.build());
        } else {
            Toast.makeText(this, "Notication for Upper Version", Toast.LENGTH_LONG).show();
        }

            

    }

}
