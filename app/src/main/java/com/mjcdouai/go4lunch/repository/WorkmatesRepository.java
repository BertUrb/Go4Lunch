package com.mjcdouai.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mjcdouai.go4lunch.model.Workmate;
import com.mjcdouai.go4lunch.utils.WorkmateWithRestaurantName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkmatesRepository {

    private static volatile WorkmatesRepository instance;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION = "workmates";


    public void insertWorkmate(Workmate workmate, String restaurantName) {
        Map<String, Object> workmateMap = new HashMap<>();
        LocalDate date = LocalDate.now();


        workmateMap.put("name", workmate.getName());
        workmateMap.put("photoUrl", workmate.getPhotoUrl());
        workmateMap.put("chosenRestaurantId", workmate.getChosenRestaurantId());
        workmateMap.put("date", date.toString());
        workmateMap.put("restaurantName", restaurantName);

        db.collection(COLLECTION).document(workmate.getMail()).set(workmateMap).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("TAG", "onComplete error: " + task.getException());
            }
        });

    }

    public MutableLiveData<List<Workmate>> getWorkmateInThis(String restaurantId) {


        MutableLiveData<List<Workmate>> mutableWorkmates = new MutableLiveData<>();
        List<Workmate> workmates = new ArrayList<>();
        LocalDate date = LocalDate.now();

        db.collection(COLLECTION).whereEqualTo("chosenRestaurantId", restaurantId)
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
        db.collection(COLLECTION).get().addOnCompleteListener(task -> {
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
        db.collection(COLLECTION).whereEqualTo("date", date.toString()).get().addOnCompleteListener(task -> {
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



}
