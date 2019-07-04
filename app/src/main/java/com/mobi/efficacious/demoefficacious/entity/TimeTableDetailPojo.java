package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeTableDetailPojo {

    @SerializedName("TimeTableDetail")
    @Expose
    private List<TimeTableDetail> timeTableDetail = null;

    public List<TimeTableDetail> getTimeTableDetail() {
        return timeTableDetail;
    }

    public void setTimeTableDetail(List<TimeTableDetail> timeTableDetail) {
        this.timeTableDetail = timeTableDetail;
    }

}