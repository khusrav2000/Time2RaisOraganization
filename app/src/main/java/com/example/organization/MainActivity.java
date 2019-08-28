package com.example.organization;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.Events;
import com.example.organization.data.model.InitiatorInformation;
import com.example.organization.data.model.Request;
import com.example.organization.data.model.Restaurant.RestaurantInformation;
import com.example.organization.events.EventsTabbedFragment;
import com.example.organization.events.ListEventsFragment;
import com.example.organization.events.ListMyEventsFragment;
import com.example.organization.requests.ListMyRequestsFragment;
import com.example.organization.requests.ListRequestsFragment;
import com.example.organization.requests.RequestsTabbedFragment;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements
        ListMyRequestsFragment.OnListFragmentInteractionListener,
        ListRequestsFragment.OnListFragmentInteractionListener,
        RequestsTabbedFragment.OnFragmentInteractionListener,

        ListMyEventsFragment.OnListFragmentInteractionListener,
        ListEventsFragment.OnListFragmentInteractionListener,
        EventsTabbedFragment.OnFragmentInteractionListener,

        MessagesFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;
    Fragment requestsTabbedFragment = new RequestsTabbedFragment();
    Fragment messagesFragment = new MessagesFragment();
    Fragment eventsTabbedFragment = new EventsTabbedFragment();

    final String storageUrl = "https://drive.google.com/uc?export=download&id=";
    boolean setProfileImage = false;

    ImageView profileImage;

    public Fragment getRequestTabbedFragment(){
        return requestsTabbedFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        profileImage = findViewById(R.id.icon_profile);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfile();
            }
        });

        mTextMessage = findViewById(R.id.message);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, eventsTabbedFragment);
        transaction.commit();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Получения данных о пользователе.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator organization = retrofit.create(Initiator.class);
        Call call = organization.getProfileById(LoginDataSource.getInitiator().getToken());
        System.out.println(" WILL WORK -----------------------------------");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                // TODO: Инициализировать данные о пользователе.
                if (response.isSuccessful()) {
                    setProfileImage(((InitiatorInformation) response.body()).getIconUrl());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        if (!setProfileImage){
            setProfileImage = true;
            Picasso picasso = Picasso.get();
            picasso.load(R.drawable.no_photo)
                    .fit()
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(profileImage);
        }
    }

    private void setProfileImage(String url) {
        System.out.println("Profile iamge SET ________________----------------");
        if (url != null) {
            setProfileImage = true;
            Picasso picasso = Picasso.get();

            picasso.load(storageUrl + url)
                    .fit()
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.no_photo)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(profileImage);
        }
    }

    private void startProfile() {
        Intent intent = new Intent(this, InitiatorProfile.class);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    System.out.println("Omad---------");
                    transaction.replace(R.id.fragment, eventsTabbedFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_requests:
                    System.out.println("Omad---------");

                    transaction.replace(R.id.fragment, requestsTabbedFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_messages:
                    System.out.println("Omad---------");

                    transaction.replace(R.id.fragment, messagesFragment);
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onListFragmentInteraction(Events item) {

    }

    @Override
    public void onListFragmentInteraction(RestaurantInformation item) {
        startRequestInfoActivity(item);
    }

    public void startRequestInfoActivity(RestaurantInformation requests){
        Intent intent = new Intent(this, RequestDetail.class);
        intent.putExtra("request", requests.getRestaurantId());
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Request item) {
        startEditMyRequest(item);
    }

    private void startEditMyRequest(Request myRequest) {
        Intent intent = new Intent(this, EditMyRequest.class);
        intent.putExtra("myRequest", myRequest.getRequestId());
        startActivity(intent);
    }
}
