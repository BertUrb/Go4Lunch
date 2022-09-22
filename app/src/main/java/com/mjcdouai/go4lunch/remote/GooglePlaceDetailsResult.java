package com.mjcdouai.go4lunch.remote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GooglePlaceDetailsResult {
    @SerializedName("result")
    public Result result = new Result();

    public static class Result {
        @SerializedName("international_phone_number")
        public String phone_number;

        @SerializedName("name")
        public String name;

        @SerializedName("rating")
        public float rating;

        @SerializedName("formatted_address")
        public String address;

        @SerializedName("website")
        public String website;

        @SerializedName("geometry")
        public Geometry geometry = new Geometry();
        @SerializedName("photos")
        public List<Photos> photos = new ArrayList<>();

        public static class Geometry {

            @SerializedName("location")
            public Location location = new Location();

            public static class Location {
                @SerializedName("lat")
                public double lat;
                @SerializedName("lng")
                public double lng;
            }
        }

        public static class Photos {
            @SerializedName("photo_reference")
            public String photo_reference;

        }
    }
}
