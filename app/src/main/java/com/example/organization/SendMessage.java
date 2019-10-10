package com.example.organization;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.InitiatorInformation;
import com.example.organization.data.model.room.Messages;
import com.example.organization.data.model.room.Messenger;
import com.example.organization.room.MessengerViewModel;
import com.example.organization.ui.login.LoginViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.google.firebase.firestore.DocumentChange.Type.ADDED;

public class SendMessage extends AppCompatActivity {

    ImageView conversationIconProfile;
    ImageView sendMessageButton;
    LinearLayout listMessages;
    EditText inputMessage;
    int messengerId;
    MediaPlayer mPlayer;
    RecyclerView recyclerView;
    MessengerViewModel messengerViewModel;
    ScrollView sv;
    MessageRecyclerViewAdapter adapter;
    FrameLayout gridLayout;

    boolean setFocus = false;

    LayoutInflater inflater;
    private FirebaseFirestore mFirestore;
    private EventListener<QuerySnapshot> eventListener;
    private DocumentReference reference;
    ListenerRegistration lg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        messengerId = getIntent().getIntExtra(Constants.MESSENGER_ID_PARAM, 0);

        conversationIconProfile         = findViewById(R.id.conservation_icon_profile);
        sendMessageButton               = findViewById(R.id.send_message_button);
        //listMessages                    = findViewById(R.id.list_messages);
        inputMessage                    = findViewById(R.id.input_message);
        gridLayout                      = findViewById(R.id.gridLayout);


        inflater = LayoutInflater.from(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.send_message_toolbar);

        setSupportActionBar(myToolbar);
        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setTitle("");
        //actionBar.setHomeAsUpIndicator(R.drawable.back_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        myToolbar.setTitle("");

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //onBackPressed();// возврат на предыдущий activity

               onBack();

            }
        });

        messengerViewModel = new MessengerViewModel(this.getApplication());
        recyclerView = findViewById(R.id.scroll_list_messages);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        List<Messages> messages = messengerViewModel.getAllMessagesByMessengerId(messengerId).getValue();



        adapter = new MessageRecyclerViewAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        messengerViewModel.getAllMessagesByMessengerId(messengerId).observe(this, new Observer<List<Messages>>() {
            @Override
            public void onChanged(@Nullable List<Messages> messages) {
                adapter.setMessenger(messages);
                recyclerView.setAdapter(adapter);

            }
        });



        Picasso picasso = Picasso.get();
        picasso.load(R.drawable.photo)
                .fit()
                .transform(new CircleTransform())
                .centerCrop()
                .placeholder(R.drawable.photo)
                .into(conversationIconProfile);

        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);


        Query query = mFirestore.collection("MessangerRestouran")
                .whereEqualTo("Initiator.InitiatorID", LoginDataSource.getInitiator().getInitId())
                .whereEqualTo("Restouran.RestouranID", messengerId).limit(1);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getDocuments().size() > 0) {

                        reference = task.getResult().getDocuments().get(0).getReference();
                        Query q = reference.collection("Messages").orderBy("DateTimePost");
                        lg = q.addSnapshotListener( eventListener);
                    }
                }
            }
        });

        eventListener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w("ERROR_FIREBASE", "onEvent:error", e);
                    return;
                }
                for (DocumentChange change : snapshot.getDocumentChanges()){

                    // Snapshot of the changed document
                    DocumentSnapshot snap = change.getDocument();

                    if (change.getType() == ADDED && messengerId != 0){

                        messengerViewModel.syncMessage(snap, messengerId);

                    }

                }
            }
        };

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMessage.getText().length() > 0) {
                    sendMessage();
                }
            }
        });

        inputMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ONCLICK-------------------------------------");
                //focusBottomScrollView();
            }
        });

        inputMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("ONTOUCH----------------------------------");
                setFocus = true;
                return false;
            }
        });

        if (setFocus){
            System.out.println("PERER------------");
            //focusBottomScrollView();
        }


        inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              //  focusBottomScrollView();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changePaddingBottom();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void onBack() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("backTo", R.id.navigation_messages);
        startActivity(intent);
    }

    private void changePaddingBottom() {
        System.out.println("--------------------------------------------------------change");
//        sv.setPadding(0,0,0, inputMessage.getHeight() + 160);
    }




    private void sendMessage() {
        String text = inputMessage.getText().toString();
        inputMessage.setText("");

        Messages messages = new Messages(messengerId, null, text, false, new Date());
        com.example.organization.data.model.SendMessage sendMessage =
                new com.example.organization.data.model.SendMessage(text, "", messengerId, 0);

        int id = messengerViewModel.insertMessageWithSending(messages, sendMessage);
    }

    protected void onStop(){
        super.onStop();

        if ( lg != null)
            lg.remove();
    }
}
