package com.example.organization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.Request;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestsDetail extends AppCompatActivity {

    TextView requestName;
    TextView requestDate;
    TextView requestTime;
    TextView requestAmountPrice;
    TextView requestNumberOfEvents;
    TextView requestAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int requestId = getIntent().getIntExtra("request" , 0);


        requestName                 = findViewById(R.id.request_info_name);
        requestDate                 = findViewById(R.id.request_info_date);
        requestTime                 = findViewById(R.id.request_info_time);
        requestAmountPrice          = findViewById(R.id.request_amount_part_price);
        requestNumberOfEvents       = findViewById(R.id.request_number_of_events_held);
        requestAbout                = findViewById(R.id.request_info_about);

        setContentView(R.layout.activity_requests_detail);
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator iOrganization = retrofit.create(Initiator.class);



        Call call = iOrganization.getRequestByRequestId(LoginDataSource.getInitiator().getToken(), requestId);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    Request request = (Request) response.body();
                    if (request != null)
                        setRequestInformation(request);

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void setRequestInformation(Request request){

        if (request.getName() != null)
            requestName.setText(request.getName());

        if (request.getDate() != null)
            requestDate.setText(request.getDate());

        if (request.getStart() != null && request.getEnd() != null)
            requestTime.setText(request.getStart()+ request.getEnd());

        //requestAmountPrice.setText(request.get);

        if (request.getAbout() != null)
            requestAbout.setText(request.getAbout());
    }
}
