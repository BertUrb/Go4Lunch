package com.mjcdouai.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.HomeActivityNavHeaderBinding;
import com.mjcdouai.go4lunch.ui.main.viewmodel.UserManager;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private UserManager mUserManager = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        configureToolbar();
        configureDrawerLayout();
        configureNavigationView();
        updateUserInfo();
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
        mToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
    }

    private void configureDrawerLayout(){
        mDrawerLayout =(DrawerLayout) findViewById(R.id.profile_activity_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        mNavigationView = (NavigationView) findViewById(R.id.profile_activity_navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }
}