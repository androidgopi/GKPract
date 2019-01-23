package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

public class NotThere {

    @SerializedName("Id")
    private String Id;

    @SerializedName("NTFolio")
    private String NTFolio;

    @SerializedName("NTFolioStr")
    private String NTFolioStr;

    @SerializedName("EndCustomer")
    private String EndCustomer;

    @SerializedName("LocationName")
    private String LocationName;

    @SerializedName("Status")
    private String Status;

    @SerializedName("Comments")
    private String Comments;

    @SerializedName("RootCauseComments")
    private String RootCauseComments;

    @SerializedName("RootCausePath")
    private String RootCausePath;

    @SerializedName("AddedBy")
    private String AddedBy;

    @SerializedName("AddedDate")
    private String AddedDate;

    @SerializedName("LastModifiedBy")
    private String LastModifiedBy;

    @SerializedName("LastModifiedDate")
    private String LastModifiedDate;

    @SerializedName("StatusId")
    private String StatusId;

    @SerializedName("Photos")
    private String Photos;

    @SerializedName("ProductNumber")
    private String ProductNumber;

    @SerializedName("EtiqMaster")
    private String EtiqMaster;

    @SerializedName("NtUser")
    private String NtUser;

    @SerializedName("TotalPieces")
    private String TotalPieces;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNTFolio() {
        return NTFolio;
    }

    public void setNTFolio(String NTFolio) {
        this.NTFolio = NTFolio;
    }

    public String getNTFolioStr() {
        return NTFolioStr;
    }

    public void setNTFolioStr(String NTFolioStr) {
        this.NTFolioStr = NTFolioStr;
    }

    public String getEndCustomer() {
        return EndCustomer;
    }

    public void setEndCustomer(String endCustomer) {
        EndCustomer = endCustomer;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getRootCauseComments() {
        return RootCauseComments;
    }

    public void setRootCauseComments(String rootCauseComments) {
        RootCauseComments = rootCauseComments;
    }

    public String getRootCausePath() {
        return RootCausePath;
    }

    public void setRootCausePath(String rootCausePath) {
        RootCausePath = rootCausePath;
    }

    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }

    public String getLastModifiedBy() {
        return LastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        LastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return LastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        LastModifiedDate = lastModifiedDate;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public void setProductNumber(String productNumber) {
        ProductNumber = productNumber;
    }

    public String getEtiqMaster() {
        return EtiqMaster;
    }

    public void setEtiqMaster(String etiqMaster) {
        EtiqMaster = etiqMaster;
    }

    public String getNtUser() {
        return NtUser;
    }

    public void setNtUser(String ntUser) {
        NtUser = ntUser;
    }

    public String getTotalPieces() {
        return TotalPieces;
    }

    public void setTotalPieces(String totalPieces) {
        TotalPieces = totalPieces;
    }


}
