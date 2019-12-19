package com.super5.todo.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.super5.todo.Adapter.TodoAdapter;
import com.super5.todo.BroadcastReceiver.AlarmReceiver;
import com.super5.todo.Model.TodoModel;
import com.super5.todo.R;
import com.super5.todo.db.TodoDb;
import java.util.ArrayList;
import java.util.Calendar;


public class  ViewTodoActivity extends AppCompatActivity {

    private RelativeLayout fabPriority;

    private TodoAdapter todoAdapter;
    private TodoDb todoDb;
    int hour, minute;
    int year,month,day;
    Calendar cal;
    private FlowingDrawer mDrawer;

    TextView home, share, aboutUs, help, contact, btnHigh, btnLow;

    ArrayList<TodoModel> todoModels;

    String userDate, userTime, currentTime, currentDate;

    TextView timeSetter,dateSetter;

    int visibility;
    String priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_todo);

        RecyclerView rvTodo = findViewById(R.id.rv_todo);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);

        fabPriority = findViewById(R.id.fab_priority);
        btnHigh = findViewById(R.id.btn_high);
        btnLow = findViewById(R.id.btn_low);


        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        minute=calendar.get(Calendar.MINUTE);
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        currentDate = dateFormatter(day,month,year);
        currentTime = timeFormatter(hour, minute);

        mDrawer=findViewById(R.id.drawerlayout);

        home=findViewById(R.id.tv_home);
        share=findViewById(R.id.tv_share);
        help=findViewById(R.id.tv_help);
        contact=findViewById(R.id.tv_contact);
        aboutUs =findViewById(R.id.tv_about_us);



        cal=Calendar.getInstance();

        todoDb = new TodoDb(getApplicationContext());

        todoModels = todoDb.getData();

        rvTodo.setHasFixedSize(true);
        rvTodo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        todoAdapter = new TodoAdapter(ViewTodoActivity.this,todoModels);

        Log.e("data 1",todoModels.toString());


        setupToolbar();

        rvTodo.setAdapter(todoAdapter);




                visibility = 0;



        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(todoAdapter.isopened && todoAdapter.positon!=null){
                    todoAdapter.cloaseLayout();
                }




                if (visibility == 0){
                    visibility = 1;
                    fabPriority.setVisibility(View.VISIBLE);
                } else {
                    visibility = 0;
                    fabPriority.setVisibility(View.GONE);
                }
            }
        });

        btnHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "high";
                showAddDialogueBox();
            }
        });

        btnLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "low";
                showAddDialogueBox();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHomeDialogueBox();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpDialogueBox();
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactDialogueBox();
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialogueBox();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialougeBox();
            }
        });
    }


    void showHomeDialogueBox(){
        mDrawer.closeMenu(true);
    }

    void showHelpDialogueBox(){
        mDrawer.closeMenu(true);
        startActivity(new Intent(ViewTodoActivity.this, HelpActivity.class));

    }

    void showContactDialogueBox(){
        mDrawer.closeMenu(true);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTodoActivity.this);
        View DialogueView = getLayoutInflater().inflate(R.layout.contact, null);
        builder.setView(DialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();

    }

    void showAboutDialogueBox(){
        mDrawer.closeMenu(true);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTodoActivity.this);
        View DialogueView = getLayoutInflater().inflate(R.layout.aboutus, null);
        builder.setView(DialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();

    }

    void showShareDialougeBox(){
        mDrawer.closeMenu(true);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://play.google.com/store/apps/details?id=com.super5.todo";
        String shareSub = "ToDo";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }


    void showAddDialogueBox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTodoActivity.this);
        View DialogueView = getLayoutInflater().inflate(R.layout.add_dialoguebox,null);
        final TextInputEditText txtTitle,txtNote;
        final CheckBox cbRing, cbVibration;
        Button add;
        ImageView date,timePicker;
        final TextView tvTitleCount, tvNoteCount;

        txtTitle = DialogueView.findViewById(R.id.edt_title);
        txtNote = DialogueView.findViewById(R.id.edt_note);
        cbRing = DialogueView.findViewById(R.id.cb_ring);
        cbVibration = DialogueView.findViewById(R.id.cb_vibration);
        add=DialogueView.findViewById(R.id.btn_add);

        tvTitleCount = DialogueView.findViewById(R.id.title_count);
        tvNoteCount = DialogueView.findViewById(R.id.note_count);

        date=DialogueView.findViewById(R.id.imgdate);
        timePicker=DialogueView.findViewById(R.id.imgtime);
        timeSetter=DialogueView.findViewById(R.id.timesetter);
        dateSetter=DialogueView.findViewById(R.id.daetsetter);

        timeSetter.setText(currentTime);
        dateSetter.setText(currentDate);

        if(priority == "high"){
            cbRing.setChecked(true);
            cbVibration.setChecked(true);
        } else {
            cbRing.setChecked(true);
        }


        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });

        timeSetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker();
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        dateSetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        builder.setView(DialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentText = s.toString();
                int count = currentText.length();
                tvTitleCount.setText(count + "/50");
            }
        });

        txtNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentText = s.toString();
                int count = currentText.length();
                tvNoteCount.setText(count + "/150");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int alarmID = (int) System.currentTimeMillis();
                String title=String.valueOf(txtTitle.getText());
                String note=String.valueOf(txtNote.getText());
                String date = dateSetter.getText().toString();
                String time = timeSetter.getText().toString();
                boolean ring = cbRing.isChecked();
                boolean vibration = cbVibration.isChecked();

                boolean isOk=true;
                if(TextUtils.isEmpty(title)){
                    txtTitle.setError("Field Can Not Be Empty!");
                    txtTitle.requestFocus();
                    isOk=false;
                }else if(TextUtils.isEmpty(note)){
                    txtNote.setError("Field Can Not Be Empty!");
                    txtNote.requestFocus();
                    isOk=false;
                } else if (!cbRing.isChecked() && !cbVibration.isChecked()) {
                    isOk=false;
                    Toast.makeText(ViewTodoActivity.this, "Ring or Vibration must be checked", Toast.LENGTH_SHORT).show();
                }
                if(isOk){

                    TodoModel todoModel = new TodoModel(alarmID,priority,title,note,date,time,ring,vibration,true);
                    todoDb.insertData(todoModel);
                    todoModels.add(todoModel);
                    todoAdapter.notifyDataSetChanged();

                    AlarmReceiver.setAlarm(ViewTodoActivity.this, cal, alarmID,false);

                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    private void timePicker(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(ViewTodoActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                cal.set(Calendar.MINUTE,minute);
                cal.set(Calendar.SECOND,0);

                userTime = timeFormatter(hourOfDay, minute);

                timeSetter.setText(userTime);



            }
        },hour,minute, DateFormat.is24HourFormat(getApplicationContext()));
        timePickerDialog.show();
    }

    private void datePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTodoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, final int year, final int month, final int dayOfMonth) {


                Log.e("day",String.valueOf(dayOfMonth));
                Log.e("month",String.valueOf(month));
                Log.e("year",String.valueOf(year));

                userDate = dateFormatter(dayOfMonth,month,year);

                cal.set(year,month,dayOfMonth);


                dateSetter.setText(userDate);

            }
        }, year,month-1,day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

        datePickerDialog.show();
    }



    private String timeFormatter(int hour, int minute){

        String tempTime;
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

        tempTime=sHour + ":" +sMinute;
        //tvTime.setText(sHour + ":" +sMinute );

        return tempTime;

    }
    private String dateFormatter(int day, int month, int year){

        month+=1;

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


        // tvDate.setText(date);
        return sDay + "/" +sMonth+"/" +sYear;
    }


    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
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
