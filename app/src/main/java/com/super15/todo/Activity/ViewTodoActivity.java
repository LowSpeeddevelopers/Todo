package com.super15.todo.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.super15.todo.Adapter.TodoAdapter;
import com.super15.todo.BroadcustReceiver.AlarmReceiver;
import com.super15.todo.Model.TodoModel;
import com.super15.todo.R;
import com.super15.todo.db.TodoDb;
import java.util.ArrayList;
import java.util.Calendar;


public class ViewTodoActivity extends AppCompatActivity {

    private RecyclerView rvTodo;
    private FloatingActionButton fabAdd;

    private TodoAdapter todoAdapter;
    private TodoDb todoDb;
    int hour, minute;
    int year,month,day;
    Calendar cal;
    private FlowingDrawer mDrawer;

    TextView home, setting, share, aboutus, help, contact;
    ArrayList<TodoModel> todoModels;

    String userdate,usertime,currenttime,currentdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_todo);

        rvTodo = findViewById(R.id.rv_todo);
        fabAdd = findViewById(R.id.fab_add);


        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        minute=calendar.get(Calendar.MINUTE);
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        currentdate= dateFormater(day,month,year);
        currenttime=timeFormater(hour, minute);

        mDrawer=findViewById(R.id.drawerlayout);

        home=findViewById(R.id.barighor);
        share=findViewById(R.id.choriyedao);
        setting=findViewById(R.id.genjam);
        help=findViewById(R.id.sahajjo);
        contact=findViewById(R.id.jogajog);
        aboutus=findViewById(R.id.amaderbepare);



        cal=Calendar.getInstance();

        todoDb = new TodoDb(getApplicationContext());

        todoModels = todoDb.getData();

        rvTodo.setHasFixedSize(true);
        rvTodo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        todoAdapter = new TodoAdapter(ViewTodoActivity.this,todoModels);

        Log.e("data 1",todoModels.toString());


        setupToolbar();

        rvTodo.setAdapter(todoAdapter);


