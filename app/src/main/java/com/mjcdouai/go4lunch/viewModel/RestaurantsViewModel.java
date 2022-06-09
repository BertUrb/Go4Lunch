package com.mjcdouai.go4lunch.viewModel;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class RestaurantsViewModel extends ViewModel {
   private final RestaurantRepository mRestaurantRepository = new RestaurantRepository();

   public MutableLiveData<List<Restaurant>> loadRestaurantNearby(Location location)
   {
       return mRestaurantRepository.getRestaurantNearby(location);
   }

   public MutableLiveData<Boolean> loadRestaurantDetails(int index)
   {
       return mRestaurantRepository.getDetails(index);
   }


}
