package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyllabusDetailPojo {

    @SerializedName("SyllabusDetail")
    @Expose
    private List<SyllabusDetail> syllabusDetail = null;

    public List<SyllabusDetail> getSyllabusDetail() {
        return syllabusDetail;
    }

    public void setSyllabusDetail(List<SyllabusDetail> syllabusDetail) {
        this.syllabusDetail = syllabusDetail;
    }

}