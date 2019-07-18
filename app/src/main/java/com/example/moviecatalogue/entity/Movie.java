package com.example.moviecatalogue.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.moviecatalogue.database.DatabaseContract;

import static android.os.Build.ID;
import static com.example.moviecatalogue.database.DatabaseContract.getColumnInt;
import static com.example.moviecatalogue.database.DatabaseContract.getColumnString;


public class Movie implements Parcelable {
    private int id;
    private String title;
    private String originalTitle;
    private String rating;
    private String releaseDate;
    private String overview;
    private String photoLink;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public Movie() {
    }

    public Movie(int id, String title, String originalTitle, String rating, String description, String date, String photoLink) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.rating = rating;
        this.overview = description;//
        this.releaseDate = date;//
        this.photoLink = photoLink;
    }
    public Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, ID);
        this.title = getColumnString(cursor, DatabaseContract.FavoriteColumns.TITLE);
        this.overview = getColumnString(cursor, DatabaseContract.FavoriteColumns.OVERVIEW);
        this.releaseDate = getColumnString(cursor, DatabaseContract.FavoriteColumns.RELEASE_DATE);
        this.originalTitle = getColumnString(cursor, DatabaseContract.FavoriteColumns.ORIGINAL_TITLE);
        this.rating = getColumnString(cursor, DatabaseContract.FavoriteColumns.RATING);
        this.photoLink = getColumnString(cursor, DatabaseContract.FavoriteColumns.PHOTO_LINK);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.originalTitle);
        dest.writeString(this.rating);
        dest.writeString(this.releaseDate);
        dest.writeString(this.overview);
        dest.writeString(this.photoLink);
        dest.writeString(this.type);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.originalTitle = in.readString();
        this.rating = in.readString();
        this.releaseDate = in.readString();
        this.overview = in.readString();
        this.photoLink = in.readString();
        this.type = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
