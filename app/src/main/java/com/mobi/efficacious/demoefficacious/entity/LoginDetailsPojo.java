package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginDetailsPojo {

    @SerializedName("LoginDetails")
    @Expose
    private List<LoginDetail> loginDetails = null;

    public List<LoginDetail> getLoginDetails() {
        return loginDetails;
    }

    public void setLoginDetails(List<LoginDetail> loginDetails) {
        this.loginDetails = loginDetails;
    }

}