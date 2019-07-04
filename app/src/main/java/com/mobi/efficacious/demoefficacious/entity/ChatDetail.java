package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatDetail {
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolid;

    public Integer getIntSchoolid() {
        return intSchoolid;
    }

    public void setIntSchoolid(Integer intSchoolid) {
        this.intSchoolid = intSchoolid;
    }

    public Integer getIntAcademicid() {
        return intAcademicid;
    }

    public void setIntAcademicid(Integer intAcademicid) {
        this.intAcademicid = intAcademicid;
    }

    @SerializedName("intAcademic_id")
    @Expose
    private Integer intAcademicid;


    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getSendername() {
        return Sendername;
    }

    public void setSendername(String sendername) {
        Sendername = sendername;
    }

    public String getReceivername() {
        return Receivername;
    }

    public void setReceivername(String receivername) {
        Receivername = receivername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderFCMTOken() {
        return SenderFCMTOken;
    }

    public void setSenderFCMTOken(String senderFCMTOken) {
        SenderFCMTOken = senderFCMTOken;
    }

    public String getfCMToken() {
        return fCMToken;
    }

    public void setfCMToken(String fCMToken) {
        this.fCMToken = fCMToken;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    @SerializedName("GRoupName")
    @Expose
    private String GroupName;
    @SerializedName("SenderName")
    @Expose
    private String Sendername;
    @SerializedName("ReceiverName")
    @Expose
    private String Receivername;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("SenderFCMToken")
    @Expose
    private String SenderFCMTOken;
    @SerializedName("intTeacher_id")
    @Expose
    private Integer intTeacherId;
    @SerializedName("User_id")
    @Expose
    private Integer userId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("FCMToken")
    @Expose
    private String fCMToken;
    @SerializedName("intUserType_id")
    @Expose
    private Integer userTypeId;
    @SerializedName("vchSubjectName")
    @Expose
    private String vchSubjectName;
    @SerializedName("vchStandard_name")
    @Expose
    private String vchStandardName;
    @SerializedName("vchDivisionName")
    @Expose
    private String vchDivisionName;
    @SerializedName("T_Name")
    @Expose
    private String tName;
    @SerializedName("intstandard_id")
    @Expose
    private Integer intstandardId;
    @SerializedName("intDivision_id")
    @Expose
    private Integer intDivisionId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFCMToken() {
        return fCMToken;
    }

    public void setFCMToken(String fCMToken) {
        this.fCMToken = fCMToken;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getVchSubjectName() {
        return vchSubjectName;
    }

    public void setVchSubjectName(String vchSubjectName) {
        this.vchSubjectName = vchSubjectName;
    }

    public String getVchStandardName() {
        return vchStandardName;
    }

    public void setVchStandardName(String vchStandardName) {
        this.vchStandardName = vchStandardName;
    }

    public String getVchDivisionName() {
        return vchDivisionName;
    }

    public void setVchDivisionName(String vchDivisionName) {
        this.vchDivisionName = vchDivisionName;
    }

    public String getTName() {
        return tName;
    }

    public void setTName(String tName) {
        this.tName = tName;
    }

    public Integer getIntstandardId() {
        return intstandardId;
    }

    public void setIntstandardId(Integer intstandardId) {
        this.intstandardId = intstandardId;
    }

    public Integer getIntDivisionId() {
        return intDivisionId;
    }

    public void setIntDivisionId(Integer intDivisionId) {
        this.intDivisionId = intDivisionId;
    }

    public Integer getIntTeacherId() {
        return intTeacherId;
    }

    public void setIntTeacherId(Integer intTeacherId) {
        this.intTeacherId = intTeacherId;
    }

    public ChatDetail(String sendername, String receivername, String message, String senderFCMTOken, Integer userId, String fCMToken, Integer userTypeId) {
        Sendername = sendername;
        Receivername = receivername;
        this.message = message;
        SenderFCMTOken = senderFCMTOken;
        this.userId = userId;
        this.fCMToken = fCMToken;
        this.userTypeId = userTypeId;
    }

    public ChatDetail(Integer intSchoolid, Integer intAcademicid, String groupName, String sendername, String message, Integer intTeacherId, Integer userId, Integer intstandardId, Integer intDivisionId) {
        this.intSchoolid = intSchoolid;
        this.intAcademicid = intAcademicid;
        GroupName = groupName;
        Sendername = sendername;
        this.message = message;
        this.intTeacherId = intTeacherId;
        this.userId = userId;
        this.intstandardId = intstandardId;
        this.intDivisionId = intDivisionId;
    }
}
