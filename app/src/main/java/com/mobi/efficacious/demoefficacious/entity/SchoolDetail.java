package com.mobi.efficacious.demoefficacious.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolDetail {
    @SerializedName("EventName")
    @Expose
    private String EventName;
    @SerializedName("Folder_id")
    @Expose
    private Integer Folderid;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Path")
    @Expose
    private String path;
    @SerializedName("EventDescription")
    @Expose
    private String eventDescription;
    @SerializedName("Uploadedfrom")
    @Expose
    private String uploadedfrom;
    @SerializedName("Filetype")
    @Expose
    private String filetype;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("intSchool_id")
    @Expose
    private Integer intSchoolId;
    @SerializedName("vchSchool_name")
    @Expose
    private String vchSchoolName;
    @SerializedName("intAcademic_id")
    @Expose
    private Integer intAcademicId;
    @SerializedName("AcademicYear")
    @Expose
    private String academicYear;

    public Integer getIntSchoolId() {
        return intSchoolId;
    }

    public void setIntSchoolId(Integer intSchoolId) {
        this.intSchoolId = intSchoolId;
    }

    public String getVchSchoolName() {
        return vchSchoolName;
    }

    public void setVchSchoolName(String vchSchoolName) {
        this.vchSchoolName = vchSchoolName;
    }

    public Integer getIntAcademicId() {
        return intAcademicId;
    }

    public void setIntAcademicId(Integer intAcademicId) {
        this.intAcademicId = intAcademicId;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getUploadedfrom() {
        return uploadedfrom;
    }

    public void setUploadedfrom(String uploadedfrom) {
        this.uploadedfrom = uploadedfrom;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getFolderid() {
        return Folderid;
    }

    public void setFolderid(Integer folderid) {
        Folderid = folderid;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }
}
