package com.mjcdouai.go4lunch.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.model.Workmate;
import com.mjcdouai.go4lunch.repository.WorkmatesRepository;
import com.mjcdouai.go4lunch.utils.WorkmateWithRestaurantName;

import java.util.List;

public class WorkmatesViewModel extends ViewModel {
    private final WorkmatesRepository mWorkmatesRepository = WorkmatesRepository.getInstance();
    private static volatile  WorkmatesViewModel instance;

    public MutableLiveData<List<WorkmateWithRestaurantName>> getWorkmatesWithRestaurantsNames()
    {
        return mWorkmatesRepository.getWorkmatesWithRestaurants();
    }

    public  MutableLiveData<String> getMyRestaurantChoiceId()
    {
        return mWorkmatesRepository.getMyRestaurantChoiceId();
    }

    public MutableLiveData<List<WorkmateWithRestaurantName>> getAllWorkmatesWithRestaurantNames(){
        return mWorkmatesRepository.getAllWorkMatesWithRestaurants();
    }


    public MutableLiveData<List<Workmate>> getWorkmateInThis(String restaurantId){
        return mWorkmatesRepository.getWorkmateInThis(restaurantId);
    }

    public void insertWorkmate(Workmate workmate, String restaurantName) {
        mWorkmatesRepository.insertWorkmate(workmate,restaurantName);
    }

    public void addFavoriteRestaurant(Restaurant restaurant)
    {
        mWorkmatesRepository.addFavoriteRestaurant(restaurant);
    }

    public static WorkmatesViewModel getInstance() {
        WorkmatesViewModel result = instance;

        if(result != null)
        {
            return result;
        }
        synchronized (WorkmatesViewModel.class){
            if(instance == null)
            {
                instance = new WorkmatesViewModel();
            }
            return instance;
        }
    }

    public void removeFavoriteRestaurant(Restaurant restaurant) {
        mWorkmatesRepository.removeFavoriteRestaurant(restaurant);
    }


}
