package com.mjcdouai.go4lunch.ui.main.fragment;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.ui.main.data.remote.OverpassApi;
import com.mjcdouai.go4lunch.ui.main.data.remote.OverpassQueryResult;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment  implements LocationListener {

    private MapView mMap;
    private IMapController mMapController;
    private LocationManager mLocationManager;
    private Location mLocation;
    private FloatingActionButton mFab;

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

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        mMap = view.findViewById(R.id.map);
        mMap.setTileSource(TileSourceFactory.MAPNIK);


        assert ctx != null;
        mLocationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),mMap);
        locationNewOverlay.enableMyLocation();
        mMap.getOverlays().add(locationNewOverlay);

        mMapController = mMap.getController();
        mMapController.setZoom(15.0);

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

            EasyPermissions.requestPermissions(this,"test",55,perms);

        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);

        double lat = 48.856614;
        double lg = 2.3522219;

        if(mLocation != null)
        {
            lat = mLocation.getLatitude();
            lg = mLocation.getLongitude();
        }

        GeoPoint startPosition = new GeoPoint(lat,lg);

        mMapController.setCenter(startPosition);

        mFab = view.findViewById(R.id.fab_center_view);
        mFab.setOnClickListener(v-> {
            GeoPoint geoPoint = new GeoPoint(mLocation.getLatitude(),mLocation.getLongitude());
            mMapController.animateTo(geoPoint);
            String query ="[out:json];nwr[amenity=restaurant](around:1000,"+ mLocation.getLatitude() + "," + mLocation.getLongitude()+"); out;";
            Call<OverpassQueryResult> call = mOverpassApi.loadRestaurantNear(query);

            call.enqueue(new Callback<OverpassQueryResult>() {

                @Override
                public void onResponse(Call<OverpassQueryResult> call, Response<OverpassQueryResult> response) {
                    Log.d("TAG", "onResponse: OKKKK Throwable t");
                    for (OverpassQueryResult.Element element : response.body().elements
                         ) {

                        addMarker(element);

                    }

                }

                @Override
                public void onFailure(Call<OverpassQueryResult> call, Throwable t) {
                    Log.d("TAG", "onResponse: raté ");
                }
            });

        });





        return view;
    }
    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;

    }

    void addMarker(OverpassQueryResult.Element restaurant)
    {
        Log.d("TAG", "addMarker: " + restaurant.lat);
        Marker marker = new Marker(mMap);
        marker.setPosition(new GeoPoint(restaurant.lat,restaurant.lon));
        marker.setTitle(restaurant.tags.name);
        marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        marker.showInfoWindow();
        Drawable d = AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_place_24);
        marker.setIcon(d);
        mMap.getOverlays().add(marker);

        mMap.invalidate();
    }



}