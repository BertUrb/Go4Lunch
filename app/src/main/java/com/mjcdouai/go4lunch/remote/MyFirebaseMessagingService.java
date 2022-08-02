package com.mjcdouai.go4lunch.remote;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.facebook.share.Share;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.manager.UserManager;
import com.mjcdouai.go4lunch.model.Restaurant;
import com.mjcdouai.go4lunch.ui.RestaurantDetailsActivity;
import com.mjcdouai.go4lunch.utils.SharedPrefsHelper;
import com.mjcdouai.go4lunch.viewModel.RestaurantsViewModel;

import java.time.LocalDate;
import java.util.Calendar;
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

                String restaurantName = task.getResult().getString("restaurantName");
                String res = task.getResult().getString("chosenRestaurantId");
                Log.d("TAGn", "onMessageReceived: 2" + res);
                if (!Objects.equals(restaurantName, context.getResources().getString(R.string.not_decided))) {



                    db.collection("workmates").whereEqualTo("chosenRestaurantId", res)
                            .whereEqualTo("date", date.toString())
                            .get()
                            .addOnCompleteListener(task2 -> {

                                Log.d("TAGn", "onMessageReceived: 3");

                                StringBuilder workmates = new StringBuilder();
                                for (QueryDocumentSnapshot document : task2.getResult()) {
                                    if (!document.getId().equals(userManager.getCurrentUser().getEmail())) {
                                        workmates.append(document.getString("name")).append(",");


                                    }

                                }
                                String title = context.getResources().getString(R.string.choose_notification, restaurantName);
                                if(workmates.length()>1)
                                {
                                    workmates.setLength(workmates.length() -1);
                                }
                                String body = context.getResources().getString(R.string.workmates_notification, workmates );

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

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(System.currentTimeMillis());
        Calendar dueDate = Calendar.getInstance();

        dueDate.set(Calendar.HOUR_OF_DAY,12);
        dueDate.set(Calendar.MINUTE,0);
        dueDate.set(Calendar.SECOND,0);

        if(dueDate.before(currentDate))
        {
            dueDate.add(Calendar.HOUR_OF_DAY,24);
        }

        long timeDiff= dueDate.getTimeInMillis() - currentDate.getTimeInMillis();

        Intent notificationIntent = new Intent( context, MyFirebaseMessagingService. class ) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( context, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(context);
        if(sharedPrefsHelper.getNotification()) {
            Log.d("scheduleNotification", "onReceive: ");
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeDiff, pendingIntent);
            Log.d("timediff", "scheduleNotification: 2 " + timeDiff);
        }
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
                PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_IMMUTABLE);


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