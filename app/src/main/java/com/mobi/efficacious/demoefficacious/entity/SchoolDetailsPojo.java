package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchoolDetailsPojo {

    @SerializedName("SchoolDetails")
    @Expose
    private List<SchoolDetail> schoolDetails = null;

    public List<SchoolDetail> getSchoolDetails() {
        return schoolDetails;
    }

    public void setSchoolDetails(List<SchoolDetail> schoolDetails) {
        this.schoolDetails = schoolDetails;
    }

}