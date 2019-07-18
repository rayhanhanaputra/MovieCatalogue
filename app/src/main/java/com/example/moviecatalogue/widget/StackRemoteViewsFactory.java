package com.example.moviecatalogue.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.moviecatalogue.R;
import com.example.moviecatalogue.database.FavoriteHelper;
import com.example.moviecatalogue.entity.Movie;

import java.util.ArrayList;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private FavoriteHelper favoriteHelper;
    private ArrayList<Movie> listMovie = new ArrayList<>();

    StackRemoteViewsFactory(Context context){
        mContext = context;
    }

    @Override
    public void onCreate() {
        favoriteHelper = new FavoriteHelper(mContext);
        favoriteHelper.open();
        listMovie = favoriteHelper.getAllFavorites();
        favoriteHelper.close();
    }

    @Override
    public void onDataSetChanged() {
        favoriteHelper.open();
        listMovie = favoriteHelper.getAllFavorites();
        favoriteHelper.close();

        AppWidgetManager awm = AppWidgetManager.getInstance(mContext);
        awm.notifyAppWidgetViewDataChanged(awm.getAppWidgetIds(new ComponentName(mContext, StackWidgetService.class)), R.id.stack_view);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listMovie.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        try{
            Bitmap bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load("https://image.tmdb.org/t/p/w92"+listMovie.get(i).getPhotoLink())
                    .submit(512,512)
                    .get();

            rv.setImageViewBitmap(R.id.imageView_widget_item, bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putString(ImageBannerWidget.EXTRA_ITEM, listMovie.get(i).getTitle());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView_widget_item, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
