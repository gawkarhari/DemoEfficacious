package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveDetail {

    public Integer getIntUserTypeId() {
        return intUserTypeId;
    }

    public void setIntUserTypeId(Integer intUserTypeId) {
        this.intUserTypeId = intUserTypeId;
    }

    public Integer getIntTypeId() {
        return intTypeId;
    }

    public void setIntTypeId(Integer intTypeId) {
        this.intTypeId = intTypeId;
    }

    public Integer getIntUserId() {
        return intUserId;
    }

    public void setIntUserId(Integer intUserId) {
        this.intUserId = intUserId;
    }

    public Integer getIntSchoolId() {
        return intSchoolId;
    }

    public void setIntSchoolId(Integer intSchoolId) {
        this.intSchoolId = intSchoolId;
    }

    public Integer getIntLeaveTypeId() {
        return intLeaveTypeId;
    }

    public void setIntLeaveTypeId(Integer intLeaveTypeId) {
        this.intLeaveTypeId = intLeaveTypeId;
    }

    public Integer getIntAcademicId() {
        return intAcademicId;
    }

    public void setIntAcademicId(Integer intAcademicId) {
        this.intAcademicId = intAcademicId;
    }

    @SerializedName("intUserType_id")
    @Expose
    private Integer intUserTypeId;
    @SerializedName("intType_id")
    @Expose
    private Integer intTypeId;
    @SerializedName("intUser_id")
    @Expose
    private Integer intUserId;
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolId;
    @SerializedName("intLeaveType_id")
    @Expose
    private Integer intLeaveTypeId;
    @SerializedName("intAcademic_id")
    @Expose
    private Integer intAcademicId;
    @SerializedName("intLeaveApplocation_id")
    @Expose
    private Integer intLeaveApplocationId;
    @SerializedName("vchReason")
    @Expose
    private String vchReason;
    @SerializedName("dtFrom_date")
    @Expose
    private String dtFromDate;
    @SerializedName("dtTo_Date")
    @Expose
    private String dtToDate;
    @SerializedName("intTotalDays")
    @Expose
    private Integer intTotalDays;
    @SerializedName("vchProfile")
    @Expose
    private String vchProfile;
    @SerializedName("intCL")
    @Expose
    private Integer intCL;
    @SerializedName("intML")
    @Expose
    private Integer intML;
    @SerializedName("vchLeaveType")
    @Expose
    private String vchLeaveType;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("bitAdminApproval")
    @Expose
    private String bitAdminApproval;
    @SerializedName("intTeacher_id")
    @Expose
    private Integer intTeacherId;
    @SerializedName("intStaff_id")
    @Expose
    private Integer intStaffid;
    @SerializedName("intStudent_id")
    @Expose
    private Integer intStudentId;

    public Integer getIntLeaveApplocationId() {
        return intLeaveApplocationId;
    }

    public void setIntLeaveApplocationId(Integer intLeaveApplocationId) {
        this.intLeaveApplocationId = intLeaveApplocationId;
    }

    public String getVchReason() {
        return vchReason;
    }

    public void setVchReason(String vchReason) {
        this.vchReason = vchReason;
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

    public Integer getIntTotalDays() {
        return intTotalDays;
    }

    public void setIntTotalDays(Integer intTotalDays) {
        this.intTotalDays = intTotalDays;
    }

    public String getVchProfile() {
        return vchProfile;
    }

    public void setVchProfile(String vchProfile) {
        this.vchProfile = vchProfile;
    }

    public Integer getIntCL() {
        return intCL;
    }

    public void setIntCL(Integer intCL) {
        this.intCL = intCL;
    }

    public Integer getIntML() {
        return intML;
    }

    public void setIntML(Integer intML) {
        this.intML = intML;
    }

    public String getVchLeaveType() {
        return vchLeaveType;
    }

    public void setVchLeaveType(String vchLeaveType) {
        this.vchLeaveType = vchLeaveType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBitAdminApproval() {
        return bitAdminApproval;
    }

    public void setBitAdminApproval(String bitAdminApproval) {
        this.bitAdminApproval = bitAdminApproval;
    }

    public Integer getIntTeacherId() {
        return intTeacherId;
    }

    public void setIntTeacherId(Integer intTeacherId) {
        this.intTeacherId = intTeacherId;
    }

    public Integer getIntStudentId() {
        return intStudentId;
    }

    public void setIntStudentId(Integer intStudentId) {
        this.intStudentId = intStudentId;
    }

    public LeaveDetail(Integer intCL, Integer intML, Integer intTeacherId) {
        this.intCL = intCL;
        this.intML = intML;
        this.intTeacherId = intTeacherId;
    }

    public LeaveDetail(Integer intLeaveApplocationId, String bitAdminApproval ) {
        this.intLeaveApplocationId = intLeaveApplocationId;
        this.bitAdminApproval = bitAdminApproval;

    }

    public LeaveDetail(Integer intUserTypeId, Integer intTypeId, Integer intUserId, Integer intSchoolId, Integer intLeaveTypeId, Integer intAcademicId, String vchReason, String dtFromDate, String dtToDate, Integer intTotalDays, String vchLeaveType, String bitAdminApproval,String name) {
        this.intUserTypeId = intUserTypeId;
        this.intTypeId = intTypeId;
        this.intUserId = intUserId;
        this.intSchoolId = intSchoolId;
        this.intLeaveTypeId = intLeaveTypeId;
        this.intAcademicId = intAcademicId;
        this.vchReason = vchReason;
        this.dtFromDate = dtFromDate;
        this.dtToDate = dtToDate;
        this.intTotalDays = intTotalDays;
        this.vchLeaveType = vchLeaveType;
        this.bitAdminApproval = bitAdminApproval;
        this.name=name;
    }

    public Integer getIntStaffid() {
        return intStaffid;
    }

    public void setIntStaffid(Integer intStaffid) {
        this.intStaffid = intStaffid;
    }
}