package com.example.moviecatalogue.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviecatalogue.activity.MainActivity;
import com.example.moviecatalogue.entity.Tvshow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TvshowSearchViewModel extends ViewModel {
    private static final String API_KEY = "0a1b5c5193c99d0728bc8b62a85ac8b7";
    private MutableLiveData<ArrayList<Tvshow>> listTvshows = new MutableLiveData<>();
    private String tvshowName;

    public void setTvshowName(String name){
        this.tvshowName = name;
    }

    public void setListTvshowsToDisplay(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Tvshow> listTvshow = new ArrayList<>();
        String url = " https://api.themoviedb.org/3/search/tv?api_key="+API_KEY+"&language="+ MainActivity.languageId+"&query="+tvshowName;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");

                    for(int i = 0; i < results.length(); i++){
                        JSONObject listTvshows = results.getJSONObject(i);
                        Tvshow tvshows = new Tvshow();
                        tvshows.setTitle(listTvshows.getString("name"));
                        tvshows.setOverview(listTvshows.getString("overview"));
                        tvshows.setPhotoLink(listTvshows.getString("poster_path"));
                        tvshows.setId(listTvshows.getInt("id"));
                        listTvshow.add(tvshows);
                    }
                    listTvshows.postValue(listTvshow);
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

    public LiveData<ArrayList<Tvshow>> getTvShows(){
        return listTvshows;
    }
}
