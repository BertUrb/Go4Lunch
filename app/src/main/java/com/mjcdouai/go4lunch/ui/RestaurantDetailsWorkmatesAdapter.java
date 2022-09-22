package com.mjcdouai.go4lunch.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mjcdouai.go4lunch.databinding.WorkmatesItemListBinding;
import com.mjcdouai.go4lunch.model.Workmate;

import java.util.List;

public class RestaurantDetailsWorkmatesAdapter extends RecyclerView.Adapter<RestaurantDetailsWorkmatesViewHolder> {

   private final List<Workmate> mWorkmatesList;
   private WorkmatesItemListBinding mBinding;

    public RestaurantDetailsWorkmatesAdapter(List<Workmate> workmatesList) {
        mWorkmatesList = workmatesList;
    }

    @NonNull
    @Override
    public RestaurantDetailsWorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = WorkmatesItemListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new RestaurantDetailsWorkmatesViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDetailsWorkmatesViewHolder holder, int position) {
        holder.getWorkmateNameTextView().setText(mWorkmatesList.get(position).getName());
        Glide.with(mBinding.getRoot().getContext())
                .load(mWorkmatesList.get(position).getPhotoUrl())
                .into(holder.getWorkmateProfilePictureImageView());

    }

    @Override
    public int getItemCount() {
        return mWorkmatesList.size();
    }
}
