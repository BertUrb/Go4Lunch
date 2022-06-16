package com.mjcdouai.go4lunch.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.FragmentWorkmatesBinding;
import com.mjcdouai.go4lunch.repository.WorkmatesRepository;
import com.mjcdouai.go4lunch.ui.WorkmatesAdapter;
import com.mjcdouai.go4lunch.utils.WorkmateWithRestaurantName;
import com.mjcdouai.go4lunch.viewModel.WorkmatesViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkmatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkmatesFragment extends Fragment {
    FragmentWorkmatesBinding mBinding;
    WorkmatesViewModel mWorkmatesViewModel = WorkmatesViewModel.getInstance();


    public WorkmatesFragment() {
        // Required empty public constructor
    }

      public static WorkmatesFragment newInstance() {

        return new WorkmatesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentWorkmatesBinding.inflate(inflater,container,false);


        return mBinding.getRoot();

    }
}