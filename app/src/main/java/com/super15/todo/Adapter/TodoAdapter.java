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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.super15.todo.Model.TodoModel;
import com.super15.todo.R;
import com.super15.todo.db.TodoDb;

import java.util.ArrayList;
import java.util.Calendar;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<TodoModel> mTodos;

    Calendar cal;

    TodoModel todoModel;

    EditText update_title, update_note;

    TextView update_date, update_time;

    public TodoAdapter(Context mContext, ArrayList<TodoModel> mTodos) {
        this.mContext = mContext;
        this.mTodos = mTodos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.todo_item,parent,false);

        return new TodoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        todoModel = mTodos.get(position);

        final String id, title, note, date, time;

        id = todoModel.getId();
        title = todoModel.getTitle();
        note = todoModel.getNote();
        date = todoModel.getDate();
        time = todoModel.getTime();

        holder.title.setText(title);
        holder.note.setText(note);
        holder.date.setText(date);
        holder.time.setText(time);

        cal=Calendar.getInstance();

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TodoModel upTodoModel = new TodoModel(id, title, note, date, time);

                showDialoguebox(upTodoModel, holder);

                Toast.makeText(mContext, "Update", Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setMessage("Are you sure?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TodoDb todoDb = new TodoDb(mContext);

                                todoDb.deleteData(id);

                                mTodos.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, note, date, time;
        Button update, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            note = itemView.findViewById(R.id.tv_note);
            date = itemView.findViewById(R.id.tv_date);
            time = itemView.findViewById(R.id.tv_time);
            update = itemView.findViewById(R.id.btn_update);
            delete = itemView.findViewById(R.id.btn_delete);
        }
    }


    private void showDialoguebox(final TodoModel todoModel, final ViewHolder holder){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogueView = li.inflate(R.layout.update_dialoge_box,null);

        update_title = dialogueView.findViewById(R.id.edt_update_title);
        update_note = dialogueView.findViewById(R.id.edt_update_note);

        update_date = dialogueView.findViewById(R.id.tv_update_date);
        update_time = dialogueView.findViewById(R.id.tv_update_time);

        Button updateButtton = dialogueView.findViewById(R.id.btn_update);

        update_title.setText(todoModel.getTitle());
        update_note.setText(todoModel.getNote());

        update_date.setText(todoModel.getDate());
        update_time.setText(todoModel.getTime());

        builder.setView(dialogueView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();

        update_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });


        update_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                //String times= ;


            }
        });

        updateButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = update_title.getText().toString();
                String note = update_note.getText().toString();
                String date = update_date.getText().toString();
                String time = update_time.getText().toString();

                if (title.isEmpty() || note.isEmpty()){
                    Toast.makeText(mContext, "Fields must contain data", Toast.LENGTH_SHORT).show();
                } else {
                    TodoDb todoDb = new TodoDb(mContext);

                    TodoModel todoModel1 = new TodoModel(todoModel.getId(),title,note,date,time);

                    if (todoDb.updateData(todoModel1) != 1){
                        Toast.makeText(mContext, "Someting went wrong!!", Toast.LENGTH_SHORT).show();
                    } else {
                        alertDialog.cancel();
                        mTodos.set(holder.getAdapterPosition(),todoModel1);
                        notifyItemChanged(holder.getAdapterPosition(),todoModel1);
                    }
                }

            }
        });




    }

    private void dateFormater(int day, int month, int year){

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

        update_date.setText(date);
    }

    private void timeFormater(int hour, int minute){

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

        update_time.setText(sHour + ":" +sMinute );

    }
}
