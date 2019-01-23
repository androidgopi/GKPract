package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kstl on 22-01-2018.
 */

public class Trailer_Results {

    @SerializedName("TrailerFolios")
    private ArrayList<TrailerFolios> trailerList = new ArrayList<>();

    @SerializedName("Status")
    private  String Status;

    public ArrayList<TrailerFolios> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(ArrayList<TrailerFolios> trailerList) {
        this.trailerList = trailerList;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
