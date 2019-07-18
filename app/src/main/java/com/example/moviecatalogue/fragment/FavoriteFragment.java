package com.example.moviecatalogue.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviecatalogue.adapter.CardViewMovieAdapter;
import com.example.moviecatalogue.adapter.CardViewTvshowAdapter;
import com.example.moviecatalogue.database.FavoriteHelper;
import com.example.moviecatalogue.R;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private TabLayout tabs;
    private RecyclerView rvMovFav, rvTvFav;
    private FavoriteHelper favoriteHelper;
    private int id;

    CardViewMovieAdapter movieAdapter;
    CardViewTvshowAdapter tvshowAdapter;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        movieAdapter.notifyDataSetChanged();
        tvshowAdapter.notifyDataSetChanged();

        favoriteHelper.open();
        movieAdapter.setListMovie(favoriteHelper.getAllFavorites());
        favoriteHelper.close();
        rvMovFav.setAdapter(movieAdapter);

        favoriteHelper.open();
        tvshowAdapter.setListTvshow(favoriteHelper.getAllFavoritesTv());
        favoriteHelper.close();
        rvTvFav.setAdapter(tvshowAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvMovFav = view.findViewById(R.id.rv_favorite_movie);
        rvTvFav = view.findViewById(R.id.rv_favorite_tvshow);
        rvMovFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTvFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieAdapter = new CardViewMovieAdapter(getContext());
        tvshowAdapter = new CardViewTvshowAdapter(getContext());
        favoriteHelper = new FavoriteHelper(getContext());

        favoriteHelper.open();
        movieAdapter.setListMovie(favoriteHelper.getAllFavorites());
        favoriteHelper.close();
        rvMovFav.setAdapter(movieAdapter);
        rvMovFav.setVisibility(View.VISIBLE);
        rvTvFav.setVisibility(View.GONE);
        tabs = view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText(getString(R.string.movies_string_fav)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.tv_show_string_fav)));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabs.getSelectedTabPosition() == 0){
                    favoriteHelper.open();
                    movieAdapter.setListMovie(favoriteHelper.getAllFavorites());
                    favoriteHelper.close();
                    rvMovFav.setAdapter(movieAdapter);
                    rvMovFav.setVisibility(View.VISIBLE);
                    rvTvFav.setVisibility(View.GONE);
                }else if(tabs.getSelectedTabPosition() == 1){
                    favoriteHelper.open();
                    tvshowAdapter.setListTvshow(favoriteHelper.getAllFavoritesTv());
                    favoriteHelper.close();
                    rvTvFav.setAdapter(tvshowAdapter);
                    rvMovFav.setVisibility(View.GONE);
                    rvTvFav.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
