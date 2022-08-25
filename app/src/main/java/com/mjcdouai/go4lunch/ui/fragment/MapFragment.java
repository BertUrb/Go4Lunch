package com.mjcdouai.go4lunch.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.FragmentMapBinding;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.ui.RestaurantDetailsActivity;
import com.mjcdouai.go4lunch.utils.LocationHelper;
import com.mjcdouai.go4lunch.utils.SharedPrefsHelper;
import com.mjcdouai.go4lunch.viewModel.RestaurantsViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements Marker.OnMarkerClickListener {

    private MapView mMap;
    private IMapController mMapController;
    private Location mLocation;
    private final List<GeoPoint> mLoadedLocations = new ArrayList<>();
    private static List<String> mChosenRestaurantIds;



    private RestaurantsViewModel mRestaurantsViewModel;

    public MapFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance(RestaurantsViewModel restaurantsViewModel, List<String> chosenRestaurantIds) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.mRestaurantsViewModel = restaurantsViewModel;
        mChosenRestaurantIds = chosenRestaurantIds;

        return mapFragment;
    }

    public Location getLocation() {
        return mLocation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context ctx = getActivity();

        LocationHelper locationHelper = new LocationHelper(this.getActivity(), requireContext());

        locationHelper.getLocationLiveData().observe(getViewLifecycleOwner(), this::locationObserver);
        mLocation = locationHelper.getLocation();
        int radius = new SharedPrefsHelper(requireContext()).getRadius();
        mRestaurantsViewModel.loadRestaurantNearby(mLocation,radius).observe(getViewLifecycleOwner(), this::getRestaurantObserver);

        GeoPoint startPosition = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());

        com.mjcdouai.go4lunch.databinding.FragmentMapBinding mapBinding = FragmentMapBinding.inflate(inflater, container, false);

        View view = mapBinding.getRoot();

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        mMap = mapBinding.map;
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);


        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(Objects.requireNonNull(ctx)), mMap);
        locationNewOverlay.enableMyLocation();
        mMap.getOverlays().add(locationNewOverlay);

        mMapController = mMap.getController();
        mMapController.setZoom(17.0);

        createChosenRestaurantList();



        mMapController.setCenter(startPosition);


        FloatingActionButton fab = mapBinding.fabCenterView;
        fab.setOnClickListener(this::onFabClick);




        return view;
    }

    private void createChosenRestaurantList() {

    }

    private void locationObserver(Location location) {
        mLocation = location;
        GeoPoint geoPoint = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());

        if (!mLoadedLocations.contains(geoPoint)) {

            mLoadedLocations.add(geoPoint);
            int radius = new SharedPrefsHelper(requireContext()).getRadius();
            mRestaurantsViewModel.loadRestaurantNearby(mLocation,radius).observe(getViewLifecycleOwner(), this::getRestaurantObserver);

        }




    }


    public void getRestaurantObserver(List<Restaurant> restaurantList) {
        mMap.getOverlays().clear();
        if (restaurantList.size() != 0) {
            for (Restaurant restaurant : restaurantList) {

                addMarker(restaurant);
            }
        } else {
            mLoadedLocations.remove(new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude()));
        }

    }

    public int getWorkmateCountIn(String restaurantId) {
        return Collections.frequency(mChosenRestaurantIds, restaurantId);
    }


    void addMarker(Restaurant restaurant) {

        MyMarker marker = new MyMarker(mMap);
        marker.setPosition(new GeoPoint(restaurant.getLatitude(), restaurant.getLongitude()));
        marker.setTitle(restaurant.getName());
        marker.setSubDescription(restaurant.getAddress());
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setId(restaurant.getId());
        marker.setOnMarkerClickListener(this);


        Drawable d = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_noun_restaurant_map_marker_24);
        if (getWorkmateCountIn(restaurant.getId()) > 0 ) {
            Objects.requireNonNull(d).setTint(Color.GREEN);
        } else {
            Objects.requireNonNull(d).setTint(Color.RED);
        }
        marker.setIcon(d);
        mMap.getOverlays().add(marker);
    }


    private void onFabClick(View v) {
        GeoPoint geoPoint = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
        mMapController.animateTo(geoPoint);
        // mRestaurantsViewModel.loadRestaurantNearby(mLocation).observe(getViewLifecycleOwner(), this::getRestaurantObserver);
    }


    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        Intent restaurantDetails = new Intent(getContext(), RestaurantDetailsActivity.class);
        mRestaurantsViewModel.loadRestaurantDetails(marker.getId()).observe(getViewLifecycleOwner(), restaurant -> {
            Log.d("TAG", "onMarkerClick: rating " + restaurant.getRating());
            restaurantDetails.putExtra("Restaurant", restaurant);
            GeoPoint geoPoint = new GeoPoint(restaurant.getLatitude(),restaurant.getLongitude());
            mMapController.animateTo(geoPoint);
            startActivity(restaurantDetails);
        });
        return false;
    }

    private static class MyMarker extends Marker {

        public MyMarker(MapView mapView) {
            super(mapView);
        }

        @Override
        public boolean onLongPress(MotionEvent event, MapView mapView) {
            showInfoWindow();
            return super.onLongPress(event, mapView);


        }
    }
    public void moveTo(LatLng moveTo) {
        GeoPoint geoPoint = new GeoPoint(moveTo.latitude,moveTo.longitude);
        mMapController.animateTo(geoPoint);
    }
}