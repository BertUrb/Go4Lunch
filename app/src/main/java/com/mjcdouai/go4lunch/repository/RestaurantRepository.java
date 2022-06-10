package com.mjcdouai.go4lunch.repository;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.remote.GoogleApi;
import com.mjcdouai.go4lunch.remote.GooglePlaceDetailsResult;
import com.mjcdouai.go4lunch.remote.GoogleQueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private Location mCurrentLocation;
    private List<Location> mLoadedLocations = new ArrayList<>();
    private final GoogleApi mGoogleApi = GoogleApi.retrofit.create(GoogleApi.class);
    private static final String KEY = "***REMOVED***";
    private final List<Restaurant> mRestaurantList = new ArrayList<>();

    private static volatile RestaurantRepository instance;

    private RestaurantRepository() { }

    public static RestaurantRepository getInstance() {
        RestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new RestaurantRepository();
            }
            return instance;
        }
    }

    public MutableLiveData<List<Restaurant>> getRestaurantNearby(Location location) {
        mCurrentLocation = location;
        MutableLiveData<List<Restaurant>> mutableLiveData = new MutableLiveData<>();
        if (mLoadedLocations.contains(location)) {
            mutableLiveData.setValue(mRestaurantList);
        } else {
            Call<GoogleQueryResult> call = mGoogleApi.loadRestaurantNear(location.getLatitude() + "," + location.getLongitude(), 1500, "restaurant", KEY);

            call.enqueue(new Callback<GoogleQueryResult>() {
                @Override
                public void onResponse(Call<GoogleQueryResult> call, Response<GoogleQueryResult> response) {
                    for (GoogleQueryResult.Result result : response.body().results) {
                        Restaurant restaurant = new Restaurant(result.place_id,
                                result.name,
                                result.address,
                                result.opening_hours.open_now,
                                result.geometry.location.lat,
                                result.geometry.location.lon);

                        restaurant.setPhone("");

                        List<String> photoRefs = new ArrayList<>();
                        for (int i = 0; i < result.photos.size(); i++) {
                            photoRefs.add(result.photos.get(i).mPhotoReference);
                        }


                        restaurant.setPhotoReferences(photoRefs);
                        restaurant.setRating(result.rating);


                        mRestaurantList.add(restaurant);
                    }


                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.d("TAG", "onFailure: FAIL");
                    Log.getStackTraceString(t);

                }

            });
            mutableLiveData.setValue(mRestaurantList);
            mLoadedLocations.add(location);

        }
        return mutableLiveData;
    }

    public MutableLiveData<Restaurant> getDetails(int index) {
        MutableLiveData<Restaurant> mutableLiveData = new MutableLiveData<>();
        if (Objects.equals(mRestaurantList.get(index).getPhone(), "")) {
            Call<GooglePlaceDetailsResult> call = mGoogleApi.loadRestaurantDetails(mRestaurantList.get(index).getId(), KEY);
            call.enqueue(new Callback<GooglePlaceDetailsResult>() {
                @Override
                public void onResponse(Call<GooglePlaceDetailsResult> call, Response<GooglePlaceDetailsResult> response) {

                    String phone = response.body().result.phone_number;
                    if (!Objects.equals(phone, "")) {
                        mRestaurantList.get(index).setPhone(phone);
                    } else {
                        mRestaurantList.get(index).setPhone("none");
                    }

                    String website = response.body().result.website;
                    if (!Objects.equals(website, "")) {
                        mRestaurantList.get(index).setWebsite(website);
                    } else {
                        mRestaurantList.get(index).setWebsite("none");
                    }

                }

                @Override
                public void onFailure(Call<GooglePlaceDetailsResult> call, Throwable t) {

                }

            });
        }
        mutableLiveData.setValue(mRestaurantList.get(index));
        return mutableLiveData;
    }


}