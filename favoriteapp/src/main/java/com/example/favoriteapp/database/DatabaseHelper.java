package com.example.favoriteapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "favorite.db";

    private static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATE_TABLE_FAVORITE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
                DatabaseContract.TABLE_FAVORITE,
            DatabaseContract.FavoriteColumns.ID,
                DatabaseContract.FavoriteColumns.TITLE,
                DatabaseContract.FavoriteColumns.ORIGINAL_TITLE,
                DatabaseContract.FavoriteColumns.RATING,
                DatabaseContract.FavoriteColumns.RELEASE_DATE,
                DatabaseContract.FavoriteColumns.OVERVIEW,
                DatabaseContract.FavoriteColumns.PHOTO_LINK
    );

    private static final String SQL_CREATE_TABLE_FAVORITE_TV = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_FAVORITE_TV,
            DatabaseContract.FavoriteTvColumns.ID_TV,
            DatabaseContract.FavoriteTvColumns.TITLE_TV,
            DatabaseContract.FavoriteTvColumns.ORIGINAL_TITLE_TV,
            DatabaseContract.FavoriteTvColumns.RATING_TV,
            DatabaseContract.FavoriteTvColumns.RELEASE_DATE_TV,
            DatabaseContract.FavoriteTvColumns.OVERVIEW_TV,
            DatabaseContract.FavoriteTvColumns.PHOTO_LINK_TV
    );

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FAVORITE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FAVORITE_TV);
        onCreate(sqLiteDatabase);
    }
}
