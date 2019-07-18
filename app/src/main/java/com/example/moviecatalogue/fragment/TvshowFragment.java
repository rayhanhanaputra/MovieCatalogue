package com.example.moviecatalogue.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.moviecatalogue.adapter.CardViewTvshowAdapter;
import com.example.moviecatalogue.viewmodel.TvshowSearchViewModel;
import com.example.moviecatalogue.viewmodel.TvshowViewModel;
import com.example.moviecatalogue.entity.Tvshow;
import com.example.moviecatalogue.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvshowFragment extends Fragment {
    private CardViewTvshowAdapter tvshowAdapter;
    private ArrayList<Tvshow> tvshows = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView rvTvshow;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private TvshowViewModel tvshowViewModel;
    private TvshowSearchViewModel tvshowSearchViewModel;

    public TvshowFragment() {
        // Required empty public constructor
    }

    private Observer<ArrayList<Tvshow>> getTvshow = new Observer<ArrayList<Tvshow>>() {
        @Override
        public void onChanged(ArrayList<Tvshow> tvshows) {
            if(tvshows != null){
                tvshowAdapter.setListTvshow(tvshows);
                showLoading(false);
            }
        }

    };

    private Observer<ArrayList<Tvshow>> getTvshowSearch = new Observer<ArrayList<Tvshow>>() {
        @Override
        public void onChanged(ArrayList<Tvshow> tvshows) {
            if(tvshows != null){
                tvshowAdapter.setListTvshow(tvshows);
                showLoading(false);
            }
        }

    };

    private void showLoading(Boolean state){
        if(state){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if(searchItem != null){
            searchView = (SearchView) searchItem.getActionView();
        }
        if(searchView != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    searchRecylerView(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tvshow, container, false);
        rvTvshow = view.findViewById(R.id.rv_tvshow);
        progressBar = view.findViewById(R.id.progressBar_tvshow);
        refreshRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void refreshRecyclerView(){
        showLoading(true);
        rvTvshow.setLayoutManager(new LinearLayoutManager(getActivity()));

        tvshowViewModel = ViewModelProviders.of(this).get(TvshowViewModel.class);
        tvshowViewModel.getTvShows().observe(this, getTvshow);
        tvshowViewModel.setListTvshowsToDisplay();
        tvshowAdapter = new CardViewTvshowAdapter(getContext());
        tvshowAdapter.notifyDataSetChanged();

        rvTvshow.setAdapter(tvshowAdapter);
    }

    private void searchRecylerView(String s){
        showLoading(true);
        rvTvshow.setLayoutManager(new LinearLayoutManager(getActivity()));

        tvshowSearchViewModel = ViewModelProviders.of(this).get(TvshowSearchViewModel.class);
        tvshowSearchViewModel.getTvShows().observe(this, getTvshowSearch);
        tvshowSearchViewModel.setTvshowName(s);
        tvshowSearchViewModel.setListTvshowsToDisplay();
        tvshowAdapter = new CardViewTvshowAdapter(getContext());
        tvshowAdapter.notifyDataSetChanged();

        rvTvshow.setAdapter(tvshowAdapter);
    }
}
