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

import com.example.organization.data.model.Events;
import com.example.organization.data.model.Request;
import com.example.organization.data.model.Restaurant.RestaurantInformation;
import com.example.organization.events.EventsTabbedFragment;
import com.example.organization.events.ListEventsFragment;
import com.example.organization.events.ListMyEventsFragment;
import com.example.organization.requests.ListMyRequestsFragment;
import com.example.organization.requests.ListRequestsFragment;
import com.example.organization.requests.RequestsTabbedFragment;

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


    public Fragment getRequestTabbedFragment(){
        return requestsTabbedFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        ImageView buttonProfile = findViewById(R.id.icon_profile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
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

    }
}
