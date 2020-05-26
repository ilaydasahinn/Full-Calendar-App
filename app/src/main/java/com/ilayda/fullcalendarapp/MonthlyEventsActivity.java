package com.ilayda.fullcalendarapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class MonthlyEventsActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    private ListView mListView;
    private boolean darkmode;

    int year;
    int month;
    int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_layout);

        mListView = (ListView)findViewById(R.id.display_listView);
        mDatabaseHelper = new DatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        darkmode = preferences.getBoolean("switchKey", false);
        RelativeLayout dm = findViewById(R.id.monthly_layout);
        if(darkmode){
            dm.setBackground(getResources().getDrawable(R.color.darkGrey));
        }

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        year = incomingIntent.getIntExtra("year", 0);
        month = incomingIntent.getIntExtra("month", 0) -1;
        dayOfMonth = incomingIntent.getIntExtra("dayOfMonth", 0);

        populateListView(year, month, dayOfMonth);
    }

    private void populateListView(int year, int month, int dayOfMonth){
        Cursor data = mDatabaseHelper.getData();
        ArrayList<EventObject> listData = new ArrayList<>();
        while(data.moveToNext() ) {
            if(Integer.valueOf(data.getString(1)) == year && Integer.valueOf(data.getString(2)) == month ){
                EventObject eventObjectTmp = new EventObject(data.getString(0), data.getString(3) + "/" + data.getString(2) + "/" + data.getString(1) + " " + data.getString(5) + ":" + data.getString(6), data.getString(3) + "/" + data.getString(2) + "/" + data.getString(1) + " " + data.getString(7) + ":" + data.getString(8), data.getString(4), data.getInt(5), data.getInt(6), data.getInt(7), data.getInt(8), data.getInt(9), data.getInt(10), data.getInt(11), data.getString(12), data.getInt(13), data.getInt(1), data.getInt(2), data.getInt(3));
                listData.add(eventObjectTmp);
            }
        }

        EventAdapter adapter = new EventAdapter(this, listData, mDatabaseHelper);
        mListView.setAdapter(adapter);
    }
}
