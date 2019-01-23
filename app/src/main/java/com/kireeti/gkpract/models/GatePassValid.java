package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

public class GatePassValid {

    @SerializedName("Status")
    private String Status;

    @SerializedName("TrailerDetails")
    private  TrailerDetails trailerDetails = new TrailerDetails();

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public TrailerDetails getTrailerDetails() {
        return trailerDetails;
    }

    public void setTrailerDetails(TrailerDetails trailerDetails) {
        this.trailerDetails = trailerDetails;
    }
}
