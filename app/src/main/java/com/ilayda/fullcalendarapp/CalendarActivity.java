package com.ilayda.fullcalendarapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    public static final String TAG = "CalendarActivity";
    private CalendarView calendarView;
    private ImageButton settings;
    private boolean darkmode;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mDatabaseHelper =  new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        while(data.moveToNext() ) {
                EventObject eventObjectTmp = new EventObject(data.getString(0), data.getString(3) + "/" + data.getString(2) + "/" + data.getString(1) + " " + data.getString(5) + ":" + data.getString(6), data.getString(3) + "/" + data.getString(2) + "/" + data.getString(1) + " " + data.getString(7) + ":" + data.getString(8), data.getString(4), data.getInt(5), data.getInt(6), data.getInt(7), data.getInt(8), data.getInt(9), data.getInt(10), data.getInt(11), data.getString(12), data.getInt(13), data.getInt(1), data.getInt(2), data.getInt(3));
                AlarmManagerr.setAlarm(eventObjectTmp.getEventName(), eventObjectTmp.getRemindHour(),eventObjectTmp.getRemindMinute(),eventObjectTmp.getDayOfMonth(),eventObjectTmp.getMonth(),eventObjectTmp.getYear(),getApplicationContext(), eventObjectTmp.getID());
        }

        calendarView = (CalendarView)findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(2);
        settings = (ImageButton)findViewById(R.id.btn_settings);


        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        darkmode = preferences.getBoolean("switchKey", false);
        RelativeLayout dm = findViewById(R.id.calendar_layout);

        if(darkmode){
            dm.setBackground(getResources().getDrawable(R.color.darkGrey));
        }

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month+1) + "/" + year;

                Intent intent = new Intent(CalendarActivity.this, DateActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
            }
        });





    }
}
