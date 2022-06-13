package com.mjcdouai.go4lunch.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mjcdouai.go4lunch.databinding.ActivityRestaurantDetailsBinding;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.model.Workmate;
import com.mjcdouai.go4lunch.remote.GoogleApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private ActivityRestaurantDetailsBinding mBinding;
    private List<Workmate> mDummyWorkmatesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDummyWorkmatesList.add(new Workmate("dummy@mail.com","Nicolas Sarkozy","https://upload.wikimedia.org/wikipedia/commons/a/a6/Nicolas_Sarkozy_in_2010.jpg"));
        mDummyWorkmatesList.add(new Workmate("dummy@mail.com","Lionel Jospin","https://upload.wikimedia.org/wikipedia/commons/e/e7/Lionel_Jospin%2C_mai_2014%2C_Rennes%2C_France_%28cropped%29.jpg"));
        mDummyWorkmatesList.add(new Workmate("dummy@mail.com","Nicolas Sarkozy","https://upload.wikimedia.org/wikipedia/commons/a/a6/Nicolas_Sarkozy_in_2010.jpg"));
        mDummyWorkmatesList.add(new Workmate("dummy@mail.com","Lionel Jospin","https://upload.wikimedia.org/wikipedia/commons/e/e7/Lionel_Jospin%2C_mai_2014%2C_Rennes%2C_France_%28cropped%29.jpg"));
        mDummyWorkmatesList.add(new Workmate("dummy@mail.com","Nicolas Sarkozy","https://upload.wikimedia.org/wikipedia/commons/a/a6/Nicolas_Sarkozy_in_2010.jpg"));
        mDummyWorkmatesList.add(new Workmate("dummy@mail.com","Lionel Jospin","https://upload.wikimedia.org/wikipedia/commons/e/e7/Lionel_Jospin%2C_mai_2014%2C_Rennes%2C_France_%28cropped%29.jpg"));


        mBinding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.joiningWorkmates.setAdapter(new RestaurantDetailsWorkmatesAdapter(mDummyWorkmatesList));

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
        if(!Objects.equals(restaurant.getPhone(), null) && !Objects.equals(restaurant.getPhone(), "none") )
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

        if(!Objects.equals(restaurant.getWebsite(), null) && !Objects.equals(restaurant.getWebsite(), "none"))
        {
            mBinding.webBtn.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsite()));
                startActivity(browserIntent);
            });

        }
        else {
            mBinding.webBtn.setEnabled(false);
        }

        float rating = restaurant.getRating();
        float starNumber;

        if (rating <= 1) {
            starNumber = 0;
        } else if (rating <= 2.5 && rating > 1) {
            starNumber = 1;
        } else if (rating < 4 && rating > 2.5) {
            starNumber = 2;
        } else {
            starNumber = 3;
        }
        StringBuilder stars = new StringBuilder();
        char star = 0x2B50;


        for (int i = 0; i < starNumber; i++) {
            stars.append(star);
        }

        mBinding.restaurantStars.setText(stars);



    }
}