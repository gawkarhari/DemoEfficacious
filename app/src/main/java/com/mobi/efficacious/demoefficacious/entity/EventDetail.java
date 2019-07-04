package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventDetail {

    @SerializedName("intStandard_id")
    @Expose
    private Integer intStandard_Id;
    @SerializedName("intDivision_id")
    @Expose
    private Integer intDivision_Id;

    @SerializedName("intUser_id")
    @Expose
    private Integer intUserId;
    @SerializedName("intUserType_id")
    @Expose
    private Integer intUserTypeId;
    @SerializedName("intAcademic_id")
    @Expose
    private Integer intAcademicId;
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolId;
    @SerializedName("vchStandard_id")
    @Expose
    private String vchStandardId;
    @SerializedName("dtRegistrartionStartDate")
    @Expose
    private String dtRegistrartionStartDate;
    @SerializedName("dtRegistrationEndDate")
    @Expose
    private String dtRegistrationEndDate;
    @SerializedName("dtEventStartDate")
    @Expose
    private String dtEventStartDate;
    @SerializedName("dtEventEndDate")
    @Expose
    private String dtEventEndDate;
    @SerializedName("vchEventName")
    @Expose
    private String vchEventName;
    @SerializedName("vchEventFees")
    @Expose
    private String vchEventFees;
    @SerializedName("vchEventDescription")
    @Expose
    private String vchEventDescription;
    @SerializedName("intEvent_id")
    @Expose
    private Integer intEventId;

    public String getDtRegistrartionStartDate() {
        return dtRegistrartionStartDate;
    }

    public void setDtRegistrartionStartDate(String dtRegistrartionStartDate) {
        this.dtRegistrartionStartDate = dtRegistrartionStartDate;
    }

    public String getDtRegistrationEndDate() {
        return dtRegistrationEndDate;
    }

    public void setDtRegistrationEndDate(String dtRegistrationEndDate) {
        this.dtRegistrationEndDate = dtRegistrationEndDate;
    }

    public String getDtEventStartDate() {
        return dtEventStartDate;
    }

    public void setDtEventStartDate(String dtEventStartDate) {
        this.dtEventStartDate = dtEventStartDate;
    }

    public String getDtEventEndDate() {
        return dtEventEndDate;
    }

    public void setDtEventEndDate(String dtEventEndDate) {
        this.dtEventEndDate = dtEventEndDate;
    }

    public String getVchEventName() {
        return vchEventName;
    }

    public void setVchEventName(String vchEventName) {
        this.vchEventName = vchEventName;
    }

    public String getVchEventFees() {
        return vchEventFees;
    }

    public void setVchEventFees(String vchEventFees) {
        this.vchEventFees = vchEventFees;
    }

    public String getVchEventDescription() {
        return vchEventDescription;
    }

    public void setVchEventDescription(String vchEventDescription) {
        this.vchEventDescription = vchEventDescription;
    }

    public Integer getIntEventId() {
        return intEventId;
    }

    public void setIntEventId(Integer intEventId) {
        this.intEventId = intEventId;
    }

    public EventDetail(String vchStandardId, String dtRegistrartionStartDate, String dtRegistrationEndDate, String dtEventStartDate, String dtEventEndDate, String vchEventName, String vchEventFees, String vchEventDescription,Integer IntSchoolId,Integer intUserid,Integer intAcademicid,Integer intUserTypeid) {
        this.vchStandardId = vchStandardId;
        this.dtRegistrartionStartDate = dtRegistrartionStartDate;
        this.dtRegistrationEndDate = dtRegistrationEndDate;
        this.dtEventStartDate = dtEventStartDate;
        this.dtEventEndDate = dtEventEndDate;
        this.vchEventName = vchEventName;
        this.vchEventFees = vchEventFees;
        this.vchEventDescription = vchEventDescription;
        this.intSchoolId=IntSchoolId;
        this.intUserId=intUserid;
        this.intAcademicId=intAcademicid;
        this.intUserTypeId=intUserTypeid;
    }

    public EventDetail(String vchStandardId, Integer intDivision_Id, Integer intUserId, Integer intUserTypeId, Integer intAcademicId, Integer intSchoolId, Integer intEventId) {
        this.intStandard_Id = intStandard_Id;
        this.intDivision_Id = intDivision_Id;
        this.intUserId = intUserId;
        this.intUserTypeId = intUserTypeId;
        this.intAcademicId = intAcademicId;
        this.intSchoolId = intSchoolId;
        this.intEventId = intEventId;
    }

    public String getVchStandardId() {
        return vchStandardId;
    }

    public void setVchStandardId(String vchStandardId) {
        this.vchStandardId = vchStandardId;
    }

    public Integer getIntSchoolId() {
        return intSchoolId;
    }

    public void setIntSchoolId(Integer intSchoolId) {
        this.intSchoolId = intSchoolId;
    }
    public Integer getIntUserId() {
        return intUserId;
    }

    public void setIntUserId(Integer intUserId) {
        this.intUserId = intUserId;
    }

    public Integer getIntUserTypeId() {
        return intUserTypeId;
    }

    public void setIntUserTypeId(Integer intUserTypeId) {
        this.intUserTypeId = intUserTypeId;
    }

    public Integer getIntAcademicId() {
        return intAcademicId;
    }

    public void setIntAcademicId(Integer intAcademicId) {
        this.intAcademicId = intAcademicId;
    }

    public Integer getIntStandard_Id() {
        return intStandard_Id;
    }

    public void setIntStandard_Id(Integer intStandard_Id) {
        this.intStandard_Id = intStandard_Id;
    }

    public Integer getIntDivision_Id() {
        return intDivision_Id;
    }

    public void setIntDivision_Id(Integer intDivision_Id) {
        this.intDivision_Id = intDivision_Id;
    }
}