package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileDetailsPojo {

    @SerializedName("ProfileDetails")
    @Expose
    private List<ProfileDetail> profileDetails = null;

    public List<ProfileDetail> getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(List<ProfileDetail> profileDetails) {
        this.profileDetails = profileDetails;
    }

}
