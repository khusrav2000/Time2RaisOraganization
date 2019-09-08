package com.example.organization.data;

import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.LogInInitiator;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    // Поля для временного храния данных (Класса) об организаторе.
    static LogInInitiator initiator = null;

    public Result<LogInInitiator> login(String username, String password) {

        // Соединения с сервером с помощью класса NetworkClient.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator organization = retrofit.create(Initiator.class);

        // Создания запроса для получения данных с серевера.
        Call call = organization.logInInitiator(username, password);


        try {

            // Выполнения запроса созданного вверху.
            Response response = call.execute();

            if(response.isSuccessful()){
                initiator =(LogInInitiator) response.body();
            }

        } catch (IOException e) {
            return new Result.Error(new IOException("Error logging in", new Exception()));
        }

        if(initiator != null)
            return new Result.Success<>(initiator);

        else
            return new Result.Error(new IOException("Error logging in", new Exception()));
    }


    public static LogInInitiator getInitiator() {
        return initiator;
    }

    public static void logout() {
        initiator = null;
    }
}
