package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticeboardDetail {

    @SerializedName("intNotice_id")
    @Expose
    private Integer intNoticeId;
    @SerializedName("intUserType_id")
    @Expose
    private Integer intUserTypeId;
    @SerializedName("intStandard_id")
    @Expose
    private Integer intStandardId;
    @SerializedName("intDivision_id")
    @Expose
    private Integer intDivisionId;
    @SerializedName("intStudent_id")
    @Expose
    private Integer intStudentId;
    @SerializedName("intDepartment_id")
    @Expose
    private Integer intDepartmentId;
    @SerializedName("intTeacher_id")
    @Expose
    private Integer intTeacherId;
    @SerializedName("dtIssue_date")
    @Expose
    private String dtIssueDate;
    @SerializedName("dtEnd_date")
    @Expose
    private String dtEndDate;
    @SerializedName("vchSubject")
    @Expose
    private String vchSubject;
    @SerializedName("vchNotice")
    @Expose
    private String vchNotice;
    @SerializedName("intInserted_by")
    @Expose
    private Integer intInsertedBy;
    @SerializedName("InsertIP")
    @Expose
    private String insertIP;
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolId;
    @SerializedName("intAcademic_id")
    @Expose
    private Integer intAcademicId;

    public Integer getIntNoticeId() {
        return intNoticeId;
    }

    public void setIntNoticeId(Integer intNoticeId) {
        this.intNoticeId = intNoticeId;
    }

    public Integer getIntUserTypeId() {
        return intUserTypeId;
    }

    public void setIntUserTypeId(Integer intUserTypeId) {
        this.intUserTypeId = intUserTypeId;
    }

    public Integer getIntStandardId() {
        return intStandardId;
    }

    public void setIntStandardId(Integer intStandardId) {
        this.intStandardId = intStandardId;
    }

    public Integer getIntDivisionId() {
        return intDivisionId;
    }

    public void setIntDivisionId(Integer intDivisionId) {
        this.intDivisionId = intDivisionId;
    }

    public Integer getIntStudentId() {
        return intStudentId;
    }

    public void setIntStudentId(Integer intStudentId) {
        this.intStudentId = intStudentId;
    }

    public Integer getIntDepartmentId() {
        return intDepartmentId;
    }

    public void setIntDepartmentId(Integer intDepartmentId) {
        this.intDepartmentId = intDepartmentId;
    }

    public Integer getIntTeacherId() {
        return intTeacherId;
    }

    public void setIntTeacherId(Integer intTeacherId) {
        this.intTeacherId = intTeacherId;
    }

    public String getDtIssueDate() {
        return dtIssueDate;
    }

    public void setDtIssueDate(String dtIssueDate) {
        this.dtIssueDate = dtIssueDate;
    }

    public String getDtEndDate() {
        return dtEndDate;
    }

    public void setDtEndDate(String dtEndDate) {
        this.dtEndDate = dtEndDate;
    }

    public String getVchSubject() {
        return vchSubject;
    }

    public void setVchSubject(String vchSubject) {
        this.vchSubject = vchSubject;
    }

    public String getVchNotice() {
        return vchNotice;
    }

    public void setVchNotice(String vchNotice) {
        this.vchNotice = vchNotice;
    }

    public Integer getIntInsertedBy() {
        return intInsertedBy;
    }

    public void setIntInsertedBy(Integer intInsertedBy) {
        this.intInsertedBy = intInsertedBy;
    }

    public String getInsertIP() {
        return insertIP;
    }

    public void setInsertIP(String insertIP) {
        this.insertIP = insertIP;
    }

    public Integer getIntSchoolId() {
        return intSchoolId;
    }

    public void setIntSchoolId(Integer intSchoolId) {
        this.intSchoolId = intSchoolId;
    }

    public Integer getIntAcademicId() {
        return intAcademicId;
    }

    public void setIntAcademicId(Integer intAcademicId) {
        this.intAcademicId = intAcademicId;
    }

    public NoticeboardDetail(Integer intUserTypeId, Integer intStandardId, Integer intDepartmentId, Integer intTeacherId, String dtIssueDate, String dtEndDate, String vchSubject, String vchNotice, Integer intInsertedBy, String insertIP, Integer intSchoolId) {
        this.intUserTypeId = intUserTypeId;
        this.intStandardId = intStandardId;
        this.intDepartmentId = intDepartmentId;
        this.intTeacherId = intTeacherId;
        this.dtIssueDate = dtIssueDate;
        this.dtEndDate = dtEndDate;
        this.vchSubject = vchSubject;
        this.vchNotice = vchNotice;
        this.intInsertedBy = intInsertedBy;
        this.insertIP = insertIP;
        this.intSchoolId = intSchoolId;
    }
}