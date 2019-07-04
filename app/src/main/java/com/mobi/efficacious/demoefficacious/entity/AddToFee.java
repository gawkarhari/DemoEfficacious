package com.mobi.efficacious.demoefficacious.entity;

public class AddToFee {
    String intTutionID;
    String intMonth;
    String SumAmount;

    public String getIntMonth_ID() {
        return intMonth_ID;
    }

    public void setIntMonth_ID(String intMonth_ID) {
        this.intMonth_ID = intMonth_ID;
    }

    String intMonth_ID;
    public String getIntTutionID() {
        return intTutionID;
    }

    public void setIntTutionID(String intTutionID) {
        this.intTutionID = intTutionID;
    }

    public String getIntMonth() {
        return intMonth;
    }

    public void setIntMonth(String intMonth) {
        this.intMonth = intMonth;
    }

    public String getSumAmount() {
        return SumAmount;
    }

    public void setSumAmount(String sumAmount) {
        SumAmount = sumAmount;
    }

    public AddToFee(String intTutionID, String intMonth, String sumAmount, String intmonth_ID) {
        this.intTutionID = intTutionID;
        this.intMonth = intMonth;
        SumAmount = sumAmount;
        intMonth_ID=intmonth_ID;
    }
}
