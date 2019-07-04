package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveDetailPojo {

    @SerializedName("LeaveDetail")
    @Expose
    private List<LeaveDetail> leaveDetail = null;

    public List<LeaveDetail> getLeaveDetail() {
        return leaveDetail;
    }

    public void setLeaveDetail(List<LeaveDetail> leaveDetail) {
        this.leaveDetail = leaveDetail;
    }

}