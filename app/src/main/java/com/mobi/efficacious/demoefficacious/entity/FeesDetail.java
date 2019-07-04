package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeesDetail {

    @SerializedName("intID")
    @Expose
    private Integer intID;

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    @SerializedName("Receipt_Id")
    @Expose
    private String receiptId;

    @SerializedName("intAcademic_id")
    @Expose
    private Integer intAcademicId;
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolId;
    @SerializedName("intstudent_id")
    @Expose
    private Integer intstudentId;
    @SerializedName("intStandard_id")
    @Expose
    private Integer intStandardId;
    @SerializedName("intPaidAmt")
    @Expose
    private Integer intPaidAmt;
    @SerializedName("vchPaidAmt")
    @Expose
    private String vchPaidAmt;
    @SerializedName("intTotalAmt")
    @Expose
    private Integer intTotalAmt;
    @SerializedName("intLateAmt")
    @Expose
    private Integer intLateAmt;
    @SerializedName("intDiscount")
    @Expose
    private String intDiscount;
    @SerializedName("intAfterDiscount")
    @Expose
    private Integer intAfterDiscount;
    @SerializedName("paidDate")
    @Expose
    private String paidDate;
    @SerializedName("intTutionID")
    @Expose
    private Integer intTutionID;
    @SerializedName("intPayMode")
    @Expose
    private Integer intPayMode;
    @SerializedName("vchChequeNo")
    @Expose
    private String vchChequeNo;
    @SerializedName("dtCheque")
    @Expose
    private String dtCheque;
    @SerializedName("vchBankName")
    @Expose
    private String vchBankName;
    @SerializedName("intMonth_ID")
    @Expose
    private String intMonthID;
    @SerializedName("intOtherAmt")
    @Expose
    private String intOtherAmt;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("FromMonth")
    @Expose
    private String fromMonth;
    @SerializedName("ToMonth")
    @Expose
    private String toMonth;
    @SerializedName("SumAmount")
    @Expose
    private String sumAmount;

    @SerializedName("vchLatefeeRemark")
    @Expose
    private String vchLatefeeRemark;
    @SerializedName("vchOtherfeeRemark")
    @Expose
    private String vchOtherfeeRemark;


    @SerializedName("intAmount")
    @Expose
    private Integer intAmount;
    @SerializedName("intMonth")
    @Expose
    private Integer intMonth;
    public Integer getIntAmount() {
        return intAmount;
    }

    public void setIntAmount(Integer intAmount) {
        this.intAmount = intAmount;
    }

    public Integer getIntMonth() {
        return intMonth;
    }

    public void setIntMonth(Integer intMonth) {
        this.intMonth = intMonth;
    }






    public Integer getIntID() {
        return intID;
    }

    public void setIntID(Integer intID) {
        this.intID = intID;
    }



    public Integer getIntAcademicId() {
        return intAcademicId;
    }

    public void setIntAcademicId(Integer intAcademicId) {
        this.intAcademicId = intAcademicId;
    }

    public Integer getIntSchoolId() {
        return intSchoolId;
    }

    public void setIntSchoolId(Integer intSchoolId) {
        this.intSchoolId = intSchoolId;
    }

    public Integer getIntstudentId() {
        return intstudentId;
    }

    public void setIntstudentId(Integer intstudentId) {
        this.intstudentId = intstudentId;
    }

    public Integer getIntStandardId() {
        return intStandardId;
    }

    public void setIntStandardId(Integer intStandardId) {
        this.intStandardId = intStandardId;
    }

    public Integer getIntPaidAmt() {
        return intPaidAmt;
    }

    public void setIntPaidAmt(Integer intPaidAmt) {
        this.intPaidAmt = intPaidAmt;
    }



    public Integer getIntTotalAmt() {
        return intTotalAmt;
    }

    public void setIntTotalAmt(Integer intTotalAmt) {
        this.intTotalAmt = intTotalAmt;
    }

    public Integer getIntLateAmt() {
        return intLateAmt;
    }

    public void setIntLateAmt(Integer intLateAmt) {
        this.intLateAmt = intLateAmt;
    }



    public Integer getIntAfterDiscount() {
        return intAfterDiscount;
    }

    public void setIntAfterDiscount(Integer intAfterDiscount) {
        this.intAfterDiscount = intAfterDiscount;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public Integer getIntTutionID() {
        return intTutionID;
    }

    public void setIntTutionID(Integer intTutionID) {
        this.intTutionID = intTutionID;
    }

    public Integer getIntPayMode() {
        return intPayMode;
    }

    public void setIntPayMode(Integer intPayMode) {
        this.intPayMode = intPayMode;
    }

    public String getVchChequeNo() {
        return vchChequeNo;
    }

    public void setVchChequeNo(String vchChequeNo) {
        this.vchChequeNo = vchChequeNo;
    }

    public String getDtCheque() {
        return dtCheque;
    }

    public void setDtCheque(String dtCheque) {
        this.dtCheque = dtCheque;
    }

    public String getVchBankName() {
        return vchBankName;
    }

    public void setVchBankName(String vchBankName) {
        this.vchBankName = vchBankName;
    }

    public String getIntMonthID() {
        return intMonthID;
    }

    public void setIntMonthID(String intMonthID) {
        this.intMonthID = intMonthID;
    }

    public String getIntOtherAmt() {
        return intOtherAmt;
    }

    public void setIntOtherAmt(String intOtherAmt) {
        this.intOtherAmt = intOtherAmt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(String fromMonth) {
        this.fromMonth = fromMonth;
    }

    public String getToMonth() {
        return toMonth;
    }

    public void setToMonth(String toMonth) {
        this.toMonth = toMonth;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }



    public String getVchLatefeeRemark() {
        return vchLatefeeRemark;
    }

    public void setVchLatefeeRemark(String vchLatefeeRemark) {
        this.vchLatefeeRemark = vchLatefeeRemark;
    }

    public String getVchOtherfeeRemark() {
        return vchOtherfeeRemark;
    }

    public void setVchOtherfeeRemark(String vchOtherfeeRemark) {
        this.vchOtherfeeRemark = vchOtherfeeRemark;
    }

    public String getVchPaidAmt() {
        return vchPaidAmt;
    }

    public void setVchPaidAmt(String vchPaidAmt) {
        this.vchPaidAmt = vchPaidAmt;
    }

    public String getIntDiscount() {
        return intDiscount;
    }

    public void setIntDiscount(String intDiscount) {
        this.intDiscount = intDiscount;
    }

    public FeesDetail(Integer intAcademicId, Integer intSchoolId, Integer intstudentId, Integer intStandardId, Integer intPaidAmt, Integer intTutionID, Integer intPayMode, String intMonthID, String receipt_Id, Integer intMonth) {
        this.intAcademicId = intAcademicId;
        this.intSchoolId = intSchoolId;
        this.intstudentId = intstudentId;
        this.intStandardId = intStandardId;
        this.intPaidAmt = intPaidAmt;
        this.intTutionID = intTutionID;
        this.intPayMode = intPayMode;
        this.intMonthID = intMonthID;
        this.receiptId = receipt_Id;
        this.intMonth = intMonth;
    }
}
