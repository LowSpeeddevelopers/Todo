package com.super15.todo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.super15.todo.Model.TodoModel;
import com.super15.todo.R;
import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<TodoModel> mTodos;

    TodoModel todoModel;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        todoModel = mTodos.get(position);

        String id, title, note, date, time;

        id = todoModel.getId();
        title = todoModel.getTitle();
        note = todoModel.getNote();
        date = todoModel.getDate();
        time = todoModel.getTime();

        holder.title.setText(title);
        holder.note.setText(note);
        holder.date.setText(date);
        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, note, date, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            note = itemView.findViewById(R.id.tv_note);
            date = itemView.findViewById(R.id.tv_date);
            time = itemView.findViewById(R.id.tv_time);
        }
    }

}
