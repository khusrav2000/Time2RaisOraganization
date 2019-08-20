package com.example.organization.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendInitiatorInformation {

    @SerializedName("InitID")
    @Expose
    private int initId;

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("Password")
    @Expose
    private String password;

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

    @SerializedName("GaleryUrl")
    @Expose
    private String galleryUrl;

    @SerializedName("IconUrl")
    @Expose
    private String iconUrl;

    @SerializedName("BackgrounImageUrl")
    @Expose
    private String backgroundImageUrl;

    public SendInitiatorInformation(int initId, String email, String password, String address, double lan, double lat, double stars, String phone, String name, int zipCode, String about, String galleryUrl, String iconUrl, String backgroundImageUrl) {
        this.initId = initId;
        this.email = email;
        this.password = password;
        this.address = address;
        this.lan = lan;
        this.lat = lat;
        this.stars = stars;
        this.phone = phone;
        this.name = name;
        this.zipCode = zipCode;
        this.about = about;
        this.galleryUrl = galleryUrl;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getGalleryUrl() {
        return galleryUrl;
    }

    public void setGalleryUrl(String galleryUrl) {
        this.galleryUrl = galleryUrl;
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
        return "SendInitiatorInformation{" +
                "initId=" + initId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", lan=" + lan +
                ", lat=" + lat +
                ", stars=" + stars +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", zipCode=" + zipCode +
                ", about='" + about + '\'' +
                ", galleryUrl='" + galleryUrl + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", backgroundImageUrl='" + backgroundImageUrl + '\'' +
                '}';
    }
}
