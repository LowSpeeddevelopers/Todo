package com.super5.todo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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

        final Handler handler = new Handler();

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
                handler.removeCallbacksAndMessages(null);
                closeActivity();
            }
        });
    }

    private void closeActivity(){
        player.stop();
        finish();
    }

    private void sendNotification(int alarmID, String title, String time){

        String CHANNEL_ID = "todo_channel";// The id of the channel.
        CharSequence name = "todo_channel";// The user-visible name of the channel.


        int icon = R.drawable.icon;
        String contentTitle = title;
        String contentText = "You missed an alarm at "+time;

        if (Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.O){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, String.valueOf(alarmID));

            mBuilder.setSmallIcon(icon);
            mBuilder.setContentTitle(contentTitle);
            mBuilder.setContentText(contentText);

            Intent resultIntent = new Intent(this, AlarmActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(AlarmActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(alarmID, mBuilder.build());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            Notification notification = new Notification.Builder(AlarmActivity.this, String.valueOf(alarmID))
                    .setSmallIcon(icon)
                    .setContentTitle(contentTitle)
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
