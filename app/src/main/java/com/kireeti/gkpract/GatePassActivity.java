package com.kireeti.gkpract;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kireeti.gkpract.adapter.ImageAdapter;
import com.kireeti.gkpract.adapter.PackingSlipListAdapter;
import com.kireeti.gkpract.adapter.WareHouseListAdapter;
import com.kireeti.gkpract.cameraView.CamActivity;
import com.kireeti.gkpract.customClass.TouchImageView;
import com.kireeti.gkpract.interfaces.DeletePhoto;
import com.kireeti.gkpract.interfaces.WareHouseAuto;
import com.kireeti.gkpract.models.GatePassValid;
import com.kireeti.gkpract.models.GatePass_TrailerDetails_OrgStatus;
import com.kireeti.gkpract.models.ImageFile;
import com.kireeti.gkpract.models.TrailerDetails;
import com.kireeti.gkpract.models.TrailerDetails_OrgGpStatus;
import com.kireeti.gkpract.models.Warehouse;
import com.kireeti.gkpract.networkCall.ApiClient;
import com.kireeti.gkpract.networkCall.ApiInterface;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GatePassActivity extends AppCompatActivity implements View.OnClickListener, DeletePhoto, WareHouseAuto {

    ExpandableLayout expandableLayout;
    LinearLayout expandable_button;
    private ApiInterface apiService;
    private ApiInterface apiService_another;
    private File destination;
    private int mWidth;
    File rootDir;
    int viewWidth;
    private ImageView back_image;
    private ImageView lagout_image;
    private Button gatepass_Scan_btn;
    private IntentIntegrator gate_pass_Scan, packingList_scan;
    private int REQUEST_CAMERA = 0;
    private String gatepass_scanned_Id;
    private int PHOTOS;
    private EditText gatepass_editView;
    private ProgressDialog validate_GatePass_Dialog;
    private Call<JsonObject> validateGatepass_Call;
    private Call<GatePassValid> gate_Pass_Call;
    private TextView tractor, container_number;
    private TextView driver, customer, status, trailer_number;
    private String Id = "";
    private int PHOTOS_PATH_NUMBER;
    private RoundedImageView rectangular_image;
    private int PHOTOS_NUMBER;
    ArrayList<ImageFile> AllFilePaths = new ArrayList<>();
    ArrayList<ImageFile> CapturedImage_Path = new ArrayList<>();
    LinearLayout photo_gallery, photo_list_layout;
    private RecyclerView gatePass_photos_recycler;
    private Button takephoto;
    private Button saveimage_btn;
    private ExpandableLayout expandable_layout;
    private ProgressDialog gatePass_dialog;
    private Call<JsonObject> call_UploadDoc;
    private String userId;
    private TrailerDetails trailerDetails;
    private TrailerDetails_OrgGpStatus trailerDetails_orgGpstatus;
    private CheckedTextView simpleCheckedTextView;
    private CheckedTextView total_packing_list_CheckedTextView;

    private AutoCompleteTextView warehouse_autocomplete;
    private ProgressDialog warehouse_dialog;
    private Call<List<Warehouse>> call_Warehouse;
    List<Warehouse> warehouse_list = new ArrayList<>();
    private PopupWindow warehuse_popup;
    private String warehouse_id;
    private TextView selction_for_warehouse;
    private ImageView cancel_popup;
    Button save_button;
    private String selected_autocomplete_Warehouse;
    private ProgressDialog gatePass_Exit_dialog;
    private Call<Integer> call_gatePassExit;
    private ProgressDialog gatePass_TrailerDetailsOrgGpStatus_Dialog;

    private Call<GatePass_TrailerDetails_OrgStatus> call_GatePass_TrailerDetails_OrgStatus;
    private LinearLayout orgGpOut_Layout, photos_Layout;
    private Button complete_Exit_Buttton;
    private EditText input_seal_Validate_edittext;
    private CheckedTextView seal_CheckedTextview;
    private LinearLayout loadedOut_Layout;
    private ProgressDialog save_warehouse_dialog;
    private Call<Integer> call_saveWarehouse;
    private String selected_autocomplete_Warehouse_ID;
    private TextView current_warehouse_text;

    private ArrayList<PackingSlip> list_packingslip = new ArrayList<>();
    private RecyclerView packingslip_scan_recycler;
    private String PackingSlip_str;
    private PackingSlip packing_slip;
    private Button packing_slip_scan_btn;
    private String packing_slip_scanned_Id;
    private TextView total_packing_list_text;
    private PackingSlipListAdapter mAdaper;
    private ArrayList<PackingSlip> newList = new ArrayList<>();
    private TextView no_documents_text;
    private Button gatePass_btn;
    private ProgressDialog gate_Pass_EntranceDate_dialog;
    private String dateToStr;
    private Button leave_without_access_btn;
    private ProgressDialog leave_Without_Acess_dialog;
    private String refId = "";
    private String photo_Name;
    private ProgressDialog delete_Dialog;
    private Call<JsonObject> delete_Picture;
    private Call<JsonObject> call_getpass_Leave_withOut_acess_Picture;
    private Call<JsonObject> call_getpass_Data;
    private String gatePass_existing_Photo_str;
    private TouchImageView touchImage;
    private ImageView zoom_remove_photo;
    private Dialog toucchimage_Dialog;
    private ProgressDialog waehouse_LeaveWithOut_Dialog;
    private Call<JsonObject> call_SendDriver_Leave_With_Out_Access;
    private int Leave_WithOut_Access_Id;
    private String sccaner_code;
    private String GatePass_out_Id;
    private int CAMERA_REQUEST = 0;

    private List<MultipartBody.Part> multipleFiles;
    private ProgressDialog waehouse_Dialog;
    private String trailerNumber, trTrailerFolio, tractor_str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gate_pass);
        apiService = ApiClient.getGatePassClient(GatePassActivity.this).create(ApiInterface.class);
        apiService_another = ApiClient.getClient(GatePassActivity.this).create(ApiInterface.class);

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
        dateToStr = format.format(today);

        Display display = getWindowManager().getDefaultDisplay();
        mWidth = display.getWidth();
        viewWidth = mWidth / 3;
        rootDir = new File(Environment.getExternalStorageDirectory() + "/" + "Cargoquin");
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        selected_autocomplete_Warehouse = Utilities.getPref(GatePassActivity.this, "WarehouseName", "");
        selected_autocomplete_Warehouse_ID = Utilities.getPref(GatePassActivity.this, "WarehouseId", "");

        findViewes();
    }

    private void findViewes() {
        back_image = (ImageView) findViewById(R.id.back_image);
        back_image.setOnClickListener(this);

        lagout_image = (ImageView) findViewById(R.id.lagout_image);
        lagout_image.setOnClickListener(this);

        expandable_button = (LinearLayout) findViewById(R.id.expandable_button);
        expandable_button.setOnClickListener(this);

        selction_for_warehouse = (TextView) findViewById(R.id.selction_for_warehouse);
        selction_for_warehouse.setOnClickListener(this);
        selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));

        gatepass_Scan_btn = (Button) findViewById(R.id.gatepass_Scan_btn);
        gatepass_Scan_btn.setOnClickListener(this);

        takephoto = (Button) findViewById(R.id.takephoto);
        takephoto.setOnClickListener(this);

        gatePass_photos_recycler = (RecyclerView) findViewById(R.id.gatePass_photos_recycler);

        gatepass_editView = (EditText) findViewById(R.id.gatepass_editView);
        gatepass_editView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Utilities.callFromTakePhoto = false;
                    if (!gatepass_editView.getText().toString().equalsIgnoreCase("")) {
                        expandable_layout.setVisibility(View.GONE);
                        expandable_button.setVisibility(View.GONE);
                        complete_Exit_Buttton.setVisibility(View.GONE);
                        photos_Layout.setVisibility(View.GONE);
                        orgGpOut_Layout.setVisibility(View.GONE);
                        seal_CheckedTextview.setVisibility(View.GONE);
                        loadedOut_Layout.setVisibility(View.GONE);

                        photo_list_layout.setVisibility(View.GONE);
                        list_packingslip.clear();
                        CapturedImage_Path.clear();
                        PHOTOS_PATH_NUMBER = 000;
                        PHOTOS = 000;
                        PHOTOS_NUMBER = 000;
                        AllFilePaths.clear();
                        Utilities.LastPhotoNumber = 0;
                        Utilities.LastPhotoPathNumber = 0;

                        Utilities.Loaded_Out_Photoes = false;
                        Utilities.SecondGatePassAPI = false;
                        Utilities.ToatalPackingListScannedCheck = false;
                        gatePass_btn.setVisibility(View.GONE);
                        leave_without_access_btn.setVisibility(View.GONE);
                        total_packing_list_text.setText(String.valueOf("Total Packing List : " + 0));
                        validate_Gatepass_ServiceCall(gatepass_editView.getText().toString());
                    } else {
                        Utilities.showToast(GatePassActivity.this, "Please enter correct truck folio");
                    }
                }
                return false;
            }
        });

        container_number = (TextView) findViewById(R.id.container_number);
        tractor = (TextView) findViewById(R.id.tractor);
        driver = (TextView) findViewById(R.id.driver);
        trailer_number = (TextView) findViewById(R.id.trailer_number);
        status = (TextView) findViewById(R.id.status);
        customer = (TextView) findViewById(R.id.customer);

        photo_list_layout = (LinearLayout) findViewById(R.id.photo_list_layout);
        photo_gallery = (LinearLayout) findViewById(R.id.photo_gallery);

        saveimage_btn = (Button) findViewById(R.id.saveimage_btn);
        saveimage_btn.setOnClickListener(this);

        complete_Exit_Buttton = (Button) findViewById(R.id.complete_Exit_Buttton);
        complete_Exit_Buttton.setOnClickListener(this);

        gatePass_btn = (Button) findViewById(R.id.gatePass_btn);
        gatePass_btn.setOnClickListener(this);

        leave_without_access_btn = (Button) findViewById(R.id.leave_without_access_btn);
        leave_without_access_btn.setOnClickListener(this);

