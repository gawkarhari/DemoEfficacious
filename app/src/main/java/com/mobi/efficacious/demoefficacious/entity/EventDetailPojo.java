package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventDetailPojo {

    @SerializedName("EventDetail")
    @Expose
    private List<EventDetail> eventDetail = null;

    public List<EventDetail> getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(List<EventDetail> eventDetail) {
        this.eventDetail = eventDetail;
    }

}