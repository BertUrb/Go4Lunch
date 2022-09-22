package com.mjcdouai.go4lunch.model;

public class Workmate {
    private final String mMail;
    private final String mName;
    private final String mPhotoUrl;
    private String mChosenRestaurantId;
    private String mDate;

    public Workmate(String mail, String name, String photoUrl) {

        mMail = mail;
        mName = name;
        mPhotoUrl = photoUrl;
    }

    public String getMail() {
        return mMail;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

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
}