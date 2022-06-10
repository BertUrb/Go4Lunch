package com.mjcdouai.go4lunch.viewModel;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class RestaurantsViewModel extends ViewModel {
    private final RestaurantRepository mRestaurantRepository = RestaurantRepository.getInstance();
    Location mLastLocation = new Location("Last location");
    private MutableLiveData<List<Restaurant>> mLiveData;

    public MutableLiveData<List<Restaurant>> loadRestaurantNearby(Location location) {

        if (location != mLastLocation) {
            mLiveData = mRestaurantRepository.getRestaurantNearby(location);
            mLastLocation = location;
        }
        return mLiveData;
    }

    public MutableLiveData<Restaurant> loadRestaurantDetails(int index) {
        ;
        return mRestaurantRepository.getDetails(index);
    }

}
