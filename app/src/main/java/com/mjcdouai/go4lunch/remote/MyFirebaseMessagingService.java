package com.mjcdouai.go4lunch.remote;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.manager.UserManager;
import com.mjcdouai.go4lunch.model.Workmate;
import com.mjcdouai.go4lunch.ui.HomeActivity;
import com.mjcdouai.go4lunch.viewModel.WorkmatesViewModel;

import java.time.LocalDate;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("TAGn", "onMessageReceived: ");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserManager userManager = UserManager.getInstance();
        LocalDate date = LocalDate.now();
        db.collection("workmates").document(userManager.getCurrentUser().getEmail()).get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult().getString("date")).equals(date.toString())) {


                String res = task.getResult().getString("chosenRestaurantId");
                Log.d("TAGn", "onMessageReceived: 2" + res);
              //  if(!res.equals(getResources().getString(R.string.not_decided)))
               // {

                db.collection("workmates").whereEqualTo("chosenRestaurantId", res)
                        .whereEqualTo("date", date.toString())
                        .get()
                        .addOnCompleteListener(task2 -> {
                            String restaurantName = null;
                            Log.d("TAGn", "onMessageReceived: 3");

                            StringBuilder workmates = new StringBuilder();
                            for (QueryDocumentSnapshot document : task2.getResult()) {
                                if (!document.getId().equals(userManager.getCurrentUser().getEmail())) {
                                    workmates.append(document.getString("name")).append(",");
                                    restaurantName = document.getString("restaurantName");

                                }

                            }
                            String title = getResources().getString(R.string.choose_notification) + restaurantName ;
                            String body = getResources().getString(R.string.workmates_notification) + workmates.substring(0,workmates.length()-1);

                            Log.d("TAGn", "onMessageReceived: " + title);
                            if (remoteMessage.getNotification() != null) {
                                // Since the notification is received directly from
                                // FCM, the title and the body can be fetched
                                // directly as below.
                                Log.d("TAGn", "onMessageReceived: 4");
                                showNotification(
                                       title,
                                        body);
                            }

                        });
            }
//            }
        });

    }

    // Method to get the custom Design for the display of
    // notification.
    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.go4lunch_logo_textless);
        return remoteViews;
    }

    // Method to display the notifications
    public void showNotification(String title,
                                 String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, HomeActivity.class);
        // Assign channel ID
        String channel_id = "notification_channel";
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.go4lunch_logo_textless)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.

            builder = builder.setContent(
                    getCustomDesign(title, message));

        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        NotificationChannel notificationChannel
                = new NotificationChannel(
                channel_id, "web_app",
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(
                notificationChannel);

        notificationManager.notify(0, builder.build());
    }
}