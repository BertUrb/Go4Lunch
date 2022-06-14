package com.mjcdouai.go4lunch.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.ActivityRestaurantDetailsBinding;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.model.Workmate;
import com.mjcdouai.go4lunch.remote.GoogleApi;
import com.mjcdouai.go4lunch.repository.UserRepository;
import com.mjcdouai.go4lunch.repository.WorkmatesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private ActivityRestaurantDetailsBinding mBinding;
    private Workmate mWorkmate;
    private List<Workmate> mWorkmates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        Intent intent = getIntent();
        Restaurant restaurant = intent.getParcelableExtra("Restaurant");

        if (restaurant.getPhotoReferences().size() > 0) {
            String url = GoogleApi.getImageUrl(restaurant.getPhotoReferences().get(0));

            Glide.with(mBinding.getRoot().getContext())
                    .load(url)
                    .into(mBinding.restaurantImage);
        }

        mBinding.restaurantName.setText(restaurant.getName());
        mBinding.restaurantAddressAndCuisine.setText(restaurant.getAddress());
        if (!Objects.equals(restaurant.getPhone(), null) && !Objects.equals(restaurant.getPhone(), "none")) {
            mBinding.callBtn.setOnClickListener(view -> {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + restaurant.getPhone().replaceAll(" ", "")));
                startActivity(callIntent);
            });
        } else {
            mBinding.callBtn.setEnabled(false);
        }

        if (!Objects.equals(restaurant.getWebsite(), null) && !Objects.equals(restaurant.getWebsite(), "none")) {
            mBinding.webBtn.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsite()));
                startActivity(browserIntent);
            });

        } else {
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

        WorkmatesRepository workmatesRepository = WorkmatesRepository.getInstance();
        workmatesRepository.getWorkmateInThis(restaurant.getId()).observe(this,
                workmates -> {
                    mBinding.joiningWorkmates.setAdapter(new RestaurantDetailsWorkmatesAdapter(workmates));
                    mWorkmates = workmates;

                    UserRepository userRepository = UserRepository.getInstance();

                    mWorkmate = new Workmate(userRepository.getCurrentUser().getEmail(),
                            userRepository.getCurrentUser().getDisplayName(),
                            userRepository.getCurrentUser().getPhotoUrl().toString());

                    for(Workmate workmate: mWorkmates)
                    {
                        if(Objects.equals(workmate.getMail(), mWorkmate.getMail()))
                        {
                            mWorkmate.setChosenRestaurantId(restaurant.getId());
                            Log.d("TAG", "onCreate: equals");
                        }
                    }
                    Log.d("TAG", "onCreate: mWorkmate " + mWorkmate.getChosenRestaurantId());
                    Log.d("TAG", "onCreate: resto" + restaurant.getId());
                    if (Objects.equals(mWorkmate.getChosenRestaurantId(), restaurant.getId())) {

                        mBinding.joinFab.setImageResource(R.drawable.green_check_circle_24);
                        Log.d("TAG", "onCreate: equals2");
                    } else {
                        mBinding.joinFab.setImageResource(R.drawable.green_check_circle_outline_24);
                    }
                });





        mBinding.joinFab.setOnClickListener(view -> {
            if (Objects.equals(mWorkmate.getChosenRestaurantId(), restaurant.getId())) {
                mBinding.joinFab.setImageResource(R.drawable.green_check_circle_outline_24);
                mWorkmate.setChosenRestaurantId("");
                Log.d("TAG", "onCreate: vide");
            } else {
                mBinding.joinFab.setImageResource(R.drawable.green_check_circle_24);;
                mWorkmate.setChosenRestaurantId(restaurant.getId());
                Log.d("TAG", "onCreate: plein");
            }
            workmatesRepository.insertWorkmate(mWorkmate);
        });
    }
}