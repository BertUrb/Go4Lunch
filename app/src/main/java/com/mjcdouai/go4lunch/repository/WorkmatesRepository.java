package com.mjcdouai.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mjcdouai.go4lunch.model.Workmate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkmatesRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String COLLECTION = "workmates";

    public void insertWorkmate(Workmate workmate) {
        Map<String, Object> workmateMap = new HashMap<>();
        LocalDate date = LocalDate.now();


        workmateMap.put("name", workmate.getName());
        workmateMap.put("photoUrl", workmate.getPhotoUrl());
        workmateMap.put("chosenRestaurantId", workmate.getChosenRestaurantId());
        workmateMap.put("date", date);

        db.collection(COLLECTION).document(workmate.getMail()).set(workmateMap).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("TAG", "onComplete error: " + task.getException());
            }
        });

    }

    public MutableLiveData<List<Workmate>> getWorkmateInThis(String restaurantId){


        MutableLiveData<List<Workmate>> mutableWorkmates = new MutableLiveData<>();
        List<Workmate> workmates = new ArrayList<>();
        LocalDate date = LocalDate.now();

        db.collection(COLLECTION).whereEqualTo("chosenRestaurantId",restaurantId)
                .whereEqualTo("date",date)
                .get()
                .addOnCompleteListener(task -> {
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        workmates.add(new Workmate(document.getId(),
                                document.getString("name"),
                                document.getString("photoUrl")));

                    }
                    mutableWorkmates.setValue(workmates);

                });
        return  mutableWorkmates;
    }


}
