package com.example.moviecatalogue.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.activity.DetailActivity;
import com.example.moviecatalogue.activity.MainActivity;
import com.example.moviecatalogue.entity.Movie;
import com.example.moviecatalogue.network.ApiInterface;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.ALARM_SERVICE;
import static com.example.moviecatalogue.activity.DetailActivity.EXTRA_DETAIL;

public class NotifyService extends BroadcastReceiver {
    public static final String TAG = NotifyService.class.getSimpleName();
    public static final String API_KEY = "0a1b5c5193c99d0728bc8b62a85ac8b7";
    public static final int DAILY_REQUEST_CODE = 100;
    public static final int LATEST_REQUEST_CODE = 100;
    public static final String EXTRA_TYPE = "extra_type";
    private ArrayList<Movie> listMovie = new ArrayList<>();
    private String url = "https://api.themoviedb.org/3/discover/movie?api_key="+API_KEY+"&language="+ MainActivity.languageId;

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotificationDaily(context);
    }

    public void showNotificationDaily(Context context){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 100, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "100")
                .setContentIntent(pIntent)
                .setContentTitle(context.getString(R.string.good_morning_string))
                .setContentText(context.getString(R.string.check_app_string))
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setSound(sound)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if(notificationManager != null){
            notificationManager.notify(100, notification.build());
        }
    }
}
