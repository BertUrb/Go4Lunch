package com.mjcdouai.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.mjcdouai.go4lunch.databinding.ActivityHomeBinding;
import com.mjcdouai.go4lunch.ui.fragment.ListViewFragment;
import com.mjcdouai.go4lunch.ui.fragment.MapFragment;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.ui.fragment.WorkmatesFragment;
import com.mjcdouai.go4lunch.manager.UserManager;
import com.mjcdouai.go4lunch.utils.WorkmateWithRestaurantName;
import com.mjcdouai.go4lunch.viewModel.RestaurantsViewModel;
import com.mjcdouai.go4lunch.viewModel.WorkmatesViewModel;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeBinding mHomeBinding;

    private Fragment mMapFragment;
    private Fragment mListViewFragment;
    private final Fragment mWorkmatesFragment = new WorkmatesFragment();
    final FragmentManager mFragmentManager = getSupportFragmentManager();
    private Fragment mActive = mMapFragment;

    private RestaurantsViewModel mRestaurantsViewModel;


    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private final UserManager mUserManager = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> chosenRestaurantIds = new ArrayList<>();

        WorkmatesViewModel workmatesViewModel = WorkmatesViewModel.getInstance();
        workmatesViewModel.getWorkmatesWithRestaurantsNames().observe(this, workmateWithRestaurantNames -> {
            for(WorkmateWithRestaurantName workmateWithRestaurantName : workmateWithRestaurantNames ) {
                chosenRestaurantIds.add(workmateWithRestaurantName.mWorkmate.getChosenRestaurantId());

            }
            mHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
            mRestaurantsViewModel = RestaurantsViewModel.getInstance();
            mMapFragment = MapFragment.newInstance(mRestaurantsViewModel,chosenRestaurantIds);
            mActive = mMapFragment;
            mListViewFragment = ListViewFragment.newInstance(mRestaurantsViewModel);
            setContentView(mHomeBinding.getRoot());

            mFragmentManager.beginTransaction().add(R.id.main_content, mWorkmatesFragment, "3").hide(mWorkmatesFragment).commit();
            mFragmentManager.beginTransaction().add(R.id.main_content, mListViewFragment, "2").hide(mListViewFragment).commit();
            mFragmentManager.beginTransaction().add(R.id.main_content,mMapFragment, "1").commit();

            configureToolbar();
            configureDrawerLayout();
            configureNavigationView();
            updateUserInfo();

            BottomNavigationView navigation = mHomeBinding.mainBottomNavigation;
            navigation.setOnItemSelectedListener(this::onNavigationItemSelected);
        } );


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void updateUserInfo() {
        FirebaseUser user = mUserManager.getCurrentUser();

        View headerView = mNavigationView.getHeaderView(0);

        ((TextView) headerView.findViewById(R.id.user_name)).setText(user.getDisplayName());
        ((TextView) headerView.findViewById(R.id.user_mail)).setText(user.getEmail());
        setProfilePicture(user.getPhotoUrl());




    }

    private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.user_image));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_drawer_your_lunch:
                break;
            case R.id.menu_drawer_settings:
                break;
            case R.id.menu_drawer_logout:
                mUserManager.signOut(this).addOnSuccessListener(aVoid -> {
                    finish();

                });

                break;
            case R.id.action_map:
                mFragmentManager.beginTransaction().hide(mActive).show(mMapFragment).commit();
                mActive = mMapFragment;
                break;
            case R.id.action_list:
                mFragmentManager.beginTransaction().hide(mActive).show(mListViewFragment).commit();
                mActive = mListViewFragment;

                break;
            case R.id.action_workmates:
                mFragmentManager.beginTransaction().hide(mActive).show(mWorkmatesFragment).commit();
                mActive = mWorkmatesFragment;

                break;

            default:
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    private void configureToolbar()
    {
        mToolbar = mHomeBinding.homeToolbar;
        setSupportActionBar(mToolbar);
    }

    private void configureDrawerLayout(){
        mDrawerLayout =mHomeBinding.profileActivityDrawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        mNavigationView = mHomeBinding.profileActivityNavigationView;
        mNavigationView.setNavigationItemSelectedListener(this);
    }
}