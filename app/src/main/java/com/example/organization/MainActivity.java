package com.example.organization;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.organization.data.model.Events;
import com.example.organization.events.EventsTabbedFragment;
import com.example.organization.events.ListEventsFragment;
import com.example.organization.events.ListMyEventsFragment;
import com.example.organization.requests.RequestsFragment;

public class MainActivity extends AppCompatActivity implements
        ListMyEventsFragment.OnListFragmentInteractionListener,
        ListEventsFragment.OnListFragmentInteractionListener,
        EventsTabbedFragment.OnFragmentInteractionListener,
        RequestsFragment.OnFragmentInteractionListener,
        MessagesFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;
    Fragment requestsFragment = new RequestsFragment();
    Fragment messagesFragment = new MessagesFragment();
    Fragment eventsTabbedFragment = new EventsTabbedFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, eventsTabbedFragment);
        transaction.commit();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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

                    transaction.replace(R.id.fragment, requestsFragment);
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onListFragmentInteraction(Events item) {

    }
}
