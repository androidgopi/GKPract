package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

public class GatePass_TrailerDetails_OrgStatus {

    @SerializedName("Status")
    private String Status;

    @SerializedName("trailer")
    private  TrailerDetails_OrgGpStatus trailerDetails_orgGpStatus = new TrailerDetails_OrgGpStatus();

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public TrailerDetails_OrgGpStatus getTrailerDetails_orgGpStatus() {
        return trailerDetails_orgGpStatus;
    }

    public void setTrailerDetails_orgGpStatus(TrailerDetails_OrgGpStatus trailerDetails_orgGpStatus) {
        this.trailerDetails_orgGpStatus = trailerDetails_orgGpStatus;
    }
}
