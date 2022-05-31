package com.mjcdouai.go4lunch.ui.main.data.remote;

import android.graphics.Bitmap;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApi {
    @GET("search/json")
    Call<GoogleQueryResult>  loadRestaurantNear(@Query("location") String location,
                                                @Query("radius") int radius,
                                                @Query("type") String type,
                                                @Query("key") String apiKey);


    @GET("nearbysearch/json")
    Call<GoogleQueryResult>  loadNextPage(@Query("key") String apiKey,
                                          @Query("pageToken") String nextPageToken);
    @GET("place/photo")
    Bitmap loadPhoto(@Query("photo_reference") String photoReference,
                     @Query("key") String apiKey);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
