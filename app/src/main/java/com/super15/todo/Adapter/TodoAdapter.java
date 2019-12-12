package com.super15.todo.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.super15.todo.BroadcustReceiver.AlarmReceiver;
import com.super15.todo.Model.TodoModel;
import com.super15.todo.R;
import com.super15.todo.db.TodoDb;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<TodoModel> mTodo;
    private Calendar cal;
    private TextInputEditText update_title, update_note;
    private TextView update_date, update_time;
    private CheckBox cbRing, cbVibration;
    private ViewHolder holder;
    private boolean b=true;
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
        TodoModel todoModel = mTodo.get(position);
        final String title;
        title = todoModel.getTitle();
        holder.title.setText(title);
        cal=Calendar.getInstance();
        this.holder=holder;
        holder.myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewTodoItem(position);
            }
        });

        holder.activatior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To-Do check if database has activator valu false or true
                //if database value is false then on click button will turn on
                //else if database value is true then on click button will will turn off an store the value in database.
                if(b){
                    holder.activatior.setImageDrawable(mContext.getResources().getDrawable(R.drawable.buttonon));
                    b=false;
                }else {
                    holder.activatior.setImageDrawable(mContext.getResources().getDrawable(R.drawable.buttonoff));
                    b=true;
                }

            }
        });




        if (mTodo != null && position < mTodo.size()) {
//            TodoModel data = mTodo.get(position);
//
            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(position));

            viewBinderHelper.setOpenOnlyOne(true);


            // Bind your data here
            holder.bind(position);
        }


    }

    @Override
    public int getItemCount() {
        return mTodo.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        SwipeRevealLayout swipeRevealLayout;
        TextView title, tvDelete, tvUpdate;
        ImageView vib,ring, activatior;
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
            activatior=itemView.findViewById(R.id.activator);
            myLayout=itemView.findViewById(R.id.mylayout);

        }

        void bind(final int data) {
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTodoItem(data);
                }
            });

            tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTodoItem(data);
                    holder.swipeRevealLayout.close(true);
                }
            });
        }
    }



    private void deleteTodoItem(final int position) {

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(mContext)
                // set message & title
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        TodoDb todoDb = new TodoDb(mContext);
                        todoDb.deleteData(mTodo.get(position).getId());
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

    }


    private void updateTodoItem(final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert li != null;
        View dialogueView = li.inflate(R.layout.update_dialoge_box,null);
        update_title = dialogueView.findViewById(R.id.edt_update_title);
        update_note = dialogueView.findViewById(R.id.edt_update_note);
        update_date = dialogueView.findViewById(R.id.tv_update_date);
        update_time = dialogueView.findViewById(R.id.tv_update_time);
        ImageView imgDate = dialogueView.findViewById(R.id.imgDate);
        ImageView imgTime = dialogueView.findViewById(R.id.imgTime);
        cbRing = dialogueView.findViewById(R.id.cb_ring);
        cbVibration = dialogueView.findViewById(R.id.cb_vibration);
        Button updateButtton = dialogueView.findViewById(R.id.btn_update);
        update_title.setText(mTodo.get(position).getTitle());
        update_note.setText(mTodo.get(position).getNote());
        update_date.setText(mTodo.get(position).getDate());
        update_time.setText(mTodo.get(position).getTime());
        cbRing.setChecked(mTodo.get(position).isRing());
        cbVibration.setChecked(mTodo.get(position).isVibration());


        builder.setView(dialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
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
                int id = mTodo.get(position).getId();
                int alarmId = mTodo.get(position).getAlarmId();
                String priority = mTodo.get(position).getPriority();
                String title = Objects.requireNonNull(update_title.getText()).toString();
                String note = Objects.requireNonNull(update_note.getText()).toString();
                String date = update_date.getText().toString();
                String time = update_time.getText().toString();
                boolean ring = cbRing.isChecked();
                boolean vibration = cbVibration.isChecked();
                String status = mTodo.get(position).getStatus();

                if (title.isEmpty() || note.isEmpty()){
                    Toast.makeText(mContext, "Fields must contain data", Toast.LENGTH_SHORT).show();
                } else {
                    TodoDb todoDb = new TodoDb(mContext);

                    AlarmReceiver.cancelAlarm(mContext, alarmId);

                    alarmId = (int) System.currentTimeMillis();

                    TodoModel model = new TodoModel(id,alarmId,priority,title,note,date,time,ring,vibration,status);

                    Log.e("mTodo", model.getId()+" "+model.getTitle()+" "+model.getNote()+" "+model.getDate()+" "+model.getTime());

                    Log.e("Position & ID",position+"   "+ mTodo.get(position).getId());

                    if (todoDb.updateData(model) != 1){
                        Toast.makeText(mContext, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    } else {
                        AlarmReceiver.setAlarm(mContext,cal,model);
                        alertDialog.cancel();
                        mTodo.set(position, model);
                        notifyItemChanged(position, model);
                        notifyDataSetChanged();
                    }
                }
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
            public void onDateSet(DatePicker datePicker, final int year, final int month, final int dayOfMonth) {
                Log.e("day",String.valueOf(dayOfMonth));
                Log.e("month",String.valueOf(month));
                Log.e("year",String.valueOf(year));
                dateFormater(dayOfMonth,month,year);
                cal.set(year,month,dayOfMonth);
            }
        }, year,month,day);
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
                cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                cal.set(Calendar.MINUTE,minute);
                cal.set(Calendar.SECOND,0);

            }
        },hour,minute, DateFormat.is24HourFormat(mContext));
        timePickerDialog.show();
    }

    private void dateFormater(int day, int month, int year){
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert li != null;
        final View dialogueView = li.inflate(R.layout.itemview_dialoge_box,null);

        TextView itemTitle = dialogueView.findViewById(R.id.item_title);
        TextView itemNote = dialogueView.findViewById(R.id.item_note);
        TextView itemDate = dialogueView.findViewById(R.id.item_date);
        TextView itemTime = dialogueView.findViewById(R.id.item_time);
        Button btnItemUpdate = dialogueView.findViewById(R.id.btn_item_update);
        Button btnItemDelete = dialogueView.findViewById(R.id.btn_item_delete);


        itemTitle.setText(mTodo.get(position).getTitle());
        itemNote.setText(mTodo.get(position).getNote());
        itemDate.setText(mTodo.get(position).getDate());
        itemTime.setText(mTodo.get(position).getTime());

        builder.setView(dialogueView);

        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        btnItemUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                updateTodoItem(position);
            }
        });

        btnItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                deleteTodoItem(position);
            }
        });
    }

}
