package com.mjcdouai.go4lunch.remote;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApi {
    @GET("nearbysearch/json")
    Call<GoogleQueryResult>  loadRestaurantNear(@Query("location") String location,
                                                @Query("radius") int radius,
                                                @Query("type") String type,
                                                @Query("key") String apiKey);


    static String getImageUrl(String imageReference,String apiKey) {
        return "https://maps.googleapis.com/maps/api/place/photo" +
                "?maxwidth=400" +
                "&photoreference=" + imageReference +
                "&key=" + apiKey;
    }

    @GET("details/json")
    Call<GooglePlaceDetailsResult> loadRestaurantDetails(@Query("place_id") String placeId,
                                                         @Query("key") String apiKey);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
