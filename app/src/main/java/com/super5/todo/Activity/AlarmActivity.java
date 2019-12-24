package com.super5.todo.Activity;

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
import androidx.appcompat.app.AppCompatActivity;
import com.super5.todo.Model.TodoModel;
import com.super5.todo.Notification.NotificationViewer;
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

                new NotificationViewer(AlarmActivity.this, alarmID, title, time);




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

}
