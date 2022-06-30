package com.mjcdouai.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mjcdouai.go4lunch.manager.UserManager;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.model.Workmate;
import com.mjcdouai.go4lunch.utils.WorkmateWithRestaurantName;
import com.mjcdouai.go4lunch.viewModel.RestaurantsViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WorkmatesRepository {

    private static volatile WorkmatesRepository instance;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION_WORKMATES = "workmates";

    private List<String> mFavRestaurantList = new ArrayList<>();

    public  void initFavRestaurantList() {
        mFavRestaurantList = new ArrayList<>();
        UserManager userManager = UserManager.getInstance();
        db.collection(COLLECTION_WORKMATES).document(userManager.getCurrentUser().getEmail()).get().addOnCompleteListener(task -> {
           mFavRestaurantList = (List<String>) task.getResult().get("favRestaurants");
            Log.d("TAG", "initFavRestaurantList: done");
        });


    }


    public void insertWorkmate(Workmate workmate, String restaurantName) {
        Map<String, Object> workmateMap = new HashMap<>();
        LocalDate date = LocalDate.now();


        workmateMap.put("name", workmate.getName());
        workmateMap.put("photoUrl", workmate.getPhotoUrl());
        workmateMap.put("chosenRestaurantId", workmate.getChosenRestaurantId());
        workmateMap.put("date", date.toString());
        workmateMap.put("restaurantName", restaurantName);

        db.collection(COLLECTION_WORKMATES).document(workmate.getMail()).set(workmateMap).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("TAG", "onComplete error: " + task.getException());
            }
        });

    }

    public void addFavoriteRestaurant(Restaurant restaurant) {
        if(mFavRestaurantList == null)
        {
            initFavRestaurantList();
        }




        mFavRestaurantList.add(restaurant.getId());

        RestaurantsViewModel restaurantsViewModel = RestaurantsViewModel.getInstance();

        restaurantsViewModel.likeRestaurant(restaurant);

        UserManager userManager = UserManager.getInstance();

        Map<String,Object> favMap = new HashMap<>();
        favMap.put("favRestaurants",mFavRestaurantList);



        db.collection(COLLECTION_WORKMATES).document(userManager.getCurrentUser().getEmail()).set(favMap).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("TAG", "onComplete error: " + task.getException());
            }
        });


    }

    public MutableLiveData<List<Workmate>> getWorkmateInThis(String restaurantId) {


        MutableLiveData<List<Workmate>> mutableWorkmates = new MutableLiveData<>();
        List<Workmate> workmates = new ArrayList<>();
        LocalDate date = LocalDate.now();

        db.collection(COLLECTION_WORKMATES).whereEqualTo("chosenRestaurantId", restaurantId)
                .whereEqualTo("date", date.toString())
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        workmates.add(new Workmate(document.getId(),
                                document.getString("name"),
                                document.getString("photoUrl")));

                    }
                    mutableWorkmates.setValue(workmates);

                });
        return mutableWorkmates;
    }
    public MutableLiveData<List<WorkmateWithRestaurantName>> getAllWorkMatesWithRestaurants(){
        MutableLiveData<List<WorkmateWithRestaurantName>> mutableLiveData = new MutableLiveData<>();
        List<WorkmateWithRestaurantName> workmatesWithRestaurantNames = new ArrayList<>();
        db.collection(COLLECTION_WORKMATES).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Workmate workmate = new Workmate(document.getId(),
                        document.getString("name"),
                        document.getString("photoUrl"));
                workmate.setChosenRestaurantId(document.getString("chosenRestaurantId"));
                workmate.setDate(document.getString("date"));
                workmatesWithRestaurantNames.add(new WorkmateWithRestaurantName(workmate, document.getString("restaurantName")));
            }
            mutableLiveData.setValue(workmatesWithRestaurantNames);

        });
        return mutableLiveData;
    }

    public MutableLiveData<List<WorkmateWithRestaurantName>> getWorkmatesWithRestaurants() {
        LocalDate date = LocalDate.now();
        MutableLiveData<List<WorkmateWithRestaurantName>> mutableLiveData = new MutableLiveData<>();
        List<WorkmateWithRestaurantName> workmatesWithRestaurantNames = new ArrayList<>();
        db.collection(COLLECTION_WORKMATES).whereEqualTo("date", date.toString()).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Workmate workmate = new Workmate(document.getId(),
                        document.getString("name"),
                        document.getString("photoUrl"));
                workmate.setChosenRestaurantId(document.getString("chosenRestaurantId"));
                workmatesWithRestaurantNames.add(new WorkmateWithRestaurantName(workmate, document.getString("restaurantName")));
            }
            mutableLiveData.setValue(workmatesWithRestaurantNames);

        });
        return mutableLiveData;
    }


    public static WorkmatesRepository getInstance() {
        WorkmatesRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new WorkmatesRepository();
            }
            return instance;
        }


    }


    public void removeFavoriteRestaurant(Restaurant restaurant) {
        if(mFavRestaurantList == null)
        {
            initFavRestaurantList();
        }

        mFavRestaurantList.remove(restaurant.getId());
        UserManager userManager = UserManager.getInstance();
        RestaurantsViewModel restaurantsViewModel = RestaurantsViewModel.getInstance();

        restaurantsViewModel.unlikeRestaurant(restaurant);

        Map<String,Object> favMap = new HashMap<>();
        favMap.put("favRestaurants",mFavRestaurantList);

        db.collection(COLLECTION_WORKMATES).document(userManager.getCurrentUser().getEmail()).set(favMap).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("TAG", "onComplete error: " + task.getException());
            }
        });
    }

    public boolean isFavoriteRestaurant(String restaurantId) {

        if(mFavRestaurantList == null)
        {
            Log.d("tag", "isFavoriteRestaurant: null");
            return false;
        }
        return mFavRestaurantList.contains(restaurantId);
    }

    public MutableLiveData<String> getMyRestaurantChoiceId() {
        LocalDate date = LocalDate.now();
        MutableLiveData<String> result = new MutableLiveData<>();

        db.collection(COLLECTION_WORKMATES).document(UserManager.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(task -> {
            if(Objects.requireNonNull(task.getResult().getString("date")).equals(date.toString()))
            {
                String res;
                res = task.getResult().getString("chosenRestaurantId");
                result.setValue(res);

            }
        });

        return result;
    }
}
