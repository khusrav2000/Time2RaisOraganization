package com.example.organization;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.EventToOffer;
import com.example.organization.data.model.Events;
import com.example.organization.data.model.InitiatorInformation;
import com.example.organization.data.model.Conversation;
import com.example.organization.data.model.Request;
import com.example.organization.data.model.Restaurant.RestaurantInformation;
import com.example.organization.data.model.room.Messenger;
import com.example.organization.events.EventOfferFragment;
import com.example.organization.events.EventsTabbedFragment;
import com.example.organization.events.ListEventsFragment;
import com.example.organization.events.ListMyEventsFragment;
import com.example.organization.requests.ListMyRequestsFragment;
import com.example.organization.requests.ListRequestsFragment;
import com.example.organization.requests.RequestsTabbedFragment;
import com.example.organization.room.MessengerViewModel;
import com.example.organization.service.MNotificationManager;
import com.example.organization.ui.login.LoginActivity;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.google.firebase.firestore.DocumentChange.Type.ADDED;

public class MainActivity extends AppCompatActivity implements
        ListMyRequestsFragment.OnListFragmentInteractionListener,
        ListRequestsFragment.OnListFragmentInteractionListener,
        RequestsTabbedFragment.OnFragmentInteractionListener,

        ListMyEventsFragment.OnListFragmentInteractionListener,
        ListEventsFragment.OnListFragmentInteractionListener,
        EventsTabbedFragment.OnFragmentInteractionListener,

        MessagesFragment.OnListFragmentInteractionListener,

        EventOfferFragment.OnListFragmentInteractionListener, PopupMenu.OnMenuItemClickListener {

    private TextView mTextMessage;
    Fragment requestsTabbedFragment = new RequestsTabbedFragment();
    Fragment messagesFragment = new MessagesFragment();
    Fragment eventsTabbedFragment = new EventsTabbedFragment();

    final String storageUrl = "https://drive.google.com/uc?export=download&id=";
    boolean setProfileImage = false;

    ImageView profileImage;
    FirebaseFirestore mFirestore ;
    private MessengerViewModel mMessengerViewModel;
    ListenerRegistration lg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LoginDataSource.getInitiator() == null){
            startLogin();
        }
        setContentView(R.layout.activity_main);

        MNotificationManager.getInstance(getApplication()).updateDeviceId();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        profileImage = findViewById(R.id.icon_profile);
        mTextMessage = findViewById(R.id.message);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfile();
            }
        });
        final FrameLayout searchFilter = findViewById(R.id.search_filter);

        findViewById(R.id.image_search_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICK !!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
                if (searchFilter.getVisibility() == View.GONE) {
                    searchFilter.setVisibility(View.VISIBLE);
                } else {
                    searchFilter.setVisibility(View.GONE);
                }
            }
        });

        final CheckBox searchByName = findViewById(R.id.search_by_name);
        final CheckBox searchByZipCode = findViewById(R.id.search_by_zip_code);
        searchByName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SearchView searchView = findViewById(R.id.search);
                searchView.setQuery("", false);
                if (isChecked){
                    searchByZipCode.setChecked(false);
                } else {
                    searchByZipCode.setChecked(true);
                }
            }
        });
        searchByZipCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SearchView searchView = findViewById(R.id.search);
                searchView.setQuery("", false);
                if (isChecked){
                    searchByName.setChecked(false);
                } else {
                    searchByName.setChecked(true);
                }
            }
        });

        int backTo = getIntent().getIntExtra("backTo",0);

        if( backTo != 0 && backTo == R.id.navigation_messages){

            navView.setSelectedItemId(R.id.navigation_messages);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, messagesFragment);
            transaction.commit();

        }
        else {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, eventsTabbedFragment);
            transaction.commit();
        }
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

    private void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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
            //setMenuItemId(item);
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
        System.out.println("--------------------" + requests.getRestaurantId());
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

    @Override
    public void onListFragmentInteraction(Messenger item) {
        Intent intent = new Intent(this, SendMessage.class);
        intent.putExtra(Constants.MESSENGER_ID_PARAM, item.getMessengerId());
        intent.putExtra(Constants.MESSENGER_NAME_PARAM, item.getName());
        intent.putExtra(Constants.MESSENGER_ICON_URL, item.getIconUri());
        startActivity(intent);
    }
    protected void onStart() {
        super.onStart();

        mMessengerViewModel = ViewModelProviders.of(this).get(MessengerViewModel.class);



        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        Query query = mFirestore.collection("MessangerRestouran")
                .whereEqualTo("Initiator.InitiatorID", LoginDataSource.getInitiator().getInitId());

        EventListener<QuerySnapshot> eventListener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {

                for (DocumentChange change : snapshot.getDocumentChanges()){

                    // System.out.println("Document messenger change : " + change.getDocument().getId());
                    DocumentSnapshot snap = change.getDocument();
                    System.out.println("ADEEEEEEEED ----------------- IFFFFFFFFFFF");

                    if ( change.getType() == ADDED ){

                        String email =(String) snap.get("Restouran.Email");
                        String icon =(String) snap.get("Restouran.Icon");
                        long id =(long) snap.get("Restouran.RestouranID");
                        String name =(String) snap.get("Restouran.Name");
                        //int messengerId,  String name, String email, int nNewPost, String iconUri, String lastMessage

                        Messenger messenger = new Messenger((int) id, name, email, 0, icon, "");
                        System.out.println("TO UPDATE BUT NOT!");
                        mMessengerViewModel.insert(messenger);

                    }

                }
            }
        };

        lg = query.addSnapshotListener(eventListener);
    }

    protected void onStop(){
        super.onStop();
        lg.remove();
    }

    @Override
    public void onListFragmentInteraction(EventToOffer item) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return false;
    }
}
