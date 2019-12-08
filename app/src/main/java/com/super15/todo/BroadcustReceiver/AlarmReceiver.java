package com.super15.todo.BroadcustReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.super15.todo.Activity.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Wake Up!",Toast.LENGTH_SHORT).show();
        Log.e("alarm","Alarm called");

        Intent i = new Intent(context, AlarmActivity.class);

        Bundle b = new Bundle();
        b.putString("id",intent.getStringExtra("id"));
        b.putString("title",intent.getStringExtra("title"));
        b.putString("note",intent.getStringExtra("note"));
        b.putString("time",intent.getStringExtra("time"));
        b.putString("date",intent.getStringExtra("date"));
        i.putExtras(b);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
