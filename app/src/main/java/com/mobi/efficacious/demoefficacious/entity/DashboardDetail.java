package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDetail {
    public String getIntBusAlert1() {
        return IntBusAlert1;
    }

    public void setIntBusAlert1(String intBusAlert1) {
        IntBusAlert1 = intBusAlert1;
    }

    public String getIntBusAlert2() {
        return IntBusAlert2;
    }

    public void setIntBusAlert2(String intBusAlert2) {
        IntBusAlert2 = intBusAlert2;
    }

    public String getFCMTokeN() {
        return FCMTokeN;
    }

    public void setFCMTokeN(String FCMTokeN) {
        this.FCMTokeN = FCMTokeN;
    }

    public String getIntmobileno() {
        return intmobileno;
    }

    public void setIntmobileno(String intmobileno) {
        this.intmobileno = intmobileno;
    }

    @SerializedName("intmobileNo")
    @Expose
    private String intmobileno;
    @SerializedName("intBusAlert1")
    @Expose
    private String IntBusAlert1;
    @SerializedName("intBusAlert2")
    @Expose
    private String IntBusAlert2;
    @SerializedName("FCMToken")
    @Expose
    private String FCMTokeN;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("holiday")
    @Expose
    private String holiday;
    @SerializedName("dtFromDate")
    @Expose
    private String dtFromDate;
    @SerializedName("dtToDate")
    @Expose
    private String dtToDate;
    @SerializedName("intNoOfDay")
    @Expose
    private Integer intNoOfDay;
    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Present")
    @Expose
    private Integer present;
    @SerializedName("Absent")
    @Expose
    private Integer absent;
    @SerializedName("Leave")
    @Expose
    private Integer leave;
    @SerializedName("intschool_id")
    @Expose
    private Integer intschoolId;
    @SerializedName("Standard")
    @Expose
    private String standard;
    @SerializedName("Division")
    @Expose
    private String division;
    @SerializedName("Male")
    @Expose
    private Integer male;
    @SerializedName("Female")
    @Expose
    private Integer female;
    @SerializedName("Total")
    @Expose
    private Integer total;
    @SerializedName("Subject")
    @Expose
    private String subject;
    @SerializedName("intNotice_id")
    @Expose
    private Integer intNoticeId;
    @SerializedName("Notice")
    @Expose
    private String notice;
    @SerializedName("Issue_Date")
    @Expose
    private String issueDate;
    @SerializedName("End_Date")
    @Expose
    private String endDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getDtFromDate() {
        return dtFromDate;
    }

    public void setDtFromDate(String dtFromDate) {
        this.dtFromDate = dtFromDate;
    }

    public String getDtToDate() {
        return dtToDate;
    }

    public void setDtToDate(String dtToDate) {
        this.dtToDate = dtToDate;
    }

    public Integer getIntNoOfDay() {
        return intNoOfDay;
    }

    public void setIntNoOfDay(Integer intNoOfDay) {
        this.intNoOfDay = intNoOfDay;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPresent() {
        return present;
    }

    public void setPresent(Integer present) {
        this.present = present;
    }

    public Integer getAbsent() {
        return absent;
    }

    public void setAbsent(Integer absent) {
        this.absent = absent;
    }

    public Integer getLeave() {
        return leave;
    }

    public void setLeave(Integer leave) {
        this.leave = leave;
    }

    public Integer getIntschoolId() {
        return intschoolId;
    }

    public void setIntschoolId(Integer intschoolId) {
        this.intschoolId = intschoolId;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Integer getMale() {
        return male;
    }

    public void setMale(Integer male) {
        this.male = male;
    }

    public Integer getFemale() {
        return female;
    }

    public void setFemale(Integer female) {
        this.female = female;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getIntNoticeId() {
        return intNoticeId;
    }

    public void setIntNoticeId(Integer intNoticeId) {
        this.intNoticeId = intNoticeId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public DashboardDetail(String subject, String notice, String issueDate, String endDate) {
        this.subject = subject;
        this.notice = notice;
        this.issueDate = issueDate;
        this.endDate = endDate;
    }
}
