package com.mjcdouai.go4lunch.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mjcdouai.go4lunch.callback.OnClickRestaurantListener;
import com.mjcdouai.go4lunch.databinding.RestaurantItemListBinding;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final RestaurantItemListBinding mBinding;
    private final OnClickRestaurantListener mOnClickRestaurantListener;
    public RestaurantViewHolder(RestaurantItemListBinding binding, OnClickRestaurantListener onClickRestaurantListener) {
        super(binding.getRoot());
        mOnClickRestaurantListener = onClickRestaurantListener;
        mBinding = binding;
        mBinding.getRoot().setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        mOnClickRestaurantListener.onRestaurantClick(getBindingAdapterPosition());
    }
}
