package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NoticeboardDetailsPojo {

    @SerializedName("NoticeboardDetails")
    @Expose
    private List<NoticeboardDetail> noticeboardDetails = null;

    public List<NoticeboardDetail> getNoticeboardDetails() {
        return noticeboardDetails;
    }

    public void setNoticeboardDetails(List<NoticeboardDetail> noticeboardDetails) {
        this.noticeboardDetails = noticeboardDetails;
    }

}
