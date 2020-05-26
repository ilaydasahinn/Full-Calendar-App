package com.ilayda.fullcalendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class DateActivity extends AppCompatActivity {

    public static final String TAG = "DateActivity";
    private TextView theDate;
    private Button btnGoCalendar;
    private Button btnAddNewEvent;
    private ListView mListView;
    private Button weekly;
    private Button monthly;
    private boolean darkmode;
    DatabaseHelper mDatabaseHelper;
    int year;
    int month;
    int dayOfMonth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date2_layout);

        mDatabaseHelper =  new DatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        darkmode = preferences.getBoolean("switchKey", false);
        RelativeLayout dm = findViewById(R.id.date2_layout);
        if(darkmode){
            dm.setBackground(getResources().getDrawable(R.color.darkGrey));
        }

        theDate = (TextView)findViewById(R.id.date);
        btnGoCalendar = (Button)findViewById(R.id.btnGoCalendar);
        btnAddNewEvent = (Button)findViewById(R.id.btnAddNewEvent);
        mListView = (ListView)findViewById(R.id.display_listView);
        weekly = (Button)findViewById(R.id.weekly);
        monthly = (Button)findViewById(R.id.monthly);

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        year = incomingIntent.getIntExtra("year", 0);
        month = incomingIntent.getIntExtra("month", 0) + 1;
        dayOfMonth = incomingIntent.getIntExtra("dayOfMonth", 0);
        theDate.setText(date);

        populateListView(year, month, dayOfMonth);

        btnGoCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DateActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        btnAddNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DateActivity.this, AddEventActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DateActivity.this, WeeklyEventsActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month + 1);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DateActivity.this, MonthlyEventsActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month + 1);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
            }
        });

    }

    private void populateListView(int year, int month, int dayOfMonth){
        Cursor data = mDatabaseHelper.getData();
        ArrayList<EventObject> listData = new ArrayList<>();
        while(data.moveToNext() ) {
            if(Integer.valueOf(data.getString(1)) == year && Integer.valueOf(data.getString(2)) == month && Integer.valueOf(data.getString(3)) == dayOfMonth){
                EventObject eventObjectTmp = new EventObject(data.getString(0), data.getString(3) + "/" + data.getString(2) + "/" + data.getString(1) + " " + data.getString(5) + ":" + data.getString(6), data.getString(3) + "/" + data.getString(2) + "/" + data.getString(1) + " " + data.getString(7) + ":" + data.getString(8), data.getString(4), data.getInt(5), data.getInt(6), data.getInt(7), data.getInt(8), data.getInt(9), data.getInt(10), data.getInt(11), data.getString(12), data.getInt(13), data.getInt(1), data.getInt(2), data.getInt(3));
                listData.add(eventObjectTmp);
            }
        }

        EventAdapter adapter = new EventAdapter(this, listData, mDatabaseHelper);
        mListView.setAdapter(adapter);
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
