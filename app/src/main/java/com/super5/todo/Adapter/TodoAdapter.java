package com.super5.todo.Adapter;
//https://www.red5pro.com/docs/streaming/android.html#add-your-sdk-license-key-required

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.super5.todo.Activity.ViewTodoActivity;
import com.super5.todo.BroadcastReceiver.AlarmReceiver;
import com.super5.todo.Model.TodoModel;
import com.super5.todo.R;
import com.super5.todo.db.TodoDb;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<TodoModel> mTodo;
    private TextInputEditText update_title, update_note;
    private TextView update_date, update_time;
    private CheckBox cbRing, cbVibration;

    private TextView update_title_count, update_note_count;

    public boolean isopened =  false;
    public String positon = null;

    AlertDialog.Builder builder;
    LayoutInflater li;
    View dialogView;

    AlertDialog alertDialog;

    public void closeLayout(){
       viewBinderHelper.closeLayout(positon);
    }
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    public TodoAdapter(Context mContext, ArrayList<TodoModel> mTodo) {
        this.mContext = mContext;
        this.mTodo = mTodo;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.todoitem,parent,false);
        return new TodoAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final TodoModel todoModel = mTodo.get(position);
        String title = todoModel.getTitle();
        holder.title.setText(title);

        builder = new AlertDialog.Builder(mContext);
        li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Calendar calendar = dateAndTimeParse(todoModel.getDate(), todoModel.getTime());

        if (calendar.before(Calendar.getInstance())){
            holder.activator.setVisibility(View.GONE);
        } else {
            holder.activator.setVisibility(View.VISIBLE);
            if (todoModel.isStatus()){
                holder.activator.setImageDrawable(mContext.getResources().getDrawable(R.drawable.buttonon));
            } else {
                holder.activator.setImageDrawable(mContext.getResources().getDrawable(R.drawable.buttonoff));
            }
        }
        if (todoModel.isRing()){
            holder.ring.setVisibility(View.VISIBLE);
        } else {
            holder.ring.setVisibility(View.GONE);
        }
        if (todoModel.isVibration()){
            holder.vib.setVisibility(View.VISIBLE);
        } else {
            holder.vib.setVisibility(View.GONE);
        }
        holder.myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewTodoItem(position);
                Log.e("mylayout","called");
                Log.e("isopened",String.valueOf(isopened));
                if (isopened){
                   viewBinderHelper.closeLayout(positon);
                }

                if(ViewTodoActivity.visibility){
                    ViewTodoActivity.fabhiderfromothers();
                }
            }
        });
        holder.activator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ViewTodoActivity.visibility){
                    ViewTodoActivity.fabhiderfromothers();
                }
                // To-Do check if database has activator valu false or true
                //if database value is false then on click button will turn on
                //else if database value is true then on click button will will turn off an store the value in database.
                TodoModel model = mTodo.get(position);
                Log.e("position", position+"");
                if(mTodo.get(position).isStatus()){
                    holder.activator.setImageDrawable(mContext.getResources().getDrawable(R.drawable.buttonoff));
                    reminderStatusUpdate(position, model, false);
                    AlarmReceiver.cancelAlarm(mContext, model.getAlarmId());
                }else {
                    holder.activator.setImageDrawable(mContext.getResources().getDrawable(R.drawable.buttonon));
                    reminderStatusUpdate(position, model, true);
                }
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ViewTodoActivity.visibility){
                    ViewTodoActivity.fabhiderfromothers();
                }
                if (holder.swipeRevealLayout.isOpened()){
                    viewBinderHelper.closeLayout(String.valueOf(position));
                }
                deleteTodoItem(position);
            }
        });
        holder.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewTodoActivity.visibility){
                    ViewTodoActivity.fabhiderfromothers();
                }
                updateTodoItem(position);
                holder.swipeRevealLayout.close(true);

            }
        });
        holder.swipeRevealLayout.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
            @Override
            public void onClosed(SwipeRevealLayout view) {


                //isopened=false;

                if(isopened && positon.equals(String.valueOf(position))){
                    isopened=false;
                }
            }
            @Override
            public void onOpened(SwipeRevealLayout view) {

                if(isopened && positon!=null){
                    viewBinderHelper.closeLayout(positon);
                }
                isopened=true;
                positon = String.valueOf(position);
            }

            @Override
            public void onSlide(SwipeRevealLayout view, float slideOffset) {
                if(ViewTodoActivity.visibility){
                    ViewTodoActivity.fabhiderfromothers();
                }
            }
        });
        if (mTodo != null && position < mTodo.size()) {
//            TodoModel data = mTodo.get(position);
//
            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView

//            if (holder.swipeRevealLayout.isOpened()){
//
//                viewBinderHelper.closeLayout(String.valueOf(position));
//            }

            // put an unique string id as value, can be any string which uniquely define the data
            viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(position));
            //viewBinderHelper.setOpenOnlyOne(true);
            // Bind your data here

        }
    }
    @Override
    public int getItemCount() {
        return mTodo.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        SwipeRevealLayout swipeRevealLayout;
        TextView title, tvDelete, tvUpdate;
        ImageView vib,ring, activator;
        LinearLayout myLayout;
        View sideView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            sideView = itemView.findViewById(R.id.side_view);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_layout);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            tvUpdate = itemView.findViewById(R.id.tv_update);
            title = itemView.findViewById(R.id.tvTitle);
            vib=itemView.findViewById(R.id.imgVibrate);
            ring=itemView.findViewById(R.id.imgRing);
            activator =itemView.findViewById(R.id.activator);
            myLayout=itemView.findViewById(R.id.mylayout);

        }
    }
    private void reminderStatusUpdate(int position, TodoModel model, boolean status){
        TodoDb todoDb = new TodoDb(mContext);

        model.setStatus(status);

        Log.d("dataid", model.getAlarmId().toString());
        Log.d("datatitle", model.toString());

        if(todoDb.updateData(model) >= 0){
            Calendar calendar = dateAndTimeParse(model.getDate(), model.getTime());
            Log.e("CalendarValue", String.valueOf(calendar));
            AlarmReceiver.setAlarm(mContext,calendar,model.getAlarmId());
            mTodo.set(position, model);
            notifyItemChanged(position, model);
        } else {
            Toast.makeText(mContext, "Something went wrong!!", Toast.LENGTH_SHORT).show();
        }
    }

    private Calendar dateAndTimeParse(String date, String time){
        Calendar calendar = Calendar.getInstance();
        String[] strDate=date.split("/",3);
        int day=Integer.parseInt(strDate[0]);
        int month=Integer.parseInt(strDate[1]);
        int year=Integer.parseInt(strDate[2]);
        String[] strTime=time.split(":",2);
        int hour=Integer.parseInt(strTime[0]);
        int minute=Integer.parseInt(strTime[1]);
        month--;
        calendar.set(year,month,day,hour,minute,0);
        return calendar;
    }
    private void deleteTodoItem(final int position) {

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(mContext)
                // set message & title
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        TodoDb todoDb = new TodoDb(mContext);
                        todoDb.deleteData(mTodo.get(position).getAlarmId());
                        AlarmReceiver.cancelAlarm(mContext, mTodo.get(position).getAlarmId());
                        mTodo.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        myQuittingDialogBox.show();
        Button btnPositive = myQuittingDialogBox.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.leftMargin=20;
        btnPositive.setLayoutParams(layoutParams);
    }
    private void updateTodoItem(final int position){
        dialogView = li.inflate(R.layout.update_dialoge_box,null);
        update_title = dialogView.findViewById(R.id.edt_update_title);
        update_note = dialogView.findViewById(R.id.edt_update_note);
        update_date = dialogView.findViewById(R.id.tv_update_date);
        update_time = dialogView.findViewById(R.id.tv_update_time);
        ImageView imgDate = dialogView.findViewById(R.id.imgDate);
        ImageView imgTime = dialogView.findViewById(R.id.imgTime);
        cbRing = dialogView.findViewById(R.id.cb_ring);
        cbVibration = dialogView.findViewById(R.id.cb_vibration);
        Button updateButtton = dialogView.findViewById(R.id.btn_update);

        update_title_count = dialogView.findViewById(R.id.update_title_count);
        update_note_count = dialogView.findViewById(R.id.update_note_count);

        update_title.setText(mTodo.get(position).getTitle());
        update_note.setText(mTodo.get(position).getNote());
        update_date.setText(mTodo.get(position).getDate());
        update_time.setText(mTodo.get(position).getTime());
        cbRing.setChecked(mTodo.get(position).isRing());
        cbVibration.setChecked(mTodo.get(position).isVibration());


        builder.setView(null);
        builder.setView(dialogView);

        alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialogDismiss();

        alertDialog.show();

        update_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        update_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        imgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker();
            }
        });

        updateButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int alarmId = mTodo.get(position).getAlarmId();
                String priority = mTodo.get(position).getPriority();
                String title = Objects.requireNonNull(update_title.getText()).toString();
                String note = Objects.requireNonNull(update_note.getText()).toString();
                String date = update_date.getText().toString();
                String time = update_time.getText().toString();
                boolean ring = cbRing.isChecked();
                boolean vibration = cbVibration.isChecked();
                boolean status = mTodo.get(position).isStatus();

                TodoModel model = new TodoModel(alarmId, priority, title, note, date, time, ring, vibration, status, false);

                Calendar cal = dateAndTimeParse(model.getDate(), model.getTime());

                if (title.isEmpty() || note.isEmpty()){
                    Toast.makeText(mContext, "Fields must contain data", Toast.LENGTH_SHORT).show();
                } else if (cal.before(Calendar.getInstance())) {
                    Toast.makeText(mContext, "Your selected date before then now... Please check", Toast.LENGTH_LONG).show();
                } else {
                    TodoDb todoDb = new TodoDb(mContext);
                    Log.e("data", String.valueOf(alarmId));
                    //AlarmReceiver.cancelAlarm(mContext, alarmId);

                    if (todoDb.updateData(model) <= 0) {
                        Toast.makeText(mContext, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    } else {

                        AlarmReceiver.setAlarm(mContext, cal, model.getAlarmId());
                        alertDialogDismiss();

                        mTodo.set(position, model);
                        notifyItemChanged(position, model);
                        notifyDataSetChanged();


                    }
                }
            }
        });

        update_title.addTextChangedListener(new TextWatcher() {
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
                update_title_count.setText(count+"/50");
            }
        });
        update_note.addTextChangedListener(new TextWatcher() {
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
                update_note_count.setText(count + "/150");
            }
        });
    }

    private void datePicker(){
        String date= update_date.getText().toString();
        String[] strDate=date.split("/",3);
        int day=Integer.parseInt(strDate[0]);
        int month=Integer.parseInt(strDate[1]);
        int year=Integer.parseInt(strDate[2]);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, final int year, int month, final int dayOfMonth) {
                month++;
                Log.e("day",String.valueOf(dayOfMonth));
                Log.e("month",String.valueOf(month));
                Log.e("year",String.valueOf(year));
                dateFormater(dayOfMonth,month,year);
            }
        }, year,month-1,day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private void timePicker(){
        String time= update_time.getText().toString();
        String[] strTime=time.split(":",2);
        int hour=Integer.parseInt(strTime[0]);
        int minute=Integer.parseInt(strTime[1]);
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeFormater(hourOfDay, minute);
            }
        },hour,minute, DateFormat.is24HourFormat(mContext));
        timePickerDialog.show();
    }

    private void dateFormater(int day, int month, int year){
        //month+=1;
        String sDay, sMonth, sYear;
        if (day<10){sDay="0"+day;}else{sDay=""+day; }
        if (month<10){sMonth="0"+month;}else{sMonth=""+month; }
        sYear = ""+year;
        String date= sDay + "/" +sMonth+"/" +sYear;
        update_date.setText(date);
    }

    private void timeFormater(int hour, int minute){
        String sMinute, sHour;
        if(minute < 10){sMinute="0"+minute;}else{sMinute=""+minute;}
        if(hour<10){sHour="0"+hour;}else{sHour=""+hour;}
        update_time.setText(sHour + ":" +sMinute );
    }

    private void viewTodoItem(final int position){
        dialogView = li.inflate(R.layout.itemview_dialoge_box,null);

        TextView itemTitle = dialogView.findViewById(R.id.item_title);
        TextView itemNote = dialogView.findViewById(R.id.item_note);
        TextView itemDate = dialogView.findViewById(R.id.item_date);
        TextView itemTime = dialogView.findViewById(R.id.item_time);
        Button btnItemUpdate = dialogView.findViewById(R.id.btn_item_update);
        Button btnItemDelete = dialogView.findViewById(R.id.btn_item_delete);
        ImageView imgItemRing = dialogView.findViewById(R.id.img_item_ring);
        ImageView imgItemVibration = dialogView.findViewById(R.id.img_item_vibrate);
        View viewSound = dialogView.findViewById(R.id.view_sound);


        itemTitle.setText(mTodo.get(position).getTitle());
        itemNote.setText(mTodo.get(position).getNote());
        itemDate.setText(mTodo.get(position).getDate());
        itemTime.setText(mTodo.get(position).getTime());

        if (mTodo.get(position).isRing()){
            imgItemRing.setVisibility(View.VISIBLE);
        } else {
            imgItemRing.setVisibility(View.GONE);
        }

        if (mTodo.get(position).isVibration()){
            imgItemVibration.setVisibility(View.VISIBLE);
        } else {
            imgItemVibration.setVisibility(View.GONE);
        }

        if (mTodo.get(position).isRing() && mTodo.get(position).isVibration()){
            viewSound.setVisibility(View.VISIBLE);
        } else {
            viewSound.setVisibility(View.GONE);
        }

        builder.setView(null);
        builder.setView(dialogView);

        alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialogDismiss();
        alertDialog.show();


        btnItemUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDismiss();
                updateTodoItem(position);
            }
        });

        btnItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogDismiss();
                deleteTodoItem(position);
            }
        });
    }

    private void alertDialogDismiss(){
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }

}
