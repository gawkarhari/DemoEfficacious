package com.mobi.efficacious.demoefficacious.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarkAttendence {

    @SerializedName("intUserType_id")
    @Expose
    private Integer intUserTypeId;
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolId;
    @SerializedName("Standard_Id")
    @Expose
    private String standardId;
    @SerializedName("intDivision_id")
    @Expose
    private Integer intDivisionId;
    @SerializedName("intAcademic_id")
    @Expose
    private Integer intAcademicId;
    @SerializedName("dtDate")
    @Expose
    private String dtDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("FCMToken")
    @Expose
    private String fCMToken;

    public MarkAttendence(Integer intUserTypeId, Integer intSchoolId, String standardId, Integer intDivisionId, Integer intAcademicId, String dtDate, String status, Integer userId, String fCMToken) {
        this.intUserTypeId = intUserTypeId;
        this.intSchoolId = intSchoolId;
        this.standardId = standardId;
        this.intDivisionId = intDivisionId;
        this.intAcademicId = intAcademicId;
        this.dtDate = dtDate;
        this.status = status;
        this.userId = userId;
        this.fCMToken = fCMToken;
    }

    public MarkAttendence(Integer intUserTypeId, Integer intSchoolId, Integer intAcademicId, String dtDate, String status, Integer userId, String fCMToken) {
        this.intUserTypeId = intUserTypeId;
        this.intSchoolId = intSchoolId;
        this.intAcademicId = intAcademicId;
        this.dtDate = dtDate;
        this.status = status;
        this.userId = userId;
        this.fCMToken = fCMToken;
    }

    public Integer getIntUserTypeId() {
        return intUserTypeId;
    }

    public void setIntUserTypeId(Integer intUserTypeId) {
        this.intUserTypeId = intUserTypeId;
    }

    public Integer getIntSchoolId() {
        return intSchoolId;
    }

    public void setIntSchoolId(Integer intSchoolId) {
        this.intSchoolId = intSchoolId;
    }

    public String getStandardId() {
        return standardId;
    }

    public void setStandardId(String standardId) {
        this.standardId = standardId;
    }

    public Integer getIntDivisionId() {
        return intDivisionId;
    }

    public void setIntDivisionId(Integer intDivisionId) {
        this.intDivisionId = intDivisionId;
    }

    public Integer getIntAcademicId() {
        return intAcademicId;
    }

    public void setIntAcademicId(Integer intAcademicId) {
        this.intAcademicId = intAcademicId;
    }

    public String getDtDate() {
        return dtDate;
    }

    public void setDtDate(String dtDate) {
        this.dtDate = dtDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFCMToken() {
        return fCMToken;
    }

    public void setFCMToken(String fCMToken) {
        this.fCMToken = fCMToken;
    }

}