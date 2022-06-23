package com.mjcdouai.go4lunch.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.WorkmatesItemListBinding;
import com.mjcdouai.go4lunch.utils.WorkmateWithRestaurantName;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder>{

    private final List<WorkmateWithRestaurantName> mWorkmatesWithRestaurants;
    WorkmatesItemListBinding mBinding;

    public WorkmatesAdapter(List<WorkmateWithRestaurantName> workmatesWithRestaurants) {
       mWorkmatesWithRestaurants = workmatesWithRestaurants;

    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = WorkmatesItemListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
    return new WorkmatesViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {

        Glide.with(mBinding.getRoot().getContext())
                .load(mWorkmatesWithRestaurants.get(position).mWorkmate.getPhotoUrl())
                .into(holder.getWorkmateProfilePictureImageView());

        String text = mWorkmatesWithRestaurants.get(position).mWorkmate.getName() + " ";
        LocalDate date = LocalDate.now();

        if(!Objects.equals(mWorkmatesWithRestaurants.get(position).mWorkmate.getDate(), date.toString())){
            mWorkmatesWithRestaurants.get(position).mRestaurantName =  mBinding.getRoot().getResources().getString(R.string.not_decided);
        }


        if(Objects.equals(mWorkmatesWithRestaurants.get(position).mRestaurantName, mBinding.getRoot().getResources().getString(R.string.not_decided))) {
            text += mBinding.getRoot().getResources().getString(R.string.not_decided);
        }
        else {
            text += mBinding.getRoot().getResources().getString(R.string.choose)
                    + " "
                    + mWorkmatesWithRestaurants.get(position).mRestaurantName;
        }
        holder.getWorkmateNameTextView().setText(text);

    }

    @Override
    public int getItemCount() {
        return mWorkmatesWithRestaurants.size();
    }
}
