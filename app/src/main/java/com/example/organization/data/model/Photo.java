package com.example.organization.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("Url")
    @Expose
    private  String url;

    @SerializedName("Ismain")
    @Expose
    private  boolean ismain;

    @SerializedName("Type")
    @Expose
    private  String type;

    public Photo(String url, boolean ismain, String type) {
        this.url = url;
        this.ismain = ismain;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isIsmain() {
        return ismain;
    }

    public void setIsmain(boolean ismain) {
        this.ismain = ismain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "url='" + url + '\'' +
                ", ismain=" + ismain +
                ", type='" + type + '\'' +
                '}';
    }
}
