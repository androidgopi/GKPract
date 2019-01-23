package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

public class QAFindingFolioList {

    @SerializedName("Id")
    private String Id;

    @SerializedName("QFFolio")
    private String QFFolio;

    @SerializedName("QFFOlioStr")
    private String QFFOlioStr;

    @SerializedName("WarehouseId")
    private String WarehouseId;

    @SerializedName("Warehouse")
    private String Warehouse;

    @SerializedName("OpenIssueComments")
    private String OpenIssueComments;

    @SerializedName("ClosingComments")
    private String ClosingComments;

    @SerializedName("RootCause")
    private String RootCause;

    @SerializedName("StatusId")
    private String StatusId;

    @SerializedName("AddedBy")
    private Integer AddedBy;

    @SerializedName("AddedDate")
    private String AddedDate;

    @SerializedName("DoneBy")
    private String DoneBy;

    @SerializedName("DoneDate")
    private String DoneDate;

    @SerializedName("LastClosedBy")
    private String LastClosedBy;

    @SerializedName("LastClosedDate")
    private String LastClosedDate;

    @SerializedName("ReOpendBy")
    private String ReOpendBy;

    @SerializedName("ReOpendDate")
    private String ReOpendDate;

    @SerializedName("Status")
    private String Status;

    @SerializedName("FirstName")
    private String FirstName;

    @SerializedName("BeforePhotos")
    private String BeforePhotos;

    @SerializedName("DonePhotos")
    private String DonePhotos;

    @SerializedName("QAFCount")
    private String QAFCount;

    @SerializedName("QAFUser")
    private String QAFUser;

    @SerializedName("BeforePhotoComments")
    private String BeforePhotoComments;

    @SerializedName("DonePhotoComments")
    private String DonePhotoComments;

    @SerializedName("Event")
    private String Event;

    @SerializedName("WarehouseMail")
    private String WarehouseMail;

    @SerializedName("RootCauseComment")
    private String RootCauseComment;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getQFFolio() {
        return QFFolio;
    }

    public void setQFFolio(String QFFolio) {
        this.QFFolio = QFFolio;
    }

    public String getQFFOlioStr() {
        return QFFOlioStr;
    }

    public void setQFFOlioStr(String QFFOlioStr) {
        this.QFFOlioStr = QFFOlioStr;
    }

    public String getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        WarehouseId = warehouseId;
    }

    public String getWarehouse() {
        return Warehouse;
    }

    public void setWarehouse(String warehouse) {
        Warehouse = warehouse;
    }

    public String getOpenIssueComments() {
        return OpenIssueComments;
    }

    public void setOpenIssueComments(String openIssueComments) {
        OpenIssueComments = openIssueComments;
    }

    public String getClosingComments() {
        return ClosingComments;
    }

    public void setClosingComments(String closingComments) {
        ClosingComments = closingComments;
    }

    public String getRootCause() {
        return RootCause;
    }

    public void setRootCause(String rootCause) {
        RootCause = rootCause;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public Integer getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(Integer addedBy) {
        AddedBy = addedBy;
    }

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }

    public String getDoneBy() {
        return DoneBy;
    }

    public void setDoneBy(String doneBy) {
        DoneBy = doneBy;
    }

    public String getDoneDate() {
        return DoneDate;
    }

    public void setDoneDate(String doneDate) {
        DoneDate = doneDate;
    }

    public String getLastClosedBy() {
        return LastClosedBy;
    }

    public void setLastClosedBy(String lastClosedBy) {
        LastClosedBy = lastClosedBy;
    }

    public String getLastClosedDate() {
        return LastClosedDate;
    }

    public void setLastClosedDate(String lastClosedDate) {
        LastClosedDate = lastClosedDate;
    }

    public String getReOpendBy() {
        return ReOpendBy;
    }

    public void setReOpendBy(String reOpendBy) {
        ReOpendBy = reOpendBy;
    }

    public String getReOpendDate() {
        return ReOpendDate;
    }

    public void setReOpendDate(String reOpendDate) {
        ReOpendDate = reOpendDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getBeforePhotos() {
        return BeforePhotos;
    }

    public void setBeforePhotos(String beforePhotos) {
        BeforePhotos = beforePhotos;
    }

    public String getDonePhotos() {
        return DonePhotos;
    }

    public void setDonePhotos(String donePhotos) {
        DonePhotos = donePhotos;
    }

    public String getQAFCount() {
        return QAFCount;
    }

    public void setQAFCount(String QAFCount) {
        this.QAFCount = QAFCount;
    }

    public String getQAFUser() {
        return QAFUser;
    }

    public void setQAFUser(String QAFUser) {
        this.QAFUser = QAFUser;
    }

    public String getBeforePhotoComments() {
        return BeforePhotoComments;
    }

    public void setBeforePhotoComments(String beforePhotoComments) {
        BeforePhotoComments = beforePhotoComments;
    }

    public String getDonePhotoComments() {
        return DonePhotoComments;
    }

    public void setDonePhotoComments(String donePhotoComments) {
        DonePhotoComments = donePhotoComments;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getWarehouseMail() {
        return WarehouseMail;
    }

    public void setWarehouseMail(String warehouseMail) {
        WarehouseMail = warehouseMail;
    }

    public String getRootCauseComment() {
        return RootCauseComment;
    }

    public void setRootCauseComment(String rootCauseComment) {
        RootCauseComment = rootCauseComment;
    }
}
