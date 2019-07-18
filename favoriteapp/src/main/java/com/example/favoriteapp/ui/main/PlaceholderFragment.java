package com.example.favoriteapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.favoriteapp.R;
import com.example.favoriteapp.adapter.CardViewMovieAdapter;
import com.example.favoriteapp.entity.Movie;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private RecyclerView rvMovie;
    private CardViewMovieAdapter movieAdapter;

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance() {
        return new PlaceholderFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        rvMovie = root.findViewById(R.id.rv_favorite_movie);
        rvMovie.setHasFixedSize(true);
        refreshRecylerView();
        return root;
    }

    private Observer<ArrayList<Movie>> getMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if(movies != null){
                movieAdapter.setListMovie(movies);
            }
        }

    };

    private void refreshRecylerView(){
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.getContext(getContext());
        pageViewModel.setListMoviesToDisplay();
        pageViewModel.getMovies().observe(this, getMovie);
        movieAdapter = new CardViewMovieAdapter(getContext());
        movieAdapter.notifyDataSetChanged();

        rvMovie.setAdapter(movieAdapter);
    }
}