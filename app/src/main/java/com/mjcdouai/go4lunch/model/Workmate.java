package com.mjcdouai.go4lunch.model;

public class Workmate {
    public String getMail() {
        return mMail;
    }

    private final String mMail;
    private final String mName;
    private final String mPhotoUrl;
    private String mChosenRestaurantId;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    private String mDate;

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

    public Workmate(String mail, String name, String photoUrl) {

       mMail=mail;
       mName = name;
        mPhotoUrl = photoUrl;
    }
}