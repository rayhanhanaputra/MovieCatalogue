package com.example.moviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.os.Handler;

import com.example.moviecatalogue.activity.MainActivity;
import com.example.moviecatalogue.database.DatabaseContract;
import com.example.moviecatalogue.database.DatabaseHelper;
import com.example.moviecatalogue.database.FavoriteHelper;

import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.ID;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteTvColumns.ID_TV;
import static com.example.moviecatalogue.database.DatabaseContract.TABLE_FAVORITE;
import static com.example.moviecatalogue.database.DatabaseContract.TABLE_FAVORITE_TV;

public class FavoriteProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.moviecatalogue";

    public static final Uri CONTENT_URI = DatabaseContract.FavoriteColumns.CONTENT_URI;
    public static final Uri CONTENT_URI_TV = DatabaseContract.FavoriteTvColumns.CONTENT_URI_TV;

    private static final int mMovie = 1;
    private static final int mTvshow = 2;
    private static final UriMatcher uriMatcher;

    private FavoriteHelper favoriteHelper;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY , TABLE_FAVORITE, mMovie);
        uriMatcher.addURI(AUTHORITY , TABLE_FAVORITE_TV , mTvshow);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        favoriteHelper.open();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case mMovie:
                cursor = favoriteHelper.database.query(TABLE_FAVORITE, null,
                        null,
                        null,
                        null,
                        null,
                        ID + " ASC",
                        null);
                break;
            case mTvshow:
                cursor = favoriteHelper.database.query(TABLE_FAVORITE_TV, null,
                        null,
                        null,
                        null,
                        null,
                        ID_TV + " ASC",
                        null);
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        favoriteHelper.open();
        Uri uri_ = null;

        switch (uriMatcher.match(uri)) {
            case mMovie:
                long rowId = favoriteHelper.database.insert(TABLE_FAVORITE, null, contentValues);
                if(rowId > 0){
                    uri_ = ContentUris.withAppendedId(CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(uri_, null);
                }
                break;
            case mTvshow:
                long rowId_ = favoriteHelper.database.insert(TABLE_FAVORITE_TV, null, contentValues);
                if(rowId_ > 0){
                    uri_ = ContentUris.withAppendedId(CONTENT_URI_TV, rowId_);
                    getContext().getContentResolver().notifyChange(uri_, null);
                }
                break;
            default:
                try{
                    throw new SQLException("Failed to insert : " + uri);
                }catch (SQLException e){
                    e.printStackTrace();
                }
        }
        return uri_;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        favoriteHelper.open();
        int deleted = 0;
        switch (uriMatcher.match(uri)) {
            case mMovie:
                deleted = favoriteHelper.database.delete(TABLE_FAVORITE, s, strings);
                break;
            case mTvshow:
                deleted = favoriteHelper.database.delete(TABLE_FAVORITE_TV, s, strings);
                break;
        }
        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        favoriteHelper.open();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case mMovie:
                count = favoriteHelper.database.update(TABLE_FAVORITE, contentValues, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case mTvshow:
                count = favoriteHelper.database.update(TABLE_FAVORITE_TV, contentValues, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
        }
        return count;
    }
}
