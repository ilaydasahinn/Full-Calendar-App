package com.ilayda.fullcalendarapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "AddEventActivity";
    DatabaseHelper mDatabaseHelper;
    private EditText eventName;
    private EditText eventDetail;
    private EditText startTime;
    private EditText finishTime;
    private EditText remindTime;
    private RadioGroup repeat;
    private EditText address;
    private Button saveButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    TimePickerDialog picker;
    int Remindhour;
    int Remindminutes;
    int Finishhour;
    int Finishminutes;
    int Starthour;
    int Startminutes;
    final static int REQUEST_CODE = 1;
    private boolean darkmode;
    private int default_remind_hour;
    private int default_remind_minute;
    private int default_repeat;


    Geocoder geocoder;
    List<Address> addresses;
    Double latitude;
    Double longitude;
    String addressText;
    String area;
    String city;
    String country;
    String postalCode;
    String fullAddress;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private Button btnGetAddress;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_layout);

        eventName = (EditText) findViewById(R.id.et_nameEvent);
        eventDetail = (EditText) findViewById(R.id.et_detailEvent);
        startTime = (EditText) findViewById(R.id.et_startTime);
        finishTime = (EditText) findViewById(R.id.et_finishTime);
        remindTime = (EditText) findViewById(R.id.et_remindTime);
        repeat = (RadioGroup) findViewById(R.id.rgroup_repeatTime);
        address = (EditText) findViewById(R.id.et_address);
        saveButton = (Button) findViewById(R.id.btn_SaveEvent);
        mDatabaseHelper = new DatabaseHelper(this);
        btnGetAddress = (Button)findViewById(R.id.btnAddress);




        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        btnGetAddress.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        darkmode = preferences.getBoolean("switchKey", false);
        default_remind_hour = preferences.getInt("rt_hourKey", 0);
        default_remind_minute = preferences.getInt("rt_minuteKey", 0);
        default_repeat = preferences.getInt("SAVED_RADIO_BUTTON_INDEX", 3);


        ConstraintLayout dm = findViewById(R.id.add_event_layout);
        if(darkmode){
            dm.setBackground(getResources().getDrawable(R.color.darkGrey));
        }

        Intent incomingIntent = getIntent();
        final int year = incomingIntent.getIntExtra("year", 0);
        final int month = incomingIntent.getIntExtra("month", 0);
        final int day_of_month = incomingIntent.getIntExtra("dayOfMonth", 0);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                Starthour = cldr.get(Calendar.HOUR_OF_DAY);
                Startminutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(AddEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                startTime.setText(sHour + ":" + sMinute);
                                Starthour = sHour;
                                Startminutes = sMinute;
                            }
                        }, Starthour, Startminutes, true);
                picker.show();
            }
        });

        finishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                Finishhour = cldr.get(Calendar.HOUR_OF_DAY);
                Finishminutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(AddEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                finishTime.setText(sHour + ":" + sMinute);
                                Finishhour = sHour;
                                Finishminutes = sMinute;
                            }
                        }, Finishhour, Finishminutes, true);
                picker.show();
            }
        });

        remindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                Remindhour = cldr.get(Calendar.HOUR_OF_DAY);
                Remindminutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(AddEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                remindTime.setText(sHour + ":" + sMinute);
                                Remindhour = sHour;
                                Remindminutes = sMinute;
                            }
                        }, Remindhour, Remindminutes, true);
                picker.show();
            }
        });



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //InsertDatabase(v);
                Log.e(TAG, "onClick: " + "Save Butonu" );
                if(eventName.length() != 0){


                    if(remindTime.length() == 0){
                        Remindhour = default_remind_hour;
                        Remindminutes = default_remind_minute;
                    }
                    if (repeat.getCheckedRadioButtonId() == -1) {
                        ((RadioButton) repeat.getChildAt(default_repeat)).setChecked(true);
                    }
                    addData(eventName.getText().toString(), year, month, day_of_month, eventDetail.getText().toString(), Starthour, Startminutes,
                            Finishhour, Finishminutes, Remindhour, Remindminutes,  repeat.indexOfChild(findViewById(repeat.getCheckedRadioButtonId())), address.getText().toString());
                    Intent intent = new Intent(AddEventActivity.this, DateActivity.class);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month-1);
                    intent.putExtra("dayOfMonth", day_of_month);
                    startActivity(intent);
                }else {
                    toastMessage("You should enter event name!");
                }
            }


        });
    }

    public void addData(String event_name, int year, int month, int day_of_month, String detail, int start_time_hour, int start_time_minute,
                        int finish_time_hour, int finish_time_minute, int remind_time_hour, int remind_time_minute, int repeat, String address){

        int i = 0;
        Calendar calSet = Calendar.getInstance();
        calSet.set(Calendar.HOUR_OF_DAY, remind_time_hour);
        calSet.set(Calendar.MINUTE, remind_time_minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        calSet.set(Calendar.YEAR, year);
        calSet.set(Calendar.MONTH, month-1);
        calSet.set(Calendar.DAY_OF_MONTH, day_of_month);

        if(repeat == 0){
            for(i=0; i< 50; i++){ // 2 yil boyunca her gune eklemek cok uzun surdu.
                calSet.add(Calendar.DATE, 1);
                int dayOfMonths = calSet.get(Calendar.DAY_OF_MONTH);
                int months = calSet.get(Calendar.MONTH)+1;
                int years = calSet.get(Calendar.YEAR);
                long insertData = mDatabaseHelper.addData(event_name, years, months, dayOfMonths, detail, start_time_hour, start_time_minute,
                        finish_time_hour, finish_time_minute,  remind_time_hour, remind_time_minute, repeat, address);

                if(insertData != -1 ){
                    AlarmManagerr.setAlarm(event_name, remind_time_hour,remind_time_minute,dayOfMonths,months,years,getApplicationContext(), (int)insertData);
                }
            }
        }else if(repeat == 1){ // 2 yil boyunca her haftaya eklemek cok uzun surdu.
            for(i=0; i< 7; i++) {
                calSet.add(Calendar.DATE, 7);
                int dayOfMonths = calSet.get(Calendar.DAY_OF_MONTH);
                int months = calSet.get(Calendar.MONTH)+1;
                int years = calSet.get(Calendar.YEAR);
                long insertData = mDatabaseHelper.addData(event_name, years, months, dayOfMonths, detail, start_time_hour, start_time_minute,
                        finish_time_hour, finish_time_minute,  remind_time_hour, remind_time_minute, repeat, address);

                if(insertData != -1 ){
                    AlarmManagerr.setAlarm(event_name, remind_time_hour,remind_time_minute,dayOfMonths,months,years,getApplicationContext(), (int)insertData);
                }
            }
        }else if(repeat == 2){
            for(i=0; i< 2*12; i++) {
                calSet.add(Calendar.MONTH, 1);
                int dayOfMonths = calSet.get(Calendar.DAY_OF_MONTH);
                int months = calSet.get(Calendar.MONTH)+1;
                int years = calSet.get(Calendar.YEAR);
                long insertData = mDatabaseHelper.addData(event_name, years, months, dayOfMonths, detail, start_time_hour, start_time_minute,
                        finish_time_hour, finish_time_minute,  remind_time_hour, remind_time_minute, repeat, address);

                if(insertData != -1 ){
                    AlarmManagerr.setAlarm(event_name, remind_time_hour,remind_time_minute,dayOfMonths,months,years,getApplicationContext(), (int)insertData);
                }
            }
        }else{
            for(i=0; i< 2*1; i++) {
                calSet.add(Calendar.YEAR, 1);
                int dayOfMonths = calSet.get(Calendar.DAY_OF_MONTH);
                int months = calSet.get(Calendar.MONTH)+1;
                int years = calSet.get(Calendar.YEAR);
                long insertData = mDatabaseHelper.addData(event_name, years, months, dayOfMonths, detail, start_time_hour, start_time_minute,
                        finish_time_hour, finish_time_minute,  remind_time_hour, remind_time_minute, repeat, address);


                if(insertData != -1 ){
                    AlarmManagerr.setAlarm(event_name, remind_time_hour,remind_time_minute,dayOfMonths,months,years,getApplicationContext(), (int)insertData);
                }

            }
        }

        long insertData = mDatabaseHelper.addData(event_name, year, month, day_of_month, detail, start_time_hour, start_time_minute,
                finish_time_hour, finish_time_minute,  remind_time_hour, remind_time_minute, repeat, address);

        if(insertData != -1 ){
            AlarmManagerr.setAlarm(event_name, remind_time_hour,remind_time_minute,day_of_month,month,year,getApplicationContext(), (int)insertData);
            toastMessage("Data Successfully Inserted!");
        }else{
            toastMessage("Something went wrong! ");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            geocoder = new Geocoder(this, Locale.getDefault());
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            getLocation();
        }


    }

    private void getLocation(){
        if(ActivityCompat.checkSelfPermission(AddEventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (AddEventActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddEventActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.e(TAG, "getLocation: " + location );

            if(location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    addressText = addresses.get(0).getAddressLine(0);
                    area = addresses.get(0).getLocality();
                    city = addresses.get(0).getAdminArea();
                    country = addresses.get(0).getCountryName();
                    postalCode = addresses.get(0).getPostalCode();
                    fullAddress = addressText + "," + area + "," + city + "," + country + "," + postalCode;
                    Log.e(TAG, "onCreate: " + fullAddress );
                    address.setText(fullAddress);

                } catch (IOException e) {
                    Log.e(TAG, "onCreate: " + "dfsg" );
                    e.printStackTrace();
                }
            }else{
                toastMessage("Unable to trace your location");
            }
        }
    }

    protected void buildAlertMessageNoGps(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn On Your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
