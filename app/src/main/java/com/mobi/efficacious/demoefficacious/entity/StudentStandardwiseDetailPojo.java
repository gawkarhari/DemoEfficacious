package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentStandardwiseDetailPojo {

    @SerializedName("StudentStandardwiseDetail")
    @Expose
    private List<StudentStandardwiseDetail> studentStandardwiseDetail = null;

    public List<StudentStandardwiseDetail> getStudentStandardwiseDetail() {
        return studentStandardwiseDetail;
    }

    public void setStudentStandardwiseDetail(List<StudentStandardwiseDetail> studentStandardwiseDetail) {
        this.studentStandardwiseDetail = studentStandardwiseDetail;
    }

}