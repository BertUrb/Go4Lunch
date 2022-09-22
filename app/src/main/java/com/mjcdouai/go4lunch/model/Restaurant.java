package com.mjcdouai.go4lunch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Restaurant implements Parcelable {
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
    private final String mAddress;
    private final double mLatitude;
    private final double mLongitude;
    private final String mId;
    private String mName;
    private boolean mIsOpen;
    private String mWebsite;
    private boolean mLiked;
    private boolean mChosen;
    private String mPhone;
    private boolean mDetailsLoaded;
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

    public Restaurant(String id, String name, String address, boolean isOpen, double latitude, double longitude) {
        mId = id;
        mName = name;
        mAddress = address;
        this.mIsOpen = isOpen;
        mLatitude = latitude;
        mLongitude = longitude;
        mDetailsLoaded = false;
    }

    public String getId() {
        return mId;
    }

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

    public boolean getDetailsLoaded() {
        return mDetailsLoaded;
    }

    public void setDetailsLoaded(boolean detailsLoaded) {
        mDetailsLoaded = detailsLoaded;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public void setOpen(boolean open) {
        mIsOpen = open;
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

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public boolean isLiked() {
        return mLiked;
    }

    public void setLiked(boolean liked) {
        mLiked = liked;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
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

    @NonNull
    @Override
    public String toString() {
        return "Restaurant{" +
                "mName='" + mName + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mIsOpen=" + mIsOpen +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mWebsite='" + mWebsite + '\'' +
                ", mLiked=" + mLiked +
                ", mChosen=" + mChosen +
                ", mPhone='" + mPhone + '\'' +
                ", mId='" + mId + '\'' +
                ", mPhotoReferences=" + mPhotoReferences +
                ", mRating=" + mRating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return mId.equals(that.mId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId);
    }
}
