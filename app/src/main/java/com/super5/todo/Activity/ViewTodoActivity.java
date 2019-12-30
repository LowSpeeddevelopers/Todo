package com.super5.todo.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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

    LinearLayout linearLayout;
    private static RelativeLayout fabPriority;
    private TodoAdapter todoAdapter;
    private TodoDb todoDb;
    private static FloatingActionButton fabAdd;
    Calendar cal;
    private FlowingDrawer mDrawer;
    TextView home, share, aboutUs, help, contact, btnHigh, btnLow, tvHigh, tvLow;
    ArrayList<TodoModel> remainderData, lowRemainder, highRemainder;
    String userDate, userTime;
    TextView timeSetter,dateSetter;
    public static boolean visibility;
    String priority;

    Calendar calendar;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    View dialogView;
    public RecyclerView rvTodo;


    private InterstitialAd mInterstitialAd;



    @Override
    protected void onStart() {
        super.onStart();
        Log.e("tag","onStart called");

    }

    @Override
    protected void onResume() {
        super.onResume();
        todoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("tag","onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_todo);
        rvTodo = findViewById(R.id.rv_todo);
        fabAdd = findViewById(R.id.fab_add);
        linearLayout=findViewById(R.id.linearLayout);
        fabPriority = findViewById(R.id.fab_priority);
        btnHigh = findViewById(R.id.btn_high);
        btnLow = findViewById(R.id.btn_low);

        tvHigh = findViewById(R.id.tv_high);
        tvLow = findViewById(R.id.tv_low);

        builder = new AlertDialog.Builder(ViewTodoActivity.this);


        calendar = Calendar.getInstance();
        mDrawer=findViewById(R.id.drawerlayout);
        home=findViewById(R.id.tv_home);
        share=findViewById(R.id.tv_share);
        help=findViewById(R.id.tv_help);
        contact=findViewById(R.id.tv_contact);
        aboutUs =findViewById(R.id.tv_about_us);
        cal=Calendar.getInstance();
        todoDb = new TodoDb(getApplicationContext());
        remainderData = todoDb.getData();
        highRemainder = new ArrayList<>();
        lowRemainder = new ArrayList<>();
        rvTodo.setHasFixedSize(true);
        rvTodo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        todoAdapter = new TodoAdapter(ViewTodoActivity.this, remainderData);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        context=getApplicationContext();
        Log.e("data 1", remainderData.toString());
        setupToolbar();
        rvTodo.setAdapter(todoAdapter);
        visibility = false;

        tvHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                highRemainder.clear();

                for (int i = 0; i<remainderData.size(); i++){
                    if (remainderData.get(i).getPriority().equals("high")){
                        highRemainder.add(remainderData.get(i));
                    }
                }

                if (highRemainder!=null){
                    todoAdapter = new TodoAdapter(ViewTodoActivity.this, highRemainder);
                    rvTodo.setAdapter(todoAdapter);
                } else {
                    Toast.makeText(getApplicationContext(),"Data Null", Toast.LENGTH_LONG);
                }
            }
        });

        tvLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lowRemainder.clear();

                for (int i = 0; i<remainderData.size(); i++){
                    if (remainderData.get(i).getPriority().equals("low")){
                        lowRemainder.add(remainderData.get(i));
                    }
                }

                if (lowRemainder!=null){
                    todoAdapter = new TodoAdapter(ViewTodoActivity.this, lowRemainder);
                    rvTodo.setAdapter(todoAdapter);
                } else {
                    Toast.makeText(getApplicationContext(),"Data Null", Toast.LENGTH_LONG);
                }


            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(todoAdapter.isopened && todoAdapter.positon!=null){
                    todoAdapter.closeLayout();
                }
                fabHider();
            }
        });

        rvTodo.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null) {
                    // tapped on child
                    return false;
                } else {
                   if(visibility){
                       fabHiderFromOthers();
                   }

                    if(todoAdapter.isopened && todoAdapter.positon!=null){
                        todoAdapter.closeLayout();
                    }

                    return true;
                }
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

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


        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

    }

    public void fabHider(){
        if (!visibility){
            visibility = true;
            fabPriority.setVisibility(View.VISIBLE);
            fabAdd.setImageDrawable(this.getResources().getDrawable(R.drawable.close_button));
        } else {
            visibility = false;
            fabPriority.setVisibility(View.GONE);
            fabAdd.setImageDrawable(this.getResources().getDrawable(R.drawable.fab_plus));
        }
    }

    static Context context;
    public static void fabHiderFromOthers(){
        visibility = false;
        fabPriority.setVisibility(View.GONE);
        fabAdd.setImageDrawable(context.getResources().getDrawable(R.drawable.fab_plus));
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
        dialogView = getLayoutInflater().inflate(R.layout.contact, null);
        builder.setView(null);
        builder.setView(dialogView);

        alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(true);

        alertDialogDismiss();

        alertDialog.show();

    }
    void showAboutDialogueBox(){
        mDrawer.closeMenu(true);

        dialogView = getLayoutInflater().inflate(R.layout.aboutus, null);
        builder.setView(null);
        builder.setView(dialogView);

        alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialogDismiss();
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
        dialogView = getLayoutInflater().inflate(R.layout.add_dialoguebox,null);
        final TextInputEditText txtTitle,txtNote;
        final CheckBox cbRing, cbVibration;
        Button add;
        ImageView date,timePicker;
        final TextView tvTitleCount, tvNoteCount;

        txtTitle = dialogView.findViewById(R.id.edt_title);
        txtNote = dialogView.findViewById(R.id.edt_note);
        cbRing = dialogView.findViewById(R.id.cb_ring);
        cbVibration = dialogView.findViewById(R.id.cb_vibration);
        add= dialogView.findViewById(R.id.btn_add);

        tvTitleCount = dialogView.findViewById(R.id.title_count);
        tvNoteCount = dialogView.findViewById(R.id.note_count);

        date= dialogView.findViewById(R.id.imgdate);
        timePicker= dialogView.findViewById(R.id.imgtime);
        timeSetter= dialogView.findViewById(R.id.timesetter);
        dateSetter= dialogView.findViewById(R.id.daetsetter);

        timeSetter.setText(timeFormatter(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        dateSetter.setText(dateFormatter(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR)));
        if(priority.equals("high")){
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
        alertDialog=builder.create();

        builder.setView(null);
        builder.setView(dialogView);

        alertDialog=builder.create();
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
                int alarmID = (int) cal.getTimeInMillis();
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
                } else if (cal.before(Calendar.getInstance())){
                    isOk=false;
                    Toast.makeText(ViewTodoActivity.this, "Your selected time before then now... Please check", Toast.LENGTH_LONG).show();
                }
                if(isOk){
                    TodoModel todoModel = new TodoModel(alarmID,priority,title,note,date,time,ring,vibration,true, false);
                    todoDb.insertData(todoModel);
                    remainderData.add(todoModel);
                    if (priority.equals("high")){
                        highRemainder.add(todoModel);
                    } else {
                        lowRemainder.add(todoModel);
                    }
                    todoAdapter.notifyDataSetChanged();
                    AlarmReceiver.setAlarm(ViewTodoActivity.this, cal, alarmID, false);
                    alertDialog.dismiss();
                    fabHiderFromOthers();

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {

                    }

                }
            }
        });


        alertDialogDismiss();
        alertDialog.show();
    }

    private void alertDialogDismiss(){
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
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
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getApplicationContext()));
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
        }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)-1,calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }

    private String timeFormatter(int hour, int minute){
        String tempTime;
        String sMinute, sHour;
        if (minute < 10){ sMinute="0"+minute; } else { sMinute=""+minute; }
        if (hour < 10){ sHour="0"+hour; } else { sHour=""+hour; }
        tempTime=sHour + ":" +sMinute;
        //tvTime.setText(sHour + ":" +sMinute );
        return tempTime;
    }
    private String dateFormatter(int day, int month, int year){
        month+=1;
        String sDay, sMonth, sYear;
        if (day<10){ sDay = "0"+day; } else { sDay = ""+day; }
        if (month<10){ sMonth = "0"+month; } else { sMonth = ""+month; }
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
