package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeTableDetail {

    @SerializedName("intExamSchedule_id")
    @Expose
    private Integer intExamScheduleId;
    @SerializedName("dtExamination_date")
    @Expose
    private String dtExaminationDate;
    @SerializedName("theDayName")
    @Expose
    private String theDayName;
    @SerializedName("dtExamination_FromTime")
    @Expose
    private String dtExaminationFromTime;
    @SerializedName("dtExamination_ToTime")
    @Expose
    private String dtExaminationToTime;
    @SerializedName("dtExamination_Time")
    @Expose
    private String dtExaminationTime;
    @SerializedName("intsubject_id")
    @Expose
    private Integer intsubjectId;
    @SerializedName("vchExamination_name")
    @Expose
    private String vchExaminationName;

    @SerializedName("vchDay")
    @Expose
    private String vchDay;
    @SerializedName("vchLecture_name")
    @Expose
    private String vchLectureName;
    @SerializedName("vchSubjectName")
    @Expose
    private String vchSubjectName;
    @SerializedName("FrmTm")
    @Expose
    private String frmTm;
    @SerializedName("ToTm")
    @Expose
    private String toTm;
    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("btrecess")
    @Expose
    private Boolean btrecess;
    @SerializedName("T_Name")
    @Expose
    private String tName;
    @SerializedName("vchStandard_name")
    @Expose
    private String vchStandardName;

    public String getVchDay() {
        return vchDay;
    }

    public void setVchDay(String vchDay) {
        this.vchDay = vchDay;
    }

    public String getVchLectureName() {
        return vchLectureName;
    }

    public void setVchLectureName(String vchLectureName) {
        this.vchLectureName = vchLectureName;
    }

    public String getVchSubjectName() {
        return vchSubjectName;
    }

    public void setVchSubjectName(String vchSubjectName) {
        this.vchSubjectName = vchSubjectName;
    }

    public String getFrmTm() {
        return frmTm;
    }

    public void setFrmTm(String frmTm) {
        this.frmTm = frmTm;
    }

    public String getToTm() {
        return toTm;
    }

    public void setToTm(String toTm) {
        this.toTm = toTm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getBtrecess() {
        return btrecess;
    }

    public void setBtrecess(Boolean btrecess) {
        this.btrecess = btrecess;
    }

    public String getTName() {
        return tName;
    }

    public void setTName(String tName) {
        this.tName = tName;
    }

    public String getVchStandardName() {
        return vchStandardName;
    }

    public void setVchStandardName(String vchStandardName) {
        this.vchStandardName = vchStandardName;
    }
    public Integer getIntExamScheduleId() {
        return intExamScheduleId;
    }

    public void setIntExamScheduleId(Integer intExamScheduleId) {
        this.intExamScheduleId = intExamScheduleId;
    }

    public String getDtExaminationDate() {
        return dtExaminationDate;
    }

    public void setDtExaminationDate(String dtExaminationDate) {
        this.dtExaminationDate = dtExaminationDate;
    }

    public String getTheDayName() {
        return theDayName;
    }

    public void setTheDayName(String theDayName) {
        this.theDayName = theDayName;
    }

    public String getDtExaminationFromTime() {
        return dtExaminationFromTime;
    }

    public void setDtExaminationFromTime(String dtExaminationFromTime) {
        this.dtExaminationFromTime = dtExaminationFromTime;
    }

    public String getDtExaminationToTime() {
        return dtExaminationToTime;
    }

    public void setDtExaminationToTime(String dtExaminationToTime) {
        this.dtExaminationToTime = dtExaminationToTime;
    }

    public String getDtExaminationTime() {
        return dtExaminationTime;
    }

    public void setDtExaminationTime(String dtExaminationTime) {
        this.dtExaminationTime = dtExaminationTime;
    }

    public Integer getIntsubjectId() {
        return intsubjectId;
    }

    public void setIntsubjectId(Integer intsubjectId) {
        this.intsubjectId = intsubjectId;
    }


    public String getVchExaminationName() {
        return vchExaminationName;
    }

    public void setVchExaminationName(String vchExaminationName) {
        this.vchExaminationName = vchExaminationName;
    }
}