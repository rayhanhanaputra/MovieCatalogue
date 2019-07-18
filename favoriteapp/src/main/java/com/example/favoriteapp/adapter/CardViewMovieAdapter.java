package com.example.favoriteapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.favoriteapp.R;
import com.example.favoriteapp.entity.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CardViewMovieAdapter extends RecyclerView.Adapter<CardViewMovieAdapter.CardViewViewHolder> {
    private Context context;
    private ArrayList<Movie> listMovie = new ArrayList<>();
    private final String linkPhoto = "https://image.tmdb.org/t/p/w185";

    public ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<Movie> listMovies) {
        listMovie.clear();
        listMovie.addAll(listMovies);
        notifyDataSetChanged();
    }

    public CardViewMovieAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        Movie movies = getListMovie().get(position);

        Glide.with(holder.imgPhoto.getContext())
                .load(linkPhoto+movies.getPhotoLink())
                .into(holder.imgPhoto);
        holder.tvName.setText(movies.getTitle());
        holder.tvDesc.setText(movies.getOverview());

//        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
//            @Override
//            public void onItemClicked(View view, int position) {
//                Movie movies = getListMovie().get(position);
//
//                Movie mov = new Movie();
//                mov.setId(movies.getId());
//                mov.setType("movie");
//                mov.setTitle(movies.getTitle());
//
//                Uri uri = Uri.parse(CONTENT_URI + "/" + movies.getId());
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.setData(uri);
//                intent.putExtra(DetailActivity.EXTRA_DETAIL,mov);
//                context.startActivity(intent);
//            }
//        }));
    }

    @Override
    public int getItemCount() {
        return getListMovie().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName,tvDesc;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDesc = itemView.findViewById(R.id.tv_item_desc);
        }
    }
}
