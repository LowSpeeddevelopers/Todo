package com.super15.todo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.super15.todo.BroadcustReceiver.AlarmReceiver;
import com.super15.todo.Model.TodoModel;
import com.super15.todo.R;
import com.super15.todo.db.TodoDb;

import java.util.Calendar;
import java.util.Date;

public class AddTodoActivity extends AppCompatActivity {

    EditText edtTitle, edtNote;
    TextView tvDate, tvTime;
    Button btnAdd;
    Calendar cal;

    TodoDb todoDb;

    DatePickerDialog.OnDateSetListener Datesetsistener;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        edtTitle = findViewById(R.id.edt_title);
        edtNote = findViewById(R.id.edt_note);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        btnAdd = findViewById(R.id.btn_add);

        cal=Calendar.getInstance();

        todoDb = new TodoDb(getApplicationContext());


        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        minute=calendar.get(Calendar.MINUTE);
        hour=calendar.get(Calendar.HOUR_OF_DAY);

        tvDate.setText(day + "/" +month+"/" +year);

        tvTime.setText(hour + ":" +minute );



        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTodoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int year,final int month, final int dayOfMonth) {


                        Log.e("day",String.valueOf(dayOfMonth));
                        Log.e("month",String.valueOf(month));
                        Log.e("year",String.valueOf(year));

                        String date= dayOfMonth + "/" +month+"/" +year;

                        cal.set(year,month,dayOfMonth);


                        tvDate.setText(date);


                    }
                }, year,month,day);

                datePickerDialog.show();

            }
        });


        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTodoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvTime.setText(hourOfDay + ":" +minute );
                        
                        cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        cal.set(Calendar.MINUTE,minute);
                        cal.set(Calendar.SECOND,0);

                    }
                },hour,minute, DateFormat.is24HourFormat(getApplicationContext()));
                timePickerDialog.show();
                //String times= ;


            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edtTitle.getText().toString();
                String note = edtNote.getText().toString();
                String date = tvDate.getText().toString();
                String time = tvTime.getText().toString();





                if (title.isEmpty() || note.isEmpty() || date.isEmpty() || time.isEmpty()){
                    Toast.makeText(AddTodoActivity.this, "Empty not allowed", Toast.LENGTH_SHORT).show();
                } else{

                    TodoModel todoModel = new TodoModel();
                    todoModel.setTitle(title);
                    todoModel.setNote(note);
                    todoModel.setDate(date);
                    todoModel.setTime(time);
                    todoDb.insertData(todoModel);
                    Toast.makeText(AddTodoActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();


                    SetAlarm(cal);
                    if(cal.before(Calendar.getInstance())){
                        Log.e("time false","true");
                    }
                    startActivity(new Intent(AddTodoActivity.this,ViewTodoActivity.class));
                }
            }
        });

    }

    private void SetAlarm(Calendar calender){
        AlarmManager alarmManager=(AlarmManager) AddTodoActivity.this.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(AddTodoActivity.this, AlarmReceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(AddTodoActivity.this,1,i,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),pi);

        Log.e("current time",String.valueOf(Calendar.getInstance().getTimeInMillis()));
        Log.e("current date",String.valueOf(Calendar.getInstance().getTime()));
        Log.e("set time",String.valueOf(calender.getTime()));
        Log.e("alarm","Alarm has been saved");

    }
}
