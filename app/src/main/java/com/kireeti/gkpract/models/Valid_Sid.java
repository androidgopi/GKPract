package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

public class Valid_Sid {

    @SerializedName("Status")
    private  String Status;

    @SerializedName("InventoryDetails")
    private  InventoryDetails inventoryDetails = new InventoryDetails();

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public InventoryDetails getInventoryDetails() {
        return inventoryDetails;
    }

    public void setInventoryDetails(InventoryDetails inventoryDetails) {
        this.inventoryDetails = inventoryDetails;
    }
}
