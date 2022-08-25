package com.mjcdouai.go4lunch.repository;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mjcdouai.go4lunch.BuildConfig;
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
    private final GoogleApi mGoogleApi = GoogleApi.retrofit.create(GoogleApi.class);
    private static final String KEY = BuildConfig.GMAP_API_KEY;
    private final List<Restaurant> mRestaurantList = new ArrayList<>();
    MutableLiveData<List<Restaurant>> mutableLiveData = new MutableLiveData<>();

    private static volatile RestaurantRepository instance;

    private RestaurantRepository() {
    }

    public void likeRestaurant(Restaurant restaurant) {
        mRestaurantList.get(mRestaurantList.indexOf(restaurant)).setLiked(true);
        Log.d("tag", "likeRestaurant: true");
    }

    public void unlikeRestaurant(Restaurant restaurant) {
        mRestaurantList.get(mRestaurantList.indexOf(restaurant)).setLiked(false);
        Log.d("tag", "likeRestaurant: false");
    }

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

    public MutableLiveData<List<Restaurant>> getRestaurantNearby(Location location, int radius) {


        Call<GoogleQueryResult> call = mGoogleApi.loadRestaurantNear(location.getLatitude() + "," + location.getLongitude(), radius, "restaurant", KEY);

        call.enqueue(new Callback<GoogleQueryResult>() {
            @Override
            public void onResponse(Call<GoogleQueryResult> call, Response<GoogleQueryResult> response) {
                Log.d("TAG", "onResponse: API");
                mRestaurantList.clear();
                for (GoogleQueryResult.Result result : response.body().results) {
                    Log.d("TAG", "onResponse: " + result.name);
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

                    restaurant.setLiked(WorkmatesRepository.getInstance().isFavoriteRestaurant(restaurant.getId()));
                    Log.d("TAG", "LIKED ?  " + restaurant.isLiked());

                    mRestaurantList.add(restaurant);
                }
                mutableLiveData.setValue(mRestaurantList);
                /*if (!Objects.equals(response.body().next_page_token, null)) {
                    getNextPageResults(response.body().next_page_token);
                }*/


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("TAG", "onFailure: FAIL");
                Log.getStackTraceString(t);

            }

        });


        return mutableLiveData;
    }

    public void getNextPageResults(String pageToken) {
        Log.d("TAG", "getNextPageResults: " + pageToken);
        Call<GoogleQueryResult> call = mGoogleApi.loadNextPage(pageToken, KEY);

        call.enqueue(new Callback<GoogleQueryResult>() {
            @Override
            public void onResponse(Call<GoogleQueryResult> call, Response<GoogleQueryResult> response) {
                if (response.body().status.equals("INVALID_REQUEST")) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Log.getStackTraceString(e);
                    }
                    getNextPageResults(pageToken);
                }
                for (GoogleQueryResult.Result result : response.body().results) {
                    Log.d("TAG", "nextpage: " + result.name);
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

                    restaurant.setLiked(WorkmatesRepository.getInstance().isFavoriteRestaurant(restaurant.getId()));
                    Log.d("TAG", "LIKED ?  " + restaurant.isLiked());

                    mRestaurantList.add(restaurant);
                }
                if (!Objects.equals(response.body().next_page_token, null)) {
                    getNextPageResults(response.body().next_page_token);
                }
                mutableLiveData.setValue(mRestaurantList);

            }

            @Override
            public void onFailure(Call<GoogleQueryResult> call, Throwable t) {
                Log.d("TAG", "onFailure: FAIL");
                Log.getStackTraceString(t);
            }
        });
    }


    public MutableLiveData<Restaurant> getDetails(String id) {
        MutableLiveData<Restaurant> mutableLiveData = new MutableLiveData<>();
        Call<GooglePlaceDetailsResult> call = mGoogleApi.loadRestaurantDetails(id, KEY);
        boolean next = true;
        int index = getTabIndex(id);
        if (index > 0) {
            if (mRestaurantList.get(index).getDetailsLoaded()) {
                mutableLiveData.setValue(mRestaurantList.get(index));
                next = false;
            }
        }
        if (next) {

            call.enqueue(new Callback<GooglePlaceDetailsResult>() {

                @Override
                public void onResponse(Call<GooglePlaceDetailsResult> call, Response<GooglePlaceDetailsResult> response) {


                    Restaurant restaurant = new Restaurant(id,
                            response.body().result.name,
                            response.body().result.address,
                            true,
                            response.body().result.geometry.location.lat,
                            response.body().result.geometry.location.lng);

                    List<String> refs = new ArrayList<>();
                    for(GooglePlaceDetailsResult.Result.Photos photo : response.body().result.photos) {
                        refs.add(photo.photo_reference);
                    }
                    restaurant.setPhotoReferences(refs);

                    restaurant.setWebsite(response.body().result.website);
                    restaurant.setRating(response.body().result.rating);
                    restaurant.setDetailsLoaded(true);
                    restaurant.setPhone(response.body().result.phone_number);


                    if (index > 0) {
                        restaurant.setOpen(mRestaurantList.get(index).isOpen());
                        mRestaurantList.set(index, restaurant);
                    }

                    mutableLiveData.setValue(restaurant);


                }

                @Override
                public void onFailure(Call<GooglePlaceDetailsResult> call, Throwable t) {

                }

            });
        }


        return mutableLiveData;
    }

    public int getTabIndex(String restaurantId) {
        Restaurant restaurant = new Restaurant(restaurantId, "nom", "adresse", true, 10, 20);


        return mRestaurantList.indexOf(restaurant);
    }

}
