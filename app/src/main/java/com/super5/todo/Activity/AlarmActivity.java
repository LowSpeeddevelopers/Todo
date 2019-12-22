package com.super5.todo.Activity;

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
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.super5.todo.Model.TodoModel;
import com.super5.todo.R;
import com.super5.todo.db.TodoDb;


public class AlarmActivity extends AppCompatActivity {

    MediaPlayer player;
    Vibrator v;

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

        TodoDb todoDb = new TodoDb(getApplicationContext());

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

        Uri alarmTune = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        model.setAlarmRes(true);

        if (todoDb.updateData(model) > 0){
            player = MediaPlayer.create(this, alarmTune);
            player.setLooping(true);

            v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            long[] mVibratePattern = new long[]{0, 400, 800, 600, 800, 800, 800, 1000};
            int[] mAmplitudes = new int[]{0, 255, 0, 255, 0, 255, 0, 255};

            if (model.isRing() && model.isVibration()){

                player.start();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createWaveform(mVibratePattern, mAmplitudes, 5));
                } else {
                    //deprecated in API 26
                    v.vibrate(mVibratePattern, 0);
                }
            } else if (model.isRing()){
                player.start();
            } else if (model.isVibration()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createWaveform(mVibratePattern, mAmplitudes, 5));
                } else {
                    //deprecated in API 26
                    v.vibrate(mVibratePattern, 0);
                }
            }
        }



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
        v.cancel();
        finish();
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

            Intent resultIntent = new Intent(this, AlarmActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(AlarmActivity.class);

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

            Notification notification = new Notification.Builder(AlarmActivity.this, String.valueOf(alarmID))
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
