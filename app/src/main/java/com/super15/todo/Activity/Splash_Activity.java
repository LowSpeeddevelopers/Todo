package com.super15.todo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.super15.todo.R;

public class Splash_Activity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash_Activity.this, ViewTodoActivity.class);
                startActivity(intent);
            }
        },5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
