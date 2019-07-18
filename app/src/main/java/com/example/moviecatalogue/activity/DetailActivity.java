package com.example.moviecatalogue.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviecatalogue.database.FavoriteHelper;
import com.example.moviecatalogue.entity.Movie;
import com.example.moviecatalogue.entity.Tvshow;
import com.example.moviecatalogue.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.ID;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.ORIGINAL_TITLE;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.PHOTO_LINK;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.RATING;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.RELEASE_DATE;
import static com.example.moviecatalogue.database.DatabaseContract.FavoriteColumns.TITLE;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DETAIL = "extra_detail";
    private static final String API_KEY = "0a1b5c5193c99d0728bc8b62a85ac8b7";
    private final String linkPhoto = "https://image.tmdb.org/t/p/w185";
    private String linkGan = "";
    private String titleBar;
    private Menu menuItem;
    private MenuItem itemStar;
    private int ids;
    private String type;

    private FavoriteHelper favoriteHelper;
    private Boolean isFavorite = false;

    ImageView imgDetail;
    LinearLayout linearLayout;
    TextView nameDetail, descDetail, rateDetail, dateDetail, tvDivider, tvImageView, overview;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        menuItem = menu;
        itemStar = menu.getItem(0);
        itemStar.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_to_favorite_button){
            if(isFavorite) {
                if(type.equals("tv")){
                    removeFavoriteTV();
                }
                if (type.equals("movie")) {
                    removeFavorite();
                }
                Toast.makeText(getApplicationContext(),"Removed from favorite",Toast.LENGTH_SHORT).show();
            } else {
                if(type.equals("tv")){
                    saveFavoriteTV();
                }
                if (type.equals("movie")) {
                    saveFavorite();
                }
                Toast.makeText(getApplicationContext(),"Added to favorite",Toast.LENGTH_SHORT).show();
            }
            isFavorite = !isFavorite;
            setFavorite();
            return true;
        }
        finish();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("name",nameDetail.getText().toString());
        savedInstanceState.putString("overview", descDetail.getText().toString());
        savedInstanceState.putString("vote",rateDetail.getText().toString());
        savedInstanceState.putString("date",dateDetail.getText().toString());
        savedInstanceState.putString("image",tvImageView.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final ProgressBar progressBar = findViewById(R.id.progressBar_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        linearLayout = findViewById(R.id.linearLayout);
        imgDetail = findViewById(R.id.img_detail);
        nameDetail = findViewById(R.id.tv_name_detail);
        descDetail = findViewById(R.id.tv_desc_detail);
        rateDetail = findViewById(R.id.tv_rate_detail);
        dateDetail = findViewById(R.id.tv_releasedate_detail);
        tvDivider = findViewById(R.id.tv_divider);
        overview = findViewById(R.id.overview);
        tvImageView = findViewById(R.id.tv_imageView);

        if(savedInstanceState != null){
            nameDetail.setText(savedInstanceState.getString("name"));
            descDetail.setText(savedInstanceState.getString("overview"));
            rateDetail.setText(savedInstanceState.getString("vote"));
            dateDetail.setText(savedInstanceState.getString("date"));
            tvImageView.setText(savedInstanceState.getString("image"));
            Glide.with(DetailActivity.this)
                    .load(savedInstanceState.getString("image"))
                    .into(imgDetail);
        }

        if(savedInstanceState == null){
            progressBar.setVisibility(View.VISIBLE);
            setVisibilityWidget(false);

            Movie movie = getIntent().getParcelableExtra(EXTRA_DETAIL);
            ids = movie.getId();
            titleBar = movie.getTitle();
            type = movie.getType();

            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://api.themoviedb.org/3/"+type+"/"+ids+"?api_key="+ API_KEY +"&language="+ MainActivity.languageId;
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try{
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        String detailName = "";
                        String dateAir = "";
                        Movie movie = getIntent().getParcelableExtra(EXTRA_DETAIL);
                        String type = movie.getType();

                        switch(type){
                            case "tv":
                                detailName = "original_name";
                                dateAir = "first_air_date";
                                break;
                            case "movie":
                                detailName = "original_title";
                                dateAir = "release_date";
                                break;
                        }
                        nameDetail.setText(responseObject.getString(detailName));
                        descDetail.setText(responseObject.getString("overview"));
                        rateDetail.setText(responseObject.getString("vote_average"));
                        dateDetail.setText(responseObject.getString(dateAir).substring(0,4));
                        linkGan = linkPhoto+responseObject.getString("poster_path");
                        tvImageView.setText(responseObject.getString("poster_path"));
                        Glide.with(DetailActivity.this)
                                .load(linkGan)
                                .into(imgDetail);

                        progressBar.setVisibility(View.GONE);
                        setVisibilityWidget(true);
                        itemStar.setVisible(true);

                        if(type.equals("tv")){
                            favoriteStateTV();
                        }
                        if(type.equals("movie")){
                            favoriteState();
                        }

                    } catch (Exception e){
                        Log.d("Exception", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                    Log.d("onFailure", "error code: "+statusCode);
                }
            });

            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle(titleBar);
            }
        }

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void favoriteState(){
        favoriteHelper.open();

        if(favoriteHelper.checkIfMovieExists(ids)){
            isFavorite = true;
            setFavorite();
        }
        else{
            isFavorite = false;
            setFavorite();
        }

        favoriteHelper.close();
    }

    private void favoriteStateTV(){
        favoriteHelper.open();

        if(favoriteHelper.checkIfTvExists(ids)){
            isFavorite = true;
            setFavorite();
        }
        else{
            isFavorite = false;
            setFavorite();
        }
        favoriteHelper.close();
    }

    private void saveFavorite(){
        Movie fav = new Movie();
        fav.setId(ids);
        fav.setTitle(titleBar);
        fav.setOriginalTitle(nameDetail.getText().toString());
        fav.setRating(rateDetail.getText().toString());
        fav.setReleaseDate(dateDetail.getText().toString());
        fav.setOverview(descDetail.getText().toString());
        fav.setPhotoLink(tvImageView.getText().toString());
        favoriteHelper.open();
        favoriteHelper.insertFavorite(fav);
        favoriteHelper.close();

        favoriteHelper.updateWidget(this);
    }

    private void saveFavoriteTV(){
        Tvshow fav = new Tvshow();
        fav.setId(ids);
        fav.setTitle(titleBar);
        fav.setOriginalTitle(nameDetail.getText().toString());
        fav.setRating(rateDetail.getText().toString());
        fav.setReleaseDate(dateDetail.getText().toString());
        fav.setOverview(descDetail.getText().toString());
        fav.setPhotoLink(tvImageView.getText().toString());
        favoriteHelper.open();
        favoriteHelper.insertFavoriteTv(fav);
        favoriteHelper.close();
    }

    private void removeFavorite(){
        favoriteHelper.open();
        favoriteHelper.deleteFavorite(ids);
        favoriteHelper.close();
        favoriteHelper.updateWidget(this);
    }

    private void removeFavoriteTV(){
        favoriteHelper.open();
        favoriteHelper.deleteFavoriteTv(ids);
        favoriteHelper.close();
    }

    private void setFavorite(){
        if(isFavorite){
            menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites));
        }else{
            menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites));
        }
    }

    private void setVisibilityWidget(Boolean bool){
        if(bool){
            linearLayout.setVisibility(View.VISIBLE);
            imgDetail.setVisibility(View.VISIBLE);
            nameDetail.setVisibility(View.VISIBLE);
            rateDetail.setVisibility(View.VISIBLE);
            dateDetail.setVisibility(View.VISIBLE);
            tvDivider.setVisibility(View.VISIBLE);
            descDetail.setVisibility(View.VISIBLE);
            overview.setVisibility(View.VISIBLE);

        } else{
            linearLayout.setVisibility(View.GONE);
            imgDetail.setVisibility(View.GONE);
            nameDetail.setVisibility(View.GONE);
            rateDetail.setVisibility(View.GONE);
            dateDetail.setVisibility(View.GONE);
            tvDivider.setVisibility(View.GONE);
            descDetail.setVisibility(View.GONE);
            overview.setVisibility(View.GONE);
        }
    }
}