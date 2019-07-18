package com.example.moviecatalogue.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviecatalogue.activity.MainActivity;
import com.example.moviecatalogue.entity.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = "0a1b5c5193c99d0728bc8b62a85ac8b7";
    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

    public void setListMoviesToDisplay(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listMovie = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key="+API_KEY+"&language="+ MainActivity.languageId;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");

                    for(int i = 0; i < results.length(); i++){
                        JSONObject listMovies = results.getJSONObject(i);
                        Movie movies = new Movie();
                        movies.setTitle(listMovies.getString("title"));
                        movies.setOverview(listMovies.getString("overview"));
                        movies.setPhotoLink(listMovies.getString("poster_path"));
                        movies.setId(listMovies.getInt("id"));
                        listMovie.add(movies);
                    }
                    listMovies.postValue(listMovie);
                } catch (Exception e){
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<Movie>> getMovies(){
        return listMovies;
    }
}
