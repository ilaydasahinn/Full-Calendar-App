package com.ilayda.fullcalendarapp;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {
    public static final String TAG = "DateActivity";
    Context context;
    ArrayList<EventObject> data;
    private static LayoutInflater inflater = null;
    DatabaseHelper mDatabaseHelper;


    public EventAdapter(Context context, ArrayList<EventObject> data, DatabaseHelper db) {
        mDatabaseHelper = db;
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.display_event_row, null);
        TextView eventName = (TextView) vi.findViewById(R.id.row_event_name);
        final TextView startTime = (TextView) vi.findViewById(R.id.row_event_starttime);
        TextView finishTime = (TextView) vi.findViewById(R.id.row_event_finishtime);
        ImageButton delete = (ImageButton) vi.findViewById(R.id.row_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.delete(data.get(position).getID());
                Toast.makeText(context, "The event deleted!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, DateActivity.class);
                intent.putExtra("year", data.get(position).getYear());
                intent.putExtra("month", data.get(position).getMonth()-1);
                intent.putExtra("dayOfMonth", data.get(position).getDayOfMonth());
                AlarmManagerr.cancelAlarm(data.get(position).getID(), context);
                context.startActivity(intent);
            }
        });
        ImageButton edit = (ImageButton) vi.findViewById(R.id.row_edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("eventName", data.get(position).getEventName());
                intent.putExtra("detail", data.get(position).getDetail());
                intent.putExtra("startHour", data.get(position).getStartHour());
                intent.putExtra("startMinute", data.get(position).getStartMinute());
                intent.putExtra("finishHour", data.get(position).getFinishHour());
                intent.putExtra("finishMinute", data.get(position).getFinishMinute());
                intent.putExtra("remindHour", data.get(position).getRemindHour());
                intent.putExtra("remindMinute", data.get(position).getRemindMinute());
                intent.putExtra("repeat", data.get(position).getRepeat());
                intent.putExtra("address", data.get(position).getAddress());
                intent.putExtra("ID", data.get(position).getID());
                intent.putExtra("year", data.get(position).getYear());
                intent.putExtra("month", data.get(position).getMonth());
                intent.putExtra("dayOfMonth", data.get(position).getDayOfMonth());
                context.startActivity(intent);
            }
        });

        ImageButton share =(ImageButton) vi.findViewById(R.id.row_share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TEXT, "Event Name: " + data.get(position).getEventName() + "\n" +
                                                         "Event Date: "  + data.get(position).getDayOfMonth() + "/" + data.get(position).getMonth() + "/" + data.get(position).getYear() + "\n" +
                                                         "Event Start Time: " + data.get(position).getStartHour() + ":" + data.get(position).getStartMinute() + "\n" +
                                                         "Event Finish Time: " + data.get(position).getFinishHour() + ":" + data.get(position).getFinishMinute() + "\n" +
                                                         "Event Address: " + data.get(position).getAddress() + "\n" +
                                                         "Event Details: " + data.get(position).getDetail());
                context.startActivity(intent.createChooser(intent, "Share event"));
            }
        });


        eventName.setText(data.get(position).getEventName());
        startTime.setText(data.get(position).getStartTime());
        finishTime.setText(data.get(position).getFinishTime());
        return vi;
    }

}
