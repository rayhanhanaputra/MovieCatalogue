package com.example.favoriteapp.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.favoriteapp.R;
import com.example.favoriteapp.adapter.CardViewMovieAdapter;
import com.example.favoriteapp.adapter.CardViewTvshowAdapter;
import com.example.favoriteapp.entity.Movie;
import com.example.favoriteapp.entity.Tvshow;

import java.util.ArrayList;

public class TvFavFragment extends Fragment {

    private TvFavViewModel mViewModel;
    private RecyclerView rvTvshow;
    private CardViewTvshowAdapter tvshowAdapter;

    public static TvFavFragment newInstance() {
        return new TvFavFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main2, container, false);
        rvTvshow = view.findViewById(R.id.rv_favorite_tvshow);
        rvTvshow.setHasFixedSize(true);
        refreshRecylerView();
        return view;
    }

    private Observer<ArrayList<Tvshow>> getTvshow = new Observer<ArrayList<Tvshow>>() {
        @Override
        public void onChanged(ArrayList<Tvshow> tvshows) {
            if(tvshows != null){
                tvshowAdapter.setListTvshow(tvshows);
            }
        }

    };

    private void refreshRecylerView(){
        rvTvshow.setLayoutManager(new LinearLayoutManager(getActivity()));

        mViewModel = ViewModelProviders.of(this).get(TvFavViewModel.class);
        mViewModel.getContext(getContext());
        mViewModel.setListTvshowToDisplay();
        mViewModel.getTvshow().observe(this, getTvshow);
        tvshowAdapter = new CardViewTvshowAdapter(getContext());
        tvshowAdapter.notifyDataSetChanged();

        rvTvshow.setAdapter(tvshowAdapter);
    }

}
