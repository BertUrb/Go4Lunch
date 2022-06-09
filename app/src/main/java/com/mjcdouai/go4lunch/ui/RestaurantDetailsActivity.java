package com.mjcdouai.go4lunch.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mjcdouai.go4lunch.databinding.ActivityRestaurantDetailsBinding;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.remote.GoogleApi;

import java.util.Objects;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private ActivityRestaurantDetailsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Intent intent = getIntent();
        Restaurant restaurant = intent.getParcelableExtra("Restaurant");

        if(restaurant.getPhotoReferences().size() >0)
        {
            String url = GoogleApi.getImageUrl(restaurant.getPhotoReferences().get(0));

            Glide.with(mBinding.getRoot().getContext())
                    .load(url)
                    .into(mBinding.restaurantImage);
        }

        mBinding.restaurantName.setText(restaurant.getName());
        mBinding.restaurantAddressAndCuisine.setText(restaurant.getAddress());
        if(!Objects.equals(restaurant.getPhone(), null))
        {
            mBinding.callBtn.setOnClickListener(view -> {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + restaurant.getPhone().replaceAll(" ","")));
                startActivity(callIntent);
            });
        }
        else {
            mBinding.callBtn.setEnabled(false);
        }

        if(!Objects.equals(restaurant.getWebsite(), null))
        {
            mBinding.webBtn.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsite()));
                startActivity(browserIntent);
            });

        }
        else {
            mBinding.webBtn.setEnabled(false);
        }



    }
}