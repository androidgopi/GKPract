package com.kireeti.gkpract.models;

import com.google.gson.annotations.SerializedName;

public class TrailerDetails {

    @SerializedName("Id")
    private  String Id;

    @SerializedName("GatePassId")
    private  String GatePassId;

    @SerializedName("CustomerId")
    private  String CustomerId;

    @SerializedName("StatusId")
    private  String StatusId;

    @SerializedName("DriverId")
    private  String DriverId;

    @SerializedName("TrailerFolio")
    private  String TrailerFolio;

    @SerializedName("Folio")
    private  String Folio;

    @SerializedName("GatePassFolioNo")
    private  String GatePassFolioNo;

    @SerializedName("CQCustomerId")
    private  String CQCustomerId;

    @SerializedName("CQCustomer")
    private  String CQCustomer;

    @SerializedName("TrailerNumber")
    private  String TrailerNumber;

    @SerializedName("Driver")
    private  String Driver;

    @SerializedName("TransportationLine")
    private  String TransportationLine;

    @SerializedName("DateTime")
    private  String DateTime;

    @SerializedName("ReceivingStart")
    private  String ReceivingStart;

    @SerializedName("Status")
    private  String Status;

    @SerializedName("Location")
    private  String Location;

    @SerializedName("Documents")
    private  String Documents;

    @SerializedName("Photos")
    private  String Photos;

    @SerializedName("Customer")
    private  String Customer;

    @SerializedName("TruckNo")
    private  String TruckNo;

    @SerializedName("Carrier")
    private  String Carrier;

    @SerializedName("DriverName")
    private  String DriverName;

    @SerializedName("DriverLicense")
    private  String DriverLicense;

    @SerializedName("Reason")
    private  String Reason;

    @SerializedName("RecTrailerStatus")
    private  String RecTrailerStatus;

    @SerializedName("ShippingTrailerStatus")
    private  String ShippingTrailerStatus;

    @SerializedName("TrStatus")
    private  String TrStatus;

    @SerializedName("TrailerLocation")
    private  String TrailerLocation;

    @SerializedName("TrailerLocationId")
    private  String TrailerLocationId;

    @SerializedName("UploadedDocuments")
    private  String UploadedDocuments;

    @SerializedName("UploadedPhotos")
    private  String UploadedPhotos;

    @SerializedName("Tractor")
    private  String Tractor;

    @SerializedName("TransportType")
    private  String TransportType;

    @SerializedName("Seal")
    private  String Seal;

    @SerializedName("TransportTypeId")
    private  String TransportTypeId;

    @SerializedName("TrailerLicencePlate")
    private  String TrailerLicencePlate;

    @SerializedName("RecTrailerId")
    private  String RecTrailerId;

    @SerializedName("TruckTranportId")
    private  String TruckTranportId;

    @SerializedName("ShippingTrailerDBRefId")
    private  String ShippingTrailerDBRefId;

    @SerializedName("RecTrailerDBRefId")
    private  String RecTrailerDBRefId;

    @SerializedName("TRFolio")
    private  String TRFolio;

    @SerializedName("EntranceDate")
    private  String EntranceDate;

    @SerializedName("CQCustomerCode")
    private  String CQCustomerCode;

    @SerializedName("WareHouseId")
    private  String WareHouseId;

    @SerializedName("TRTrailerFolio")
    private  String TRTrailerFolio;

    @SerializedName("WareHouseCode")
    private  String WareHouseCode;

    @SerializedName("TruckTransportationLineId")
    private  String TruckTransportationLineId;

    @SerializedName("TruckTransportationLine")
    private  String TruckTransportationLine;

    @SerializedName("TrailerTypeId")
    private  String TrailerTypeId;

    @SerializedName("TrailerType")
    private  String TrailerType;

    @SerializedName("TruckStatus")
    private  String TruckStatus;

    @SerializedName("TruckLocation")
    private  String TruckLocation;

    @SerializedName("TruckFolio")
    private  String TruckFolio;

