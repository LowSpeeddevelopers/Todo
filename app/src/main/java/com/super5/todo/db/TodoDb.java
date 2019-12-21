package com.super5.todo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.super5.todo.Model.TodoModel;

import java.util.ArrayList;

public class TodoDb extends DbHelper {


    public TodoDb(Context context) {
        super(context);
    }

    public void insertData(TodoModel todoModel){
        SQLiteDatabase db = getWritableDatabase();




        Log.e("todoid",String.valueOf(todoModel.getAlarmId()));

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbHelper.COL_ALARM_ID, todoModel.getAlarmId());
        contentValues.put(DbHelper.COL_PRIORITY, todoModel.getPriority());
        contentValues.put(DbHelper.COL_TITLE, todoModel.getTitle());
        contentValues.put(DbHelper.COL_DATE, todoModel.getDate());
        contentValues.put(DbHelper.COL_TIME, todoModel.getTime());
        contentValues.put(DbHelper.COL_NOTE, todoModel.getNote());
        contentValues.put(DbHelper.COL_RING, todoModel.isRing());
        contentValues.put(DbHelper.COL_VIBRATION, todoModel.isVibration());
        contentValues.put(DbHelper.COL_STATUS, todoModel.isStatus());

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

        while (c.moveToNext()){
            int alarmId = c.getInt(c.getColumnIndex(COL_ALARM_ID));
            String priority = c.getString(c.getColumnIndex(COL_PRIORITY));
            String title = c.getString(c.getColumnIndex(COL_TITLE));
            String note = c.getString(c.getColumnIndex(COL_NOTE));
            String date = c.getString(c.getColumnIndex(COL_DATE));
            String time = c.getString(c.getColumnIndex(COL_TIME));
            boolean ring = c.getInt(c.getColumnIndex(COL_RING)) > 0;
            boolean vibration = c.getInt(c.getColumnIndex(COL_VIBRATION)) > 0;
            boolean status = c.getInt(c.getColumnIndex(COL_STATUS)) > 0;

            TodoModel todoModel = new TodoModel(alarmId,priority,title,note,date,time,ring,vibration,status);
            data.add(todoModel);

        }

        db.close();
        c.close();

        Log.e("data 2", data.toString());



        return data;
    }

    public TodoModel getDataByAlarmID(int alarmID){

        TodoModel todoModel = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + DbHelper.TABLE_NAME+ " where "+DbHelper.COL_ALARM_ID+"="+ alarmID +"";

        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()){
            int alarmId = c.getInt(c.getColumnIndex(COL_ALARM_ID));
            String priority = c.getString(c.getColumnIndex(COL_PRIORITY));
            String title = c.getString(c.getColumnIndex(COL_TITLE));
            String note = c.getString(c.getColumnIndex(COL_NOTE));
            String date = c.getString(c.getColumnIndex(COL_DATE));
            String time = c.getString(c.getColumnIndex(COL_TIME));
            boolean ring = c.getInt(c.getColumnIndex(COL_RING)) > 0;
            boolean vibration = c.getInt(c.getColumnIndex(COL_VIBRATION)) > 0;
            boolean status = c.getInt(c.getColumnIndex(COL_STATUS)) > 0;

            todoModel = new TodoModel(alarmId,priority,title,note,date,time,ring,vibration,status);

        }

        db.close();
        c.close();

        return todoModel;
    }


    public int updateData(TodoModel todoModels){
        SQLiteDatabase db = this.getWritableDatabase();

        int id = todoModels.getAlarmId();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbHelper.COL_ALARM_ID, todoModels.getAlarmId());
        contentValues.put(DbHelper.COL_PRIORITY, todoModels.getPriority());
        contentValues.put(DbHelper.COL_TITLE, todoModels.getTitle());
        contentValues.put(DbHelper.COL_DATE, todoModels.getDate() );
        contentValues.put(DbHelper.COL_TIME, todoModels.getTime());
        contentValues.put(DbHelper.COL_NOTE, todoModels.getNote());
        contentValues.put(DbHelper.COL_RING, todoModels.isRing());
        contentValues.put(DbHelper.COL_VIBRATION, todoModels.isVibration());
        contentValues.put(DbHelper.COL_STATUS, todoModels.isStatus());

        return db.update(DbHelper.TABLE_NAME,contentValues,DbHelper.COL_ALARM_ID+"="+id,null);
    }

    public int deleteData(Integer id){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(DbHelper.TABLE_NAME,DbHelper.COL_ALARM_ID+"="+id,null);

    }


}
