package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceDetail {

    @SerializedName("std")
    @Expose
    private String Standard;
    @SerializedName("intStudent_id")
    @Expose
    private Integer intStudentId;
    @SerializedName("dtdate")
    @Expose
    private String dtdate;
    @SerializedName("intUser_id")
    @Expose
    private Integer intUserId;


    @SerializedName("intDivision_id")
    @Expose
    private Integer intDivision_Id;
    @SerializedName("User_id")
    @Expose
    private Integer userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Standard_id")
    @Expose
    private String standardId;

    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("FCMToken")
    @Expose
    private String fCMToken;
    @SerializedName("Attstatus")
    @Expose
    private String attstatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dtstarttime")
    @Expose
    private String dtstarttime;
    @SerializedName("dtEndTime")
    @Expose
    private String dtEndTime;
    @SerializedName("intHomePhoneNo1")
    @Expose
    private String intHomePhoneNo1;
    @SerializedName("intActive_flg")
    @Expose
    private Boolean intActiveFlg;
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolId;

    private boolean selected;
    private boolean p_selected;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStandardId() {
        return standardId;
    }

    public void setStandardId(String standardId) {
        this.standardId = standardId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFCMToken() {
        return fCMToken;
    }

    public void setFCMToken(String fCMToken) {
        this.fCMToken = fCMToken;
    }

    public String getAttstatus() {
        return attstatus;
    }

    public void setAttstatus(String attstatus) {
        this.attstatus = attstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDtstarttime() {
        return dtstarttime;
    }

    public void setDtstarttime(String dtstarttime) {
        this.dtstarttime = dtstarttime;
    }

    public String getDtEndTime() {
        return dtEndTime;
    }

    public void setDtEndTime(String dtEndTime) {
        this.dtEndTime = dtEndTime;
    }

    public String getIntHomePhoneNo1() {
        return intHomePhoneNo1;
    }

    public void setIntHomePhoneNo1(String intHomePhoneNo1) {
        this.intHomePhoneNo1 = intHomePhoneNo1;
    }

    public Boolean getIntActiveFlg() {
        return intActiveFlg;
    }

    public void setIntActiveFlg(Boolean intActiveFlg) {
        this.intActiveFlg = intActiveFlg;
    }

    public Integer getIntSchoolId() {
        return intSchoolId;
    }

    public void setIntSchoolId(Integer intSchoolId) {
        this.intSchoolId = intSchoolId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isP_selected() {
        return p_selected;
    }

    public void setP_selected(boolean p_selected) {
        this.p_selected = p_selected;
    }

    public Integer getIntDivision_Id() {
        return intDivision_Id;
    }

    public void setIntDivision_Id(Integer intDivision_Id) {
        this.intDivision_Id = intDivision_Id;
    }


    public Integer getIntStudentId() {
        return intStudentId;
    }

    public void setIntStudentId(Integer intStudentId) {
        this.intStudentId = intStudentId;
    }

    public String getDtdate() {
        return dtdate;
    }

    public void setDtdate(String dtdate) {
        this.dtdate = dtdate;
    }


    public Integer getIntUserId() {
        return intUserId;
    }

    public void setIntUserId(Integer intUserId) {
        this.intUserId = intUserId;
    }

    public String getStandard() {
        return Standard;
    }

    public void setStandard(String standard) {
        Standard = standard;
    }

}
