package com.mjcdouai.go4lunch.ui;

import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.callback.OnClickRestaurantListener;
import com.mjcdouai.go4lunch.databinding.RestaurantItemListBinding;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.remote.GoogleApi;
import com.mjcdouai.go4lunch.utils.RestaurantListWithMyLocation;

import java.util.List;

public class RestaurantRecyclerviewAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private List<Restaurant> mRestaurantList;
    private RestaurantItemListBinding mBinding;
    private final OnClickRestaurantListener mOnClickRestaurantListener;
    Location myLocation;


    public RestaurantRecyclerviewAdapter(RestaurantListWithMyLocation restaurantListWithMyLocation, OnClickRestaurantListener onClickRestaurantListener) {
        mRestaurantList = restaurantListWithMyLocation.mRestaurantList;
        mOnClickRestaurantListener = onClickRestaurantListener;
        myLocation = restaurantListWithMyLocation.myLocation;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = RestaurantItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RestaurantViewHolder(mBinding,mOnClickRestaurantListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        int starNumber;
        holder.getName().setText(mRestaurantList.get(position).getName());
        holder.getAddress().setText(mRestaurantList.get(position).getAddress());


        if (mRestaurantList.get(position).getPhotoReferences().size() > 0) {
            String url = GoogleApi.getImageUrl(mRestaurantList.get(position).getPhotoReferences().get(0));

            Glide.with(mBinding.getRoot().getContext())
                    .load(url)
                    .into(holder.getImage());
        }
        float rating = mRestaurantList.get(position).getRating();

        if (rating <= 1) {
            starNumber = 0;
        } else if (rating <= 2.5 && rating > 1) {
            starNumber = 1;
        } else if (rating < 4 && rating > 2.5) {
            starNumber = 2;
        } else {
            starNumber = 3;
        }
        StringBuilder stars = new StringBuilder();
        char star = 0x2B50;


        for (int i = 0; i < starNumber; i++) {
            stars.append(star);
        }

        if (mRestaurantList.get(position).isOpen()) {
            holder.getOpenUntil().setText(R.string.Open);
            holder.getOpenUntil().setTextColor(Color.GREEN);
        } else {
            holder.getOpenUntil().setText(R.string.Close);
            holder.getOpenUntil().setTextColor(Color.RED);
        }

        holder.getStars().setText(stars);

        holder.getDistance().setText(getDistanceBetweenMeAndLocation(mRestaurantList.get(position).getLatitude(), mRestaurantList.get(position).getLongitude()));

    }

    private String getDistanceBetweenMeAndLocation(double locationLat, double locationLon) {
        Location temp = new Location("temp location");
        temp.setLatitude(locationLat);
        temp.setLongitude(locationLon);

        return Math.round(myLocation.distanceTo(temp)) + " m";
    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }
}
