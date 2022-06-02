package com.mjcdouai.go4lunch.ui;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mjcdouai.go4lunch.databinding.RestaurantItemListBinding;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {
    private final RestaurantItemListBinding mBinding;
    public RestaurantViewHolder(RestaurantItemListBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public TextView getName()
    {
        return mBinding.restaurantName;
    }

    public TextView getAddress()
    {
        return mBinding.restaurantAddress;
    }

    public TextView getOpenUntil()
    {
        return mBinding.restaurantOpenUntil;
    }

    public ImageView getImage()
    {
        return mBinding.restaurantImage;
    }

    public TextView getDistance() {
        return mBinding.restaurantDistance;
    }

    public TextView getWorkmatesCount()
    {
        return mBinding.workmatesCount;
    }
    public TextView getStars()
    {
        return mBinding.stars;
    }
}
