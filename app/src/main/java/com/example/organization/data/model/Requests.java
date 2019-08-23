package com.example.organization.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Requests {
    @SerializedName("RequestID")
    @Expose
    private int requestId;

    @SerializedName("InitID")
    @Expose
    private int initID;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("Start")
    @Expose
    private String start;

    @SerializedName("End")
    @Expose
    private String end;

    @SerializedName("About")
    @Expose
    private String about;

    @SerializedName("IsActive")
    @Expose
    private boolean isActive;

    public Requests(int requestId, int initID, String name, String date, String start, String end, String about, boolean isActive) {
        this.requestId = requestId;
        this.initID = initID;
        this.name = name;
        this.date = date;
        this.start = start;
        this.end = end;
        this.about = about;
        this.isActive = isActive;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getInitID() {
        return initID;
    }

    public void setInitID(int initID) {
        this.initID = initID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Requests{" +
                "requestId=" + requestId +
                ", initID=" + initID +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", about='" + about + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
