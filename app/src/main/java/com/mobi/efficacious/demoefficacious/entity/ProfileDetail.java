package com.mobi.efficacious.demoefficacious.entity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDetail {
    public String getVchFatherNAme() {
        return vchFatherNAme;
    }

    public void setVchFatherNAme(String vchFatherNAme) {
        this.vchFatherNAme = vchFatherNAme;
    }

    public Integer getIntUserid() {
        return intUserid;
    }

    public void setIntUserid(Integer intUserid) {
        this.intUserid = intUserid;
    }

    public String getVchMotherNAme() {
        return vchMotherNAme;
    }

    public void setVchMotherNAme(String vchMotherNAme) {
        this.vchMotherNAme = vchMotherNAme;
    }

    public String getVchStandardname() {
        return vchStandardname;
    }

    public void setVchStandardname(String vchStandardname) {
        this.vchStandardname = vchStandardname;
    }

    public String getVchDivisionNAme() {
        return vchDivisionNAme;
    }

    public void setVchDivisionNAme(String vchDivisionNAme) {
        this.vchDivisionNAme = vchDivisionNAme;
    }

    public Integer getIntschoolId() {
        return intschoolId;
    }

    public void setIntschoolId(Integer intschoolId) {
        this.intschoolId = intschoolId;
    }

    public Integer getIntAcademicId() {
        return intAcademicId;
    }

    public void setIntAcademicId(Integer intAcademicId) {
        this.intAcademicId = intAcademicId;
    }

    @SerializedName("intschool_id")
    @Expose
    private Integer intschoolId;
    @SerializedName("intAcademic_id")
    @Expose
    private Integer intAcademicId;
    @SerializedName("vchFatherName")
    @Expose
    private String vchFatherNAme;
    @SerializedName("intUser_id")
    @Expose
    private Integer intUserid;
    @SerializedName("vchMotherName")
    @Expose
    private String vchMotherNAme;

    public Integer getIntRollNO() {
        return intRollNO;
    }

    public void setIntRollNO(Integer intRollNO) {
        this.intRollNO = intRollNO;
    }

    @SerializedName("intRollNo")
    @Expose
    private Integer intRollNO;
    @SerializedName("vchStandard_name")
    @Expose
    private String vchStandardname;
    @SerializedName("vchDivisionName")
    @Expose
    private String vchDivisionNAme;
    @SerializedName("Last_name")
    @Expose
    private String lastName;
    @SerializedName("intAdmin_id")
    @Expose
    private Integer intAdminId;
    @SerializedName("vchImageURL")
    @Expose
    private String vchImageURL;
    @SerializedName("vchFirst_name")
    @Expose
    private String vchFirstName;
    @SerializedName("vchEmail")
    @Expose
    private String vchEmail;
    @SerializedName("intMobileNo")
    @Expose
    private String intMobileNo;
    @SerializedName("vchAddress")
    @Expose
    private String vchAddress;
    @SerializedName("intStudent_id")
    @Expose
    private Integer intStudentId;
    @SerializedName("intTeacher_id")
    @Expose
    private Integer intTeacherId;
    @SerializedName("intPrincipal_id")
    @Expose
    private Integer intPrincipalId;
    @SerializedName("intManager_id")
    @Expose
    private Integer intManagerId;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getIntAdminId() {
        return intAdminId;
    }

    public void setIntAdminId(Integer intAdminId) {
        this.intAdminId = intAdminId;
    }

    public String getVchImageURL() {
        return vchImageURL;
    }

    public void setVchImageURL(String vchImageURL) {
        this.vchImageURL = vchImageURL;
    }

    public String getVchFirstName() {
        return vchFirstName;
    }

    public void setVchFirstName(String vchFirstName) {
        this.vchFirstName = vchFirstName;
    }

    public String getVchEmail() {
        return vchEmail;
    }

    public void setVchEmail(String vchEmail) {
        this.vchEmail = vchEmail;
    }

    public String getIntMobileNo() {
        return intMobileNo;
    }

    public void setIntMobileNo(String intMobileNo) {
        this.intMobileNo = intMobileNo;
    }

    public String getVchAddress() {
        return vchAddress;
    }

    public void setVchAddress(String vchAddress) {
        this.vchAddress = vchAddress;
    }

    public Integer getIntStudentId() {
        return intStudentId;
    }

    public void setIntStudentId(Integer intStudentId) {
        this.intStudentId = intStudentId;
    }

    public Integer getIntTeacherId() {
        return intTeacherId;
    }

    public void setIntTeacherId(Integer intTeacherId) {
        this.intTeacherId = intTeacherId;
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

    public ProfileDetail(Integer intschoolId, Integer intAcademicId, Integer intUserid) {
        this.intschoolId = intschoolId;
        this.intAcademicId = intAcademicId;
        this.intUserid = intUserid;
    }


}