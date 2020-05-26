package com.ilayda.fullcalendarapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";
    private TextView tv_default_mode;
    private TextView tv_default_remind_time;
    private EditText default_remind_time;
    private Switch dark_mode;
    private Button save_settings;
    private RadioGroup default_time_radio;
    TimePickerDialog picker;
    SharedPreferences sharedPreferences;
    static final String myPreference = "my_pref";
    static final String switchh = "switchKey";
    static final String default_time_hour = "defaultTimeHourKey";
    static final String default_time_minute = "defaultTimeMinuteKey";
    static final String KEY_SAVED_RADIO_BUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    static final String remind_time_hour_key = "rt_hourKey";
    static final String remind_time_minute_key = "rt_minuteKey";

    private boolean darkmode;


    int default_remind_time_hour;
    int default_remind_time_minute;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        default_remind_time = (EditText)findViewById(R.id.et_default_remind_time);
        dark_mode = (Switch)findViewById(R.id.switch_dark_mode);
        save_settings = (Button)findViewById(R.id.btn_save_settings);
        default_time_radio = (RadioGroup)findViewById(R.id.default_time_radio);
        tv_default_mode = (TextView)findViewById(R.id.default_mode);
        tv_default_remind_time = (TextView)findViewById(R.id.default_remind_time);

        final SharedPreferences sharedPreferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);


        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        darkmode = preferences.getBoolean("switchKey", false);

        ConstraintLayout dm = findViewById(R.id.settings_layout);


        if(darkmode){
            dm.setBackground(getResources().getDrawable(R.color.darkGrey));
            tv_default_mode.setTextColor(getResources().getColor(R.color.white));
        }

        if(sharedPreferences.contains(remind_time_hour_key) && sharedPreferences.contains(remind_time_minute_key)){
           int a = sharedPreferences.getInt(remind_time_hour_key, 00);
           int b = sharedPreferences.getInt(remind_time_minute_key, 00);

           String a_string;
           String b_string;

           if(a < 10){
                a_string = "0" + String.valueOf(a);
           }else{
               a_string = String.valueOf(a);
           }
            if(b < 10){
                b_string = "0" + String.valueOf(b);
            }else{
                b_string = String.valueOf(b);
            }
           default_remind_time.setText(a_string + ":" + b_string);
        }else{
            default_remind_time.setText("00" + ":" + "00");
        }

        if(sharedPreferences.contains(KEY_SAVED_RADIO_BUTTON_INDEX)){
            int savedRadioIndex = sharedPreferences.getInt(KEY_SAVED_RADIO_BUTTON_INDEX, 0);
            ((RadioButton)default_time_radio.getChildAt(savedRadioIndex)).setChecked(true);
        }else{
            ((RadioButton)default_time_radio.getChildAt(3)).setChecked(true);
        }


        if(sharedPreferences.contains(switchh)){
            boolean switchBool = sharedPreferences.getBoolean(switchh, false);
            dark_mode.setChecked(switchBool);
        }

        default_remind_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                default_remind_time_hour = cldr.get(Calendar.HOUR_OF_DAY);
                default_remind_time_minute = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SettingsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                default_remind_time.setText(sHour + ":" + sMinute);
                            }
                        }, default_remind_time_hour, default_remind_time_minute, true);
                picker.show();
            }
        });


        save_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SharedPreferences.Editor editor = sharedPreferences.edit();
                //int default_time_hour = default_remind_time_hour;
                //int default_time_minute = default_remind_time_minute;

                String total_time = default_remind_time.getText().toString();

                String[] parts = total_time.split(":");
                String part1 = parts[0];
                String part2 = parts[1];

                int default_time_hour = Integer.parseInt(part1);
                int default_time_minute = Integer.parseInt(part2);


                if(dark_mode.isChecked()){
                    editor.putBoolean(switchh, true);

                }else{
                    editor.putBoolean(switchh, false);
                }

                int selectedRadioButtonId = default_time_radio.getCheckedRadioButtonId();
                View radioButton = default_time_radio.findViewById(selectedRadioButtonId);
                int idx = default_time_radio.indexOfChild(radioButton);

                editor.putInt(remind_time_hour_key, default_time_hour);
                editor.putInt(remind_time_minute_key, default_time_minute);
                editor.putInt(KEY_SAVED_RADIO_BUTTON_INDEX, idx);
                editor.commit();

                Intent intent = new Intent(SettingsActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }



}