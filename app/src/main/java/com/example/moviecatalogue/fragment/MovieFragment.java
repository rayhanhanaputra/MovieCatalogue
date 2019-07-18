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

import com.example.moviecatalogue.adapter.CardViewMovieAdapter;
import com.example.moviecatalogue.entity.Movie;
import com.example.moviecatalogue.viewmodel.MovieSearchViewModel;
import com.example.moviecatalogue.viewmodel.MovieViewModel;
import com.example.moviecatalogue.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    private CardViewMovieAdapter movieAdapter;
    private ArrayList<Movie> movies = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView rvMovie;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private MovieViewModel movieViewModel;
    private MovieSearchViewModel movieSearchViewModel;

    public MovieFragment() {
        // Required empty public constructor
    }

    private Observer<ArrayList<Movie>> getMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if(movies != null){
                movieAdapter.setListMovie(movies);
                showLoading(false);
            }
        }

    };

    private Observer<ArrayList<Movie>> getMovieSearch = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if(movies != null){
                movieAdapter.setListMovie(movies);
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
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        rvMovie = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progressBar_movie);
        refreshRecylerView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void refreshRecylerView(){
        showLoading(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, getMovie);
        movieViewModel.setListMoviesToDisplay();
        movieAdapter = new CardViewMovieAdapter(getContext());
        movieAdapter.notifyDataSetChanged();

        rvMovie.setAdapter(movieAdapter);
    }

    private void searchRecylerView(String s){
        showLoading(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));

        movieSearchViewModel = ViewModelProviders.of(this).get(MovieSearchViewModel.class);
        movieSearchViewModel.getMovies().observe(this, getMovieSearch);
        movieSearchViewModel.setMovieName(s);
        movieSearchViewModel.setListMoviesSearchToDisplay();
        movieAdapter = new CardViewMovieAdapter(getContext());
        movieAdapter.notifyDataSetChanged();

        rvMovie.setAdapter(movieAdapter);
    }

}
