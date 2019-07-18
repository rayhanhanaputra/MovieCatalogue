package com.example.moviecatalogue.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.service.NotifyService;

import static com.example.moviecatalogue.activity.MainActivity.cancelAlarm;
import static com.example.moviecatalogue.activity.MainActivity.setAlarmDaily;
import static com.example.moviecatalogue.activity.MainActivity.setAlarmRelease;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle(R.string.settings_menu);
        Switch dailySwitch = findViewById(R.id.switch_daily);
        Switch latestSwitch = findViewById(R.id.switch_latest);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean dailyCondition = preferences.getBoolean("dailyCondition", true);
        boolean latestCondition = preferences.getBoolean("latestCondition", true);

        dailySwitch.setOnCheckedChangeListener(null);
        if(dailyCondition){
            dailySwitch.setChecked(true);
        }else{
            dailySwitch.setChecked(false);
        }

        latestSwitch.setOnCheckedChangeListener(null);
        if(latestCondition){
            latestSwitch.setChecked(true);
        }else{
            latestSwitch.setChecked(false);
        }

        dailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    setAlarmDaily(getApplicationContext());
                    editor.putBoolean("dailyCondition", true);
                    editor.apply();

                }else{
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    cancelAlarm(getApplicationContext(), "daily");
                    editor.putBoolean("dailyCondition", false);
                    editor.apply();
                }
            }
        });

        latestSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    setAlarmRelease(getApplicationContext());
                    editor.putBoolean("latestCondition", true);
                    editor.apply();
                }else{
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    cancelAlarm(getApplicationContext(), "latest");
                    editor.putBoolean("latestCondition", false);
                    editor.apply();
                }
            }
        });
    }
}
