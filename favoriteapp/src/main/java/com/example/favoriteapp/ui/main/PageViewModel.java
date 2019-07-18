package com.example.favoriteapp.ui.main;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.favoriteapp.entity.Movie;

import java.util.ArrayList;

public class PageViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();
    private Context mContext;

    public void setListMoviesToDisplay(){
        Uri uri = Uri.parse("content://com.example.moviecatalogue/favorite_movie");
        ContentProviderClient myCr = mContext.getContentResolver().acquireContentProviderClient(uri);

        try{
            ArrayList<Movie> arrayList = new ArrayList<>();
            Cursor cursor = myCr.query(uri, null, null, null, null);
            cursor.moveToFirst();

            Movie favorite;
            if(cursor.getCount() > 0){
                do {
                    favorite = new Movie();
                    favorite.setId(Integer.valueOf(cursor.getString(0)));
                    favorite.setTitle(cursor.getString(1));
                    favorite.setOriginalTitle(cursor.getString(2));
                    favorite.setRating(cursor.getString(3));
                    favorite.setReleaseDate(cursor.getString(4));
                    favorite.setOverview(cursor.getString(5));
                    favorite.setPhotoLink(cursor.getString(6));
                    arrayList.add(favorite);
                    cursor.moveToNext();

                } while (!cursor.isAfterLast());
            }
            cursor.close();
            listMovies.postValue(arrayList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public LiveData<ArrayList<Movie>> getMovies(){
        return listMovies;
    }

    public void getContext(Context context){
        this.mContext = context;
    }
}