package com.example.moviecatalogue.activity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Bundle;

import com.example.moviecatalogue.entity.Movie;
import com.example.moviecatalogue.service.NotifyService;
import com.example.moviecatalogue.fragment.FavoriteFragment;
import com.example.moviecatalogue.fragment.MovieFragment;
import com.example.moviecatalogue.R;
import com.example.moviecatalogue.fragment.TvshowFragment;
import com.example.moviecatalogue.service.ReleaseReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.moviecatalogue.provider.FavoriteProvider.CONTENT_URI;
import static com.example.moviecatalogue.service.NotifyService.DAILY_REQUEST_CODE;
import static com.example.moviecatalogue.service.NotifyService.EXTRA_TYPE;
import static com.example.moviecatalogue.service.NotifyService.LATEST_REQUEST_CODE;


public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    public static String languageId;
    private NotifyService notifyService = new NotifyService();
    public static ArrayList<Movie> listMovie;

    private static HandlerThread handlerThread;
    private DataObserver dataObserver;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    fragment = new MovieFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_tvshow:
                    fragment = new TvshowFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_favorite:
                    fragment = new FavoriteFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_change_language){
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);

        }
        if(item.getItemId() == R.id.action_notification_settings){
            Intent intent2 = new Intent(this, SettingsActivity.class);
            startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String Language = Locale.getDefault().getLanguage();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                    ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
            getSupportActionBar().setIcon(R.drawable.ic_local_movies_white_24dp);
        }

        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        dataObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        int a = preferences.getInt("launches", 1);

        if(a < 2){
            setAlarmRelease(this);
            setAlarmDaily(this);
            a++;
            editor.putInt("launches", a);
            editor.apply();
        }

        switch(Language){
            case "in":
                languageId = "id-ID";
                break;
            default:
                languageId = "en-US";
                break;
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState == null){
            navView.setSelectedItemId(R.id.navigation_movie);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    }

    public static void setAlarmDaily(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);

        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(EXTRA_TYPE, DAILY_REQUEST_CODE);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100,intent, 0);

        long dateTime = calendar.getTimeInMillis();
        if(dateTime <= System.currentTimeMillis()){
            dateTime = dateTime + 24 * 3600 *1000;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, dateTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }else{
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, dateTime, AlarmManager.INTERVAL_DAY,pendingIntent);
        }

    }

    public static void setAlarmRelease(Context context){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());
        calendar1.set(Calendar.HOUR_OF_DAY, 8);
        calendar1.set(Calendar.MINUTE, 0);

        Intent intent = new Intent(context, ReleaseReceiver.class);
        intent.putExtra(EXTRA_TYPE, LATEST_REQUEST_CODE);
        AlarmManager alarmManager1 = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,101,intent, 0);

        long dateTime = calendar1.getTimeInMillis();
        if(dateTime <= System.currentTimeMillis()){
            dateTime = dateTime + 24 * 3600 *1000;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmManager1.setWindow(AlarmManager.RTC_WAKEUP, dateTime, AlarmManager.INTERVAL_DAY, pendingIntent1);
        }else{
            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, dateTime, AlarmManager.INTERVAL_DAY,pendingIntent1);
        }

    }

    public static void cancelAlarm(Context context, String type){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = null;
        int request = 0;

        if(type.equalsIgnoreCase("daily")){
            request = 100;
            intent = new Intent(context, NotifyService.class);
        }
        if(type.equalsIgnoreCase("latest")){
            request = 101;
            intent = new Intent(context, ReleaseReceiver.class);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, request, intent, 0);

        if(alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }

}
