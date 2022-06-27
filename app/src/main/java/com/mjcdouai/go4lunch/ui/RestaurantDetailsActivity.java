package com.mjcdouai.go4lunch.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.mjcdouai.go4lunch.BuildConfig;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.ActivityRestaurantDetailsBinding;
import com.mjcdouai.go4lunch.manager.UserManager;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.model.Workmate;
import com.mjcdouai.go4lunch.remote.GoogleApi;
import com.mjcdouai.go4lunch.viewModel.WorkmatesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private ActivityRestaurantDetailsBinding mBinding;
    private Workmate mWorkmate;
    private List<Workmate> mWorkmates = new ArrayList<>();
    private final WorkmatesViewModel mWorkmatesViewModel = WorkmatesViewModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        Intent intent = getIntent();
        Restaurant restaurant = intent.getParcelableExtra("Restaurant");

        if (restaurant.getPhotoReferences().size() > 0) {
            String url = GoogleApi.getImageUrl(restaurant.getPhotoReferences().get(0),BuildConfig.GMAP_API_KEY);

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

        Log.d("TAG", "onCreate: " + restaurant.isLiked());
        changeLikeStatus(restaurant.isLiked());

        mBinding.starBtn.setOnClickListener(view ->  {


            if(!restaurant.isLiked()) {
                mWorkmatesViewModel.addFavoriteRestaurant(restaurant);
                restaurant.setLiked(true);

            }
            else {
                mWorkmatesViewModel.removeFavoriteRestaurant(restaurant);
                restaurant.setLiked(false);
            }
            changeLikeStatus(restaurant.isLiked());

        });

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


        mWorkmatesViewModel.getWorkmateInThis(restaurant.getId()).observe(this,
                workmates -> {
                    mWorkmates = workmates;
                    mBinding.joiningWorkmates.setAdapter(new RestaurantDetailsWorkmatesAdapter(mWorkmates));


                    UserManager userManager = UserManager.getInstance();

                    mWorkmate = new Workmate(userManager.getCurrentUser().getEmail(),
                            userManager.getCurrentUser().getDisplayName(),
                            Objects.requireNonNull(userManager.getCurrentUser().getPhotoUrl()).toString());

                    for(Workmate workmate: mWorkmates)
                    {
                        if(Objects.equals(workmate.getMail(), mWorkmate.getMail()))
                        {
                            mWorkmate.setChosenRestaurantId(restaurant.getId());
                        }
                    }
                    if (Objects.equals(mWorkmate.getChosenRestaurantId(), restaurant.getId())) {

                        mBinding.joinFab.setImageResource(R.drawable.green_check_circle_24);
                    } else {
                        mBinding.joinFab.setImageResource(R.drawable.green_check_circle_outline_24);
                    }
                });





        mBinding.joinFab.setOnClickListener(view -> {
            if (Objects.equals(mWorkmate.getChosenRestaurantId(), restaurant.getId())) {
                mBinding.joinFab.setImageResource(R.drawable.green_check_circle_outline_24);
                mWorkmate.setChosenRestaurantId("");
                Objects.requireNonNull(mBinding.joiningWorkmates.getAdapter()).notifyItemRemoved(mWorkmates.indexOf(mWorkmate));
                mWorkmates.remove(mWorkmate);
                mWorkmatesViewModel.insertWorkmate(mWorkmate,getResources().getString(R.string.not_decided));

            } else {
                mBinding.joinFab.setImageResource(R.drawable.green_check_circle_24);
                mWorkmate.setChosenRestaurantId(restaurant.getId());
                mWorkmatesViewModel.insertWorkmate(mWorkmate,restaurant.getName());
                mWorkmates.add(mWorkmate);
                Objects.requireNonNull(mBinding.joiningWorkmates.getAdapter()).notifyItemInserted(mWorkmates.indexOf(mWorkmate));
            }


        });
    }

    private void changeLikeStatus(Boolean isLiked) {
        int resourceId = (isLiked) ? R.drawable.ic_baseline_star_24 : R.drawable.ic_baseline_star_outline_48;
        Drawable drawable = ContextCompat.getDrawable(this, resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), 120);
        mBinding.starBtn.setCompoundDrawables(null, drawable, null, null);
        mBinding.starBtn.setBackgroundResource(resourceId);
    }
}