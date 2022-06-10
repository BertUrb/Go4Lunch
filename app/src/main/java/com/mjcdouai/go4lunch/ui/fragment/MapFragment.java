package com.mjcdouai.go4lunch.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.FragmentMapBinding;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.utils.LocationHelper;
import com.mjcdouai.go4lunch.viewModel.RestaurantsViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    private MapView mMap;
    private IMapController mMapController;
    private LocationHelper mLocationHelper;
    private Location mLocation;
    private FloatingActionButton mFab;
    private FragmentMapBinding mMapBinding;




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
    public static MapFragment newInstance(RestaurantsViewModel restaurantsViewModel) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.mRestaurantsViewModel = restaurantsViewModel;

        return mapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context ctx = getActivity();

        mLocationHelper = new LocationHelper(this.getActivity(),getContext());
        mLocation=  mLocationHelper.getLocation();

        GeoPoint startPosition = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());

        mRestaurantsViewModel.loadRestaurantNearby(mLocation).observe(getViewLifecycleOwner(), this::getRestaurantObserver);
        mMapBinding = FragmentMapBinding.inflate(inflater,container,false);

        View view = mMapBinding.getRoot();

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        mMap = mMapBinding.map;
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);

        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), mMap);
        locationNewOverlay.enableMyLocation();
        mMap.getOverlays().add(locationNewOverlay);

        mMapController = mMap.getController();
        mMapController.setZoom(15.0);

        mMapController.setCenter(startPosition);


              mFab = mMapBinding.fabCenterView;
        mFab.setOnClickListener(this::onFabClick);

        return view;
    }


    public void getRestaurantObserver(List<Restaurant> restaurantList)
    {
        for (Restaurant restaurant: restaurantList) {
            addMarker(restaurant);

        }

    }


    void addMarker(Restaurant restaurant) {

            Log.d("TAG", "addMarker: " + restaurant.getName());
            Marker marker = new Marker(mMap);
            marker.setPosition(new GeoPoint(restaurant.getLatitude(), restaurant.getLongitude()));
            marker.setTitle(restaurant.getName());
            marker.setSubDescription(restaurant.getAddress());
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.showInfoWindow();
            Drawable d = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_place_24);
            marker.setIcon(d);
            mMap.getOverlays().add(marker);

            mMap.invalidate();

    }


    private void onFabClick(View v) {
        mLocation = mLocationHelper.getLocation();
        GeoPoint geoPoint = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
        mMapController.animateTo(geoPoint);
        mRestaurantsViewModel.loadRestaurantNearby(mLocation).observe(getViewLifecycleOwner(), this::getRestaurantObserver);
    }

}