package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kstl on 23-05-2018.
 */

public class WarehouseResult {

    @SerializedName("Id")
    private  String Id;

    @SerializedName("WarehouseName")
    private  String WarehouseName;

    @SerializedName("AddressId")
    private  String AddressId;

    @SerializedName("Active")
    private  String Active;

    @SerializedName("UserEditable")
    private  String UserEditable;

    @SerializedName("PrinterId")
    private  String PrinterId;

    @SerializedName("PrinterIds")
    private  String PrinterIds;

    @SerializedName("PrinterName")
    private  String PrinterName;

    @SerializedName("LookUpField")
    private  String LookUpField;


    @SerializedName("LabelPrinterId")
    private  String LabelPrinterId;


    @SerializedName("TotalSqFt")
    private  String TotalSqFt;


    @SerializedName("WarehousePrinterNames")
    private  String WarehousePrinterNames;

    @SerializedName("SentTo")
    private  String SentTo;

    @SerializedName("WarehouseCode")
    private  String WarehouseCode;

    public String getSentTo() {
        return SentTo;
    }

    public void setSentTo(String sentTo) {
        SentTo = sentTo;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getWarehouseName() {
        return WarehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        WarehouseName = warehouseName;
    }

    public String getAddressId() {
        return AddressId;
    }

    public void setAddressId(String addressId) {
        AddressId = addressId;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getUserEditable() {
        return UserEditable;
    }

    public void setUserEditable(String userEditable) {
        UserEditable = userEditable;
    }

    public String getPrinterId() {
        return PrinterId;
    }

    public void setPrinterId(String printerId) {
        PrinterId = printerId;
    }

    public String getPrinterIds() {
        return PrinterIds;
    }

    public void setPrinterIds(String printerIds) {
        PrinterIds = printerIds;
    }

    public String getPrinterName() {
        return PrinterName;
    }

    public void setPrinterName(String printerName) {
        PrinterName = printerName;
    }

    public String getLookUpField() {
        return LookUpField;
    }

    public void setLookUpField(String lookUpField) {
        LookUpField = lookUpField;
    }

    public String getLabelPrinterId() {
        return LabelPrinterId;
    }

    public void setLabelPrinterId(String labelPrinterId) {
        LabelPrinterId = labelPrinterId;
    }

    public String getTotalSqFt() {
        return TotalSqFt;
    }

    public void setTotalSqFt(String totalSqFt) {
        TotalSqFt = totalSqFt;
    }

    public String getWarehousePrinterNames() {
        return WarehousePrinterNames;
    }

    public void setWarehousePrinterNames(String warehousePrinterNames) {
        WarehousePrinterNames = warehousePrinterNames;
    }

    public String getWarehouseCode() {
        return WarehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        WarehouseCode = warehouseCode;
    }
}
