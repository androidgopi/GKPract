package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kstl on 22-01-2018.
 */

public class TrailerFolios {

    @SerializedName("Id")
    private String Id;

    @SerializedName("TrailerFolio")
    private String TrailerFolio;

    @SerializedName("Container")
    private String Container;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTrailerFolio() {
        return TrailerFolio;
    }

    public void setTrailerFolio(String trailerFolio) {
        TrailerFolio = trailerFolio;
    }

    public String getContainer() {
        return Container;
    }

    public void setContainer(String container) {
        Container = container;
    }
}
