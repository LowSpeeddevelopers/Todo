package com.super15.todo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.super15.todo.Adapter.TodoAdapter;
import com.super15.todo.Model.TodoModel;
import com.super15.todo.R;
import com.super15.todo.db.TodoDb;

import java.util.ArrayList;

public class ViewTodoActivity extends AppCompatActivity {

    private RecyclerView rvTodo;
    private FloatingActionButton fabAdd;

    private TodoAdapter todoAdapter;
    private TodoDb todoDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_todo);

        rvTodo = findViewById(R.id.rv_todo);
        fabAdd = findViewById(R.id.fab_add);

        todoDb = new TodoDb(getApplicationContext());

        ArrayList<TodoModel> todoModels = todoDb.getData();

        rvTodo.setHasFixedSize(true);
        rvTodo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        todoAdapter = new TodoAdapter(getApplicationContext(),todoModels);

        Log.e("data 1",todoModels.toString());



        rvTodo.setAdapter(todoAdapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewTodoActivity.this,AddTodoActivity.class));
            }
        });
    }
}
