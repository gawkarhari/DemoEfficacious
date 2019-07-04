package com.mobi.efficacious.demoefficacious.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SyllabusDetail {
    @SerializedName("SyllabusURL")
    @Expose
    private String syllabusurl;
    @SerializedName("intTopic_id")
    @Expose
    private Integer intTopicId;
    @SerializedName("vchTopicName")
    @Expose
    private String vchTopicName;
    @SerializedName("vchStandard_name")
    @Expose
    private String vchStandardName;
    @SerializedName("intSubject_id")
    @Expose
    private Integer intSubjectId;
    @SerializedName("vchSubjectName")
    @Expose
    private String vchSubjectName;
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolId;
    @SerializedName("btActiveFlg")
    @Expose
    private Boolean btActiveFlg;
    @SerializedName("intSTD_id")
    @Expose
    private Integer intSTDId;

    public SyllabusDetail(String vchSubjectName, Integer intSTDId) {
        this.vchSubjectName = vchSubjectName;
        this.intSTDId = intSTDId;
    }

    public SyllabusDetail(String syllabusurl, String vchTopicName, String vchSubjectName) {
        this.syllabusurl = syllabusurl;
        this.vchTopicName = vchTopicName;
        this.vchSubjectName = vchSubjectName;
    }

    public Integer getIntTopicId() {
        return intTopicId;
    }

    public void setIntTopicId(Integer intTopicId) {
        this.intTopicId = intTopicId;
    }

    public String getVchTopicName() {
        return vchTopicName;
    }

    public void setVchTopicName(String vchTopicName) {
        this.vchTopicName = vchTopicName;
    }

    public String getVchStandardName() {
        return vchStandardName;
    }

    public void setVchStandardName(String vchStandardName) {
        this.vchStandardName = vchStandardName;
    }

    public Integer getIntSubjectId() {
        return intSubjectId;
    }

    public void setIntSubjectId(Integer intSubjectId) {
        this.intSubjectId = intSubjectId;
    }

    public String getVchSubjectName() {
        return vchSubjectName;
    }

    public void setVchSubjectName(String vchSubjectName) {
        this.vchSubjectName = vchSubjectName;
    }

    public Integer getIntSchoolId() {
        return intSchoolId;
    }

    public void setIntSchoolId(Integer intSchoolId) {
        this.intSchoolId = intSchoolId;
    }

    public Boolean getBtActiveFlg() {
        return btActiveFlg;
    }

    public void setBtActiveFlg(Boolean btActiveFlg) {
        this.btActiveFlg = btActiveFlg;
    }

    public Integer getIntSTDId() {
        return intSTDId;
    }

    public void setIntSTDId(Integer intSTDId) {
        this.intSTDId = intSTDId;
    }

    public String getSyllabusurl() {
        return syllabusurl;
    }

    public void setSyllabusurl(String syllabusurl) {
        this.syllabusurl = syllabusurl;
    }
}