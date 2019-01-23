package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kstl on 15-05-2018.
 */

public class PhotoResults {

    @SerializedName("Photos")
    private ArrayList<ImageFile> photoList = new ArrayList<>();

    @SerializedName("Status")
    private String Status;

    public ArrayList<ImageFile> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(ArrayList<ImageFile> photoList) {
        this.photoList = photoList;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
