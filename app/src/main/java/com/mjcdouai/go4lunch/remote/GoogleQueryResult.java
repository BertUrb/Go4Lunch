package com.mjcdouai.go4lunch.remote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GoogleQueryResult {
    @SerializedName("results")
    public List<Result> results = new ArrayList<>();

    @SerializedName("next_page_token")
    public String next_page_token;

    @SerializedName("status")
    public String status;


    public static class Result {

        @SerializedName("name")
        public String name;

        @SerializedName("rating")
        public float rating;

        @SerializedName("vicinity")
        public String address;

        @SerializedName("place_id")
        public String place_id;

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
        @SerializedName("opening_hours")
        public OpeningHours opening_hours = new OpeningHours();

        public static class OpeningHours {
            @SerializedName("open_now")
            public boolean open_now;

        }
        @SerializedName("photos")
        public List<Photo> photos = new ArrayList<>();

        public static class Photo {
            @SerializedName("photo_reference")
            public String mPhotoReference;
        }
    }



}
