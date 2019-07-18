package com.example.moviecatalogue.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.activity.DetailActivity;
import com.example.moviecatalogue.activity.MainActivity;
import com.example.moviecatalogue.entity.Movie;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.moviecatalogue.activity.DetailActivity.EXTRA_DETAIL;

public class ReleaseReceiver extends BroadcastReceiver {
    public static final String TAG = ReleaseReceiver.class.getSimpleName();
    public static final int DAILY_REQUEST_CODE = 100;
    public int LATEST_REQUEST_CODE = 101;
    private ArrayList<Movie> listMovie = new ArrayList<>();
        private String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=0a1b5c5193c99d0728bc8b62a85ac8b7&language="+MainActivity.languageId+"&page=1";

    @Override
    public void onReceive(final Context context, Intent intent) {
        final PendingResult pendingResult = goAsync();
        Thread thread = new Thread(){
            public void run(){
                SyncHttpClient client = new SyncHttpClient();
                client.get(url, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try{
                            ArrayList<Movie> listMov = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                int id = jsonArray.getJSONObject(i).getInt("id");
                                String title = jsonArray.getJSONObject(i).getString("title");
                                String releaseDate = jsonArray.getJSONObject(i).getString("release_date");

                                Movie mov = new Movie();
                                mov.setId(id);
                                mov.setTitle(title);
                                mov.setReleaseDate(releaseDate);
                                listMov.add(mov);
                            }
                            listMovie = listMov;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d("ReleaseReceiver", "Fetch Upcoming JSON Succeed");
                        Log.d("ReleaseReceiver", "Total ListMovies = " + listMovie.size());

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.d("ReleaseReceiver", "Fetch Upcoming JSON Failed");
                    }
                });

                ArrayList<Movie> movies = listMovie;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = new Date();
                String now = dateFormat.format(date);
                for(Movie movieItem : movies){
                    if(movieItem.getReleaseDate().equals(now)){
                        Log.d("ReleaseReceiver","Release Today Found!");
                        showNotificationRelease(context, movieItem.getId(), movieItem.getTitle());
                    }
                }
                pendingResult.finish();
            }
        };
        thread.start();
    }

    public void showNotificationRelease(Context context, int id, String title){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManagerCompat notificationManager2 = NotificationManagerCompat.from(context);
        Intent i = new Intent(context, DetailActivity.class);
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setType("movie");
        i.putExtra(EXTRA_DETAIL,movie);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent2 = PendingIntent.getActivity(context, 101, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification2 = new NotificationCompat.Builder(context,"101")
                .setContentIntent(pIntent2)
                .setContentTitle(context.getString(R.string.release_now_string))
                .setContentText(title + " " + context.getString(R.string.has_release_string))
                .setSmallIcon(R.drawable.ic_live_tv_black_24dp)
                .setSound(sound)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        int notificationNumber = preferences.getInt("notificationNumber", 102);

        if(notificationManager2 != null){
            notificationManager2.notify(notificationNumber, notification2.build());
            notificationNumber++;
            editor.putInt("notificationNumber", notificationNumber);
            editor.apply();
        }
    }
}
