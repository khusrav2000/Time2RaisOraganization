package com.example.organization.data.apis;

import com.example.organization.ListEventsFragment;
import com.example.organization.data.model.Events;
import com.example.organization.data.model.InitiatorInformation;
import com.example.organization.data.model.LogInInitiator;
import com.example.organization.data.model.SendInitiatorInformation;
import com.example.organization.data.model.UniquensessEmail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Initiator {

    // Отправка данных о организатое, и обратно получения данных о нём.
    @POST("/api/init")
    Call<InitiatorInformation> registration(@Body SendInitiatorInformation org);

    // Получения данных с сервера для организатора.
    @FormUrlEncoded
    @POST("/api/init/auth")
    Call<LogInInitiator> logInInitiator ( @Field("email")  String email, @Field("password") String password ) ;

    // Проверка на то, что есть ли такая почта.
    // В ответ получим сообщения.
    @GET("/api/init/valid/{email}")
    Call<UniquensessEmail> validationEmil(@Path("email") String email);


    @GET("api/init/event/{limit}")
    Call< List<Events> > getEvents(@Header("token") String token, @Path("limit") int limit);

}
