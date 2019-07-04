package com.mobi.efficacious.demoefficacious.entity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentStandardwiseDetail {

    @SerializedName("Student_id")
    @Expose
    private Integer studentId;
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolId;
    @SerializedName("vchProfile")
    @Expose
    private String vchProfile;
    @SerializedName("Standard_name")
    @Expose
    private String standardName;
    @SerializedName("Division_name")
    @Expose
    private String divisionName;
    @SerializedName("Standard_id")
    @Expose
    private Integer standardId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Division_id")
    @Expose
    private Integer divisionId;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getIntSchoolId() {
        return intSchoolId;
    }

    public void setIntSchoolId(Integer intSchoolId) {
        this.intSchoolId = intSchoolId;
    }

    public String getVchProfile() {
        return vchProfile;
    }

    public void setVchProfile(String vchProfile) {
        this.vchProfile = vchProfile;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public Integer getStandardId() {
        return standardId;
    }

    public void setStandardId(Integer standardId) {
        this.standardId = standardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

}