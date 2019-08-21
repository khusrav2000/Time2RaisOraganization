package com.example.organization.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Events {

    @SerializedName("EventId")
    @Expose
    private String eventId;

    @SerializedName("OrgId")
    @Expose
    private String orgId;

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

    @SerializedName("Photos")
    @Expose
    private List<Photo> photos;

    public Events(String eventId, String orgId, String name, String date, String start, String end, String about, List<Photo> photos) {
        this.eventId = eventId;
        this.orgId = orgId;
        this.name = name;
        this.date = date;
        this.start = start;
        this.end = end;
        this.about = about;
        this.photos = photos;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "Events{" +
                "eventId='" + eventId + '\'' +
                ", orgId='" + orgId + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", about='" + about + '\'' +
                ", photos=" + photos +
                '}';
    }
}
