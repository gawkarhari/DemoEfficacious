package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDetail {

    public String getVchFCMTOken() {
        return vchFCMTOken;
    }

    public void setVchFCMTOken(String vchFCMTOken) {
        this.vchFCMTOken = vchFCMTOken;
    }

    public String getVchEmaIl() {
        return vchEmaIl;
    }

    public void setVchEmaIl(String vchEmaIl) {
        this.vchEmaIl = vchEmaIl;
    }

    public Integer getIntUserId() {
        return intUserId;
    }

    public void setIntUserId(Integer intUserId) {
        this.intUserId = intUserId;
    }

    public Integer getIntSchoolIid() {
        return intSchoolIid;
    }

    public void setIntSchoolIid(Integer intSchoolIid) {
        this.intSchoolIid = intSchoolIid;
    }

    @SerializedName("vchFCMToken")
    @Expose
    private String vchFCMTOken;
    @SerializedName("vchEmail")
    @Expose
    private String vchEmaIl;
    @SerializedName("intUser_id")
    @Expose
    private Integer intUserId;
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolIid;
    @SerializedName("intUserType_id")
    @Expose
    private Integer intUserTypeId;
    @SerializedName("intStudent_id")
    @Expose
    private Integer intStudentId;
    @SerializedName("vchStudentFirst_name")
    @Expose
    private String vchStudentFirstName;
    @SerializedName("vchStudentLast_name")
    @Expose
    private String vchStudentLastName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("std")
    @Expose
    private String std;
    @SerializedName("intstandard_id")
    @Expose
    private Integer intstandardId;
    @SerializedName("intDivision_id")
    @Expose
    private Integer intDivisionId;
    @SerializedName("vchStandard_name")
    @Expose
    private String vchStandardName;
    @SerializedName("vchDivisionName")
    @Expose
    private String vchDivisionName;
    @SerializedName("vchUser_name")
    @Expose
    private String vchUserName;
    @SerializedName("vchPassword")
    @Expose
    private String vchPassword;
    @SerializedName("intAcademic_id")
    @Expose
    private Integer intAcademicId;
    @SerializedName("intTeacher_id")
    @Expose
    private Integer intTeacherId;
    @SerializedName("vchFirst_name")
    @Expose
    private String vchFirstName;
    @SerializedName("vchLast_name")
    @Expose
    private String vchLastName;
    @SerializedName("intStaff_id")
    @Expose
    private Integer intStaffId;
    @SerializedName("intAdmin_id")
    @Expose
    private Integer intAdminId;
    @SerializedName("intPrincipal_id")
    @Expose
    private Integer intPrincipalId;
    @SerializedName("intManager_id")
    @Expose
    private Integer intManagerId;

    public Integer getIntUserTypeId() {
        return intUserTypeId;
    }

    public void setIntUserTypeId(Integer intUserTypeId) {
        this.intUserTypeId = intUserTypeId;
    }

    public Integer getIntStudentId() {
        return intStudentId;
    }

    public void setIntStudentId(Integer intStudentId) {
        this.intStudentId = intStudentId;
    }

    public String getVchStudentFirstName() {
        return vchStudentFirstName;
    }

    public void setVchStudentFirstName(String vchStudentFirstName) {
        this.vchStudentFirstName = vchStudentFirstName;
    }

    public String getVchStudentLastName() {
        return vchStudentLastName;
    }

    public void setVchStudentLastName(String vchStudentLastName) {
        this.vchStudentLastName = vchStudentLastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public Integer getIntstandardId() {
        return intstandardId;
    }

    public void setIntstandardId(Integer intstandardId) {
        this.intstandardId = intstandardId;
    }

    public Integer getIntDivisionId() {
        return intDivisionId;
    }

    public void setIntDivisionId(Integer intDivisionId) {
        this.intDivisionId = intDivisionId;
    }

    public String getVchStandardName() {
        return vchStandardName;
    }

    public void setVchStandardName(String vchStandardName) {
        this.vchStandardName = vchStandardName;
    }

    public String getVchDivisionName() {
        return vchDivisionName;
    }

    public void setVchDivisionName(String vchDivisionName) {
        this.vchDivisionName = vchDivisionName;
    }

    public String getVchUserName() {
        return vchUserName;
    }

    public void setVchUserName(String vchUserName) {
        this.vchUserName = vchUserName;
    }

    public String getVchPassword() {
        return vchPassword;
    }

    public void setVchPassword(String vchPassword) {
        this.vchPassword = vchPassword;
    }

    public Integer getIntAcademicId() {
        return intAcademicId;
    }

    public void setIntAcademicId(Integer intAcademicId) {
        this.intAcademicId = intAcademicId;
    }

    public Integer getIntTeacherId() {
        return intTeacherId;
    }

    public void setIntTeacherId(Integer intTeacherId) {
        this.intTeacherId = intTeacherId;
    }

    public String getVchFirstName() {
        return vchFirstName;
    }

    public void setVchFirstName(String vchFirstName) {
        this.vchFirstName = vchFirstName;
    }

    public String getVchLastName() {
        return vchLastName;
    }

    public void setVchLastName(String vchLastName) {
        this.vchLastName = vchLastName;
    }

    public Integer getIntStaffId() {
        return intStaffId;
    }

    public void setIntStaffId(Integer intStaffId) {
        this.intStaffId = intStaffId;
    }

    public Integer getIntAdminId() {
        return intAdminId;
    }

    public void setIntAdminId(Integer intAdminId) {
        this.intAdminId = intAdminId;
    }

    public Integer getIntPrincipalId() {
        return intPrincipalId;
    }

    public void setIntPrincipalId(Integer intPrincipalId) {
        this.intPrincipalId = intPrincipalId;
    }

    public Integer getIntManagerId() {
        return intManagerId;
    }

    public void setIntManagerId(Integer intManagerId) {
        this.intManagerId = intManagerId;
    }

    public LoginDetail(String vchFCMTOken, String vchEmaIl, Integer intUserId, Integer intSchoolIid, Integer intAcademicId) {
        this.vchFCMTOken = vchFCMTOken;
        this.vchEmaIl = vchEmaIl;
        this.intUserId = intUserId;
        this.intSchoolIid = intSchoolIid;
        this.intAcademicId = intAcademicId;
    }

    public LoginDetail(Integer intUserId, Integer intSchoolIid, Integer intUserTypeId, String vchUserName, String vchPassword, Integer intAcademicId) {
        this.intUserId = intUserId;
        this.intSchoolIid = intSchoolIid;
        this.intUserTypeId = intUserTypeId;
        this.vchUserName = vchUserName;
        this.vchPassword = vchPassword;
        this.intAcademicId = intAcademicId;
    }
}