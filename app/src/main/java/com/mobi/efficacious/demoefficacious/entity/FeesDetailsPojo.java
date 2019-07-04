package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeesDetailsPojo {

    @SerializedName("FeesDetail")
    @Expose
    private List<FeesDetail> feesDetail = null;

    public List<FeesDetail> getFeesDetail() {
        return feesDetail;
    }

    public void setFeesDetail(List<FeesDetail> feesDetail) {
        this.feesDetail = feesDetail;
    }

}
