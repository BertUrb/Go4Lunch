package com.mjcdouai.go4lunch.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Restaurant implements Parcelable {
    private String mName;
    private String mAddress;
    private boolean mIsOpen;
    private double mLatitude;
    private double mLongitude;
    private String mWebsite;
    private boolean mLiked;
    private boolean mChosen;
    private String mPhone;

    public String getId() {
        return mId;
    }

    private final String mId;
    private List<String> mPhotoReferences;
    private float mRating;


    protected Restaurant(Parcel in) {
        mName = in.readString();
        mAddress = in.readString();
        mIsOpen = in.readByte() != 0;
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mWebsite = in.readString();
        mLiked = in.readByte() != 0;
        mChosen = in.readByte() != 0;
        mPhone = in.readString();
        mId = in.readString();
        mPhotoReferences = in.createStringArrayList();
        mRating = in.readFloat();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        mRating = rating;
    }



    public List<String> getPhotoReferences() {
        return mPhotoReferences;
    }

    public void setPhotoReferences(List<String> photoReferences) {
        mPhotoReferences = photoReferences;
    }



    public Restaurant(String id,String name, String address, boolean isOpen, double latitude, double longitude) {
        mId=id;
        mName = name;
        mAddress = address;
        this.mIsOpen = isOpen;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setOpen(boolean open) {
        mIsOpen = open;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public void setLiked(boolean liked) {
        mLiked = liked;
    }

    public void setChosen(boolean chosen) {
        mChosen = chosen;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public boolean isLiked() {
        return mLiked;
    }

    public boolean isChosen() {
        return mChosen;
    }

    public String getPhone() {
        return mPhone;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mAddress);
        parcel.writeByte((byte) (mIsOpen ? 1 : 0));
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeString(mWebsite);
        parcel.writeByte((byte) (mLiked ? 1 : 0));
        parcel.writeByte((byte) (mChosen ? 1 : 0));
        parcel.writeString(mPhone);
        parcel.writeString(mId);
        parcel.writeStringList(mPhotoReferences);
        parcel.writeFloat(mRating);
    }


}
