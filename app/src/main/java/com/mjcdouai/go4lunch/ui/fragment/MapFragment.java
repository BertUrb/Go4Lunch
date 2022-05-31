package com.mjcdouai.go4lunch.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.FragmentMapBinding;
import com.mjcdouai.go4lunch.remote.GoogleQueryResult;
import com.mjcdouai.go4lunch.remote.OverpassApi;
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

import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements LocationListener, RestaurantsViewModel.RestaurantViewModelCallBack {

    private MapView mMap;
    private IMapController mMapController;
    private LocationManager mLocationManager;
    private Location mLocation;
    private FloatingActionButton mFab;
    private FragmentMapBinding mMapBinding;

    RestaurantsViewModel.RestaurantViewModelCallBack callBack = this;

    private RestaurantsViewModel mRestaurantsViewModel;

    final private OverpassApi mOverpassApi = OverpassApi.retrofit.create(OverpassApi.class);

    public MapFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance() {

        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context ctx = getActivity();

        mMapBinding = FragmentMapBinding.inflate(inflater,container,false);

        mRestaurantsViewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);
        View view = mMapBinding.getRoot();

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        mMap = mMapBinding.map;
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);

        mMap.addMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                Log.d("tag", "onScroll: ");
                mRestaurantsViewModel.fetchRestaurant(callBack,mMap.getMapCenter().getLatitude(),mMap.getMapCenter().getLongitude(),1000,null);
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                return false;
            }
        },1000));



        assert ctx != null;
        mLocationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), mMap);
        locationNewOverlay.enableMyLocation();
        mMap.getOverlays().add(locationNewOverlay);

        mMapController = mMap.getController();
        mMapController.setZoom(15.0);

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

            EasyPermissions.requestPermissions(this, "test", 55, perms);

        }
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (mLocation == null) {
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (mLocation == null) {
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);

        double lat = 48.856614;
        double lg = 2.3522219;


        if (mLocation != null) {
            lat = mLocation.getLatitude();
            lg = mLocation.getLongitude();
        }

        GeoPoint startPosition = new GeoPoint(lat, lg);

        mMapController.setCenter(startPosition);
        mRestaurantsViewModel.fetchRestaurant(this,lat,lg,1000,null);

        mFab = mMapBinding.fabCenterView;
        mFab.setOnClickListener(this::onFabClick);


        return view;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;

    }

    void addMarker(GoogleQueryResult.Result restaurant) {
        if(Objects.equals(restaurant.business_status, "OPERATIONAL")) {
            Log.d("TAG", "addMarker: " + restaurant.name);
            Marker marker = new Marker(mMap);
            marker.setPosition(new GeoPoint(restaurant.geometry.location.lat, restaurant.geometry.location.lon));
            marker.setTitle(restaurant.name);
            marker.setSubDescription(restaurant.address);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.showInfoWindow();
            Drawable d = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_place_24);
            marker.setIcon(d);
            mMap.getOverlays().add(marker);

            mMap.invalidate();
        }
    }


    private void onFabClick(View v) {
        GeoPoint geoPoint = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
        mMapController.animateTo(geoPoint);
        mRestaurantsViewModel.fetchRestaurant(callBack,mLocation.getLatitude(),mLocation.getLongitude(),1000,null);

    }
    @Override
    public void onResponse(GoogleQueryResult result) {

        Log.d("TAG", "Map fragment onResponse: " + result.status);


        for(GoogleQueryResult.Result res : result.results)
        {
            addMarker(res);
        }

    }

    @Override
    public void onFailure() {
        Log.d("TAG", "onFailure: FAIL");

    }
}