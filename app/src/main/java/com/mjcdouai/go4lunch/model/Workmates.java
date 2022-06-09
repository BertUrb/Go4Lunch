package com.mjcdouai.go4lunch.model;

public class Workmates {
    private final String mName;
    private final String mPhotoUrl;
    private String mChosenRestaurantId;

    public String getName() {
        return mName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public String getChosenRestaurantId() {
        return mChosenRestaurantId;
    }

    public void setChosenRestaurantId(String chosenRestaurantId) {
        mChosenRestaurantId = chosenRestaurantId;
    }

    public Workmates(String name, String photoUrl) {
        mName = name;
        mPhotoUrl = photoUrl;
    }
}