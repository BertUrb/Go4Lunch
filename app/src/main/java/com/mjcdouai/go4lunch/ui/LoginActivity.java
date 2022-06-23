package com.mjcdouai.go4lunch.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.manager.UserManager;
import com.mjcdouai.go4lunch.model.Workmate;
import com.mjcdouai.go4lunch.repository.WorkmatesRepository;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private UserManager mUserManager = UserManager.getInstance();

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );



    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Workmate workmate = new Workmate(user.getEmail(),user.getDisplayName(),user.getPhotoUrl().toString());
            WorkmatesRepository workmatesRepository = WorkmatesRepository.getInstance();
            workmatesRepository.insertWorkmate(workmate,getResources().getString(R.string.not_decided));
            workmatesRepository.initFavRestaurantList();
            startProfileActivity();

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }
    @Override
    public void onStart() {

        super.onStart();

        if(mUserManager.isCurrentUserLogged())
        {
            WorkmatesRepository.getInstance().initFavRestaurantList();
            startProfileActivity();
        }
        else
        {
            startSignInIntent();
        }




    }
    private void startProfileActivity()
    {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }

    private void startSignInIntent()
    {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

// Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.go4lunch_logo)
                .setTheme(R.style.LoginTheme)
                .build();
        signInLauncher.launch(signInIntent);
    }



    }
