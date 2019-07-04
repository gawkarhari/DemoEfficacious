package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StandardDetail {

    public String getTeacher_Name() {
        return Teacher_Name;
    }

    public void setTeacher_Name(String teacher_Name) {
        Teacher_Name = teacher_Name;
    }

    public String getMessagestatus() {
        return messagestatus;
    }

    public void setMessagestatus(String messagestatus) {
        this.messagestatus = messagestatus;
    }

    @SerializedName("TeacherName")
    @Expose
    private String Teacher_Name;

    @SerializedName("messageStatus")
    @Expose
    private String messagestatus;
    @SerializedName("intAcademic_id")
    @Expose
    private Integer intAcademic_Id;
    @SerializedName("intTeacher_id")
    @Expose
    private Integer intTeacher_Id;
    @SerializedName("vchType")
    @Expose
    private String VchType;
    @SerializedName("intStandard_Id")
    @Expose
    private Integer intStandardId;
    @SerializedName("vchStandard_name")
    @Expose
    private String vchStandardName;
    @SerializedName("intschool_id")
    @Expose
    private Integer intschoolId;
    @SerializedName("intDivision_id")
    @Expose
    private Integer intDivisionId;
    @SerializedName("vchDivisionName")
    @Expose
    private String vchDivisionName;
    @SerializedName("intSubject_id")
    @Expose
    private Integer intSubjectId;
    @SerializedName("vchSubjectName")
    @Expose
    private String vchSubjectName;
    @SerializedName("vchFileName")
    @Expose
    private String vchFileName;
    @SerializedName("vchFilePath")
    @Expose
    private String vchFilePath;
    @SerializedName("vchFilePath2")
    @Expose
    private String vchFilePath2;
    @SerializedName("vchFilePath3")
    @Expose
    private String vchFilePath3;
    @SerializedName("int_Approval")
    @Expose
    private Integer intApproval;
    @SerializedName("vchComment")
    @Expose
    private String vchComment;
    @SerializedName("dtDatetime")
    @Expose
    private String dtDatetime;
    @SerializedName("dtInserted_Datetime")
    @Expose
    private String dtInsertedDatetime;
    @SerializedName("vchProfile")
    @Expose
    private String vchProfile;
    @SerializedName("intMy_id")
    @Expose
    private Integer intMyId;
    @SerializedName("name")
    @Expose
    private String name;

    public StandardDetail(String messagestatus, Integer intStandardId, Integer intDivisionId, String vchSubjectName, Integer intApproval, Integer intMyId) {
        this.messagestatus = messagestatus;
        this.intStandardId = intStandardId;
        this.intDivisionId = intDivisionId;
        this.vchSubjectName = vchSubjectName;
        this.intApproval = intApproval;
        this.intMyId = intMyId;
    }

    public StandardDetail(Integer intStandardId, String vchStandardName, Integer intschoolId) {
        this.intStandardId = intStandardId;
        this.vchStandardName = vchStandardName;
        this.intschoolId = intschoolId;
    }

    public StandardDetail(Integer intDivisionId, String vchDivisionName) {
        this.intDivisionId = intDivisionId;
        this.vchDivisionName = vchDivisionName;
    }

    public StandardDetail(Integer intTeacher_Id, Integer intSubjectId, String vchSubjectName) {
        this.intTeacher_Id = intTeacher_Id;
        this.intSubjectId = intSubjectId;
        this.vchSubjectName = vchSubjectName;
    }

    public StandardDetail(Integer intAcademic_Id, Integer intTeacher_Id, String vchType, Integer intStandardId, Integer intschoolId, Integer intDivisionId, Integer intSubjectId, String vchFileName, String vchFilePath, String vchFilePath2, String vchFilePath3, Integer intApproval, String vchComment, String dtDatetime,String TeacherName,String vchStandard_name,String vchDivisionName,String vchSubjectName ) {
        this.intAcademic_Id = intAcademic_Id;
        this.intTeacher_Id = intTeacher_Id;
        VchType = vchType;
        this.intStandardId = intStandardId;
        this.intschoolId = intschoolId;
        this.intDivisionId = intDivisionId;
        this.intSubjectId = intSubjectId;
        this.vchFileName = vchFileName;
        this.vchFilePath = vchFilePath;
        this.vchFilePath2 = vchFilePath2;
        this.vchFilePath3 = vchFilePath3;
        this.intApproval = intApproval;
        this.vchComment = vchComment;
        this.dtDatetime = dtDatetime;

        this.Teacher_Name = TeacherName;
        this.vchStandardName = vchStandard_name;
        this.vchDivisionName = vchDivisionName;
        this.vchSubjectName = vchSubjectName;
    }

    public Integer getIntStandardId() {
        return intStandardId;
    }

    public void setIntStandardId(Integer intStandardId) {
        this.intStandardId = intStandardId;
    }

    public String getVchStandardName() {
        return vchStandardName;
    }

    public void setVchStandardName(String vchStandardName) {
        this.vchStandardName = vchStandardName;
    }

    public Integer getIntschoolId() {
        return intschoolId;
    }

    public void setIntschoolId(Integer intschoolId) {
        this.intschoolId = intschoolId;
    }

    public Integer getIntDivisionId() {
        return intDivisionId;
    }

    public void setIntDivisionId(Integer intDivisionId) {
        this.intDivisionId = intDivisionId;
    }

    public String getVchDivisionName() {
        return vchDivisionName;
    }

    public void setVchDivisionName(String vchDivisionName) {
        this.vchDivisionName = vchDivisionName;
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

    public String getVchFileName() {
        return vchFileName;
    }

    public void setVchFileName(String vchFileName) {
        this.vchFileName = vchFileName;
    }

    public String getVchFilePath() {
        return vchFilePath;
    }

    public void setVchFilePath(String vchFilePath) {
        this.vchFilePath = vchFilePath;
    }

    public String getVchFilePath2() {
        return vchFilePath2;
    }

    public void setVchFilePath2(String vchFilePath2) {
        this.vchFilePath2 = vchFilePath2;
    }

    public String getVchFilePath3() {
        return vchFilePath3;
    }

    public void setVchFilePath3(String vchFilePath3) {
        this.vchFilePath3 = vchFilePath3;
    }

    public Integer getIntApproval() {
        return intApproval;
    }

    public void setIntApproval(Integer intApproval) {
        this.intApproval = intApproval;
    }

    public String getVchComment() {
        return vchComment;
    }

    public void setVchComment(String vchComment) {
        this.vchComment = vchComment;
    }

    public String getDtDatetime() {
        return dtDatetime;
    }

    public void setDtDatetime(String dtDatetime) {
        this.dtDatetime = dtDatetime;
    }

    public String getDtInsertedDatetime() {
        return dtInsertedDatetime;
    }

    public void setDtInsertedDatetime(String dtInsertedDatetime) {
        this.dtInsertedDatetime = dtInsertedDatetime;
    }

    public String getVchProfile() {
        return vchProfile;
    }

    public void setVchProfile(String vchProfile) {
        this.vchProfile = vchProfile;
    }

    public Integer getIntMyId() {
        return intMyId;
    }

    public void setIntMyId(Integer intMyId) {
        this.intMyId = intMyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIntAcademic_Id() {
        return intAcademic_Id;
    }

    public void setIntAcademic_Id(Integer intAcademic_Id) {
        this.intAcademic_Id = intAcademic_Id;
    }

    public Integer getIntTeacher_Id() {
        return intTeacher_Id;
    }

    public void setIntTeacher_Id(Integer intTeacher_Id) {
        this.intTeacher_Id = intTeacher_Id;
    }

    public String getVchType() {
        return VchType;
    }

    public void setVchType(String vchType) {
        VchType = vchType;
    }
}
