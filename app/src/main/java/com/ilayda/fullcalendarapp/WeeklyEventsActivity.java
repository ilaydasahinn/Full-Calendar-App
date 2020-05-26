package com.ilayda.fullcalendarapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class WeeklyEventsActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    private ListView mListView;
    private boolean darkmode;
    int year;
    int month;
    int dayOfMonth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_layout);

        mListView = (ListView)findViewById(R.id.display_listView);
        mDatabaseHelper = new DatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        darkmode = preferences.getBoolean("switchKey", false);
        RelativeLayout dm = findViewById(R.id.weekly_layout);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void populateListView(int year, int month, int dayOfMonth){

        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month -1, dayOfMonth);
        int now = dayOfWeek.getValue();

        int firstDay;
        int lastDay;
        int firstMonth;
        int lastMonth;
        if(now == 1){ //monday
            calendar.add(Calendar.DATE, 0);
            firstDay = calendar.get(Calendar.DAY_OF_MONTH);
            firstMonth = calendar.get(Calendar.MONTH );
            calendar.add(Calendar.DATE, 6); //pazar gunu
            lastDay = calendar.get(Calendar.DAY_OF_MONTH);
            lastMonth = calendar.get(Calendar.MONTH );

        } else if(now == 2){ //tuesday
            calendar.add(Calendar.DATE, -1);
            firstDay = calendar.get(Calendar.DAY_OF_MONTH);
            firstMonth = calendar.get(Calendar.MONTH);
            calendar.add(Calendar.DATE, 6);
            lastDay = calendar.get(Calendar.DAY_OF_MONTH);
            lastMonth = calendar.get(Calendar.MONTH);

        } else if(now == 3){ //wednesday
            calendar.add(Calendar.DATE, -2);
            firstDay = calendar.get(Calendar.DAY_OF_MONTH);
            firstMonth = calendar.get(Calendar.MONTH);
            calendar.add(Calendar.DATE, 6);
            lastDay = calendar.get(Calendar.DAY_OF_MONTH);
            lastMonth = calendar.get(Calendar.MONTH);

        } else if(now == 4){ //thursday
            calendar.add(Calendar.DATE, -3);
            firstDay = calendar.get(Calendar.DAY_OF_MONTH);
            firstMonth = calendar.get(Calendar.MONTH);
            calendar.add(Calendar.DATE, 6);
            lastDay = calendar.get(Calendar.DAY_OF_MONTH);
            lastMonth = calendar.get(Calendar.MONTH);

        } else if(now == 5){ //friday
            calendar.add(Calendar.DATE, -4);
            firstDay = calendar.get(Calendar.DAY_OF_MONTH);
            firstMonth = calendar.get(Calendar.MONTH);
            calendar.add(Calendar.DATE, 6);
            lastDay = calendar.get(Calendar.DAY_OF_MONTH);
            lastMonth = calendar.get(Calendar.MONTH);

        } else if(now == 6){ //saturday
            calendar.add(Calendar.DATE, -5);
            firstDay = calendar.get(Calendar.DAY_OF_MONTH);
            firstMonth = calendar.get(Calendar.MONTH);
            calendar.add(Calendar.DATE, 6);
            lastDay = calendar.get(Calendar.DAY_OF_MONTH);
            lastMonth = calendar.get(Calendar.MONTH);

        } else { //sunday
            calendar.add(Calendar.DATE, -6);
            firstDay = calendar.get(Calendar.DAY_OF_MONTH);
            firstMonth = calendar.get(Calendar.MONTH);
            calendar.add(Calendar.DATE, 6);
            lastDay = calendar.get(Calendar.DAY_OF_MONTH);
            lastMonth = calendar.get(Calendar.MONTH);
        }

        firstMonth = firstMonth +1;
        lastMonth = lastMonth + 1;

        Cursor data = mDatabaseHelper.getData();
        ArrayList<EventObject> listData = new ArrayList<>();
        while(data.moveToNext() ) {


            if(dayOfMonth >= firstDay && dayOfMonth<=lastDay){
                if(Integer.valueOf(data.getString(1)) == year && Integer.valueOf(data.getString(2)) == firstMonth && Integer.valueOf(data.getString(2)) == lastMonth && Integer.valueOf(data.getString(3)) >= firstDay && Integer.valueOf(data.getString(3)) <= lastDay){
                    EventObject eventObjectTmp = new EventObject(data.getString(0), data.getString(3) + "/" + data.getString(2) + "/" + data.getString(1) + " " + data.getString(5) + ":" + data.getString(6), data.getString(3) + "/" + data.getString(2) + "/" + data.getString(1) + " " + data.getString(7) + ":" + data.getString(8), data.getString(4), data.getInt(5), data.getInt(6), data.getInt(7), data.getInt(8), data.getInt(9), data.getInt(10), data.getInt(11), data.getString(12), data.getInt(13), data.getInt(1), data.getInt(2), data.getInt(3));
                    listData.add(eventObjectTmp);
                }
            }else{
                if(Integer.valueOf(data.getString(1)) == year && ((Integer.valueOf(data.getString(2)) == firstMonth && Integer.valueOf(data.getString(3)) >= firstDay) || (Integer.valueOf(data.getString(2)) == lastMonth && Integer.valueOf(data.getString(3)) <= lastDay ))){
                    EventObject eventObjectTmp = new EventObject(data.getString(0), data.getString(3) + "/" + data.getString(2) + "/" + data.getString(1) + " " + data.getString(5) + ":" + data.getString(6), data.getString(3) + "/" + data.getString(2) + "/" + data.getString(1) + " " + data.getString(7) + ":" + data.getString(8), data.getString(4), data.getInt(5), data.getInt(6), data.getInt(7), data.getInt(8), data.getInt(9), data.getInt(10), data.getInt(11), data.getString(12), data.getInt(13), data.getInt(1), data.getInt(2), data.getInt(3));
                    listData.add(eventObjectTmp);
                }
            }

        }

        EventAdapter adapter = new EventAdapter(this, listData, mDatabaseHelper);
        mListView.setAdapter(adapter);
    }


}
