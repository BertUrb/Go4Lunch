package com.mjcdouai.go4lunch.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GooglePlaceDetailsResult {
    @SerializedName("result")
    public Result result = new Result();

    public static class Result  {
        @SerializedName("international_phone_number")
        public String phone_number;

        @SerializedName("website")
        public String website;

        @SerializedName("opening_hours")
        public OpeningHours openingHours = new OpeningHours();

        public static class OpeningHours{
            @SerializedName("periods")
            public List<Period> periods;

            public static class Period{
                @SerializedName("close")
                public Close close = new Close();

                public static class Close {
                    @SerializedName("day")
                    public int day;

                    @SerializedName("time")
                    public String time;
                }
            }

        }
    }
}
