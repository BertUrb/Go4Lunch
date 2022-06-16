package com.mjcdouai.go4lunch.ui.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.mjcdouai.go4lunch.callback.OnClickRestaurantListener;
import com.mjcdouai.go4lunch.databinding.FragmentListViewBinding;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.repository.WorkmatesRepository;
import com.mjcdouai.go4lunch.ui.RestaurantDetailsActivity;
import com.mjcdouai.go4lunch.ui.RestaurantRecyclerviewAdapter;
import com.mjcdouai.go4lunch.utils.LocationHelper;
import com.mjcdouai.go4lunch.utils.WorkmateWithRestaurantName;
import com.mjcdouai.go4lunch.viewModel.RestaurantsViewModel;
import com.mjcdouai.go4lunch.viewModel.WorkmatesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment implements OnClickRestaurantListener {

    private FragmentListViewBinding mBinding;
    private RestaurantsViewModel mRestaurantsViewModel;
    private List<Restaurant> mRestaurantList;
    private List<String> mChosenRestaurantIds;
    private LocationHelper mLocationHelper;
    private Location mLocation;
    private RestaurantRecyclerviewAdapter mRestaurantRecyclerviewAdapter;
    private final WorkmatesViewModel mWorkmatesViewModel = WorkmatesViewModel.getInstance();

    public ListViewFragment() {
        // Required empty public constructor
    }

    public static ListViewFragment newInstance(RestaurantsViewModel restaurantsViewModel) {
        ListViewFragment listViewFragment = new ListViewFragment();
        listViewFragment.mRestaurantsViewModel = restaurantsViewModel;
        return listViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentListViewBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mLocationHelper = new LocationHelper(getActivity(), getContext());
        mLocation = mLocationHelper.getLocation();

        OnClickRestaurantListener listener = this;
        mChosenRestaurantIds = new ArrayList<>();

        mWorkmatesViewModel.getWorkmatesWithRestaurantsNames().observe(getViewLifecycleOwner(), workmateWithRestaurantNames -> {
            for(WorkmateWithRestaurantName workmateWithRestaurantName : workmateWithRestaurantNames ) {
                mChosenRestaurantIds.add(workmateWithRestaurantName.mWorkmate.getChosenRestaurantId());
            }
        } );


        mRestaurantsViewModel.loadRestaurantNearby(mLocation).observe(getViewLifecycleOwner(), restaurants -> {
            mRestaurantList = restaurants;


            mRestaurantRecyclerviewAdapter = new RestaurantRecyclerviewAdapter(
                    mRestaurantList,mChosenRestaurantIds, listener);
            mBinding.recyclerview.setAdapter(mRestaurantRecyclerviewAdapter);
        });

        mLocationHelper.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
            Log.d("TAG", "onLocationChanged: ");
            if(mRestaurantRecyclerviewAdapter != null) {
                mRestaurantRecyclerviewAdapter.setLocation(location);
            }
            mLocation = location;

        });
        return view;
    }


    @Override
    public void onRestaurantClick(int position) {
        Intent restaurantDetails = new Intent(getContext(), RestaurantDetailsActivity.class);
        mRestaurantsViewModel.loadRestaurantDetails(position).observe(getViewLifecycleOwner(), restaurant -> {
            restaurantDetails.putExtra("Restaurant", restaurant);
            startActivity(restaurantDetails);
        });
    }


}

