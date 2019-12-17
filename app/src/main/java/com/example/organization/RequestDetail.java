package com.example.organization;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.Restaurant.CashBackItems;
import com.example.organization.data.model.Restaurant.RestaurantInformation;
import com.example.organization.data.model.Restaurant.TimeTable;
import com.example.organization.data.model.Restaurant.WeekDay;
import com.example.organization.data.model.room.Messenger;
import com.example.organization.room.MessengerViewModel;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestDetail extends AppCompatActivity {

    RestaurantInformation restaurantInformation;
    int restaurantId;

    final String storageUrl = "https://drive.google.com/uc?export=download&id=";
    private MessengerViewModel mMessengerViewModel;
    TextView workingStartMonday;
    TextView workingStartTuesday;
    TextView workingStartWednesday;
    TextView workingStartThursday;
    TextView workingStartFriday;
    TextView workingStartSaturday;
    TextView workingStartSunday;

    TextView workingEndMonday;
    TextView workingEndTuesday;
    TextView workingEndWednesday;
    TextView workingEndThursday;
    TextView workingEndFriday;
    TextView workingEndSaturday;
    TextView workingEndSunday;
    Button  sendMessages;
    int countDonatingBack = 0 ;
    public TextView nameRestaurant;
    public TextView restaurantEmail;
    public TextView phoneRestaurant;
    public TextView aboutRestaurant;

    ImageView requestBackgroundImage;
    ImageView requestImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurantId = getIntent().getIntExtra("request" , 0);
        System.out.println("get ------ -- - -- - - -" + restaurantId);
        setContentView(R.layout.activity_requests_detail);

        // Верхняя меню для кнопки назад.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.request_detail);
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
        mMessengerViewModel = new MessengerViewModel(this.getApplication());
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });

        nameRestaurant = findViewById(R.id.restaurant_name);
        restaurantEmail = findViewById(R.id.restaurant_email);
        aboutRestaurant = findViewById(R.id.about_restaurant);

        requestBackgroundImage = findViewById(R.id.request_background_image);
        requestImage = findViewById(R.id.request_image);

        workingStartMonday          = findViewById(R.id.working_start_monday);
        workingStartTuesday         = findViewById(R.id.working_start_tuesday);
        workingStartWednesday       = findViewById(R.id.working_start_wednesday);
        workingStartThursday        = findViewById(R.id.working_start_thursday);
        workingStartFriday          = findViewById(R.id.working_start_friday);
        workingStartSaturday        = findViewById(R.id.working_start_saturday);
        workingStartSunday          = findViewById(R.id.working_start_sunday);

        workingEndMonday            = findViewById(R.id.working_end_monday);
        workingEndTuesday           = findViewById(R.id.working_end_tuesday);
        workingEndWednesday         = findViewById(R.id.working_end_wednesday);
        workingEndThursday          = findViewById(R.id.working_end_thursday);
        workingEndFriday            = findViewById(R.id.working_end_friday);
        workingEndSaturday          = findViewById(R.id.working_end_saturday);
        workingEndSunday            = findViewById(R.id.working_end_sunday);

        sendMessages                = findViewById(R.id.send_message);

        sendMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSendMessage();
            }
        });

        loadRestaurantInformation();
    }

    private void startSendMessage() {
        // Добавления рес

        Messenger messenger = new Messenger(restaurantId, restaurantInformation.getName(), restaurantInformation.getEmail(), 0,
                restaurantInformation.getIconUrl(), "" );
        mMessengerViewModel.insert(messenger);

        Intent intent = new Intent(this, SendMessage.class);
        intent.putExtra(Constants.MESSENGER_ID_PARAM, restaurantId);
        intent.putExtra(Constants.MESSENGER_NAME_PARAM, restaurantInformation.getName());
        intent.putExtra(Constants.MESSENGER_ICON_URL, restaurantInformation.getIconUrl());
        startActivity(intent);
    }

    private void loadRestaurantInformation() {

        final Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator iOrganization = retrofit.create(Initiator.class);

        Call call = iOrganization.getRestaurantById(LoginDataSource.getInitiator().getToken(), restaurantId);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                restaurantInformation = (RestaurantInformation) response.body();
                System.out.println(restaurantInformation.toString());
                if (response.isSuccessful())
                    System.out.println(restaurantInformation.toString());
                    fillFields();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    // Заполнения полей request-а (Ресторана).
    private void fillFields(){
        Picasso picasso = Picasso.get();
        System.out.println("-------------------" + restaurantInformation.toString());
        if (restaurantInformation.getBackgroundImageUrl() != null) {
            picasso.load(storageUrl + restaurantInformation.getBackgroundImageUrl())
                    .fit()// Длина и ширина фотографии будет таким каким задано в XML файле.
                    .centerCrop()// Get center. Берём ценрт фотографии, все что помешается в заданный размер.
                    .into(requestBackgroundImage);
        }
        else {
            picasso.load(R.drawable.no_photo)
                    .fit()
                    .centerCrop()
                    .into(requestBackgroundImage);
        }

        picasso.load(storageUrl + restaurantInformation.getIconUrl())
                .fit()
                .centerCrop()
                .transform(new CircleTransform())
                .into(requestImage);

        nameRestaurant.setText(restaurantInformation.getName());
        restaurantEmail.setText(restaurantInformation.getEmail());
        //phoneRestaurant.setText(restaurantInformation.getPhone());
        aboutRestaurant.setText(restaurantInformation.getAbout());

        TimeTable timeTable = restaurantInformation.getTimeTable();
        List<WeekDay> weekdays = timeTable.getWeekDays();


        if (weekdays != null && weekdays.size() > 0) {


            workingStartMonday.setText(getCorrectTime(weekdays.get(0).getStart()));
            workingEndMonday.setText(getCorrectTime(weekdays.get(0).getEnd()));
            workingStartTuesday.setText(getCorrectTime(weekdays.get(1).getStart()));
            workingEndTuesday.setText(getCorrectTime(weekdays.get(1).getEnd()));
            workingStartWednesday.setText(getCorrectTime(weekdays.get(2).getStart()));
            workingEndWednesday.setText(getCorrectTime(weekdays.get(2).getEnd()));
            workingStartThursday.setText(getCorrectTime(weekdays.get(3).getStart()));
            workingEndThursday.setText(getCorrectTime(weekdays.get(3).getEnd()));
            workingStartFriday.setText(getCorrectTime(weekdays.get(4).getStart()));
            workingEndFriday.setText(getCorrectTime(weekdays.get(4).getEnd()));
            workingStartSaturday.setText(getCorrectTime(weekdays.get(5).getStart()));
            workingEndSaturday.setText(getCorrectTime(weekdays.get(5).getEnd()));
            workingStartSunday.setText(getCorrectTime(weekdays.get(6).getStart()));
            workingEndSunday.setText(getCorrectTime(weekdays.get(6).getEnd()));
        }

        List<CashBackItems> donatingBack = restaurantInformation.getCashBackItems();
        GridLayout gridLayout = findViewById(R.id.grid_donating_back);

        if(donatingBack != null) {

            for (int i = 0; i < donatingBack.size(); i++) {
                //Добавления View для сумми кэбека
                TextView donatingBackAmount = new TextView(getBaseContext());

                GridLayout.LayoutParams poss = new GridLayout.LayoutParams();

                poss.width = getBaseContext().getResources().getDimensionPixelOffset(R.dimen.request_detail_cash_back_amount_width);
                poss.height = getBaseContext().getResources().getDimensionPixelOffset(R.dimen.request_detail_cash_back_height);

                poss.columnSpec = GridLayout.spec(0);
                poss.rowSpec = GridLayout.spec(0 + countDonatingBack);
                poss.setGravity(Gravity.CENTER_VERTICAL);
                poss.setMargins(0, 6, 6, 6);

                donatingBackAmount.setLayoutParams(poss);

                donatingBackAmount.setInputType(InputType.TYPE_CLASS_TEXT);
                donatingBackAmount.setBackgroundResource(R.drawable.working_time_orange_border);
                donatingBackAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_cash_back,
                        0, 0, 0);

                donatingBackAmount.setCompoundDrawablePadding((int) (5));
                donatingBackAmount.setText(String.valueOf(donatingBack.get(i).getAmount()));
                donatingBackAmount.setGravity(Gravity.CENTER_VERTICAL);
                donatingBackAmount.setPadding(
                        getBaseContext().getResources().getDimensionPixelOffset(R.dimen.donating_back_text_padding)
                        ,0,
                        getBaseContext().getResources().getDimensionPixelOffset(R.dimen.donating_back_text_padding)
                        ,0);

                donatingBackAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                gridLayout.addView(donatingBackAmount);

                //Добавления View для процента кэшбека
                TextView donatingBackPrice = new TextView(getBaseContext());

                GridLayout.LayoutParams priceParams = new GridLayout.LayoutParams();

                priceParams.width = getBaseContext().getResources().getDimensionPixelOffset(R.dimen.request_detail_cash_back_percent_width);
                priceParams.height = getBaseContext().getResources().getDimensionPixelOffset(R.dimen.request_detail_cash_back_height);

                priceParams.columnSpec = GridLayout.spec(1);
                priceParams.rowSpec = GridLayout.spec(0 + countDonatingBack);
                priceParams.setGravity(Gravity.CENTER_VERTICAL);
                priceParams.setMargins(6, 6, 0, 6);

                donatingBackPrice.setLayoutParams(priceParams);

                donatingBackPrice.setInputType(InputType.TYPE_CLASS_TEXT);
                donatingBackPrice.setBackgroundResource(R.drawable.working_time_orange_border);
                donatingBackPrice.setText(String.valueOf(donatingBack.get(i).getPercent()) + "%");
                donatingBackPrice.setGravity(Gravity.CENTER_VERTICAL);
                donatingBackPrice.setPadding(
                        getBaseContext().getResources().getDimensionPixelOffset(R.dimen.donating_back_text_padding)
                        ,0
                        , getBaseContext().getResources().getDimensionPixelOffset(R.dimen.donating_back_text_padding)
                        ,0);
                donatingBackPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                gridLayout.addView(donatingBackPrice);
                countDonatingBack++;
            }
        }
    }

    // Возвращает нужный нам вид времени.
    private String getCorrectTime(String time)  {

        System.out.println(time);
        String format = "HH:mm";

        SimpleDateFormat format1 = new SimpleDateFormat(format);

        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(time);
            return format1.format(date1);
        }catch (Exception e){
            e.printStackTrace();
            return "00:00";
        }
    }
}
