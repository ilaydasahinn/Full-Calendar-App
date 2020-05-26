package com.ilayda.fullcalendarapp;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import java.util.HashMap;

public class AlarmManagerr extends Application {

    static HashMap<Integer, AlarmManager> alarmManagers = new HashMap<Integer, AlarmManager>();
    static  HashMap<Integer, Intent> intents = new HashMap<Integer, Intent>();

    public static void setAlarm(String eventName, int remindHour, int remindMinute, int dayOfMonth, int month, int year, Context context, int id){


        if(!intents.containsKey(id)){
            Calendar tmpCal = Calendar.getInstance();
            Calendar calSet = Calendar.getInstance();

            calSet.set(Calendar.HOUR_OF_DAY, remindHour);
            calSet.set(Calendar.MINUTE, remindMinute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            calSet.set(Calendar.YEAR, year);
            calSet.set(Calendar.MONTH, month-1);
            calSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if(tmpCal.before(calSet)){
                intents.put(id, new Intent(context, AlarmReceiver.class));
                intents.get(id).putExtra("eventName", eventName);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intents.get(id), 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
                alarmManagers.put(id, alarmManager);
            }

        }

    }

    public static void cancelAlarm(int id, Context context){
        if(intents.containsKey(id)){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intents.get(id), PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManagers.get(id).cancel(pendingIntent);
        }
    }

    public static void updateAlarm(String eventName, int remindHour, int remindMinute, int dayOfMonth, int month, int year, Context context, int id){

        cancelAlarm(id, context);
        Calendar tmpCal = Calendar.getInstance();
        Calendar calSet = Calendar.getInstance();

        calSet.set(Calendar.HOUR_OF_DAY, remindHour);
        calSet.set(Calendar.MINUTE, remindMinute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        calSet.set(Calendar.YEAR, year);
        calSet.set(Calendar.MONTH, month-1);
        calSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if(tmpCal.before(calSet)){
            intents.put(id, new Intent(context, AlarmReceiver.class));
            intents.get(id).putExtra("eventName", eventName);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intents.get(id), 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
            alarmManagers.put(id, alarmManager);
        }
    }
}