//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(rvTodo);




        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ViewTodoActivity.this,AddTodoActivity.class));
                showDialoguebox();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showhomeDialoguebox();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showshareDialoguebox();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showsettingDialoguebox();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showhelpDialoguebox();
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcontactDialoguebox();
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showaboutDialoguebox();
            }
        });
    }


    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Toast.makeText(ViewTodoActivity.this, "on Move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            Toast.makeText(ViewTodoActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
            //Remove swiped item from list and notify the RecyclerView
            int position = viewHolder.getAdapterPosition();
            todoModels.remove(position);
            todoAdapter.notifyDataSetChanged();

        }
    };
    void showhomeDialoguebox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTodoActivity.this);
        View DialogueView = getLayoutInflater().inflate(R.layout.homeactivity, null);
        builder.setView(DialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();

    }

    void showshareDialoguebox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTodoActivity.this);
        View DialogueView = getLayoutInflater().inflate(R.layout.share, null);
        builder.setView(DialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();

    }

    void showsettingDialoguebox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTodoActivity.this);
        View DialogueView = getLayoutInflater().inflate(R.layout.setting, null);
        builder.setView(DialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();

    }

    void showhelpDialoguebox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTodoActivity.this);
        View DialogueView = getLayoutInflater().inflate(R.layout.help, null);
        builder.setView(DialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();

    }

    void showcontactDialoguebox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTodoActivity.this);
        View DialogueView = getLayoutInflater().inflate(R.layout.contact, null);
        builder.setView(DialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();

    }

    void showaboutDialoguebox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTodoActivity.this);
        View DialogueView = getLayoutInflater().inflate(R.layout.aboutus, null);
        builder.setView(DialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();

    }






    void showDialoguebox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTodoActivity.this);
        View DialogueView = getLayoutInflater().inflate(R.layout.add_dialoguebox,null);
        final TextInputEditText titletext,description;
        Button add;
        ImageView date,timepicker;
        final TextView timesetter,datesetter;

        titletext=DialogueView.findViewById(R.id.edt_title);
        description=DialogueView.findViewById(R.id.edt_note);
        add=DialogueView.findViewById(R.id.btn_add);

        date=DialogueView.findViewById(R.id.imgdate);
        timepicker=DialogueView.findViewById(R.id.imgtime);
        timesetter=DialogueView.findViewById(R.id.timesetter);
        datesetter=DialogueView.findViewById(R.id.daetsetter);

        timesetter.setText(currenttime);
        datesetter.setText(currentdate);



        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ViewTodoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        timeFormater(hourOfDay, minute);

                        cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        cal.set(Calendar.MINUTE,minute);
                        cal.set(Calendar.SECOND,0);

                        usertime = timeFormater(hourOfDay, minute);

                        timesetter.setText(usertime);



                    }
                },hour,minute, DateFormat.is24HourFormat(getApplicationContext()));
                timePickerDialog.show();
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTodoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int year, final int month, final int dayOfMonth) {


                        Log.e("day",String.valueOf(dayOfMonth));
                        Log.e("month",String.valueOf(month));
                        Log.e("year",String.valueOf(year));

                        userdate = dateFormater(dayOfMonth,month,year);

                        cal.set(year,month,dayOfMonth);


                        datesetter.setText(userdate);

                    }
                }, year,month,day);

                datePickerDialog.show();
            }
        });





        builder.setView(DialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todotext=String.valueOf(titletext.getText());
                String descryption=String.valueOf(description.getText());
                String date = datesetter.getText().toString();
                String time = timesetter.getText().toString();

                View focusview=null;
                boolean isok=true;
                if(TextUtils.isEmpty(todotext)){
                    titletext.setError("Field Can Not Be Empty!");
                    focusview=titletext;
                    isok=false;
                }else if(TextUtils.isEmpty(descryption)){
                    description.setError("Field Can Not Be Empty!");
                    focusview=description;
                    isok=false;
                }
                if(isok){


                    TodoModel todoModel = new TodoModel();
                    todoModel.setTitle(todotext);
                    todoModel.setNote(descryption);
                    todoModel.setDate(date);
                    todoModel.setTime(time);
                    todoDb.insertData(todoModel);
                    todoModels.add(todoModel);
                    todoAdapter.notifyDataSetChanged();


                    SetAlarm(cal, todoModel);

                    alertDialog.dismiss();
                }else {
                    focusview.requestFocus();
                    isok=true;

                }
            }
        });
        alertDialog.show();
    }



    private String timeFormater(int hour, int minute){

        String temptime;
        String sMinute, sHour;

        if (minute < 10){
            sMinute="0"+minute;
        } else {
            sMinute=""+minute;
        }

        if (hour < 10){
            sHour="0"+hour;
        } else {
            sHour=""+hour;
        }

        temptime=sHour + ":" +sMinute;
        //tvTime.setText(sHour + ":" +sMinute );

        return temptime;

    }
    private String dateFormater(int day, int month, int year){

        String sDay, sMonth, sYear;

        if (day<10){
            sDay = "0"+day;
        } else {
            sDay = ""+day;
        }

        if (month<10){
            sMonth = "0"+month;
        } else {
            sMonth = ""+month;
        }

        sYear = ""+year;




        String date= sDay + "/" +sMonth+"/" +sYear;





       // tvDate.setText(date);
        return date;
    }

    private void SetAlarm(Calendar calender, TodoModel todoModel){
        AlarmManager alarmManager=(AlarmManager) ViewTodoActivity.this.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ViewTodoActivity.this, AlarmReceiver.class);

        Bundle b = new Bundle();
        b.putString("id",todoModel.getId());
        b.putString("title",todoModel.getTitle());
        b.putString("note",todoModel.getNote());
        b.putString("time",todoModel.getTime());
        b.putString("date",todoModel.getDate());
        i.putExtras(b);

        PendingIntent pi=PendingIntent.getBroadcast(ViewTodoActivity.this,1,i,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),pi);

        Log.e("current time",String.valueOf(Calendar.getInstance().getTimeInMillis()));
        Log.e("current date",String.valueOf(Calendar.getInstance().getTime()));
        Log.e("set time",String.valueOf(calender.getTime()));
        Log.e("alarm","Alarm has been saved");

    }


    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menuwhite);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.toggleMenu();
            }
        });
    }








}
