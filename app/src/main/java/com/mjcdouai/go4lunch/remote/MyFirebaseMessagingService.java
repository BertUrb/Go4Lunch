package com.mjcdouai.go4lunch.remote;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.manager.UserManager;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.ui.RestaurantDetailsActivity;
import com.mjcdouai.go4lunch.viewModel.RestaurantsViewModel;

import java.time.LocalDate;
import java.util.Objects;

public class MyFirebaseMessagingService extends BroadcastReceiver {

    private final RestaurantsViewModel mRestaurantsViewModel = RestaurantsViewModel.getInstance();


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAGn", "onMessageReceived: ");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserManager userManager = UserManager.getInstance();
        LocalDate date = LocalDate.now();
        db.collection("workmates").document(Objects.requireNonNull(userManager.getCurrentUser().getEmail())).get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult().getString("date")).equals(date.toString())) {


                String res = task.getResult().getString("chosenRestaurantId");
                Log.d("TAGn", "onMessageReceived: 2" + res);
                if (!Objects.equals(res, context.getResources().getString(R.string.not_decided))) {



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
                                String title = context.getResources().getString(R.string.choose_notification, restaurantName);
                                String body = context.getResources().getString(R.string.workmates_notification, workmates.substring(0, workmates.length() - 1));

                                Log.d("TAGn", "onMessageReceived: " + title);
                                    Log.d("TAGn", "onMessageReceived: 4");
                                    mRestaurantsViewModel.loadRestaurantDetails(res).observeForever(restaurant -> showNotification(context,
                                            title,
                                            body,
                                            restaurant));



                            });
                }
            }
        });

    }

    private RemoteViews getCustomDesign(Context context,String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                context.getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.go4lunch_logo_textless);
        return remoteViews;
    }


    public void showNotification(Context context,String title,
                                 String message, Restaurant restaurant) {

        Intent intent
                = new Intent(context, RestaurantDetailsActivity.class);

        String channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             intent.putExtra("Restaurant",restaurant);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(context,
                channel_id)
                .setSmallIcon(R.drawable.go4lunch_logo_textless)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);


        builder = builder.setContent(
                getCustomDesign(context,title, message));


        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel
                = new NotificationChannel(
                channel_id, "web_app",
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(
                notificationChannel);

        notificationManager.notify(0, builder.build());
    }
}