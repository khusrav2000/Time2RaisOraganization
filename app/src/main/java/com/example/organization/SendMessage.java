package com.example.organization;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SendMessage extends AppCompatActivity {

    ImageView conversationIconProfile;
    ImageView sendMessageButton;
    LinearLayout listMessages;
    EditText inputMessage;

    MediaPlayer mPlayer;

    ScrollView sv;

    FrameLayout gridLayout;

    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        conversationIconProfile         = findViewById(R.id.conservation_icon_profile);
        sendMessageButton               = findViewById(R.id.send_message_button);
        listMessages                    = findViewById(R.id.list_messages);
        inputMessage                    = findViewById(R.id.input_message);
        gridLayout                      = findViewById(R.id.gridLayout);
        sv                              = (ScrollView)findViewById(R.id.scroll_list_messages);

        inflater = LayoutInflater.from(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.send_message_toolbar);

        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        actionBar.setHomeAsUpIndicator(R.drawable.back_button);

        Picasso picasso = Picasso.get();
        picasso.load(R.drawable.photo)
                .fit()
                .transform(new CircleTransform())
                .centerCrop()
                .placeholder(R.drawable.photo)
                .into(conversationIconProfile);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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

    private void changePaddingBottom() {
        System.out.println("--------------------------------------------------------change");
        sv.setPadding(0,0,0, inputMessage.getHeight() + 160);
    }




    private void sendMessage() {
        View view = inflater.inflate(R.layout.send_message_text, listMessages, false);

        TextView message = view.findViewById(R.id.massage_text);
        message.setText(inputMessage.getText().toString());

        listMessages.addView(view);


        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        mPlayer = MediaPlayer.create(this, R.raw.sent_message);
        mPlayer.start();

    }
}
