package com.example.organization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class EditMyRequest extends AppCompatActivity {

    int myRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_request);
        myRequestId = getIntent().getIntExtra("myRequest" , 0);
        System.out.println("ID ----------------------------------------------- " + myRequestId);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.edit_requests_toolbar);

        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
