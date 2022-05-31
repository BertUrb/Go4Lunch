package com.mjcdouai.go4lunch.remote;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OverpassApi {
    @GET("api/interpreter")
    Call<OverpassQueryResult> loadRestaurantNear(@Query(value="data") String data);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://overpass-api.de/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
