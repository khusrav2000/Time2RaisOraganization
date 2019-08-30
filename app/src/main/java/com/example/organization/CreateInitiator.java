package com.example.organization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.InitiatorInformation;
import com.example.organization.data.model.SendInitiatorInformation;
import com.example.organization.ui.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateInitiator extends AppCompatActivity {

    Button initiatorLogin;
    Button initiatorRegister;
    EditText initiatorName;
    EditText initiatorEmail;
    EditText initiatorZipCode;
    EditText initiatorPhone;
    EditText initiatorPassword;
    EditText initiatorRepeatPassword;

    CheckBox agreeWithTerms;


    String name;
    String email;
    String phone;
    int zipCode;
    boolean agree;
    String password;
    String repeatPassword;

    private boolean emailIsExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_initiator);


        // Данные вводимыми организатором.
        initiatorLogin              = findViewById(R.id.initiator_login);
        initiatorRegister           = findViewById(R.id.initiator_register);
        initiatorName               = findViewById(R.id.initiator_name);
        initiatorEmail              = findViewById(R.id.initiator_email);
        initiatorPhone              = findViewById(R.id.initiator_phone);
        initiatorZipCode            = findViewById(R.id.initiator_zip_code);
        initiatorPassword           = findViewById(R.id.initiator_password);
        initiatorRepeatPassword     = findViewById(R.id.initiator_repeat_password);
        agreeWithTerms              = findViewById(R.id.agree_with_terms);

        initiatorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });

        initiatorRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Сохранения данные о организаторе в полях.
                name = initiatorName.getText().toString().trim();
                email = initiatorEmail.getText().toString().trim();
                phone = initiatorPhone.getText().toString().trim();
                zipCode = Integer.parseInt(initiatorZipCode.getText().toString());
                password = initiatorPassword.getText().toString().trim();
                repeatPassword = initiatorRepeatPassword.getText().toString().trim();
                agree = agreeWithTerms.isChecked();

                if (validateData()){

                    System.out.println("--------------------------------------------------------");
                    // Создания модели с данными об организаторе.

                    SendInitiatorInformation sendInitiatorInformation = new SendInitiatorInformation(
                            0, email, password, "", 0.0, 0.0, 0.0,
                            phone, name, zipCode, "", "", "", ""
                    );

                    Retrofit retrofit = NetworkClient.getRetrofitClient();
                    Initiator organization = retrofit.create(Initiator.class);
                    Call call = organization.registration(sendInitiatorInformation);

                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.body() != null){
                                InitiatorInformation initiatorInfo = (InitiatorInformation) response.body();
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            System.out.println(t.getStackTrace());
                        }
                    });
                    startLogin();
                }

            }
        });
    }

    private boolean validateData() {
        String validRexEmail = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        String validRexPhone = "(\\+1|0)[0-9]{10}";

        if (name.equals("")) {

            initiatorName.setError( getString(R.string.initiator_name_error));
            initiatorName.requestFocus();
            return false;
        }

        // Проверка на правилность почты.
        if (email.equals("")) {

            initiatorEmail.setError( getString(R.string.email_error));
            initiatorEmail.requestFocus();
            return false;
        }
        else if (!email.matches(validRexEmail)){
            initiatorEmail.setError( getString(R.string.incorrect_email));
            initiatorEmail.requestFocus();
            return false;
        }

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator initiator = retrofit.create(Initiator.class);
        Call call = initiator.validationEmil(email);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                System.out.println("code="+response.code());
                if(response.code() != 404) {
                    initiatorEmail.setError( getString(R.string.email_error_exists));
                    initiatorEmail.requestFocus();
                    emailIsExists = true;
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                emailIsExists = true;
            }
        });

        if (emailIsExists){
            emailIsExists = false;
            return false;
        }

        // Проверка на правилность номера телефона.
        if (phone.equals("")) {

            initiatorPhone.setError( getString(R.string.phone_error));
            initiatorPhone.requestFocus();
            return false;
        }
        else if (!phone.matches(validRexPhone)){
            initiatorPhone.setError( getString(R.string.incorrect_phone));
            initiatorPhone.requestFocus();
            return false;
        }

        if (password.equals("")) {

            initiatorPassword.setError(getString(R.string.password_error));
            initiatorPassword.requestFocus();
            return false;
        }
        else if (password.length() < 6){
            initiatorPassword.setError(getString(R.string.password_error_count));
            initiatorPassword.requestFocus();
            return false;
        }

        if (repeatPassword.equals("")) {

            initiatorRepeatPassword.setError( getString(R.string.repest_password_error));
            initiatorRepeatPassword.requestFocus();
            return false;
        }
        else if (!repeatPassword.equals(password)) {

            initiatorRepeatPassword.setError( getString(R.string.confirm_password));
            initiatorRepeatPassword.requestFocus();
            return false;
        }

        if (!agree){
            agreeWithTerms.setError(getString(R.string.agree_with_terms_error));
            agreeWithTerms.requestFocus();
            return false;
        }


        return true;
    }

    private void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
