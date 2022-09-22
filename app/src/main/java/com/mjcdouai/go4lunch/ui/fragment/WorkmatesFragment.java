package com.mjcdouai.go4lunch.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mjcdouai.go4lunch.databinding.FragmentWorkmatesBinding;
import com.mjcdouai.go4lunch.ui.WorkmatesAdapter;
import com.mjcdouai.go4lunch.viewModel.WorkmatesViewModel;

public class WorkmatesFragment extends Fragment {
    FragmentWorkmatesBinding mBinding;
    WorkmatesViewModel mWorkmatesViewModel = WorkmatesViewModel.getInstance();


    public WorkmatesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
        {
            mWorkmatesViewModel.getAllWorkmatesWithRestaurantNames()
                    .observe(getViewLifecycleOwner(),
                            workmateWithRestaurantNames -> mBinding.workmatesRecyclerview.setAdapter(new WorkmatesAdapter(workmateWithRestaurantNames)));

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentWorkmatesBinding.inflate(inflater,container,false);


        return mBinding.getRoot();

    }
}