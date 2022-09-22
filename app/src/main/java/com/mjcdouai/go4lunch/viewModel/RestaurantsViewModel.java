package com.mjcdouai.go4lunch.viewModel;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class RestaurantsViewModel extends ViewModel {
    private static volatile RestaurantsViewModel instance;
    private final RestaurantRepository mRestaurantRepository = RestaurantRepository.getInstance();
    Location mLastLocation = new Location("Last location");
    private MutableLiveData<List<Restaurant>> mLiveData;

    public static RestaurantsViewModel getInstance() {
        RestaurantsViewModel result = instance;

        if (result != null) {
            return result;
        }
        synchronized (WorkmatesViewModel.class) {
            if (instance == null) {
                instance = new RestaurantsViewModel();
            }
            return instance;
        }
    }

    public MutableLiveData<List<Restaurant>> loadRestaurantNearby(Location location, int radius) {


        if (location.getLatitude() != mLastLocation.getLatitude() && location.getLongitude() != mLastLocation.getLongitude()) {
            mLiveData = mRestaurantRepository.getRestaurantNearby(location, radius);
            mLastLocation = location;
        }
        return mLiveData;
    }

    public MutableLiveData<Restaurant> loadRestaurantDetails(String id) {

        return mRestaurantRepository.getDetails(id);
    }

    public void likeRestaurant(Restaurant restaurant) {
        mRestaurantRepository.likeRestaurant(restaurant);
    }

    public void unlikeRestaurant(Restaurant restaurant) {
        mRestaurantRepository.unlikeRestaurant(restaurant);
    }


}
