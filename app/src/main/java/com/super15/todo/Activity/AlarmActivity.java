package com.super15.todo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.super15.todo.R;

public class AlarmActivity extends AppCompatActivity {

    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();

        String id, title, note, date, time;

        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        note = intent.getStringExtra("note");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");

        TextView tvTitle, tvNote, tvDate, tvTime;
        Button btnTurnOff;

        tvTitle = findViewById(R.id.tv_title);
        tvNote = findViewById(R.id.tv_note);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        btnTurnOff = findViewById(R.id.btn_turn_off);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                closeActivity();
            }
        }, 60000);

        tvTitle.setText(title);
        tvNote.setText(note);
        tvDate.setText(date);
        tvTime.setText(time);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        player = MediaPlayer.create(this, notification);
        player.setLooping(true);
        player.start();

        btnTurnOff.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
