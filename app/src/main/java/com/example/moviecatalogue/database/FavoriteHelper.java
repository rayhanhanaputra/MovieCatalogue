package com.example.moviecatalogue.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.moviecatalogue.entity.Movie;
import com.example.moviecatalogue.entity.Tvshow;
import com.example.moviecatalogue.provider.FavoriteProvider;
import com.example.moviecatalogue.widget.ImageBannerWidget;

import java.util.ArrayList;

import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.ID;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.ORIGINAL_TITLE;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.PHOTO_LINK;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.RATING;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.RELEASE_DATE;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.TITLE;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteTvColumns.ID_TV;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteTvColumns.ORIGINAL_TITLE_TV;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteTvColumns.OVERVIEW_TV;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteTvColumns.PHOTO_LINK_TV;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteTvColumns.RATING_TV;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteTvColumns.RELEASE_DATE_TV;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteTvColumns.TITLE_TV;
import static com.example.moviecatalogue.database.DatabaseContract.TABLE_FAVORITE;
import static com.example.moviecatalogue.database.DatabaseContract.TABLE_FAVORITE_TV;
import static com.example.moviecatalogue.provider.FavoriteProvider.CONTENT_URI;

public class FavoriteHelper {

    public static final String DATABASE_TABLE = TABLE_FAVORITE;
    public static final String DATABASE_TABLE_TV = TABLE_FAVORITE_TV;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTANCE;
    private Context mContext;

    public static SQLiteDatabase database;

    public FavoriteHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
        mContext = context;
    }

    public static FavoriteHelper getInstance(Context context){
        if(INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if(INSTANCE == null){
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();

        if(database.isOpen()){
            database.close();
        }
    }

    public ArrayList<Movie> getAllFavorites(){
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie favorite;
        if(cursor.getCount() > 0){
            do {
                favorite = new Movie();
                favorite.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favorite.setOriginalTitle(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_TITLE)));
                favorite.setRating(cursor.getString(cursor.getColumnIndexOrThrow(RATING)));
                favorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                favorite.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                favorite.setPhotoLink(cursor.getString(cursor.getColumnIndexOrThrow(PHOTO_LINK)));
                arrayList.add(favorite);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<Tvshow> getAllFavoritesTv(){
        ArrayList<Tvshow> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE_TV, null,
                null,
                null,
                null,
                null,
                ID_TV + " ASC",
                null);
        cursor.moveToFirst();
        Tvshow favorite;
        if(cursor.getCount() > 0){
            do {
                favorite = new Tvshow();
                favorite.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_TV)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE_TV)));
                favorite.setOriginalTitle(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_TITLE_TV)));
                favorite.setRating(cursor.getString(cursor.getColumnIndexOrThrow(RATING_TV)));
                favorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE_TV)));
                favorite.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW_TV)));
                favorite.setPhotoLink(cursor.getString(cursor.getColumnIndexOrThrow(PHOTO_LINK_TV)));
                arrayList.add(favorite);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public boolean checkIfMovieExists(int id){
        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + ID + " = " + id;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIfTvExists(int id){
        String query = "SELECT * FROM " + DATABASE_TABLE_TV + " WHERE " + ID_TV + " = " + id;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public long insertFavorite(Movie favorite){
        ContentValues args = new ContentValues();
        args.put(ID, favorite.getId());
        args.put(TITLE, favorite.getTitle());
        args.put(ORIGINAL_TITLE, favorite.getOriginalTitle());
        args.put(RATING, favorite.getRating());
        args.put(RELEASE_DATE, favorite.getReleaseDate());
        args.put(OVERVIEW, favorite.getOverview());
        args.put(PHOTO_LINK, favorite.getPhotoLink());

        if(checkIfMovieExists(favorite.getId())){
            return database.update(DATABASE_TABLE, args, "id = " + favorite.getId(), null);
        } else{
            return database.insert(DATABASE_TABLE, null, args);
        }

    }

    public long insertFavoriteTv(Tvshow favorite){
        ContentValues args = new ContentValues();
        args.put(ID_TV, favorite.getId());
        args.put(TITLE_TV, favorite.getTitle());
        args.put(ORIGINAL_TITLE_TV, favorite.getOriginalTitle());
        args.put(RATING_TV, favorite.getRating());
        args.put(RELEASE_DATE_TV, favorite.getReleaseDate());
        args.put(OVERVIEW_TV, favorite.getOverview());
        args.put(PHOTO_LINK_TV, favorite.getPhotoLink());

        if(checkIfTvExists(favorite.getId())){
            return database.update(DATABASE_TABLE_TV, args, "id = " + favorite.getId(), null);
        } else{
            return database.insert(DATABASE_TABLE_TV, null, args);
        }
    }

    public int deleteFavorite(int id){
        return database.delete(TABLE_FAVORITE, ID + " = '" + id + "'", null);
    }

    public int deleteFavoriteTv(int id){
        return database.delete(TABLE_FAVORITE_TV, ID_TV + " = '" + id + "'", null);
    }

    public static void updateWidget(Context context){
        Intent intent = new Intent(context, ImageBannerWidget.class);
        intent.setAction(ImageBannerWidget.UPDATE_WIDGET);
        context.sendBroadcast(intent);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryByIdProviderTv(String id) {
        return database.query(DATABASE_TABLE_TV, null
                , ID_TV + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , ID + " ASC");
    }

    public Cursor queryProviderTv() {
        return database.query(DATABASE_TABLE_TV
                , null
                , null
                , null
                , null
                , null
                , ID_TV + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public long insertProviderTv(ContentValues values) {
        return database.insert(DATABASE_TABLE_TV, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, ID + " = ?", new String[]{id});
    }

    public int updateProviderTv(String id, ContentValues values) {
        return database.update(DATABASE_TABLE_TV, values, ID_TV + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, ID + " = ?", new String[]{id});
    }

    public int deleteProviderTv(String id) {
        return database.delete(DATABASE_TABLE_TV, ID_TV + " = ?", new String[]{id});
    }
}
