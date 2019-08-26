package com.example.organization;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.InitiatorInformation;
import com.example.organization.data.model.SendInitiatorInformation;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InitiatorProfile extends AppCompatActivity {

    ImageView backgroundProfileImage;
    ImageView profileImage;
    TextView initiatorName;
    TextView initiatorEmail;
    Button addPhotoInitiator;
    EditText editInitiatorName;
    EditText editInitiatorEmail;
    EditText editInitiatorPhone;
    EditText editInitiatorAddress;
    EditText editInitiatorZipCode;
    EditText editAboutUs;
    Button buttonSaveInitiator;
    EditText initiatorOldPassword;
    EditText initiatorNewPassword;
    EditText initiatorConfirmPassword;
    Button buttonChangePassword;

    final String storageUrl = "https://drive.google.com/uc?export=download&id=";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiator_profile);

        // Инициализация данных поля профиля организатора.
        backgroundProfileImage           = findViewById(R.id.background_profile_image);
        profileImage                    = findViewById(R.id.profile_image);
        initiatorName                   = findViewById(R.id.initiator_name);
        initiatorEmail                  = findViewById(R.id.initiator_email);
        addPhotoInitiator               = findViewById(R.id.add_photo_initiator);
        editInitiatorName               = findViewById(R.id.edit_initiator_name);
        editInitiatorEmail              = findViewById(R.id.edit_initiator_email);
        editInitiatorPhone              = findViewById(R.id.edit_initiator_phone);
        editInitiatorAddress            = findViewById(R.id.edit_initiator_address);
        editInitiatorZipCode            = findViewById(R.id.edit_initiator_zip_code);
        editAboutUs                     = findViewById(R.id.edit_about_us);
        buttonSaveInitiator             = findViewById(R.id.save_initiator);
        initiatorOldPassword            = findViewById(R.id.initiator_old_password);
        initiatorNewPassword            = findViewById(R.id.initiator_new_password);
        initiatorConfirmPassword        = findViewById(R.id.initiator_confirm_password);
        buttonChangePassword            = findViewById(R.id.button_change_password);

        // Получения данных о пользователе.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator organization = retrofit.create(Initiator.class);
        Call call = organization.getProfileById(LoginDataSource.getInitiator().getToken());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                // TODO: Инициализировать данные о пользователе.
                if (response.isSuccessful())
                    saveInitiatorInformation((InitiatorInformation) response.body());
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        buttonSaveInitiator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInitiatorInformation();
            }
        });
    }

    private void setInitiatorInformation() {
        SendInitiatorInformation sendInitiatorInformation = new SendInitiatorInformation(
                0, editInitiatorEmail.getText().toString(),
                "", editInitiatorAddress.getText().toString(),
                0.0, 0.0, 0 , editInitiatorPhone.getText().toString(),
                editInitiatorName.getText().toString(),
                Integer.parseInt(editInitiatorZipCode.getText().toString()),
                editAboutUs.getText().toString(), "",
                "",""
        );


        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator organization = retrofit.create(Initiator.class);
        Call call = organization.setProfileInformation(LoginDataSource.getInitiator().getToken(), sendInitiatorInformation);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("----" + response.body().toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    //
    public void saveInitiatorInformation(InitiatorInformation initiatorInformation) {
        Picasso picasso = Picasso.get();
        picasso.load(storageUrl + initiatorInformation.getBackgroundImageUrl())
                .fit()// Длина и ширина фотографии будет таким каким задано в XML файле.
                .centerCrop()// Get center. Берём ценрт фотографии, все что помешается в заданный размер.
                .into(backgroundProfileImage);

        picasso.load(storageUrl + initiatorInformation.getIconUrl())
                .into(profileImage);


        // Проверка на то, чтобы полученные поля не были путыми.
        if (initiatorInformation.getName() != null){
            initiatorName.setText(initiatorInformation.getName());
        }

        if (initiatorInformation.getEmail() != null) {
            initiatorEmail.setText(initiatorInformation.getEmail());
        }

        if (initiatorInformation.getName() != null) {
            editInitiatorName.setText(initiatorInformation.getName());
        }

        if (initiatorInformation.getEmail() != null) {
            editInitiatorEmail.setText(initiatorInformation.getEmail());
        }

        if (initiatorInformation.getPhone() != null) {
            editInitiatorPhone.setText(initiatorInformation.getPhone());
        }

        if (initiatorInformation.getAddress() != null) {
            editInitiatorAddress.setText(initiatorInformation.getAddress());
        }

        if (true) {
            editInitiatorZipCode.setText(String.valueOf(initiatorInformation.getZipCode()));
        }

        if (initiatorInformation.getAbout() != null) {
            editAboutUs.setText(initiatorInformation.getAbout());
        }



    }
}
