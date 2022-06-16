package com.mjcdouai.go4lunch.utils;

import com.mjcdouai.go4lunch.model.Workmate;

public class WorkmateWithRestaurantName {
    public Workmate mWorkmate;
    public String mRestaurantName;


    public WorkmateWithRestaurantName(Workmate workmate, String restaurantName) {
        mWorkmate = workmate;
        mRestaurantName = restaurantName;
    }
}
