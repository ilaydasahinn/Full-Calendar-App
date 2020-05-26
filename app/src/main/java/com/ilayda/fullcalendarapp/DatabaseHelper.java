package com.ilayda.fullcalendarapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "event_table5";
    private static final String COL1 = "Event";
    private static final String COL2 = "Year";
    private static final String COL3 = "Month";
    private static final String COL4 = "DayOfMonth";
    private static final String COL5 = "Detail";
    private static final String COL6 = "StartTimeHour";
    private static final String COL7 = "StartTimeMinute";
    private static final String COL8 = "FinishTimeHour";
    private static final String COL9 = "FinishTimeMinute";
    private static final String COL10 = "RemindTimeHour";
    private static final String COL11 = "RemindTimeMinute";
    private static final String COL12 = "Repeat";
    private static final String COL13 = "Address";


    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" + COL1 + " TEXT," + COL2 + " INTEGER," + COL3 + " INTEGER," + COL4 + " INTEGER," + COL5 + " TEXT," + COL6 + " INTEGER," + COL7 + " INTEGER," + COL8 + " INTEGER," + COL9 + " INTEGER," + COL10 + " INTEGER," + COL11 + " INTEGER," + COL12 + " INTEGER," + COL13 + " TEXT, ID INTEGER PRIMARY KEY AUTOINCREMENT);";

        try{
            db.execSQL(createTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    public long addData(String event_name, int year, int month, int day_of_month, String detail, int start_time_hour, int start_time_minute,
                           int finish_time_hour, int finish_time_minute, int remind_time_hour, int remind_time_minute, int repeat, String address){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, event_name);
        contentValues.put(COL2, year);
        contentValues.put(COL3, month);
        contentValues.put(COL4, day_of_month);
        contentValues.put(COL5, detail);
        contentValues.put(COL6, start_time_hour);
        contentValues.put(COL7, start_time_minute);
        contentValues.put(COL8, finish_time_hour);
        contentValues.put(COL9, finish_time_minute);
        contentValues.put(COL10, remind_time_hour);
        contentValues.put(COL11, remind_time_minute);
        contentValues.put(COL12, repeat);
        contentValues.put(COL13, address);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean update(String event_name, int year, int month, int day_of_month, String detail, int start_time_hour, int start_time_minute,
                           int finish_time_hour, int finish_time_minute, int remind_time_hour, int remind_time_minute, int repeat, String address, int ID){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, event_name);
        contentValues.put(COL2, year);
        contentValues.put(COL3, month);
        contentValues.put(COL4, day_of_month);
        contentValues.put(COL5, detail);
        contentValues.put(COL6, start_time_hour);
        contentValues.put(COL7, start_time_minute);
        contentValues.put(COL8, finish_time_hour);
        contentValues.put(COL9, finish_time_minute);
        contentValues.put(COL10, remind_time_hour);
        contentValues.put(COL11, remind_time_minute);
        contentValues.put(COL12, repeat);
        contentValues.put(COL13, address);

        long result = db.update(TABLE_NAME, contentValues, "ID=" + ID, null);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean delete(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID=?", new String[] {String.valueOf(ID)}) > 0;

    }
}
