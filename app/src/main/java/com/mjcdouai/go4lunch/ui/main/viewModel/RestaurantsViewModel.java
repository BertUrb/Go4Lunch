package com.mjcdouai.go4lunch.ui.main.viewModel;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.mjcdouai.go4lunch.ui.main.data.remote.GoogleApi;
import com.mjcdouai.go4lunch.ui.main.data.remote.GoogleQueryResult;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsViewModel extends ViewModel {
    private static final String KEY = "***REMOVED***";
    private final GoogleApi mGoogleApi = GoogleApi.retrofit.create(GoogleApi.class);

    boolean first = true;

    public void fetchRestaurant(RestaurantViewModelCallBack callback, double centerLat, double centerLon, int radius,@Nullable String nextPage)
    {

        Call<GoogleQueryResult> call;
        if(nextPage == null && first) {
           call = mGoogleApi.loadRestaurantNear(centerLat + "," + centerLon,
                    radius, "restaurant", KEY);
           first = false;

        }
        else
        {
            call = mGoogleApi.loadRestaurantNear(centerLat + "," + centerLon,
                    radius, "restaurant", KEY,nextPage);
            Log.d("TAG", "fetchRestaurant: " + nextPage);
        }
        Log.d("TAG", "fetchRestaurant: " + centerLat + "," + centerLon);

        final WeakReference<RestaurantViewModelCallBack> callbacksWeakReference = new WeakReference<>(callback);

        call.enqueue(new Callback<GoogleQueryResult>() {
            @Override
            public void onResponse(Call<GoogleQueryResult> call, Response<GoogleQueryResult> response) {
                callbacksWeakReference.get().onResponse(response.body());
                if(!Objects.equals(response.body().next_page_token, ""))
                {
                    fetchRestaurant(callback,centerLat,centerLon,radius,response.body().next_page_token);


                }


            }

            @Override
            public void onFailure(Call<GoogleQueryResult> call, Throwable t) {

            }
        });



    }
    public interface RestaurantViewModelCallBack {
        void onResponse(GoogleQueryResult result);

        void onFailure();
    }

}
