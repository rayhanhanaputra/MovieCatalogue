package com.example.favoriteapp.ui.main;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.favoriteapp.entity.Tvshow;

import java.util.ArrayList;

public class TvFavViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Tvshow>> listTvshow = new MutableLiveData<>();
    private Context mContext;

    public void setListTvshowToDisplay(){
        Uri uri = Uri.parse("content://com.example.moviecatalogue/favorite_tvshow");
        ContentProviderClient myCr = mContext.getContentResolver().acquireContentProviderClient(uri);

        try{
            ArrayList<Tvshow> arrayList = new ArrayList<>();
            Cursor cursor = myCr.query(uri, null, null, null, null);
            cursor.moveToFirst();

            Tvshow favorite;
            if(cursor.getCount() > 0){
                do {
                    favorite = new Tvshow();
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
            listTvshow.postValue(arrayList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public LiveData<ArrayList<Tvshow>> getTvshow(){
        return listTvshow;
    }

    public void getContext(Context context){
        this.mContext = context;
    }
}
