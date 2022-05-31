package com.mjcdouai.go4lunch.ui.main.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GoogleQueryResult {
    @SerializedName("results")
    public List<Result> results = new ArrayList();

    @SerializedName("next_page_token")
    public String next_page_token;

    @SerializedName("status")
    public String status;


    public static class Result {

        @SerializedName("business_status")
        public String business_status;

        @SerializedName("name")
        public String name;

        @SerializedName("rating")
        public float rating;

        @SerializedName("vicinity")
        public String address;

        @SerializedName("geometry")
        public Geometry geometry = new Geometry();

        public static class Geometry {
            @SerializedName("location")
            public Location location = new Location();
            public static class Location {
                @SerializedName("lat")
                public double lat;

                @SerializedName("lng")
                public double lon;
            }
        }
    }

    public static class Photo {
        @SerializedName("photo_reference")
        public String mPhotoReference;
    }

}
