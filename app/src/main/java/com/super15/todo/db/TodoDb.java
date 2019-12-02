package com.super15.todo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.super15.todo.Model.TodoModel;

import java.util.ArrayList;

public class TodoDb extends DbHelper {


    public TodoDb(Context context) {
        super(context);
    }

    public void insertData(TodoModel todoModel){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbHelper.COL_TITLE, todoModel.getTitle() );
        contentValues.put(DbHelper.COL_DATE, todoModel.getDate() );
        contentValues.put(DbHelper.COL_TIME, todoModel.getTime());
        contentValues.put(DbHelper.COL_NOTE, todoModel.getNote());

        try {
            db.insert(DbHelper.TABLE_NAME,null,contentValues);
            Log.i("Insert", "Hoise");
        } catch (SQLException e)
        {
            Log.e("Insert Eroor", e.toString());
        }

        db.close();
    }

    public ArrayList<TodoModel> getData(){

        ArrayList<TodoModel> data = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_NAME,null,null,null,null,null,null);

        c.moveToFirst();

        while (c.moveToNext()){
            String id = c.getString(c.getColumnIndex(COL_ID));
            String title = c.getString(c.getColumnIndex(COL_TITLE));
            String note = c.getString(c.getColumnIndex(COL_NOTE));
            String date = c.getString(c.getColumnIndex(COL_DATE));
            String time = c.getString(c.getColumnIndex(COL_TIME));

            TodoModel todoModel = new TodoModel(id,title,note,date,time);
            data.add(todoModel);

        }

        db.close();

        Log.e("data 2", data.toString());



        return data;
    }

    public int updateData(TodoModel todoModels){
        SQLiteDatabase db = this.getWritableDatabase();



        String id = todoModels.getId();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbHelper.COL_TITLE, todoModels.getTitle());
        contentValues.put(DbHelper.COL_DATE, todoModels.getDate() );
        contentValues.put(DbHelper.COL_TIME, todoModels.getTime());
        contentValues.put(DbHelper.COL_NOTE, todoModels.getNote());

        return db.update(DbHelper.TABLE_NAME,contentValues,DbHelper.COL_ID+"="+id,null);
    }

    public int deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(DbHelper.TABLE_NAME,DbHelper.COL_ID+"="+id,null);

    }


}
