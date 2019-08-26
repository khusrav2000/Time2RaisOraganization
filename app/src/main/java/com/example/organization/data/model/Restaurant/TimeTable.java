package com.example.organization.data.model.Restaurant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeTable {
    @SerializedName("TimeTableId")
    @Expose
    private int timeTableId;

    @SerializedName("OrgID")
    @Expose
    private int orgId;

    @SerializedName("Days")
    @Expose
    private List<WeekDay> weekDays;

    public TimeTable(int timeTableId, int orgId, List<WeekDay> weekDays) {
        this.timeTableId = timeTableId;
        this.orgId = orgId;
        this.weekDays = weekDays;
    }

    public int getTimeTableId() {
        return timeTableId;
    }

    public void setTimeTableId(int timeTableId) {
        this.timeTableId = timeTableId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public List<WeekDay> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(List<WeekDay> weekDays) {
        this.weekDays = weekDays;
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "timeTableId=" + timeTableId +
                ", orgId=" + orgId +
                ", weekDay=" + weekDays +
                '}';
    }
}
