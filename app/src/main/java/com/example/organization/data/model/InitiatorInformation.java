package com.example.organization.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InitiatorInformation {

    @SerializedName("InitID")
    @Expose
    private int initId;

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
    private double stars;

    @SerializedName("Phone")
    @Expose
    private String phone;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("ZipCode")
    @Expose
    private int zipCode;

    @SerializedName("About")
    @Expose
    private String about;

    @SerializedName("IconUrl")
    @Expose
    private String iconUrl;

    @SerializedName("BackgrounImageUrl")
    @Expose
    private String backgroundImageUrl;

    public InitiatorInformation(int initId, String email, String adress, double lan, double lat, double stars, String phone, String name, int zipCode, String about, String iconUrl, String backgroundImageUrl) {
        this.initId = initId;
        this.email = email;
        this.address = adress;
        this.lan = lan;
        this.lat = lat;
        this.stars = stars;
        this.phone = phone;
        this.name = name;
        this.zipCode = zipCode;
        this.about = about;
        this.iconUrl = iconUrl;
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public int getInitId() {
        return initId;
    }

    public void setInitId(int initId) {
        this.initId = initId;
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

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    @Override
    public String toString() {
        return "InitiatorInformation{" +
                "initId=" + initId +
                ", email='" + email + '\'' +
                ", adress='" + address + '\'' +
                ", lan=" + lan +
                ", lat=" + lat +
                ", stars=" + stars +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", zipCode=" + zipCode +
                ", about='" + about + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", backgroundImageUrl='" + backgroundImageUrl + '\'' +
                '}';
    }
}
