package com.ilayda.fullcalendarapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "EditActivity";
    DatabaseHelper mDatabaseHelper;

    private EditText et_eventName;
    private EditText et_eventDetail;
    private EditText et_startTime;
    private EditText et_finishTime;
    private EditText et_remindTime;
    private RadioGroup rg_repeat;
    private EditText et_address;
    private Button saveButton;

    TimePickerDialog picker;
    int Remindhour ;
    int Remindminutes ;
    int Finishhour ;
    int Finishminutes ;
    int Starthour ;
    int Startminutes ;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        et_eventName = (EditText) findViewById(R.id.et_nameEvent);
        et_eventDetail = (EditText) findViewById(R.id.et_detailEvent);
        et_startTime = (EditText) findViewById(R.id.et_startTime);
        et_finishTime = (EditText) findViewById(R.id.et_finishTime);
        et_remindTime = (EditText) findViewById(R.id.et_remindTime);
        rg_repeat = (RadioGroup) findViewById(R.id.rgroup_repeatTime);
        et_address = (EditText) findViewById(R.id.et_address);
        saveButton = (Button) findViewById(R.id.btn_SaveEvent);
        mDatabaseHelper = new DatabaseHelper(this);
        btnGetAddress = (Button)findViewById(R.id.btnAddress);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        btnGetAddress.setOnClickListener(this);


        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        darkmode = preferences.getBoolean("switchKey", false);
        ConstraintLayout dm = findViewById(R.id.edit_layout);
        if (darkmode) {
            dm.setBackground(getResources().getDrawable(R.color.darkGrey));
        }

        default_remind_hour = preferences.getInt("rt_hourKey", 0);
        default_remind_minute = preferences.getInt("rt_minuteKey", 0);
        default_repeat = preferences.getInt("SAVED_RADIO_BUTTON_INDEX", 0);


        Intent incomingIntent = getIntent();
        final String eventName = incomingIntent.getStringExtra("eventName");
        final String detail = incomingIntent.getStringExtra("detail");
        final int startHour = incomingIntent.getIntExtra("startHour", 0);
        final int startMinute = incomingIntent.getIntExtra("startMinute", 0);
        final int finishHour = incomingIntent.getIntExtra("finishHour", 0);
        final int finishMinute = incomingIntent.getIntExtra("finishMinute", 0);
        final int remindHour = incomingIntent.getIntExtra("remindHour", default_remind_hour);
        final int remindMinute = incomingIntent.getIntExtra("remindMinute", default_remind_minute);
        final int repeat = incomingIntent.getIntExtra("repeat", default_repeat);
        final String address = incomingIntent.getStringExtra("address");
        final int ID = incomingIntent.getIntExtra("ID", 0);

        Remindhour = remindHour ;
        Remindminutes = remindMinute;
        Finishhour = finishHour;
        Finishminutes = finishMinute;
        Starthour = startHour;
        Startminutes = startMinute;


        if (eventName.length() != 0) {
            et_eventName.setText(eventName);
        }
        if (detail.length() != 0) {
            et_eventDetail.setText(detail);
        }

        int a = startHour;
        int b = startMinute;
        String a_string;
        String b_string;

        if (a < 10) {
            a_string = "0" + String.valueOf(a);
        } else {
            a_string = String.valueOf(a);
        }
        if (b < 10) {
            b_string = "0" + String.valueOf(b);
        } else {
            b_string = String.valueOf(b);
        }
        et_startTime.setText(a_string + ":" + b_string);

        int c = finishHour;
        int d = finishMinute;
        String c_string;
        String d_string;

        if (c < 10) {
            c_string = "0" + String.valueOf(c);
        } else {
            c_string = String.valueOf(c);
        }
        if (d < 10) {
            d_string = "0" + String.valueOf(d);
        } else {
            d_string = String.valueOf(d);
        }
        et_finishTime.setText(c_string + ":" + d_string);

        int e = remindHour;
        int f = remindMinute;
        String e_string;
        String f_string;

        if (e < 10) {
            e_string = "0" + String.valueOf(e);
        } else {
            e_string = String.valueOf(e);
        }
        if (f < 10) {
            f_string = "0" + String.valueOf(f);
        } else {
            f_string = String.valueOf(f);
        }
        et_remindTime.setText(e_string + ":" + f_string);

        if (rg_repeat.getChildAt(repeat) != null) {
            ((RadioButton) rg_repeat.getChildAt(repeat)).setChecked(true);
        }
        if (address.length() != 0) {
            et_address.setText(address);
        }


        final int year = incomingIntent.getIntExtra("year", 0);
        final int month = incomingIntent.getIntExtra("month", 0);
        final int day_of_month = incomingIntent.getIntExtra("dayOfMonth", 0);


        et_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                Starthour = cldr.get(Calendar.HOUR_OF_DAY);
                Startminutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(EditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                et_startTime.setText(sHour + ":" + sMinute);
                                Starthour = sHour;
                                Startminutes = sMinute;
                            }
                        }, Starthour, Startminutes, true);
                picker.show();
            }
        });

        et_finishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                Finishhour = cldr.get(Calendar.HOUR_OF_DAY);
                Finishminutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(EditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                et_finishTime.setText(sHour + ":" + sMinute);
                                Finishhour = sHour;
                                Finishminutes = sMinute;
                            }
                        }, Finishhour, Finishminutes, true);
                picker.show();
            }
        });

        et_remindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                Remindhour = cldr.get(Calendar.HOUR_OF_DAY);
                Remindminutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(EditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                et_remindTime.setText(sHour + ":" + sMinute);
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


                if (eventName.length() != 0) {

                    updateData(et_eventName.getText().toString(), year, month, day_of_month, et_eventDetail.getText().toString(), Starthour, Startminutes,
                            Finishhour, Finishminutes, Remindhour, Remindminutes, rg_repeat.indexOfChild(findViewById(rg_repeat.getCheckedRadioButtonId())), et_address.getText().toString(), ID);
                    Intent intent = new Intent(EditActivity.this, DateActivity.class);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month-1);
                    intent.putExtra("dayOfMonth", day_of_month);
                    AlarmManagerr.updateAlarm(et_eventName.getText().toString(),Remindhour, Remindminutes, day_of_month,month,year, getApplicationContext(),ID);
                    startActivity(intent);
                } else {
                    toastMessage("You should enter event name!");
                }

            }


        });

    }

        public void updateData(String event_name, int year, int month, int day_of_month, String detail, int start_time_hour, int start_time_minute,
        int finish_time_hour, int finish_time_minute, int remind_time_hour, int remind_time_minute, int repeat, String address, int ID){

            boolean insertData = mDatabaseHelper.update(event_name, year, month, day_of_month, detail, start_time_hour, start_time_minute,
                    finish_time_hour, finish_time_minute,  remind_time_hour, remind_time_minute, repeat, address, ID);

            if(insertData){
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
        if(ActivityCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (EditActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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
                    et_address.setText(fullAddress);

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
