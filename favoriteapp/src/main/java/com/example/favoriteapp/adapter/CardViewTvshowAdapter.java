package com.example.favoriteapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.favoriteapp.R;
import com.example.favoriteapp.entity.Tvshow;

import java.util.ArrayList;

public class CardViewTvshowAdapter extends RecyclerView.Adapter<CardViewTvshowAdapter.CardViewViewHolderTv> {
    private Context context;
    private ArrayList<Tvshow> listTvshow = new ArrayList<>();
    private final String linkPhoto = "https://image.tmdb.org/t/p/w185";

    public ArrayList<Tvshow> getListTvshow() {
        return listTvshow;
    }

    public void setListTvshow(ArrayList<Tvshow> listTvshows) {
        listTvshow.clear();
        listTvshow.addAll(listTvshows);
        notifyDataSetChanged();
    }

    public CardViewTvshowAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewViewHolderTv onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new CardViewViewHolderTv(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolderTv holder, int position) {
        Tvshow tvshow = getListTvshow().get(position);

        Glide.with(holder.imgPhoto.getContext())
                .load(linkPhoto+tvshow.getPhotoLink())
                .into(holder.imgPhoto);
        holder.tvName.setText(tvshow.getTitle());
        holder.tvDesc.setText(tvshow.getOverview());

//        holder.itemView.setOnClickListener(new com.example.moviecatalogue.adapter.CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
//            @Override
//            public void onItemClicked(View view, int position) {
//                Tvshow tvshows = getListTvshow().get(position);
//                Movie mov = new Movie();
//                mov.setId(tvshows.getId());
//                mov.setType("tv");
//                mov.setTitle(tvshows.getTitle());
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra(DetailActivity.EXTRA_DETAIL, mov);
//                context.startActivity(intent);
//            }
//        }));
    }

    @Override
    public int getItemCount() {
        return getListTvshow().size();
    }

    public class CardViewViewHolderTv extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvDesc;
        public CardViewViewHolderTv(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDesc = itemView.findViewById(R.id.tv_item_desc);
        }
    }
}
