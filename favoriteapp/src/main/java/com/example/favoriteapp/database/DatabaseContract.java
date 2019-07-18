package com.example.favoriteapp.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    private static final String AUTHORITY = "com.example.moviecatalogue";
    private static final String SCHEME = "content";

    public static String TABLE_FAVORITE = "favorite_movie";

    public static final class FavoriteColumns implements BaseColumns{

        public static String ID = "id_movie";
        public static String TITLE = "title_movie";
        public static String ORIGINAL_TITLE = "original_title_movie";
        public static String RATING = "rating_movie";
        public static String RELEASE_DATE = "release_date_movie";
        public static String OVERVIEW = "overview_movie";
        public static String PHOTO_LINK = "photo_link_movie";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_FAVORITE);
    }

    public static String TABLE_FAVORITE_TV = "favorite_tvshow";

    public static final class FavoriteTvColumns implements BaseColumns {

        public static String ID_TV = "id_tv";
        public static String TITLE_TV = "title_tv";
        public static String ORIGINAL_TITLE_TV = "original_title_tv";
        public static String RATING_TV = "rating_tv";
        public static String RELEASE_DATE_TV = "release_date_tv";
        public static String OVERVIEW_TV = "overview_tv";
        public static String PHOTO_LINK_TV = "photo_link_tv";
        public static final Uri CONTENT_URI_TV = Uri.parse("content://" + AUTHORITY + "/" + TABLE_FAVORITE_TV);
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

}
