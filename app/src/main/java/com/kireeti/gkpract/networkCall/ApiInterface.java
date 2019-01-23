package com.kireeti.gkpract.networkCall;

import com.google.gson.JsonObject;
import com.kireeti.gkpract.models.GatePassValid;
import com.kireeti.gkpract.models.GatePass_TrailerDetails_OrgStatus;
import com.kireeti.gkpract.models.NotThere;
import com.kireeti.gkpract.models.PhotoResults;
import com.kireeti.gkpract.models.QAFindingFolio;
import com.kireeti.gkpract.models.QAFindingFolioList;
import com.kireeti.gkpract.models.Trailer_Results;
import com.kireeti.gkpract.models.UserScreens;
import com.kireeti.gkpract.models.Valid_Sid;
import com.kireeti.gkpract.models.Warehouse;
import com.kireeti.gkpract.models.WarehouseResult;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("Photos/LogOn")
    Call<JsonObject> loginAction(@Field("UserId") String username,
                                 @Field("PWD") String password);   // user name-admin, password-test

    @GET("Photos/GetTrailers")
    Call<Trailer_Results> getTrailers(@Query("UserId") String userId,
                                      @Query("Type") int type,
                                      @Query("ValueToCheck") String valueToCheck);


    @Multipart
    @POST("Photos/SaveImages")
    Call<JsonObject> uploadMultipleFiles1(@Query("UserId") String userId,
                                         @Query("Type") int type,
                                         @Query("RefId") String refId,
                                         @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Photos/SaveImages")
    Call<ResponseBody> uploadMultipleFiles(@Query("UserId") String userId,
                                           @Query("Type") int type,
                                           @Query("RefId") String refId,
                                           @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Photos/SaveImages")
    Call<ResponseBody> uploadMultipleFiles_NotThere(@Query("UserId") String userId,
                                                    @Query("Type") int type,
                                                    @Query("RefId") String refId,
                                                    @Query("LocationId") String locationId,
                                                    @Part List<MultipartBody.Part> files);

    @GET("Photos/ValidateSID?")
    Call<Valid_Sid> validateSid(@Query("UserId") String userId,
                                @Query("Type") int type,
                                @Query("SID") String sid);

    @GET("Photos/GetFolioBasedPhotos")
    Call<PhotoResults> getFolioPhotos(@Query("id") String id,
                                      @Query("type") int type);

    @POST("Photos/Delete")
    Call<JsonObject> deletePhoto(@Query("id") String id,
                                 @Query("fileName") String fileName);

    @POST("/warehouse/List")
    Call<List<WarehouseResult>> getWarehouse(@Body JSONObject object);

    @GET("Photos/NextAvailableQAFindingFolio")
    Call<JsonObject> nextAvailableQAFinding(@Query("id") String id);

    @GET("Photos/GetQAFindingFolio")
    Call<List<QAFindingFolio>> getQAFindingFolio(@Query("id") String id,
                                                 @Query("value") String value);

    @Multipart
    @POST("Photos/SaveImages")
    Call<ResponseBody> uploadfile_QualityFinding(@Query("UserId") String userId,
                                                 @Query("Type") int type,
                                                 @Query("RefId") String refId,
                                                 @Query("Folio") String folio,
                                                 @Query("WhId") String warehouseId,
                                                 @Query("WhCode") String warehouseCode,
                                                 @Query("Comments") String comment,
                                                 @Query("PhotoComments") String imaageComment,
                                                 @Query("QAFCount") String qafCount,
                                                 @Query("isClosed") String close,
                                                 @Query("Email") String email,
                                                 @Part List<MultipartBody.Part> files);

    @POST("Inventory/QAFindingList")
    Call<List<QAFindingFolioList>> getQAFindingFolioList(@Body JsonObject data);


    @GET("User/GetUserScreens?")
    Call<List<UserScreens>> getUserScreens(@Query("Id") String id,
                                           @Query("culture") String value,
                                           @Query("appType") String apptype);

    @POST("Inventory/NotThereList")
    Call<List<NotThere>> getNotThere(@Body JsonObject data);


    @GET("GatePass/ValidateGatePass?")
    Call<GatePassValid> gatepassDetails1(@Query("folio") String id,
                                         @Query("whId") String whId);

    @GET("warehouse/WarehouseAutoList?")
    Call<List<Warehouse>> getWarehousesList(@Query("name") String id,
                                            @Query("userId") String userid);

    @GET("User/ChangeWarehouse?")
    Call<Integer> saveWarehouse(@Query("id") String id,
                                @Query("userId") String userid);

    @GET("Inventory/TrailerDetailsGet?")
    Call<GatePass_TrailerDetails_OrgStatus> gateTrailerDetailsOrgGpStatus(@Query("Id") String id);

    @GET("GatePass/GatePassExitMob?")
    Call<Integer> gatePassExit(@Query("Id") String id,
                               @Query("userId") String userid,
                               @Query("date") String date);

    @GET("GatePass/GatePassEntranceDate?")
    Call<Integer> gatePassEntranceDate(@Query("id") String id,
                                       @Query("date") String date);

    @Multipart
    @POST("GatePass/SaveImages")
    Call<JsonObject> uploadMultipleFiles_Gatepass(@Query("RefId") String refId,
                                                    @Query("Type") int type,
                                                    @Part List<MultipartBody.Part> files);

    @GET("GatePass/GatePassLeaveWithOutAccess?")
    Call<Integer> gatePassLeaveWithOutAcessDate(@Query("Id") String id,
                                                @Query("ExitGatePassDate") String date);

    @POST("GatePass/Delete?")
    Call<JsonObject> deleteGatePassPhoto(@Query("id") String id,
                                         @Query("fileName") String fileName,
                                         @Query("type") String type);

    @GET("GatePass/GetDriverLeaveWithOutAccesData?")
    Call<JsonObject> gateDriverLeaveWithOutAccesData(@Query("Id") String id,
                                                     @Query("isMob") String value);

    @GET("GatePass/GetWarehouseData?")
    Call<JsonObject> gateWarehouseData(@Query("Id") String id,
                                       @Query("isMob") String value);

    @POST("GatePass/SendDriverLeaveNotification?")
    Call<JsonObject> sendDriverLeaveNotification(@Body JsonObject string);

//    @GET("GatePass/GetWarehouseData?")
//    Call<JsonObject> gateWarehouseDataAddress(@Query("Id") String id);
//
//
//    @POST(" GatePass/SendDriverLeaveNotification")
//    Call<JsonObject> sendDriverLeaveWithoutNotification(@Body JsonObject string);
}
