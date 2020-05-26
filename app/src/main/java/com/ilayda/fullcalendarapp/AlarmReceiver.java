package com.ilayda.fullcalendarapp;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alert is ringing!", Toast.LENGTH_LONG).show();

        String eventName = intent.getStringExtra("eventName");

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle(eventName)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        Intent i = new Intent(context, CalendarActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 999999, i, PendingIntent.FLAG_ONE_SHOT);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        builder.setLights(0xFFb71c1c, 1000, 2000);
        builder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        manager.notify(12345, builder.build());

        if (alarmUri == null)
        {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        ringtone.play();


        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(10000);
                    ringtone.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        t.start();

    }
}