//        simpleCheckedTextView = (CheckedTextView) findViewById(R.id.simpleCheckedTextView);
//        simpleCheckedTextView.setOnClickListener(this);

        rectangular_image = (RoundedImageView) findViewById(R.id.rectangular_image);
        rectangular_image.setOnClickListener(this);


        expandable_layout = (ExpandableLayout) findViewById(R.id.expandable_layout);

        orgGpOut_Layout = (LinearLayout) findViewById(R.id.orgGpOut_Layout);
        photos_Layout = (LinearLayout) findViewById(R.id.photos_Layout);

        packingslip_scan_recycler = (RecyclerView) findViewById(R.id.scan_barcode_recycler);

        seal_CheckedTextview = (CheckedTextView) findViewById(R.id.seal_CheckedTextview);

        loadedOut_Layout = (LinearLayout) findViewById(R.id.loadedOut_Layout);

        total_packing_list_text = (TextView) findViewById(R.id.total_packing_list_text);
        total_packing_list_CheckedTextView = (CheckedTextView) findViewById(R.id.total_packing_list_CheckedTextView);
        total_packing_list_CheckedTextView.setVisibility(View.GONE);

        no_documents_text = (TextView) findViewById(R.id.no_documents_text);


        packing_slip_scan_btn = (Button) findViewById(R.id.packing_slip_scan_btn);
        packing_slip_scan_btn.setOnClickListener(this);

        input_seal_Validate_edittext = (EditText) findViewById(R.id.input_seal_Validate_edittext);
        input_seal_Validate_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (trailerDetails_orgGpstatus.getExitSealNumber() != null) {
                    if (trailerDetails_orgGpstatus.getExitSealNumber().equalsIgnoreCase(input_seal_Validate_edittext.getText().toString().trim())) {
                        seal_CheckedTextview.setVisibility(View.VISIBLE);
                    } else {
                        seal_CheckedTextview.setVisibility(View.GONE);
                        Utilities.showToast(GatePassActivity.this, " Please enter Correct ExitSeal Number");
                    }
                }
                return false;
            }
        });

        if (!Utilities.getPref(GatePassActivity.this, "UserId", "").equalsIgnoreCase("")) {
            userId = Utilities.getPref(GatePassActivity.this, "UserId", "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Utilities.saveButton) {
            Intent cam = new Intent(GatePassActivity.this, CamActivity.class);
            startActivityForResult(cam, 0);
        }

        selected_autocomplete_Warehouse = Utilities.getPref(GatePassActivity.this, "WarehouseName", "");
        selected_autocomplete_Warehouse_ID = Utilities.getPref(GatePassActivity.this, "WarehouseId", "");

        selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.expandable_button:
                expandable_layout.toggle();
                break;

            case R.id.back_image:
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_left);
                break;

            case R.id.lagout_image:
                Utilities.savebooleanPref(getApplicationContext(), "HasLogged_In", false);
                Intent returnTOLogin = new Intent(getApplicationContext(), LoginActivity.class);
                returnTOLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                returnTOLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(returnTOLogin);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.gatepass_Scan_btn:
                expandable_layout.setVisibility(View.GONE);
                expandable_button.setVisibility(View.GONE);
                complete_Exit_Buttton.setVisibility(View.GONE);
                photos_Layout.setVisibility(View.GONE);
                orgGpOut_Layout.setVisibility(View.GONE);
                seal_CheckedTextview.setVisibility(View.GONE);
                loadedOut_Layout.setVisibility(View.GONE);
                list_packingslip.clear();
                Utilities.ToatalPackingListScannedCheck = false;
                gatePass_btn.setVisibility(View.GONE);
                leave_without_access_btn.setVisibility(View.GONE);
                Utilities.SecondGatePassAPI = false;
                Utilities.Loaded_Out_Photoes = false;
                photo_list_layout.setVisibility(View.GONE);
                total_packing_list_text.setText(String.valueOf("Total Packing List : " + 0));

                list_packingslip.clear();
                CapturedImage_Path.clear();
                Utilities.LastPhotoNumber = 0;
                Utilities.LastPhotoPathNumber = 0;
                PHOTOS_PATH_NUMBER = 000;
                PHOTOS = 000;
                PHOTOS_NUMBER = 000;
                AllFilePaths.clear();

                Utilities.GATEPASS_SCANNER = true;
                Utilities.PACKING_SLIP_SCANNER = false;

                gatepass_editView.setText("");
                gate_pass_Scan = new IntentIntegrator(GatePassActivity.this);
                gate_pass_Scan.setOrientationLocked(true);
                // Intent intent = gate_pass_Scan.createScanIntent();
                // startActivityForResult(intent, 1);
                gate_pass_Scan.initiateScan();
                break;

            case R.id.takephoto:
                if (Id.length() > 0) {
                    Intent cam = new Intent(GatePassActivity.this, CamActivity.class);
                    startActivityForResult(cam, 0);

//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                } else {
                    Utilities.showToast(GatePassActivity.this, "Please enter or scan truck folio ");
                }
                break;

            case R.id.saveimage_btn:
                if (AllFilePaths.size() > 0) {
                    uploadDocuments();
                } else {
                    Utilities.showToast(GatePassActivity.this, "Please Select or Capture at least one Photo");
                }
                break;

            case R.id.total_packing_list_CheckedTextView:
                if (simpleCheckedTextView.isChecked()) {
                    simpleCheckedTextView.setCheckMarkDrawable(0);
                    simpleCheckedTextView.setChecked(false);
                } else {
                    simpleCheckedTextView.setCheckMarkDrawable(R.drawable.ic_verified);
                    simpleCheckedTextView.setChecked(true);
                }
                break;

            case R.id.selction_for_warehouse:
                show_Popup_Warehouse();
                break;

            case R.id.complete_Exit_Buttton:
                if (Utilities.SecondGatePassAPI) {

                    if (trailerDetails_orgGpstatus.getOrgGPStatusOut() != null) {
                        if (trailerDetails_orgGpstatus.getOrgGPStatusOut().equalsIgnoreCase("Loaded In")) {
                            if (input_seal_Validate_edittext.getText().toString().equalsIgnoreCase(trailerDetails_orgGpstatus.getExitSealNumber())) {
                                saveWarehouse();
                            } else {
                                Utilities.showToast(GatePassActivity.this, "Please Enter Correct InputSeal");
                            }
                        } else if (trailerDetails_orgGpstatus.getOrgGPStatusOut().equalsIgnoreCase("Loaded Out")) {
                            if (Utilities.ToatalPackingListScannedCheck) {
                                if (input_seal_Validate_edittext.getText().toString().trim().equalsIgnoreCase(trailerDetails_orgGpstatus.getExitSealNumber())) {
                                    saveWarehouse();
                                } else {
                                    Utilities.showToast(GatePassActivity.this, "Please Enter Correct InputSeal");
                                }
                            } else {
                                Utilities.showToast(GatePassActivity.this, "Please Scan Barcode ");
                            }
                        } else if (trailerDetails_orgGpstatus.getOrgGPStatusOut().equalsIgnoreCase("Empty Out")) {
                            saveWarehouse();

                        } else if (trailerDetails_orgGpstatus.getOrgGPStatusOut().equalsIgnoreCase("Empty In")) {
                            saveWarehouse();
                        } else {
                            saveWarehouse();
                        }
                    }
                } else {
                    saveWarehouse();
                }

                break;

            case R.id.packing_slip_scan_btn:
                Utilities.Packing_Slip_Scan_Check_Toast = false;
                Utilities.PACKING_SLIP_SCANNER = true;
                Utilities.GATEPASS_SCANNER = false;
                packingList_scan = new IntentIntegrator(GatePassActivity.this);
                packingList_scan.setOrientationLocked(true);
                packingList_scan.initiateScan();
                break;

            case R.id.gatePass_btn:
                gatePassEntranceDate();
                break;

            case R.id.leave_without_access_btn:
                leaveWithoutAccessService();
                break;

            case R.id.rectangular_image:
                if (AllFilePaths.size() > 0) {
                    if (!rectangular_image.getTag().toString().equalsIgnoreCase("")) {
                        photo_list_layout.setVisibility(View.VISIBLE);
                        zoom_CapturedImage(rectangular_image.getTag().toString());
                    }
                } else {
                    photo_list_layout.setVisibility(View.GONE);
                    Utilities.showToast(GatePassActivity.this, "Please Select or Capture at least one Photo");

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            addImageToRecycleView(data, requestCode);
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (Utilities.GATEPASS_SCANNER) {
                    Utilities.GATEPASS_SCANNER = false;
                    if (result.getContents() == null) {
                        Utilities.showToast(GatePassActivity.this, "Result Not Found");
                    } else {
                        gatepass_scanned_Id = result.getContents();

                        if (!gatepass_scanned_Id.equalsIgnoreCase("")) {

                            try {
                                if (gatepass_scanned_Id.contains("http")) {
                                    String[] items = gatepass_scanned_Id.split("ftk=");
                                    sccaner_code = items[1];
                                } else {
                                    sccaner_code = gatepass_scanned_Id;
                                }

                                gatepass_editView.setText(sccaner_code);
                                validate_Gatepass_ServiceCall(sccaner_code.trim());
                                PHOTOS = 000;
                            } catch (Exception e) {
                                e.printStackTrace();
                                gatepass_editView.setText(sccaner_code);
                                validate_Gatepass_ServiceCall(sccaner_code);
                                PHOTOS = 000;
                            }
                        }
                    }
                } else if (Utilities.PACKING_SLIP_SCANNER) {
                    Utilities.PACKING_SLIP_SCANNER = false;
                    if (result.getContents() == null) {
                        Utilities.showToast(GatePassActivity.this, "Result Not Found");
                    } else {
                        packing_slip_scanned_Id = result.getContents();
                        if (!packing_slip_scanned_Id.equalsIgnoreCase("")) {
                            checked_packingSlip_scannerId(packing_slip_scanned_Id);
                        }
                    }
                }
            } else {
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void checked_packingSlip_scannerId(String packing_slip_scanned_id) {
        newList = getSelectedInfo(packing_slip_scanned_id);
        int count = 0;
        for (int i = 0; i < newList.size(); i++) {
            if (newList.get(i).isAvailable()) {
                count = count + 1;
            }
        }
        total_packing_list_text.setText(String.valueOf("Total Packing List : " + count));

        if (count == newList.size()) {
            Utilities.ToatalPackingListScannedCheck = true;
            total_packing_list_CheckedTextView.setVisibility(View.VISIBLE);
        } else {
            Utilities.ToatalPackingListScannedCheck = false;
            total_packing_list_CheckedTextView.setVisibility(View.GONE);
        }

        PackingSlipListAdapter mAdaper = new PackingSlipListAdapter(GatePassActivity.this, newList);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(GatePassActivity.this, LinearLayoutManager.VERTICAL, false);
        packingslip_scan_recycler.setLayoutManager(layoutManager2);
        //  mAdaper.setCallBack(GatePassActivity.this);
        packingslip_scan_recycler.setAdapter(mAdaper);
        mAdaper.notifyDataSetChanged();
    }

    public ArrayList<PackingSlip> getSelectedInfo(String value) {
        for (PackingSlip autoFill : list_packingslip) {
            if (value.equals(autoFill.getPackingslipText())) {
                Utilities.Packing_Slip_Scan_Check_Toast = true;
                autoFill.setAvailable(true);
            }
        }

        if (Utilities.Packing_Slip_Scan_Check_Toast) {

        } else {
            Toast.makeText(getApplicationContext(), "Please scan correct barcode", Toast.LENGTH_SHORT).show();
        }

        return list_packingslip;
    }

    private void validate_Gatepass_ServiceCall(String scannerId) {
        validate_GatePass_Dialog = new ProgressDialog(GatePassActivity.this);
        validate_GatePass_Dialog.setMessage("Loading...");
        validate_GatePass_Dialog.setCanceledOnTouchOutside(false);
        validate_GatePass_Dialog.setCancelable(false);
        if (validate_GatePass_Dialog != null && !validate_GatePass_Dialog.isShowing()) {
            validate_GatePass_Dialog.show();
        }
        gate_Pass_Call = apiService.gatepassDetails1(scannerId,selected_autocomplete_Warehouse_ID);
        gate_Pass_Call.enqueue(new Callback<GatePassValid>() {
            @Override
            public void onResponse(Call<GatePassValid> call, Response<GatePassValid> response) {
                if (validate_GatePass_Dialog != null && validate_GatePass_Dialog.isShowing()) {
                    validate_GatePass_Dialog.dismiss();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("Success")) {
                            if (response.body().getTrailerDetails() != null) {
                                trailerDetails = new TrailerDetails();
                                trailerDetails.setTruckNo(response.body().getTrailerDetails().getTruckNo());
                                trailerDetails.setStatus(response.body().getTrailerDetails().getStatus());
                                trailerDetails.setCustomer(response.body().getTrailerDetails().getCustomer());
                                trailerDetails.setTrailerNumber(response.body().getTrailerDetails().getTrailerNumber());
                                trailerDetails.setTractor(response.body().getTrailerDetails().getTractor());
                                trailerDetails.setId(response.body().getTrailerDetails().getId());
                                trailerDetails.setEntranceDate(response.body().getTrailerDetails().getEntranceDate());
                                trailerDetails.setTruckStatus(response.body().getTrailerDetails().getTruckStatus());
                                trailerDetails.setPhotosTaken(response.body().getTrailerDetails().getPhotosTaken());
                                trailerDetails.setRecTrailerDBRefId(response.body().getTrailerDetails().getRecTrailerDBRefId());
                                trailerDetails.setUploadedPhotos(response.body().getTrailerDetails().getUploadedPhotos());
                                trailerDetails.setAccessOutDate(response.body().getTrailerDetails().getAccessOutDate());
                                trailerDetails.setTRTrailerFolio(response.body().getTrailerDetails().getTRTrailerFolio());
                                trailerDetails.setTractor(response.body().getTrailerDetails().getTractor());

                                trailerDetails.setCQCustomer(response.body().getTrailerDetails().getCQCustomer());
                                trailerDetails.setDriver(response.body().getTrailerDetails().getDriver());
                                trailerDetails.setRegistrationDate(response.body().getTrailerDetails().getRegistrationDate());
                                trailerDetails.setDriverLicense(response.body().getTrailerDetails().getDriverLicense());
                                trailerDetails.setTrailerType(response.body().getTrailerDetails().getTrailerType());
                                trailerDetails.setTransportationLine(response.body().getTrailerDetails().getTransportationLine());

                                expandable_button.setVisibility(View.VISIBLE);
                                expandable_layout.setVisibility(View.VISIBLE);
                            }

                            container_number.setText(trailerDetails.getTruckNo());
                            status.setText(trailerDetails.getStatus());
                            tractor.setText(trailerDetails.getTractor());
                            driver.setText(trailerDetails.getDriver());
                            status.setText(trailerDetails.getStatus());
                            trailer_number.setText(trailerDetails.getTrailerNumber());
                            customer.setText(trailerDetails.getCustomer());
                            Id = trailerDetails.getId();
                            refId = trailerDetails.getRecTrailerDBRefId();
                            gatePass_existing_Photo_str = trailerDetails.getUploadedPhotos();

                            trailerNumber = trailerDetails.getTrailerNumber();
                            trTrailerFolio = trailerDetails.getTRTrailerFolio();
                            tractor_str = trailerDetails.getTractor();




                            Utilities.showToast(GatePassActivity.this, "Valid GatePass");
                            if (trailerDetails.getTruckStatus() != null) {

                                if (trailerDetails.getTruckStatus().equalsIgnoreCase("579") || Integer.parseInt(trailerDetails.getRecTrailerDBRefId()) > 0) {
                                    if (trailerDetails.getAccessOutDate() != null) {
                                        status.setText("Shipment departured.");
                                        Utilities.showToast(GatePassActivity.this, "Trailer/Truck already exit");
                                    } else {
                                        gatepass_TrailerDetails_OrgGpStatus_ServiceCall(Id); //previously send :Id
                                    }

                                } else {

                                    existingPhotoesPrepare();
                                    if (AllFilePaths.size() > 0) {
                                        rectangular_image.setVisibility(View.VISIBLE);
                                        photo_list_layout.setVisibility(View.VISIBLE);

                                    } else {
                                        rectangular_image.setVisibility(View.GONE);
                                        photo_list_layout.setVisibility(View.GONE);
                                    }

                                    Utilities.Loaded_Out_Photoes = false;
                                    if (trailerDetails.getPhotosTaken() != null) {
                                        orgGpOut_Layout.setVisibility(View.GONE);
                                        takephoto.setVisibility(View.VISIBLE);
                                        photos_Layout.setVisibility(View.VISIBLE);
                                        complete_Exit_Buttton.setVisibility(View.VISIBLE);
                                    } else {
                                        if (trailerDetails.getEntranceDate() != null) {
                                            photos_Layout.setVisibility(View.VISIBLE);
                                            takephoto.setVisibility(View.VISIBLE);
                                            gatePass_btn.setVisibility(View.GONE);
                                            leave_without_access_btn.setVisibility(View.GONE);

                                        } else {
                                            if (trailerDetails.getAccessOutDate() != null) {
                                                gatePass_btn.setVisibility(View.GONE);
                                                leave_without_access_btn.setVisibility(View.GONE);
                                                takephoto.setVisibility(View.GONE);
                                                photos_Layout.setVisibility(View.GONE);
                                                Utilities.showToast(GatePassActivity.this, "Truck is leave without access");
                                            } else {
                                                gatePass_btn.setVisibility(View.VISIBLE);
                                                leave_without_access_btn.setVisibility(View.VISIBLE);
                                                takephoto.setVisibility(View.GONE);
                                                photos_Layout.setVisibility(View.GONE);
                                            }

                                        }
                                    }
                                }
                            }
                        } else {
                            showAlert_gatepass("Alert", "Please enter correct truck folio");
                            takephoto.setVisibility(View.GONE);
                        }
                    } else {
                        showAlert_gatepass("Alert", "Please enter correct truck folio");
                        takephoto.setVisibility(View.GONE);
                    }

                } else {
                    Utilities.showToast(GatePassActivity.this, "Please try again later");
                }

            }

            @Override
            public void onFailure(Call<GatePassValid> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(GatePassActivity.this, "Please check your internet connection.");
                    if (validate_GatePass_Dialog != null && validate_GatePass_Dialog.isShowing()) {
                        validate_GatePass_Dialog.dismiss();
                    }
                } else {
                    Utilities.showToast(GatePassActivity.this, "Please check your internet connection.");
                    Utilities.showToast(GatePassActivity.this, t.getMessage());
                    if (validate_GatePass_Dialog != null && validate_GatePass_Dialog.isShowing()) {
                        validate_GatePass_Dialog.dismiss();
                    }
                }
            }
        });
    }

    private void existingPhotoesPrepare() {
        if (gatePass_existing_Photo_str != null && !gatePass_existing_Photo_str.isEmpty()) {
            String[] items = gatePass_existing_Photo_str.split(",");
            for (int i = 0; i < items.length; i++) {

                ImageFile mModel = new ImageFile();
                mModel.setId(items[i].split("##")[1]);
                mModel.setFileName(items[i].split("\\.")[0] + ".jpg");
                mModel.setFilePath(ApiClient.PICTURE_SHOW_URL+"Content/Uploads/Photos/GatePassPhotos/" + items[i].split("\\.")[0] + ".jpg");
                AllFilePaths.add(mModel);

                Picasso.with(GatePassActivity.this).load(mModel.getFilePath())
                        .placeholder(R.drawable.ic_placeholder)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(rectangular_image);

                rectangular_image.setTag(mModel.getFilePath());

                ImageAdapter mAdaper = new ImageAdapter(GatePassActivity.this, AllFilePaths, true);
                RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(GatePassActivity.this, LinearLayoutManager.HORIZONTAL, false);
                gatePass_photos_recycler.setLayoutManager(layoutManager2);
                mAdaper.setCallBack(GatePassActivity.this);
                gatePass_photos_recycler.setAdapter(mAdaper);

            }
        }

    }

    private void showAlert_gatepass(String title_text, String message_text) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (Utilities.screenWidth * 0.90);//WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView message = (TextView) dialog.findViewById(R.id.message);

        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                gatepass_editView.setText("");
            }
        });

        title.setText(title_text);
        message.setText(message_text);
        dialog.show();
    }

    @SuppressLint("ResourceAsColor")
    private void addImageToRecycleView(Intent data, int requestCode) {
        if (data != null) {
            Bitmap thumbnail = null;
            if (requestCode == 0) {
                //  thumbnail = (Bitmap) data.getExtras().get("data");
                thumbnail = BitmapFactory.decodeFile(String.valueOf(data.getExtras().get("data")));
            } else {
                thumbnail = (Bitmap) data.getExtras().get("data");
            }

            // if you want to change the image size 300*450 pixel use this below code
            //  Bitmap resizedBitmap = Bitmap.createScaledBitmap(thumbnail, 300, 450, false);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            ImageFile bolObj = new ImageFile();

            if (Utilities.LastPhotoPathNumber == 0) {
                if (PHOTOS_PATH_NUMBER == 000) {
                    destination = new File(rootDir, gatepass_editView.getText().toString() + "_001" + ".jpg");
                    PHOTOS_PATH_NUMBER = 001;
                } else {
                    DecimalFormat formatter = new DecimalFormat("000");
                    PHOTOS_PATH_NUMBER = PHOTOS_PATH_NUMBER + 1;
                    String s = formatter.format(PHOTOS_PATH_NUMBER);
                    destination = new File(rootDir, gatepass_editView.getText().toString() + "_" + (s) + ".jpg");
                }
            } else {
                DecimalFormat formatter = new DecimalFormat("000");
                Utilities.LastPhotoPathNumber = Utilities.LastPhotoPathNumber + 1;
                String s = formatter.format(Utilities.LastPhotoPathNumber + 1);
                destination = new File(rootDir, gatepass_editView.getText().toString() + "_" + (s) + ".jpg");
            }

            String bol_filePath = destination.getAbsolutePath();
            bolObj.setFilePath(bol_filePath);

            rectangular_image.setTag(destination);
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (Utilities.Loaded_Out_Photoes) {
                photo_Name = "GatePassOut_";
            } else {
                photo_Name = "GatePassIn_";
            }

            if (Utilities.LastPhotoNumber == 0) {
                if (PHOTOS_NUMBER == 000) {
                    bolObj.setFileName(photo_Name + gatepass_editView.getText().toString() + "_001" + ".jpg");
                    PHOTOS_NUMBER = 001;
                } else {
                    DecimalFormat formatter = new DecimalFormat("000");
                    PHOTOS_NUMBER = PHOTOS_NUMBER + 1;
                    String s = formatter.format(PHOTOS_NUMBER);
                    bolObj.setFileName(photo_Name + gatepass_editView.getText().toString() + "_" + (s) + ".jpg");
                }
            } else {
                DecimalFormat formatter = new DecimalFormat("000");
                Utilities.LastPhotoNumber = Utilities.LastPhotoNumber + 1;
                String s = formatter.format(Utilities.LastPhotoNumber);
                bolObj.setFileName(photo_Name + gatepass_editView.getText().toString() + "_" + (s) + ".jpg");
            }

            CapturedImage_Path.add(bolObj);
            rectangular_image.setVisibility(View.VISIBLE);

            //Images send To APi so We adding MultiPart>Body
            File file = null;
            RequestBody reqFile = null;
            MultipartBody.Part body = null;
            multipleFiles = new ArrayList<>();

            for (int i = 0; i < CapturedImage_Path.size(); i++) {
                file = new File(CapturedImage_Path.get(i).getFilePath());
                reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                body = MultipartBody.Part.createFormData(CapturedImage_Path.get(i).getFileName(), CapturedImage_Path.get(i).getFileName(), reqFile);
                multipleFiles.add(body);
            }

            if (AllFilePaths.size() > 0) {
                photo_list_layout.setVisibility(View.VISIBLE);
            } else {
                photo_list_layout.setVisibility(View.GONE);
            }
            Picasso.with(GatePassActivity.this).load(destination)
                    .placeholder(R.drawable.ic_placeholder)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(rectangular_image);

            AllFilePaths.add(bolObj);
            ImageAdapter mAdaper = new ImageAdapter(GatePassActivity.this, AllFilePaths, false);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(GatePassActivity.this, LinearLayoutManager.HORIZONTAL, false);
            gatePass_photos_recycler.setLayoutManager(layoutManager2);
            mAdaper.setCallBack(GatePassActivity.this);
            gatePass_photos_recycler.setAdapter(mAdaper);
            mAdaper.notifyDataSetChanged();
        } else {
            Utilities.showToast(GatePassActivity.this, "Capture Picture Properlly");
        }
    }

    @Override
    public void deletePhoto(int position, ImageFile mModel) {

        if (mModel.getId() != null && !mModel.getId().equalsIgnoreCase("")) {
            delete_Gatepass_Photo_Service(mModel.getId(), position, mModel.getFileName());
        } else {
            if (CapturedImage_Path.contains(AllFilePaths.get(position))) {
                CapturedImage_Path.remove(AllFilePaths.get(position));
            }
            AllFilePaths.remove(position);

            if (PHOTOS_NUMBER == 001) {
                PHOTOS_NUMBER = 000;
            } else {

            }
            ImageAdapter mAdaper = new ImageAdapter(GatePassActivity.this, AllFilePaths, false);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(GatePassActivity.this, LinearLayoutManager.HORIZONTAL, false);
            gatePass_photos_recycler.setLayoutManager(layoutManager2);
            mAdaper.setCallBack(GatePassActivity.this);
            gatePass_photos_recycler.setAdapter(mAdaper);
            mAdaper.notifyDataSetChanged();

        }
        if (position > 0) {
            rectangular_image.setTag(AllFilePaths.get(position - 1).getFilePath());
            if (AllFilePaths.get(position - 1).getFilePath().contains("https")) {
                Picasso.with(GatePassActivity.this)
                        .load(AllFilePaths.get(position - 1).getFilePath())
                        .placeholder(R.drawable.ic_placeholder)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(rectangular_image);
            } else {
                Picasso.with(GatePassActivity.this)
                        .load(new File(AllFilePaths.get(position - 1).getFilePath()))
                        .placeholder(R.drawable.ic_placeholder)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(rectangular_image);

            }
        } else {
            rectangular_image.setVisibility(View.INVISIBLE);
        }

    }

    private void delete_Gatepass_Photo_Service(String id, final int position, String fileName) {
        delete_Dialog = new ProgressDialog(GatePassActivity.this);
        delete_Dialog.setMessage("Loading...");
        delete_Dialog.setCanceledOnTouchOutside(false);
        delete_Dialog.setCancelable(false);
        if (delete_Dialog != null && !delete_Dialog.isShowing()) {
            delete_Dialog.show();
        }
        delete_Picture = apiService.deleteGatePassPhoto(id, fileName, "7");
        delete_Picture.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (delete_Dialog.isShowing()) {
                    delete_Dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Utilities.showToast(GatePassActivity.this, response.body().get("Status").getAsString());

                        AllFilePaths.remove(position);
                        ImageAdapter mAdaper = new ImageAdapter(GatePassActivity.this, AllFilePaths, false);
                        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(GatePassActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        gatePass_photos_recycler.setLayoutManager(layoutManager2);
                        mAdaper.setCallBack(GatePassActivity.this);
                        gatePass_photos_recycler.setAdapter(mAdaper);
                        mAdaper.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (delete_Dialog.isShowing()) {
                    delete_Dialog.dismiss();
                }
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(GatePassActivity.this, "Please check your internet connection.");
                } else {
                    Utilities.showToast(GatePassActivity.this, t.getMessage());
                }
            }
        });

    }

    private void uploadDocuments() {
        gatePass_dialog = new ProgressDialog(GatePassActivity.this);
        gatePass_dialog.setMessage("Uploading...");
        gatePass_dialog.setCancelable(false);
        gatePass_dialog.setCanceledOnTouchOutside(false);

        if (!gatePass_dialog.isShowing()) {
            gatePass_dialog.show();
        }

        if (Utilities.Loaded_Out_Photoes) {
            //Gate Pass Out Images
            call_UploadDoc = apiService_another.uploadMultipleFiles1(userId, 9, GatePass_out_Id, multipleFiles); // refId //Id
        } else {
            call_UploadDoc = apiService.uploadMultipleFiles_Gatepass(Id, 7, multipleFiles);
        }

        call_UploadDoc.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!GatePassActivity.this.isDestroyed()) {
                    if (gatePass_dialog != null && gatePass_dialog.isShowing()) {
                        if (!GatePassActivity.this.isFinishing()) {
                            gatePass_dialog.dismiss();
                        }
                    }
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().get("Status").getAsString().equalsIgnoreCase("Fail")) {
                            Utilities.showToast(GatePassActivity.this, response.body().get("message").getAsString());
                        } else if (response.body().get("Status").getAsString().equalsIgnoreCase("Success")) {
                            Utilities.showToast(GatePassActivity.this, "Success");
                            Utilities.saveButton = false;

                            CapturedImage_Path.clear();
                            AllFilePaths.clear();
                            gatepass_editView.setText("");
                            Utilities.Loaded_Out_Photoes = false;

                            finish();
                        } else {
                            Utilities.showToast(GatePassActivity.this, "Please try again later");
                        }
                    }
                } else {
                    Utilities.showToast(GatePassActivity.this, "Please try again later");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (!GatePassActivity.this.isDestroyed()) {
                    if (gatePass_dialog != null && gatePass_dialog.isShowing()) {
                        gatePass_dialog.dismiss();
                    }
                }
                Utilities.showToast(GatePassActivity.this, "Please try again later");
            }
        });
    }

    private void show_Popup_Warehouse() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.warehouse_popup);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (Utilities.screenWidth * 0.95);//WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        current_warehouse_text = (TextView) dialog.findViewById(R.id.current_warehouse_text);
        current_warehouse_text.setText(selected_autocomplete_Warehouse);
        dialog.getWindow().setAttributes(lp);

        cancel_popup = (ImageView) dialog.findViewById(R.id.cancel_popup);
        save_button = (Button) dialog.findViewById(R.id.save_button);
        warehouse_autocomplete = (AutoCompleteTextView) dialog.findViewById(R.id.warehouse_autocomplete);
        warehouse_autocomplete.setThreshold(1);

        cancel_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selected_autocomplete_Warehouse.isEmpty() && selected_autocomplete_Warehouse != null) {
                    saveWarehouse(warehouse_id);
                    selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));

                }
                dialog.dismiss();
            }
        });
        getWareHouse();

        dialog.show();
    }

    private void getWareHouse() {
        warehouse_dialog = new ProgressDialog(GatePassActivity.this);
        warehouse_dialog.setMessage("Loading...");
        warehouse_dialog.setCancelable(false);
        warehouse_dialog.setCanceledOnTouchOutside(false);

        if (!warehouse_dialog.isShowing()) {
            warehouse_dialog.show();
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("", "");

        call_Warehouse = apiService_another.getWarehousesList("", userId);
        call_Warehouse.enqueue(new Callback<List<Warehouse>>() {
            @Override
            public void onResponse(Call<List<Warehouse>> call, Response<List<Warehouse>> response) {
                if (warehouse_dialog != null && warehouse_dialog.isShowing()) {
                    if (warehouse_dialog.isShowing()) {
                        warehouse_dialog.dismiss();
                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {
                        warehouse_list = response.body();
                        if (warehouse_list.size() > 0) {
                            getWarehouse_AutoCompleteText();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Warehouse>> call, Throwable t) {
                if (!GatePassActivity.this.isDestroyed()) {
                    if (warehouse_dialog.isShowing()) {
                        warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(GatePassActivity.this, "Please try again later");
            }
        });
    }

    private void getWarehouse_AutoCompleteText() {

        final List<String> newList = new ArrayList<>();
        for (int i = 0; i < warehouse_list.size(); i++) {
            newList.add(warehouse_list.get(i).getName());
        }
        WareHouseListAdapter adapter = new WareHouseListAdapter(GatePassActivity.this, warehouse_list);
        warehouse_autocomplete.setThreshold(1);
        adapter.setCallBack(this);
        warehouse_autocomplete.setAdapter(adapter);
    }

    @Override
    public void wareHouseAuto(Warehouse mModel) {
        warehouse_id = mModel.getId();
        warehouse_autocomplete.setText(mModel.getName());
        selected_autocomplete_Warehouse = mModel.getName();
    }


    private void gatepass_TrailerDetails_OrgGpStatus_ServiceCall(String Id) {

        gatePass_TrailerDetailsOrgGpStatus_Dialog = new ProgressDialog(GatePassActivity.this);
        gatePass_TrailerDetailsOrgGpStatus_Dialog.setMessage("Loading...");
        gatePass_TrailerDetailsOrgGpStatus_Dialog.setCanceledOnTouchOutside(false);
        gatePass_TrailerDetailsOrgGpStatus_Dialog.setCancelable(false);
        if (gatePass_TrailerDetailsOrgGpStatus_Dialog != null && !gatePass_TrailerDetailsOrgGpStatus_Dialog.isShowing()) {
            gatePass_TrailerDetailsOrgGpStatus_Dialog.show();
        }
        call_GatePass_TrailerDetails_OrgStatus = apiService_another.gateTrailerDetailsOrgGpStatus(Id);
        call_GatePass_TrailerDetails_OrgStatus.enqueue(new Callback<GatePass_TrailerDetails_OrgStatus>() {
            @Override
            public void onResponse(Call<GatePass_TrailerDetails_OrgStatus> call, Response<GatePass_TrailerDetails_OrgStatus> response) {
                if (gatePass_TrailerDetailsOrgGpStatus_Dialog != null && gatePass_TrailerDetailsOrgGpStatus_Dialog.isShowing()) {
                    gatePass_TrailerDetailsOrgGpStatus_Dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (gatePass_TrailerDetailsOrgGpStatus_Dialog != null && gatePass_TrailerDetailsOrgGpStatus_Dialog.isShowing()) {
                        gatePass_TrailerDetailsOrgGpStatus_Dialog.dismiss();
                    }

                    if (response.body() != null) {

                        if (response.body().getStatus().equalsIgnoreCase("Success")) {

                            CapturedImage_Path.clear();
                            AllFilePaths.clear();
                            rectangular_image.setTag("");

                            Utilities.SecondGatePassAPI = true;
                            if (response.body().getTrailerDetails_orgGpStatus() != null) {

                                photos_Layout.setVisibility(View.GONE);
                                takephoto.setVisibility(View.GONE);

                                trailerDetails_orgGpstatus = new TrailerDetails_OrgGpStatus();
                                trailerDetails_orgGpstatus.setTruckNumber(response.body().getTrailerDetails_orgGpStatus().getTruckNumber());
                                trailerDetails_orgGpstatus.setStatus(response.body().getTrailerDetails_orgGpStatus().getStatus());
                                trailerDetails_orgGpstatus.setDriverName(response.body().getTrailerDetails_orgGpStatus().getDriverName());
                                trailerDetails_orgGpstatus.setCustomerName(response.body().getTrailerDetails_orgGpStatus().getCustomerName());
                                trailerDetails_orgGpstatus.setTrailerNumber(response.body().getTrailerDetails_orgGpStatus().getTrailerNumber());
                                //   trailerDetails_orgGpstatus.setTractor(response.body().getTrailerDetails_orgGpStatus().getTractor());
                                trailerDetails_orgGpstatus.setId(response.body().getTrailerDetails_orgGpStatus().getId());
                                trailerDetails_orgGpstatus.setOrgGPStatusOut(response.body().getTrailerDetails_orgGpStatus().getOrgGPStatusOut());
                                trailerDetails_orgGpstatus.setExitSealNumber(response.body().getTrailerDetails_orgGpStatus().getExitSealNumber());
                                trailerDetails_orgGpstatus.setPackingSlip(response.body().getTrailerDetails_orgGpStatus().getPackingSlip());
                                trailerDetails_orgGpstatus.setShipmentPhotosTaken(response.body().getTrailerDetails_orgGpStatus().getShipmentPhotosTaken());
                                trailerDetails_orgGpstatus.setId(response.body().getTrailerDetails_orgGpStatus().getId());
                                trailerDetails_orgGpstatus.setShippingTrailerDBRefId(response.body().getTrailerDetails_orgGpStatus().getShippingTrailerDBRefId());
                                trailerDetails_orgGpstatus.setContainer(response.body().getTrailerDetails_orgGpStatus().getContainer());
                            }

                            GatePass_out_Id = trailerDetails_orgGpstatus.getId();
                            if (trailerDetails_orgGpstatus.getOrgGPStatusOut() != null) {
                                switch (trailerDetails_orgGpstatus.getOrgGPStatusOut()) {
                                    case "Truck In":
                                        orgGpOut_Layout.setVisibility(View.GONE);
                                        complete_Exit_Buttton.setVisibility(View.VISIBLE);
                                        break;

                                    case "Truck Out":
                                        orgGpOut_Layout.setVisibility(View.GONE);
                                        complete_Exit_Buttton.setVisibility(View.VISIBLE);
                                        break;


                                    case "Empty In":
                                        orgGpOut_Layout.setVisibility(View.GONE);
                                        complete_Exit_Buttton.setVisibility(View.VISIBLE);
                                        break;

                                    case "Empty Out":
                                        orgGpOut_Layout.setVisibility(View.GONE);
                                        complete_Exit_Buttton.setVisibility(View.VISIBLE);
                                        break;

                                    case "Loaded In":
                                        orgGpOut_Layout.setVisibility(View.VISIBLE);
                                        complete_Exit_Buttton.setVisibility(View.VISIBLE);
                                        loadedOut_Layout.setVisibility(View.GONE);
                                        break;

                                    case "Loaded Out":

                                        if (trailerDetails_orgGpstatus.getShipmentPhotosTaken() != null) {
                                            orgGpOut_Layout.setVisibility(View.VISIBLE);
                                            complete_Exit_Buttton.setVisibility(View.VISIBLE);
                                            loadedOut_Layout.setVisibility(View.VISIBLE);

                                            if (trailerDetails_orgGpstatus.getPackingSlip() != null) {
                                                no_documents_text.setVisibility(View.GONE);

                                                PackingSlip_str = trailerDetails_orgGpstatus.getPackingSlip();
                                                String[] items = PackingSlip_str.split(",");
                                                for (int i = 0; i < items.length; i++) {
                                                    PackingSlip packing_slip = new PackingSlip();
                                                    packing_slip.setPackingslipText(items[i]);
                                                    packing_slip.setAvailable(false);
                                                    list_packingslip.add(packing_slip);
                                                }
                                            } else {
                                                no_documents_text.setVisibility(View.VISIBLE);
                                            }
                                            mAdaper = new PackingSlipListAdapter(GatePassActivity.this, list_packingslip);
                                            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(GatePassActivity.this, LinearLayoutManager.VERTICAL, false);
                                            packingslip_scan_recycler.setLayoutManager(layoutManager2);
                                            //  mAdaper.setCallBack(GatePassActivity.this);
                                            packingslip_scan_recycler.setAdapter(mAdaper);
                                            mAdaper.notifyDataSetChanged();
                                        } else {

                                            if (AllFilePaths.size() > 0) {
                                                rectangular_image.setVisibility(View.VISIBLE);
                                                photo_list_layout.setVisibility(View.VISIBLE);
                                            } else {
                                                rectangular_image.setVisibility(View.GONE);
                                                photo_list_layout.setVisibility(View.GONE);
                                            }

                                            Utilities.Loaded_Out_Photoes = true;
                                            photos_Layout.setVisibility(View.VISIBLE);
                                            takephoto.setVisibility(View.VISIBLE);
                                            gatePass_btn.setVisibility(View.GONE);
                                            leave_without_access_btn.setVisibility(View.GONE);
                                            saveimage_btn.setVisibility(View.VISIBLE);
                                        }
                                        break;

                                    default:
                                        break;
                                }
                            } else {
                                if (Integer.parseInt(trailerDetails_orgGpstatus.getShippingTrailerDBRefId()) > 0) {
                                    Utilities.showToast(GatePassActivity.this, "Loading In Process");
                                } else {
                                    existingPhotoesPrepare();
                                    if (AllFilePaths.size() > 0) {
                                        rectangular_image.setVisibility(View.VISIBLE);
                                        photo_list_layout.setVisibility(View.VISIBLE);

                                    } else {
                                        rectangular_image.setVisibility(View.GONE);
                                        photo_list_layout.setVisibility(View.GONE);
                                    }

                                    Utilities.Loaded_Out_Photoes = false;
                                    if (trailerDetails.getPhotosTaken() != null) {
                                        orgGpOut_Layout.setVisibility(View.GONE);
                                        takephoto.setVisibility(View.GONE);
                                        photos_Layout.setVisibility(View.VISIBLE);
                                        saveimage_btn.setVisibility(View.INVISIBLE);
                                        complete_Exit_Buttton.setVisibility(View.GONE);
                                    }

                                    Utilities.showToast(GatePassActivity.this, "Unloaded In Process");
                                }

                            }

                            container_number.setText(trailerDetails_orgGpstatus.getContainer());
                            driver.setText(trailerDetails_orgGpstatus.getDriverName());
                            status.setText(trailerDetails_orgGpstatus.getOrgGPStatusOut());
                            tractor.setText(trailerDetails_orgGpstatus.getTruckNumber());
                            trailer_number.setText(trailerDetails_orgGpstatus.getTrailerNumber());
                            customer.setText(trailerDetails_orgGpstatus.getCustomerName());
                            Utilities.showToast(GatePassActivity.this, "Trailer not Ready");
                            // Id = trailerDetails.getId();
                        } else if (response.body().getStatus().equalsIgnoreCase("Fail")) {

                            existingPhotoesPrepare();
                            orgGpOut_Layout.setVisibility(View.GONE);
                            complete_Exit_Buttton.setVisibility(View.VISIBLE);
                            photos_Layout.setVisibility(View.VISIBLE);
                            takephoto.setVisibility(View.VISIBLE);

                            container_number.setText(trailerDetails.getTruckNo());
                            status.setText(trailerDetails.getStatus());
                            tractor.setText(trailerDetails.getTractor());
                            driver.setText(trailerDetails.getDriver());
                            status.setText(trailerDetails.getStatus());
                            trailer_number.setText(trailerDetails.getTrailerNumber());
                            customer.setText(trailerDetails.getCustomer());

                        }
                    }

                } else {
                    Utilities.showToast(GatePassActivity.this, "Please try again later");
                }

            }

            @Override
            public void onFailure(Call<GatePass_TrailerDetails_OrgStatus> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(GatePassActivity.this, "Please check your internet connection.");
                    if (gatePass_TrailerDetailsOrgGpStatus_Dialog != null && gatePass_TrailerDetailsOrgGpStatus_Dialog.isShowing()) {
                        gatePass_TrailerDetailsOrgGpStatus_Dialog.dismiss();
                    }
                } else {
                    Utilities.showToast(GatePassActivity.this, "Please check your internet connection.");
                    Utilities.showToast(GatePassActivity.this, t.getMessage());
                    if (gatePass_TrailerDetailsOrgGpStatus_Dialog != null && gatePass_TrailerDetailsOrgGpStatus_Dialog.isShowing()) {
                        gatePass_TrailerDetailsOrgGpStatus_Dialog.dismiss();
                    }
                }
            }
        });
    }

    private void saveWarehouse() {
        gatePass_Exit_dialog = new ProgressDialog(GatePassActivity.this);
        gatePass_Exit_dialog.setMessage("Loading...");
        gatePass_Exit_dialog.setCancelable(false);
        gatePass_Exit_dialog.setCanceledOnTouchOutside(false);

        if (!gatePass_Exit_dialog.isShowing()) {
            gatePass_Exit_dialog.show();
        }

        call_gatePassExit = apiService.gatePassExit(Id, userId, dateToStr);
        call_gatePassExit.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (gatePass_Exit_dialog != null && gatePass_Exit_dialog.isShowing()) {
                    if (gatePass_Exit_dialog.isShowing()) {
                        gatePass_Exit_dialog.dismiss();
                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Integer.parseInt(response.body().toString()) > 0) {
                            Utilities.showToast(GatePassActivity.this, "Success");
                            finish();
                        }
                    }
                } else {
                    Utilities.showToast(GatePassActivity.this, "Fail");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (!GatePassActivity.this.isDestroyed()) {
                    if (gatePass_Exit_dialog.isShowing()) {
                        gatePass_Exit_dialog.dismiss();
                    }
                }
                Utilities.showToast(GatePassActivity.this, "Please try again later");
            }
        });
    }

    private void saveWarehouse(String wharehouse_id) {
        save_warehouse_dialog = new ProgressDialog(GatePassActivity.this);
        save_warehouse_dialog.setMessage("Loading...");
        save_warehouse_dialog.setCancelable(false);
        save_warehouse_dialog.setCanceledOnTouchOutside(false);

        if (!save_warehouse_dialog.isShowing()) {
            save_warehouse_dialog.show();
        }

        call_saveWarehouse = apiService_another.saveWarehouse(wharehouse_id, userId);
        call_saveWarehouse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (save_warehouse_dialog != null && save_warehouse_dialog.isShowing()) {
                    if (save_warehouse_dialog.isShowing()) {
                        save_warehouse_dialog.dismiss();

                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Integer.parseInt(response.body().toString()) > 0) {
                            Utilities.showToast(GatePassActivity.this, "Wharehouse saved Successfully");
                            Utilities.savePref(GatePassActivity.this, "WarehouseName", selected_autocomplete_Warehouse);
                            Utilities.savePref(GatePassActivity.this, "WarehouseId", warehouse_id);
                           selected_autocomplete_Warehouse_ID =warehouse_id;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (!GatePassActivity.this.isDestroyed()) {
                    if (save_warehouse_dialog.isShowing()) {
                        save_warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(GatePassActivity.this, "Please try again later");
            }
        });
    }

    private void gatePassEntranceDate() {
        gate_Pass_EntranceDate_dialog = new ProgressDialog(GatePassActivity.this);
        gate_Pass_EntranceDate_dialog.setMessage("Loading...");
        gate_Pass_EntranceDate_dialog.setCancelable(false);
        gate_Pass_EntranceDate_dialog.setCanceledOnTouchOutside(false);

        if (!gate_Pass_EntranceDate_dialog.isShowing()) {
            gate_Pass_EntranceDate_dialog.show();
        }

        call_saveWarehouse = apiService.gatePassEntranceDate(Id, dateToStr);
        call_saveWarehouse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (gate_Pass_EntranceDate_dialog != null && gate_Pass_EntranceDate_dialog.isShowing()) {
                    if (gate_Pass_EntranceDate_dialog.isShowing()) {
                        gate_Pass_EntranceDate_dialog.dismiss();
                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Integer.parseInt(response.body().toString()) > 0) {
                            Utilities.showToast(GatePassActivity.this, " Successfully");
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (!GatePassActivity.this.isDestroyed()) {
                    if (gate_Pass_EntranceDate_dialog.isShowing()) {
                        gate_Pass_EntranceDate_dialog.dismiss();
                    }
                }
                Utilities.showToast(GatePassActivity.this, "Please try again later");
            }
        });
    }


    private void leaveWithoutAccessService() {
        leave_Without_Acess_dialog = new ProgressDialog(GatePassActivity.this);
        leave_Without_Acess_dialog.setMessage("Loading...");
        leave_Without_Acess_dialog.setCancelable(false);
        leave_Without_Acess_dialog.setCanceledOnTouchOutside(false);

        if (!leave_Without_Acess_dialog.isShowing()) {
            leave_Without_Acess_dialog.show();
        }

        call_saveWarehouse = apiService.gatePassLeaveWithOutAcessDate(Id, dateToStr);
        call_saveWarehouse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (leave_Without_Acess_dialog != null && leave_Without_Acess_dialog.isShowing()) {
                    if (leave_Without_Acess_dialog.isShowing()) {
                        leave_Without_Acess_dialog.dismiss();
                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Integer.parseInt(response.body().toString()) > 0) {
                            Leave_WithOut_Access_Id = Integer.parseInt(response.body().toString());
                            getWareHouseData_LeaveWithOutAcess(Leave_WithOut_Access_Id); //Integer.parseInt(response.body().toString()
                        } else {
                            Utilities.showToast(GatePassActivity.this, "Trailer not leave");
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (!GatePassActivity.this.isDestroyed()) {
                    if (leave_Without_Acess_dialog.isShowing()) {
                        leave_Without_Acess_dialog.dismiss();
                    }
                }
                Utilities.showToast(GatePassActivity.this, "Please try again later");
            }
        });

    }

    private void getWareHouseData_LeaveWithOutAcess(final int gate_pass_Leave_with_out_Access_id) {
        waehouse_LeaveWithOut_Dialog = new ProgressDialog(GatePassActivity.this);
        waehouse_LeaveWithOut_Dialog.setMessage("Loading...");
        waehouse_LeaveWithOut_Dialog.setCanceledOnTouchOutside(false);
        waehouse_LeaveWithOut_Dialog.setCancelable(false);
        if (waehouse_LeaveWithOut_Dialog != null && !waehouse_LeaveWithOut_Dialog.isShowing()) {
            waehouse_LeaveWithOut_Dialog.show();
        }
        call_getpass_Leave_withOut_acess_Picture = apiService.gateDriverLeaveWithOutAccesData(String.valueOf(gate_pass_Leave_with_out_Access_id), "true");
        call_getpass_Leave_withOut_acess_Picture.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (waehouse_LeaveWithOut_Dialog.isShowing()) {
                    waehouse_LeaveWithOut_Dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().get("Status").getAsString().equalsIgnoreCase("Trailer exit succefully.")) {
                            getWarehouseData(response.body().get("gatePassTrailer").getAsJsonObject().get("WareHouseId").getAsString());
                        } else {
                            Utilities.showToast(GatePassActivity.this, response.body().get("Status").getAsString());
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (waehouse_LeaveWithOut_Dialog.isShowing()) {
                    waehouse_LeaveWithOut_Dialog.dismiss();
                }
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(GatePassActivity.this, "Please check your internet connection.");
                } else {
                    Utilities.showToast(GatePassActivity.this, t.getMessage());
                }
            }
        });

    }

    private void getWarehouseData(String getPass_leave_woithout_data) {
        waehouse_LeaveWithOut_Dialog = new ProgressDialog(GatePassActivity.this);
        waehouse_LeaveWithOut_Dialog.setMessage("Loading...");
        waehouse_LeaveWithOut_Dialog.setCanceledOnTouchOutside(false);
        waehouse_LeaveWithOut_Dialog.setCancelable(false);
        if (waehouse_LeaveWithOut_Dialog != null && !waehouse_LeaveWithOut_Dialog.isShowing()) {
            waehouse_LeaveWithOut_Dialog.show();
        }
        call_getpass_Data = apiService_another.gateWarehouseData(String.valueOf(getPass_leave_woithout_data), "true");
        call_getpass_Data.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (waehouse_LeaveWithOut_Dialog.isShowing()) {
                    waehouse_LeaveWithOut_Dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().get("Status").getAsString().equalsIgnoreCase("Success.")) {

                            String Warehouse_name = response.body().get("address").getAsJsonObject().get("WareHouseName").getAsString();
                            String Warehouse_add1 = response.body().get("address").getAsJsonObject().get("Address1").getAsString();
                            String Warehouse_add2 = response.body().get("address").getAsJsonObject().get("Address2").getAsString();
                            String Warehouse_Id = response.body().get("address").getAsJsonObject().get("WareHouseId").getAsString();
                            sendDriverLeaveNotification(Warehouse_name, Warehouse_add1, Warehouse_add2, Warehouse_Id);
                        } else {
                            Utilities.showToast(GatePassActivity.this, response.body().get("Success.").getAsString());
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (waehouse_LeaveWithOut_Dialog.isShowing()) {
                    waehouse_LeaveWithOut_Dialog.dismiss();
                }
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(GatePassActivity.this, "Please check your internet connection.");
                } else {
                    Utilities.showToast(GatePassActivity.this, t.getMessage());
                }
            }
        });

    }

    private void sendDriverLeaveNotification(String Whare_house_Name, String Whare_house_Add, String Whare_house_Add2, String Warehouse_Id) {
        waehouse_LeaveWithOut_Dialog = new ProgressDialog(GatePassActivity.this);
        waehouse_LeaveWithOut_Dialog.setMessage("Loading...");
        waehouse_LeaveWithOut_Dialog.setCanceledOnTouchOutside(false);
        waehouse_LeaveWithOut_Dialog.setCancelable(false);
        if (waehouse_LeaveWithOut_Dialog != null && !waehouse_LeaveWithOut_Dialog.isShowing()) {
            waehouse_LeaveWithOut_Dialog.show();
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("WareHouseName", Whare_house_Name);
        obj.addProperty("Address1", Whare_house_Add);
        obj.addProperty("Address2", Whare_house_Add2);
        obj.addProperty("Id", Leave_WithOut_Access_Id);
        obj.addProperty("TKCompleted", "true");
        obj.addProperty("TrailerNumber", trailerNumber);
        obj.addProperty("TRTrailerFolio", trTrailerFolio);
        obj.addProperty("TruckNo", tractor_str);
        obj.addProperty("WareHouseId", Warehouse_Id);
        obj.addProperty("TruckTransportationLine", trailerDetails.getTransportationLine());
        obj.addProperty("TrailerType", trailerDetails.getTrailerType());
        obj.addProperty("DriverName", trailerDetails.getDriver());
        obj.addProperty("DriverLicense", trailerDetails.getDriverLicense());
        obj.addProperty("TrStatus", "Leave Without Access");
        obj.addProperty("RegistrationDate", trailerDetails.getRegistrationDate());
        obj.addProperty("CQCustomer", trailerDetails.getCQCustomer());




        call_SendDriver_Leave_With_Out_Access = apiService_another.sendDriverLeaveNotification(obj);

        call_SendDriver_Leave_With_Out_Access.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (waehouse_LeaveWithOut_Dialog.isShowing()) {
                    waehouse_LeaveWithOut_Dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().get("Status").getAsString().equalsIgnoreCase("Success.")) {
                            Toast.makeText(getApplicationContext(), response.body().get("Status").getAsString(), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().get("Status.").getAsString(), Toast.LENGTH_LONG).show();

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (waehouse_LeaveWithOut_Dialog.isShowing()) {
                    waehouse_LeaveWithOut_Dialog.dismiss();
                }
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(GatePassActivity.this, "Please check your internet connection.");
                } else {
                    Utilities.showToast(GatePassActivity.this, t.getMessage());
                }
            }
        });

    }

    private void zoom_CapturedImage(String imagepath) {

        View view = getLayoutInflater().inflate(R.layout.imagefullscreen, null);
        touchImage = (TouchImageView) view.findViewById(R.id.touchImage);
        zoom_remove_photo = (ImageView) view.findViewById(R.id.zoom_remove_photo);
        toucchimage_Dialog = new Dialog(this, R.style.DialogAnimation);
        toucchimage_Dialog.setContentView(view);
        toucchimage_Dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(toucchimage_Dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;//(int) (Utilities.screenHeight * 0.85);
        lp.gravity = Gravity.CENTER_VERTICAL;
        toucchimage_Dialog.getWindow().setAttributes(lp);

        if (imagepath.contains("https")) {
            Picasso.with(GatePassActivity.this)
                    .load(imagepath)
                    .placeholder(R.drawable.ic_placeholder)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(touchImage);
        }

        zoom_remove_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toucchimage_Dialog.dismiss();
            }
        });
        toucchimage_Dialog.show();
    }


}
