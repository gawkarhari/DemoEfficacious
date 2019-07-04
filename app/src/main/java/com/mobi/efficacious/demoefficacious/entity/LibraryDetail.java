package com.mobi.efficacious.demoefficacious.entity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LibraryDetail {

    public String getDtAssignedDate() {
        return dtAssignedDate;
    }

    public void setDtAssignedDate(String dtAssignedDate) {
        this.dtAssignedDate = dtAssignedDate;
    }

    public String getDtReturnDate() {
        return dtReturnDate;
    }

    public void setDtReturnDate(String dtReturnDate) {
        this.dtReturnDate = dtReturnDate;
    }

    @SerializedName("dtAssigned_Date")
    @Expose
    private String dtAssignedDate;
    @SerializedName("dtReturn_date")
    @Expose
    private String dtReturnDate;
    @SerializedName("intBookDetails_id")
    @Expose
    private Integer intBookDetailsId;
    @SerializedName("vchAccessionNo")
    @Expose
    private String vchAccessionNo;
    @SerializedName("vchBookDetails_bookName")
    @Expose
    private String vchBookDetailsBookName;
    @SerializedName("intBookPrice")
    @Expose
    private Double intBookPrice;
    @SerializedName("intBookEdition_id")
    @Expose
    private String intBookEditionId;
    @SerializedName("intBook_Author_id")
    @Expose
    private String intBookAuthorId;
    @SerializedName("intBookLanguage_id")
    @Expose
    private String intBookLanguageId;
    @SerializedName("intBookQuantity")
    @Expose
    private Double intBookQuantity;
    @SerializedName("Standard_id")
    @Expose
    private Integer standardId;
    @SerializedName("intStandard_id")
    @Expose
    private String intStandardId;
    @SerializedName("intCategory_id")
    @Expose
    private String intCategoryId;
    @SerializedName("intBook_publication_id")
    @Expose
    private String intBookPublicationId;

    public Integer getIntBookDetailsId() {
        return intBookDetailsId;
    }

    public void setIntBookDetailsId(Integer intBookDetailsId) {
        this.intBookDetailsId = intBookDetailsId;
    }

    public String getVchAccessionNo() {
        return vchAccessionNo;
    }

    public void setVchAccessionNo(String vchAccessionNo) {
        this.vchAccessionNo = vchAccessionNo;
    }

    public String getVchBookDetailsBookName() {
        return vchBookDetailsBookName;
    }

    public void setVchBookDetailsBookName(String vchBookDetailsBookName) {
        this.vchBookDetailsBookName = vchBookDetailsBookName;
    }

    public Double getIntBookPrice() {
        return intBookPrice;
    }

    public void setIntBookPrice(Double intBookPrice) {
        this.intBookPrice = intBookPrice;
    }

    public String getIntBookEditionId() {
        return intBookEditionId;
    }

    public void setIntBookEditionId(String intBookEditionId) {
        this.intBookEditionId = intBookEditionId;
    }

    public String getIntBookAuthorId() {
        return intBookAuthorId;
    }

    public void setIntBookAuthorId(String intBookAuthorId) {
        this.intBookAuthorId = intBookAuthorId;
    }

    public String getIntBookLanguageId() {
        return intBookLanguageId;
    }

    public void setIntBookLanguageId(String intBookLanguageId) {
        this.intBookLanguageId = intBookLanguageId;
    }

    public Double getIntBookQuantity() {
        return intBookQuantity;
    }

    public void setIntBookQuantity(Double intBookQuantity) {
        this.intBookQuantity = intBookQuantity;
    }

    public Integer getStandardId() {
        return standardId;
    }

    public void setStandardId(Integer standardId) {
        this.standardId = standardId;
    }

    public String getIntStandardId() {
        return intStandardId;
    }

    public void setIntStandardId(String intStandardId) {
        this.intStandardId = intStandardId;
    }

    public String getIntCategoryId() {
        return intCategoryId;
    }

    public void setIntCategoryId(String intCategoryId) {
        this.intCategoryId = intCategoryId;
    }

    public String getIntBookPublicationId() {
        return intBookPublicationId;
    }

    public void setIntBookPublicationId(String intBookPublicationId) {
        this.intBookPublicationId = intBookPublicationId;
    }

}