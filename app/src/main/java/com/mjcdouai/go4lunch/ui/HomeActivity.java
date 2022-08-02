package com.mjcdouai.go4lunch.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.mjcdouai.go4lunch.BuildConfig;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.ActivityHomeBinding;
import com.mjcdouai.go4lunch.manager.UserManager;
import com.mjcdouai.go4lunch.remote.MyFirebaseMessagingService;
import com.mjcdouai.go4lunch.ui.fragment.ListViewFragment;
import com.mjcdouai.go4lunch.ui.fragment.MapFragment;
import com.mjcdouai.go4lunch.ui.fragment.WorkmatesFragment;
import com.mjcdouai.go4lunch.utils.SharedPrefsHelper;
import com.mjcdouai.go4lunch.utils.WorkmateWithRestaurantName;
import com.mjcdouai.go4lunch.viewModel.RestaurantsViewModel;
import com.mjcdouai.go4lunch.viewModel.WorkmatesViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

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
    Intent mAutoCompleteIntent;

    ActivityResultLauncher<Intent> mStartForResult;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Location location = ((MapFragment) mMapFragment).getLocation();
        int radius = new SharedPrefsHelper(getBaseContext()).getRadius();
        RectangularBounds rectangularBounds = RectangularBounds.newInstance(getCoordinate(location.getLatitude(), location.getLongitude(), radius * -1, radius * -1),
                getCoordinate(location.getLatitude(), location.getLongitude(), radius, radius));
        Log.d("TAG", "onOptionsItemSelected: " + rectangularBounds);

        mAutoCompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                .setLocationRestriction(rectangularBounds)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(this);
        mStartForResult.launch(mAutoCompleteIntent);


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> chosenRestaurantIds = new ArrayList<>();

        Places.initialize(getBaseContext(), BuildConfig.GMAP_API_KEY);


        if(new SharedPrefsHelper(getBaseContext()).getNotification()) {
            scheduleNotification();

        }else {
            cancelNotification();
        }


        WorkmatesViewModel workmatesViewModel = WorkmatesViewModel.getInstance();
        workmatesViewModel.getWorkmatesWithRestaurantsNames().observe(this, workmateWithRestaurantNames -> {
            for (WorkmateWithRestaurantName workmateWithRestaurantName : workmateWithRestaurantNames) {
                chosenRestaurantIds.add(workmateWithRestaurantName.mWorkmate.getChosenRestaurantId());

            }
            mHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
            mRestaurantsViewModel = RestaurantsViewModel.getInstance();
            mMapFragment = MapFragment.newInstance(mRestaurantsViewModel, chosenRestaurantIds);
            mActive = mMapFragment;
            mListViewFragment = ListViewFragment.newInstance();
            setContentView(mHomeBinding.getRoot());

            mFragmentManager.beginTransaction().add(R.id.main_content, mWorkmatesFragment, "3").hide(mWorkmatesFragment).commit();
            mFragmentManager.beginTransaction().add(R.id.main_content, mListViewFragment, "2").hide(mListViewFragment).commit();
            mFragmentManager.beginTransaction().add(R.id.main_content, mMapFragment, "1").commit();

            configureToolbar();
            configureDrawerLayout();
            configureNavigationView();
            updateUserInfo();

            BottomNavigationView navigation = mHomeBinding.mainBottomNavigation;
            navigation.setOnItemSelectedListener(this::onNavigationItemSelected);
        });

        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent1 = result.getData();
                        assert intent1 != null;
                        Place place = Autocomplete.getPlaceFromIntent(intent1);

                        if(mActive == mMapFragment){
                            ((MapFragment) mMapFragment).moveTo(Objects.requireNonNull(place.getLatLng()));
                        }
                        else if (mActive == mListViewFragment) {
                            mRestaurantsViewModel.loadRestaurantDetails(place.getId()).observe(this, restaurant -> {
                                Intent restaurantDetails = new Intent(getBaseContext(), RestaurantDetailsActivity.class);
                                restaurantDetails.putExtra("Restaurant", restaurant);
                                startActivity(restaurantDetails);
                            });
                        }
                        Log.d("TAG", "onCreate: " + place);
                    }
                });





    }

    private void cancelNotification() {
        Intent notificationIntent = new Intent(this, MyFirebaseMessagingService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

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

    private void setProfilePicture(Uri profilePictureUrl) {
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.user_image));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String text = item.getTitle().toString();

        if (text.equals(getString(R.string.your_lunch))) {
            WorkmatesViewModel.getInstance().getMyRestaurantChoiceId().observe(this,
                    restaurantId -> mRestaurantsViewModel.loadRestaurantDetails(restaurantId).observe(this, restaurant -> {
                Intent restaurantDetails = new Intent(getBaseContext(), RestaurantDetailsActivity.class);
                restaurantDetails.putExtra("Restaurant", restaurant);
                startActivity(restaurantDetails);
            }));
        } else if (text.equals(getString(R.string.settings))) {
            Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsActivity);
        } else if (text.equals(getString(R.string.logout))) {
            mUserManager.signOut(this).addOnSuccessListener(aVoid -> finish());
        } else if (text.equals(getString(R.string.bottom_navigation_menu_map))) {

            mFragmentManager.beginTransaction().hide(mActive).show(mMapFragment).commit();
            mActive = mMapFragment;
            mHomeBinding.homeToolbar.getMenu().getItem(0).setVisible(true);
        } else if (text.equals(getString(R.string.bottom_navigation_menu_list))) {
            mFragmentManager.beginTransaction().hide(mActive).show(mListViewFragment).commit();
            mActive = mListViewFragment;
            mHomeBinding.homeToolbar.getMenu().getItem(0).setVisible(true);
        } else if (text.equals(getString(R.string.bottom_navigation_menu_workmates))) {
            mFragmentManager.beginTransaction().hide(mActive).show(mWorkmatesFragment).commit();
            mActive = mWorkmatesFragment;
            mHomeBinding.homeToolbar.getMenu().getItem(0).setVisible(false);
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


    private void configureToolbar() {
        mToolbar = mHomeBinding.homeToolbar;
        setSupportActionBar(mToolbar);
    }

    private void configureDrawerLayout() {
        mDrawerLayout = mHomeBinding.profileActivityDrawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        mNavigationView = mHomeBinding.profileActivityNavigationView;
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    public LatLng getCoordinate(double lat0, double lng0, long dy, long dx) {


        double lat = lat0 + ((180 / Math.PI) * (dy / 6378137d));
        double lng = lng0 + ((180 / Math.PI) * (dx / 6378137d) / Math.cos(lat0));

        return new LatLng(lat, lng);
    }

    private void scheduleNotification() {
        Log.d("TAG", "scheduleNotification: ");


        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(System.currentTimeMillis());
        Calendar dueDate = Calendar.getInstance();
        dueDate.setTimeInMillis(System.currentTimeMillis());
        dueDate.set(Calendar.HOUR_OF_DAY,12);
        dueDate.set(Calendar.MINUTE,0);
        dueDate.set(Calendar.SECOND,0);

        if(dueDate.before(currentDate))
        {
            dueDate.add(Calendar.HOUR_OF_DAY,24);
        }

        long timeDiff= dueDate.getTimeInMillis() - currentDate.getTimeInMillis();
        Log.d("timediff", "scheduleNotification: " + timeDiff);
        Intent notificationIntent = new Intent( this, MyFirebaseMessagingService. class ) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;

        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeDiff, pendingIntent) ;
    }


}