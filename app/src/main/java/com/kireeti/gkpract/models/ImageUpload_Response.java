package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kstl on 30-01-2018.
 */

public class ImageUpload_Response {

    @SerializedName("Status")
    private String Status;

    @SerializedName("message")
    private String message;


    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
