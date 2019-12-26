package com.example.organization;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.organization.requests.AddingRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditMyRequest extends AppCompatActivity {

    int myRequestId;
    EditText nameRequest;
    TextView dateRequest;
    TextView startTimeRequest;
    TextView endTimeRequest;
    EditText amountParticipants;
    EditText aboutRequest;

    // Здесь храниться дата request-а.
    Calendar requestDate = Calendar.getInstance();

    // Здесь храниться время начала request-а.
    Calendar requestStartTime = Calendar.getInstance();

    // Здесь храниться дата конца request-а.
    Calendar requestEndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_request);
        myRequestId = getIntent().getIntExtra("myRequest" , 0);
        System.out.println("ID ----------------------------------------------- " + myRequestId);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.edit_requests_toolbar);

        setSupportActionBar(myToolbar);
        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setTitle("");

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });

        nameRequest = findViewById(R.id.edit_name_request);
        dateRequest = findViewById(R.id.edit_request_date);
        startTimeRequest = findViewById(R.id.edit_request_time_start);
        endTimeRequest = findViewById(R.id.edit_request_time_end);
        amountParticipants = findViewById(R.id.edit_amount_participants);
        aboutRequest = findViewById(R.id.edit_about_request);

        // установка обработчика выбора даты для request-а.
        final DatePickerDialog.OnDateSetListener selectDateRequest = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                requestDate.set(Calendar.YEAR, year);
                requestDate.set(Calendar.MONTH, monthOfYear);
                requestDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDateRequest();
            }

        };

        // установка обработчика выбора времени для начало request-а.
        final TimePickerDialog.OnTimeSetListener selectStartTimeRequest =new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                requestStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                requestStartTime.set(Calendar.MINUTE, minute);
                setStartTimeRequest();
            }
        };

        // установка обработчика выбора времени для конца request-а.
        final TimePickerDialog.OnTimeSetListener selectEndTimeRequest = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                requestEndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                requestEndTime.set(Calendar.MINUTE, minute);
                setEndTimeRequest();
            }
        };

        dateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditMyRequest.this, selectDateRequest,
                        requestDate.get(Calendar.YEAR), requestDate.get(Calendar.MONTH),
                        requestDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTimeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditMyRequest.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        selectStartTimeRequest,
                        requestStartTime.get(Calendar.HOUR_OF_DAY),
                        requestStartTime.get(Calendar.MINUTE), false);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

        endTimeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditMyRequest.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        selectEndTimeRequest,
                        requestEndTime.get(Calendar.HOUR_OF_DAY),
                        requestEndTime.get(Calendar.MINUTE), false);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

    }

    private void setDateRequest() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateRequest.setText(sdf.format(requestDate.getTime()));
    }

    private void setStartTimeRequest() {
        String myFormat = "hh:mm aa";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startTimeRequest.setText(sdf.format(requestStartTime.getTime()));
    }

    private void setEndTimeRequest() {
        String myFormat = "hh:mm aa";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endTimeRequest.setText(sdf.format(requestEndTime.getTime()));
    }

}
