package com.mjcdouai.go4lunch.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mjcdouai.go4lunch.databinding.RestaurantItemListBinding;
import com.mjcdouai.go4lunch.remote.GoogleApi;
import com.mjcdouai.go4lunch.remote.GoogleQueryResult;

import java.util.List;

public class RestaurantRecyclerviewAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private List<GoogleQueryResult.Result> mRestaurantList;
    private RestaurantItemListBinding mBinding;


    public RestaurantRecyclerviewAdapter(GoogleQueryResult restaurantList)
    {
        mRestaurantList = restaurantList.results;

    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = RestaurantItemListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new RestaurantViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        int starNumber;
        holder.getName().setText(mRestaurantList.get(position).name);
        holder.getAddress().setText(mRestaurantList.get(position).address);

        String url = GoogleApi.getImageUrl(mRestaurantList.get(position).photos.get(0).mPhotoReference);

        Glide.with(mBinding.getRoot().getContext())
                .load(url)
                .into(holder.getImage());
        float rating = mRestaurantList.get(position).rating;
        if(rating<2 && rating > 1)
        {
            starNumber = 1;
        }else if(rating<3.5 && rating > 2)
        {
            starNumber = 2;
        }
        else {
            starNumber = 3;
        }
        StringBuilder stars = new StringBuilder();
        char star=0x2B50;


        for(int i=0;i<starNumber;i++)
        {
            stars.append(String.valueOf(star));
        }

        holder.getStars().setText(stars);

    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }
}
