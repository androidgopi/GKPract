package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

public class Warehouse {

    @SerializedName("Id")
    private String Id;

    @SerializedName("Name")
    private String Name;

    @SerializedName("Value")
    private String Value;

    @SerializedName("Description")
    private String Description;

    @SerializedName("AllocationCount")
    private String AllocationCount;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAllocationCount() {
        return AllocationCount;
    }

    public void setAllocationCount(String allocationCount) {
        AllocationCount = allocationCount;
    }
}
