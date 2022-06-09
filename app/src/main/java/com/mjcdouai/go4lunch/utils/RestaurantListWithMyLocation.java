package com.mjcdouai.go4lunch.utils;

import android.location.Location;

import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.remote.GooglePlaceDetailsResult;
import com.mjcdouai.go4lunch.remote.GoogleQueryResult;

import java.util.List;

public class RestaurantListWithMyLocation {
    public List<Restaurant> mRestaurantList;
    public Location myLocation;


    public RestaurantListWithMyLocation(List<Restaurant> restaurantList, Location myLocation) {
        mRestaurantList = restaurantList;
        this.myLocation = myLocation;
    }
}
