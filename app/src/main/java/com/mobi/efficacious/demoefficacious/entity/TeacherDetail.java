package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherDetail {

    @SerializedName("Staff_Id")
    @Expose
    private Integer Staffid;

    @SerializedName("Teacher_id")
    @Expose
    private Integer teacherId;

    @SerializedName("vchProfile")
    @Expose
    private String vchProfile;

    @SerializedName("Department_name")
    @Expose
    private String departmentName;

    @SerializedName("Designation")
    @Expose
    private String designation;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("NamewithDesig")
    @Expose
    private String namewithDesig;

    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolId;

    @SerializedName("intUser_id")
    @Expose
    private Integer intUser_id;

    public Integer getIntUser_id() {
        return intUser_id;
    }

    public void setIntUser_id(Integer intUser_id) {
        this.intUser_id = intUser_id;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getVchProfile() {
        return vchProfile;
    }

    public void setVchProfile(String vchProfile) {
        this.vchProfile = vchProfile;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamewithDesig() {
        return namewithDesig;
    }

    public void setNamewithDesig(String namewithDesig) {
        this.namewithDesig = namewithDesig;
    }

    public Integer getIntSchoolId() {
        return intSchoolId;
    }

    public void setIntSchoolId(Integer intSchoolId) {
        this.intSchoolId = intSchoolId;
    }

    public Integer getStaffid() {
        return Staffid;
    }

    public void setStaffid(Integer staffid) {
        Staffid = staffid;
    }
}