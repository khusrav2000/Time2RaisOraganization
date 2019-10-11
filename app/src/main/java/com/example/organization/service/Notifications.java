package com.example.organization.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.example.organization.Constants;
import com.example.organization.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class Notifications extends FirebaseMessagingService {

    private static final String TAG = "NEW MESSAGE";
    //private static final String CHANNEL_ID = "Time2RaiseRestouran-1";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        MNotificationManager.getInstance(getApplicationContext()).updateDeviceId();
        Log.d("UPDATE_TOKEN",token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());}

            String title = remoteMessage.getData().get("title");
            String body  = remoteMessage.getData().get("body");
            String messengerId = remoteMessage.getData().get("messengerId");

            MNotificationManager.getInstance(getApplicationContext()).showNotification(title, body, messengerId);

        // Check if message contains a data payload.
     /*   ;

            if ( true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }*/

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
             title = remoteMessage.getNotification().getTitle();
             body  = remoteMessage.getNotification().getBody();


            MNotificationManager.getInstance(getApplicationContext()).showNotification(title, body, messengerId);
        }
        else
        {
           Log.d(TAG, "Notification is null");
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }
}
