package com.example.organization.requests;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.organization.MainActivity;
import com.example.organization.R;
import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.Request;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddingRequest extends AppCompatActivity {

    Button addingRequest;
    EditText nameRequest;
    TextView dateRequest;
    TextView startTimeRequest;
    TextView endTimeRequest;
    EditText averageSumRequest;
    EditText numberOfEventsHeld;
    EditText aboutRequest;

    // Поля для хранения информаций о request-е.
    String name;
    String date;
    String startTime;
    String endTime;
    double averageSum;
    int numberOfEvents;
    String about;

    // Здесь храниться дата request-а.
    Calendar requestDate = Calendar.getInstance();

    // Здесь храниться время начала request-а.
    Calendar requestStartTime = Calendar.getInstance();

    // Здесь храниться дата конца request-а.
    Calendar requestEndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_request);


        // Данные о request-е.
        addingRequest           = findViewById(R.id.adding_request);
        nameRequest             = findViewById(R.id.name_request);
        dateRequest             = findViewById(R.id.request_date);
        startTimeRequest        = findViewById(R.id.request_time_start);
        endTimeRequest          = findViewById(R.id.request_time_end);
        averageSumRequest       = findViewById(R.id.amount_participants);
        aboutRequest            = findViewById(R.id.about_request);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.adding_requests_toolbar);

        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                new DatePickerDialog(AddingRequest.this, selectDateRequest,
                        requestDate.get(Calendar.YEAR), requestDate.get(Calendar.MONTH),
                        requestDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTimeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddingRequest.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        selectStartTimeRequest,
                        requestStartTime.get(Calendar.HOUR_OF_DAY),
                        requestStartTime.get(Calendar.MINUTE), true);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

        endTimeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddingRequest.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        selectEndTimeRequest,
                        requestEndTime.get(Calendar.HOUR_OF_DAY),
                        requestEndTime.get(Calendar.MINUTE), true);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });



        // Отслеживания кнопки для создании request-а.
        addingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Инициализация данных о request-е.
                name = nameRequest.getText().toString().trim();

                //averageSum = Double.parseDouble(averageSumRequest.getText().toString());
                //numberOfEvents = Integer.parseInt(numberOfEventsHeld.getText().toString());

                date = dateRequest.getText().toString().trim();
                startTime = startTimeRequest.getText().toString().trim();
                endTime = endTimeRequest.getText().toString().trim();
                about = aboutRequest.getText().toString().trim();

                if (validateData()){
                    Request request = new Request(-1, -1,
                            name, date, startTime, endTime, about, true);

                    Retrofit retrofit = NetworkClient.getRetrofitClient();
                    Initiator organization = retrofit.create(Initiator.class);
                    Call call = organization.addRequest(LoginDataSource.getInitiator().getToken(), request);

                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            System.out.println(" ----------------------aDDDEEED ");
                            startMainActivity();
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void setDateRequest() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateRequest.setText(sdf.format(requestDate.getTime()));
    }

    private void setStartTimeRequest() {
        String myFormat = "hh:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startTimeRequest.setText(sdf.format(requestStartTime.getTime()));
    }

    private void setEndTimeRequest() {
        String myFormat = "hh:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endTimeRequest.setText(sdf.format(requestEndTime.getTime()));
    }

    private boolean validateData() {

        if (name == null || name.equals("")){
            nameRequest.setError(getString(R.string.name_request_error));
            nameRequest.requestFocus();
            return false;
        }

        if (date == null || date.equals("")){
            dateRequest.setError(getString(R.string.date_request_error));
            dateRequest.requestFocus();
            return false;
        }

        if (startTime == null || startTime.equals("")){
            startTimeRequest.setError(getString(R.string.start_time_request_error));
            startTimeRequest.requestFocus();
            return false;
        }

        if (endTime == null || endTime.equals("")){
            endTimeRequest.setError(getString(R.string.end_time_request_error));
            endTimeRequest.requestFocus();
            return false;
        }

        return true;
    }
    public void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