    @SerializedName("TKCompleted")
    private  String TKCompleted;

    @SerializedName("PhotosTaken")
    private  String PhotosTaken;

    @SerializedName("AccessOutDate")
    private  String AccessOutDate;

    @SerializedName("RegistrationDate")
    private String RegistrationDate;


    public String getRegistrationDate() {
        return RegistrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        RegistrationDate = registrationDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getGatePassId() {
        return GatePassId;
    }

    public void setGatePassId(String gatePassId) {
        GatePassId = gatePassId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public String getTrailerFolio() {
        return TrailerFolio;
    }

    public void setTrailerFolio(String trailerFolio) {
        TrailerFolio = trailerFolio;
    }

    public String getFolio() {
        return Folio;
    }

    public void setFolio(String folio) {
        Folio = folio;
    }

    public String getGatePassFolioNo() {
        return GatePassFolioNo;
    }

    public void setGatePassFolioNo(String gatePassFolioNo) {
        GatePassFolioNo = gatePassFolioNo;
    }

    public String getCQCustomerId() {
        return CQCustomerId;
    }

    public void setCQCustomerId(String CQCustomerId) {
        this.CQCustomerId = CQCustomerId;
    }

    public String getCQCustomer() {
        return CQCustomer;
    }

    public void setCQCustomer(String CQCustomer) {
        this.CQCustomer = CQCustomer;
    }

    public String getTrailerNumber() {
        return TrailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        TrailerNumber = trailerNumber;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getTransportationLine() {
        return TransportationLine;
    }

    public void setTransportationLine(String transportationLine) {
        TransportationLine = transportationLine;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getReceivingStart() {
        return ReceivingStart;
    }

    public void setReceivingStart(String receivingStart) {
        ReceivingStart = receivingStart;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDocuments() {
        return Documents;
    }

    public void setDocuments(String documents) {
        Documents = documents;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getTruckNo() {
        return TruckNo;
    }

    public void setTruckNo(String truckNo) {
        TruckNo = truckNo;
    }

    public String getCarrier() {
        return Carrier;
    }

    public void setCarrier(String carrier) {
        Carrier = carrier;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getDriverLicense() {
        return DriverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        DriverLicense = driverLicense;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getRecTrailerStatus() {
        return RecTrailerStatus;
    }

    public void setRecTrailerStatus(String recTrailerStatus) {
        RecTrailerStatus = recTrailerStatus;
    }

    public String getShippingTrailerStatus() {
        return ShippingTrailerStatus;
    }

    public void setShippingTrailerStatus(String shippingTrailerStatus) {
        ShippingTrailerStatus = shippingTrailerStatus;
    }

    public String getTrStatus() {
        return TrStatus;
    }

    public void setTrStatus(String trStatus) {
        TrStatus = trStatus;
    }

    public String getTrailerLocation() {
        return TrailerLocation;
    }

    public void setTrailerLocation(String trailerLocation) {
        TrailerLocation = trailerLocation;
    }

    public String getTrailerLocationId() {
        return TrailerLocationId;
    }

    public void setTrailerLocationId(String trailerLocationId) {
        TrailerLocationId = trailerLocationId;
    }

    public String getUploadedDocuments() {
        return UploadedDocuments;
    }

    public void setUploadedDocuments(String uploadedDocuments) {
        UploadedDocuments = uploadedDocuments;
    }

    public String getUploadedPhotos() {
        return UploadedPhotos;
    }

    public void setUploadedPhotos(String uploadedPhotos) {
        UploadedPhotos = uploadedPhotos;
    }

    public String getTractor() {
        return Tractor;
    }

    public void setTractor(String tractor) {
        Tractor = tractor;
    }

    public String getTransportType() {
        return TransportType;
    }

    public void setTransportType(String transportType) {
        TransportType = transportType;
    }

    public String getSeal() {
        return Seal;
    }

    public void setSeal(String seal) {
        Seal = seal;
    }

    public String getTransportTypeId() {
        return TransportTypeId;
    }

    public void setTransportTypeId(String transportTypeId) {
        TransportTypeId = transportTypeId;
    }

    public String getTrailerLicencePlate() {
        return TrailerLicencePlate;
    }

    public void setTrailerLicencePlate(String trailerLicencePlate) {
        TrailerLicencePlate = trailerLicencePlate;
    }

    public String getRecTrailerId() {
        return RecTrailerId;
    }

    public void setRecTrailerId(String recTrailerId) {
        RecTrailerId = recTrailerId;
    }

    public String getTruckTranportId() {
        return TruckTranportId;
    }

    public void setTruckTranportId(String truckTranportId) {
        TruckTranportId = truckTranportId;
    }

    public String getShippingTrailerDBRefId() {
        return ShippingTrailerDBRefId;
    }

    public void setShippingTrailerDBRefId(String shippingTrailerDBRefId) {
        ShippingTrailerDBRefId = shippingTrailerDBRefId;
    }

    public String getRecTrailerDBRefId() {
        return RecTrailerDBRefId;
    }

    public void setRecTrailerDBRefId(String recTrailerDBRefId) {
        RecTrailerDBRefId = recTrailerDBRefId;
    }

    public String getTRFolio() {
        return TRFolio;
    }

    public void setTRFolio(String TRFolio) {
        this.TRFolio = TRFolio;
    }

    public String getEntranceDate() {
        return EntranceDate;
    }

    public void setEntranceDate(String entranceDate) {
        EntranceDate = entranceDate;
    }

    public String getCQCustomerCode() {
        return CQCustomerCode;
    }

    public void setCQCustomerCode(String CQCustomerCode) {
        this.CQCustomerCode = CQCustomerCode;
    }

    public String getWareHouseId() {
        return WareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        WareHouseId = wareHouseId;
    }

    public String getTRTrailerFolio() {
        return TRTrailerFolio;
    }

    public void setTRTrailerFolio(String TRTrailerFolio) {
        this.TRTrailerFolio = TRTrailerFolio;
    }

    public String getWareHouseCode() {
        return WareHouseCode;
    }

    public void setWareHouseCode(String wareHouseCode) {
        WareHouseCode = wareHouseCode;
    }

    public String getTruckTransportationLineId() {
        return TruckTransportationLineId;
    }

    public void setTruckTransportationLineId(String truckTransportationLineId) {
        TruckTransportationLineId = truckTransportationLineId;
    }

    public String getTruckTransportationLine() {
        return TruckTransportationLine;
    }

    public void setTruckTransportationLine(String truckTransportationLine) {
        TruckTransportationLine = truckTransportationLine;
    }

    public String getTrailerTypeId() {
        return TrailerTypeId;
    }

    public void setTrailerTypeId(String trailerTypeId) {
        TrailerTypeId = trailerTypeId;
    }

    public String getTrailerType() {
        return TrailerType;
    }

    public void setTrailerType(String trailerType) {
        TrailerType = trailerType;
    }

    public String getTruckStatus() {
        return TruckStatus;
    }

    public void setTruckStatus(String truckStatus) {
        TruckStatus = truckStatus;
    }

    public String getTruckLocation() {
        return TruckLocation;
    }

    public void setTruckLocation(String truckLocation) {
        TruckLocation = truckLocation;
    }

    public String getTruckFolio() {
        return TruckFolio;
    }

    public void setTruckFolio(String truckFolio) {
        TruckFolio = truckFolio;
    }

    public String getTKCompleted() {
        return TKCompleted;
    }

    public void setTKCompleted(String TKCompleted) {
        this.TKCompleted = TKCompleted;
    }

    public String getPhotosTaken() {
        return PhotosTaken;
    }

    public void setPhotosTaken(String photosTaken) {
        PhotosTaken = photosTaken;
    }

    public String getAccessOutDate() {
        return AccessOutDate;
    }

    public void setAccessOutDate(String accessOutDate) {
        AccessOutDate = accessOutDate;
    }
}
