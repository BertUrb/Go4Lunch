package com.mjcdouai.go4lunch.ui;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mjcdouai.go4lunch.databinding.WorkmatesItemListBinding;

public class RestaurantDetailsWorkmatesViewHolder extends RecyclerView.ViewHolder {

    private final WorkmatesItemListBinding mBinding;

    public RestaurantDetailsWorkmatesViewHolder(WorkmatesItemListBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public ImageView getWorkmateProfilePictureImageView() {
        return mBinding.workmatesProfilePicture;
    }

    public TextView getWorkmateNameTextView() {
        return mBinding.workmatesName;
    }
}
