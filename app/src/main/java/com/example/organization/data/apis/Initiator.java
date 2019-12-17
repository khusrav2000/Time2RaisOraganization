package com.example.organization.data.apis;

import android.provider.ContactsContract;

import com.example.organization.data.model.EventToOffer;
import com.example.organization.data.model.Events;
import com.example.organization.data.model.InitiatorInformation;
import com.example.organization.data.model.LogInInitiator;
import com.example.organization.data.model.Messages;
import com.example.organization.data.model.Photo;
import com.example.organization.data.model.Request;
import com.example.organization.data.model.Restaurant.RestaurantInformation;
import com.example.organization.data.model.SendInitiatorInformation;
import com.example.organization.data.model.SendMessage;
import com.example.organization.data.model.UniquensessEmail;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Initiator {

    // Отправка данных о организатое, и обратно получения данных о нём.
    @POST("/api/init")
    Call<InitiatorInformation> registration(@Body SendInitiatorInformation org);

    // Получения данных с сервера для организатора.
    @FormUrlEncoded
    @POST("/api/init/auth")
    Call<LogInInitiator> logInInitiator ( @Field("email")  String email, @Field("password") String password);

    // Проверка на то, что есть ли такая почта.
    // В ответ получим сообщения.
    @GET("/api/init/valid/{email}")
    Call<UniquensessEmail> validationEmil(@Path("email") String email);

    // Получения списка event-ов с лимитом в limit элементов.
    @GET("api/init/event/{limit}")
    Call< List<Events> > getEvents(@Header("token") String token, @Path("limit") int limit);

    // Получения списка event-ов организатора с лимитов в limit элементов.
    @GET("api/init/event/orgevents/{limit}")
    Call< List<Events> > getMyEvents(@Header("token") String token, @Path("limit") int limit);

    @GET("api/init/events/approval/{limit}")
    Call< List<EventToOffer> > getEventOffers(@Header("token") String token, @Path("limit") int limit);

    // Получения списка request-ов с лимитом в limit элементов.
    // Requests являеться моделю данных о ресторане.
    @GET("api/init/requests/{limit}")
    Call< List<RestaurantInformation> > getRequests(@Header("token") String token, @Path("limit") int limit);

    // Получения списка request-ов организатора с лимитов в limit элементов.
    @GET("api/init/request/orgrequests/{limit}")
    Call< List<Request> > getMyRequests(@Header("token") String token, @Path("limit") int limit);

    // Добавления request-ов.
    // В теле(Body) отправляется данные о request-е как модель Request.
    @POST("api/init/request/add")
    Call<Request> addRequest(@Header("token") String token, @Body Request requests);

    // Получения данных с фотографиями об request-е.
    @GET("api/init/request/{requestId}")
    Call<Request> getRequestByRequestId(@Header("token") String token, @Path("requestId") int requestId);

    // Получения данных и рисунков об Организаторе.
    @GET("api/init/")
    Call<InitiatorInformation> getProfileById(@Header("token") String token);

    // Обновления данных о организаторе.
    @POST("api/init/profil/update")
    Call<InitiatorInformation> setProfileInformation(@Header("token") String token,
                                                     @Body SendInitiatorInformation sendInitiatorInformation);

    // Полунения данные о ресторане по ID.
    @GET("api/init/res/{resId}")
    Call<RestaurantInformation> getRestaurantById(@Header("token") String token, @Path("resId") int resId);

    @Multipart
    @POST("/api/init/profil/backgroundimage")
    Call<Messages> addProfileCover(@Header("token") String token, @Part MultipartBody.Part file);

    @Multipart
    @POST("/api/init/profil/icon")
    Call<Messages> addProfileIcon(@Header("token") String token, @Part MultipartBody.Part file);

    @Multipart
    @POST("/api/init/photos/add")
    Call<List<Photo>> addGalleryPhotos(@Header("token") String token, @Part List<MultipartBody.Part> file);

    @POST("/api/init/msg/send")
    Call<Messages>  sendMessages(@Header("token") String token, @Body SendMessage message);

    @PUT("/api/init/deviceid")
    Call<Messages> updateDeviceId(@Header("token") String token, @Body SendInitiatorInformation information);


}
