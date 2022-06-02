package com.mjcdouai.go4lunch.viewModel;

import android.graphics.Bitmap;
import android.icu.text.Transliterator;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.mjcdouai.go4lunch.remote.GoogleApi;
import com.mjcdouai.go4lunch.remote.GoogleQueryResult;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsViewModel extends ViewModel {
    private static final String KEY = "***REMOVED***";
    private final GoogleApi mGoogleApi = GoogleApi.retrofit.create(GoogleApi.class);

    public void fetchRestaurant(RestaurantViewModelCallBack callback, double centerLat, double centerLon, int radius,@Nullable String nextPage)
    {
        Call<GoogleQueryResult> call;
      if(nextPage == null) {
           call = mGoogleApi.loadRestaurantNear(centerLat + "," + centerLon,
                    radius, "restaurant", KEY);

        }
        else
        {
            call = mGoogleApi.loadNextPage(KEY,nextPage);
            Log.d("TAG", "next: " + nextPage);

        }


        Log.d("TAG", "fetchRestaurant: " + centerLat + "," + centerLon);


        final WeakReference<RestaurantViewModelCallBack> callbacksWeakReference = new WeakReference<>(callback);

        call.enqueue(new Callback<GoogleQueryResult>() {
            @Override
            public void onResponse(Call<GoogleQueryResult> call, Response<GoogleQueryResult> response) {





                callbacksWeakReference.get().onResponse(response.body());
           /* if( response.body().next_page_token != null) {
                Log.d("TAG", "onResponse: " + response.body().next_page_token);
                Log.d("TAG", "status: " + response.body().status);
                fetchRestaurant(callback, centerLat, centerLon, radius, response.body().next_page_token);
            }*/

       }

            @Override
            public void onFailure(Call<GoogleQueryResult> call, Throwable t) {
                Log.d("TAG", "onFailure: " + Log.getStackTraceString(t) );
            }
        });








    }

    public interface RestaurantViewModelCallBack {
        void onResponse(GoogleQueryResult googleQueryResult);

        void onFailure();
    }

}
