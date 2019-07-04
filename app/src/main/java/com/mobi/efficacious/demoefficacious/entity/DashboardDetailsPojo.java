package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardDetailsPojo {

    @SerializedName("DashboardDetails")
    @Expose
    private List<DashboardDetail> dashboardDetails = null;

    public List<DashboardDetail> getDashboardDetails() {
        return dashboardDetails;
    }

    public void setDashboardDetails(List<DashboardDetail> dashboardDetails) {
        this.dashboardDetails = dashboardDetails;
    }

}