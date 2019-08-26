package com.example.organization.data.model.Restaurant;

import com.example.organization.data.model.Photo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.sql.StatementEvent;

public class RestaurantInformation {
    @SerializedName("OrgID")
    @Expose
    private int restaurantId;

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

    @SerializedName("Photos")
    @Expose
    private List<Photo> photos; // Фотографии ресторана.

    @SerializedName("TimeTable")
    @Expose
    private TimeTable timeTable;

    @SerializedName("Cashback")
    @Expose
    private List<CashBackItems> cashBackItems;

    @SerializedName("IconUrl")
    @Expose
    private String iconUrl;

    @SerializedName("BackgrounImageUrl")
    @Expose
    private String backgroundImageUrl;

    public RestaurantInformation(int restaurantId, String email, String address, double lan, double lat, double stars, String phone, String name, int zipCode, String about, List<Photo> photos, TimeTable timeTable, List<CashBackItems> cashBackItems, String iconUrl, String backgroundImageUrl) {
        this.restaurantId = restaurantId;
        this.email = email;
        this.address = address;
        this.lan = lan;
        this.lat = lat;
        this.stars = stars;
        this.phone = phone;
        this.name = name;
        this.zipCode = zipCode;
        this.about = about;
        this.photos = photos;
        this.timeTable = timeTable;
        this.cashBackItems = cashBackItems;
        this.iconUrl = iconUrl;
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
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

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public List<CashBackItems> getCashBackItems() {
        return cashBackItems;
    }

    public void setCashBackItems(List<CashBackItems> cashBackItems) {
        this.cashBackItems = cashBackItems;
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
        return "RestaurantInformation{" +
                "restaurantId=" + restaurantId +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", lan=" + lan +
                ", lat=" + lat +
                ", stars=" + stars +
                ", phone=" + phone +
                ", name=" + name +
                ", zipCode=" + zipCode +
                ", about='" + about + '\'' +
                ", photos=" + photos +
                ", timeTable=" + timeTable +
                ", cashBackItems=" + cashBackItems +
                ", iconUrl='" + iconUrl + '\'' +
                ", backgroundImageUrl='" + backgroundImageUrl + '\'' +
                '}';
    }
}
