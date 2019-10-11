package com.example.organization.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.organization.Constants;
import com.example.organization.MainActivity;
import com.example.organization.R;
import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.SendInitiatorInformation;
import com.example.organization.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MNotificationManager {

    private Context mCtx;
    private static MNotificationManager mInstance;

    private MNotificationManager(Context ctx){
        this.mCtx = ctx;
    }

    public static synchronized MNotificationManager getInstance(Context ctx)
    {
        if (mInstance == null){
            mInstance = new MNotificationManager(ctx);
        }

        return mInstance;
    }

    public void showNotification(String title, String body, String messengerId){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.mCtx, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_call)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);

        Intent intent = null;

        if (LoginDataSource.getInitiator() != null) {

            intent = new Intent(mCtx, MainActivity.class);
          //  intent.putExtra(RequestsDetailFragment.ORG_EMAIL, messengerId);
            //intent.putExtra("Item", R.id.navigation_msg);

        }
        else {
            intent = new Intent(mCtx, LoginActivity.class);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, Constants.PENDING_INTENT_NOTIFICATION_RESULT_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =(NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null){
            notificationManager.notify(1, mBuilder.build());
        }


    }

    public void updateDeviceId(){

        FirebaseInstanceId.getInstance().
                getInstanceId().
                addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                       if (LoginDataSource.getInitiator() != null){

                            SendInitiatorInformation info = new SendInitiatorInformation();
                            info.setDeviceId(task.getResult().getToken());
                            // LoginDataSource.getFakeOrg()
                            Retrofit retrofit = NetworkClient.getRetrofitClient();
                            Initiator iOrganization = retrofit.create(Initiator.class);
                            Call call = iOrganization.updateDeviceId(LoginDataSource.getInitiator().getToken(), info);
                            Log.d("TOKEN IS ", task.getResult().getToken());
                            Log.d("TOKEN LENGTH: ", String.valueOf(task.getResult().getToken().length()));
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {

                                    if(response.isSuccessful())
                                        Log.d("TOKEN", "Token updated successful");

                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }

                    }
                });
    }
}
