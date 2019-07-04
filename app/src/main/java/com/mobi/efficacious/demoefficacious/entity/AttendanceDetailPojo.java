package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceDetailPojo {

    @SerializedName("AttendanceDetail")
    @Expose
    private List<AttendanceDetail> attendanceDetail = null;

    public List<AttendanceDetail> getAttendanceDetail() {
        return attendanceDetail;
    }

    public void setAttendanceDetail(List<AttendanceDetail> attendanceDetail) {
        this.attendanceDetail = attendanceDetail;
    }

}
