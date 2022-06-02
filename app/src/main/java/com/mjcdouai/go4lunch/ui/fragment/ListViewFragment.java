package com.mjcdouai.go4lunch.ui.fragment;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.FragmentListViewBinding;
import com.mjcdouai.go4lunch.databinding.FragmentMapBinding;
import com.mjcdouai.go4lunch.remote.GoogleQueryResult;
import com.mjcdouai.go4lunch.ui.RestaurantRecyclerviewAdapter;
import com.mjcdouai.go4lunch.utils.LocationHelper;
import com.mjcdouai.go4lunch.viewModel.RestaurantsViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment  implements RestaurantsViewModel.RestaurantViewModelCallBack{

    private FragmentListViewBinding mBinding;
    private RestaurantsViewModel mRestaurantsViewModel;
    private List<GoogleQueryResult.Result> mRestaurantList;
    private LocationHelper mLocationHelper;
    private Location mLocation;

    RestaurantsViewModel.RestaurantViewModelCallBack callBack = this;

    public ListViewFragment() {
        // Required empty public constructor
    }

    public static ListViewFragment newInstance() {
        ListViewFragment fragment = new ListViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationHelper = new LocationHelper((Activity)getHost(),getContext());
        mLocation = mLocationHelper.getLocation();





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentListViewBinding.inflate(inflater,container,false);
        View view = mBinding.getRoot();
        mRestaurantsViewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);
        mRestaurantsViewModel.fetchRestaurant(callBack,mLocation.getLatitude(),mLocation.getLongitude(),1000,null);


        return view;
    }

    @Override
    public void onResponse(GoogleQueryResult result) {
        Log.d("tag", "onResponse List Frag: ");
        mRestaurantList = result.results;
        mBinding.recyclerview.setAdapter(new RestaurantRecyclerviewAdapter(result));

    }

    @Override
    public void onFailure() {

    }

    public String getDistanceBetweenMeAndLocation(Location location)
    {
        return Math.round(mLocation.distanceTo(location)) + " m";
    }
}