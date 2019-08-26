package com.example.organization.data.model.Restaurant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeekDay {
    @SerializedName("Number")
    @Expose
    private int number;

    @SerializedName("Start")
    @Expose
    private String start;

    @SerializedName("End")
    @Expose
    private String end;

    public WeekDay(int number, String start, String end) {
        this.number = number;
        this.start = start;
        this.end = end;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    @Override
    public String toString() {
        return "WeekDay{" +
                "number=" + number +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
