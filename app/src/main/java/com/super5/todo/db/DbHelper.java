package com.super5.todo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    static final String TABLE_NAME = "todo";
    static final String COL_ALARM_ID = "alarm_id";
    static final String COL_PRIORITY = "priority";
    static final String COL_TITLE = "title";
    static final String COL_DATE = "date";
    static final String COL_TIME = "time";
    static final String COL_NOTE = "note";
    static final String COL_RING = "ring";
    static final String COL_VIBRATION = "vibration";
    static final String COL_STATUS = "status";

    DbHelper(Context context) {
        super(context, "database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+COL_ALARM_ID+" INTEGER PRIMARY KEY,"+COL_PRIORITY+" TEXT,"+COL_TITLE+" TEXT,"+COL_NOTE+" TEXT, "+COL_DATE+" TEXT, "+COL_TIME+" TEXT,"+COL_RING+" BOOLEAN,"+COL_VIBRATION+" BOOLEAN,"+COL_STATUS+" BOOLEAN )";

        Log.d("query", query);

        db.execSQL(query);

        Log.d("db", "Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}