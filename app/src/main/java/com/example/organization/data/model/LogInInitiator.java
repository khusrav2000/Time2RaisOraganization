package com.example.organization.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LogInInitiator {


    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("Address")
    @Expose
    private String address;

    @SerializedName("Lan")
    @Expose
    private double lan;

    @SerializedName("Lat")
    @Expose
    private double lat;

    @SerializedName("Stars")
    @Expose
    private float stars;

    @SerializedName("Phone")
    @Expose
    private String phone;

    @SerializedName("Name")
    @Expose
    private String initiatorName;

    @SerializedName("Token")
    @Expose
    private String token;

    public LogInInitiator(String email, String address, double lan, double lat, float stars, String phone, String orgname, String token) {
        this.email = email;
        this.address = address;
        this.lan = lan;
        this.lat = lat;
        this.stars = stars;
        this.phone = phone;
        this.initiatorName = orgname;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String orgname) {
        this.initiatorName = orgname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
