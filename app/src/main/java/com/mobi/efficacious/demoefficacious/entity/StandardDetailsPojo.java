package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StandardDetailsPojo {

    @SerializedName("StandardDetails")
    @Expose
    private List<StandardDetail> standardDetails = null;

    public List<StandardDetail> getStandardDetails() {
        return standardDetails;
    }

    public void setStandardDetails(List<StandardDetail> standardDetails) {
        this.standardDetails = standardDetails;
    }

}