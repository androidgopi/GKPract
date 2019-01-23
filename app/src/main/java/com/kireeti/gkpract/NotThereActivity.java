package com.kireeti.gkpract;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kireeti.gkpract.adapter.FolioAdapter;
import com.kireeti.gkpract.adapter.ImageAdapter;
import com.kireeti.gkpract.adapter.NotThereFolioListAdapter;
import com.kireeti.gkpract.adapter.WareHouseListAdapter;
import com.kireeti.gkpract.cameraView.CamActivity;
import com.kireeti.gkpract.customClass.TouchImageView;
import com.kireeti.gkpract.interfaces.DeletePhoto;
import com.kireeti.gkpract.interfaces.SelectNotFolio;
import com.kireeti.gkpract.interfaces.WareHouseAuto;
import com.kireeti.gkpract.models.ImageFile;
import com.kireeti.gkpract.models.InventoryDetails;
import com.kireeti.gkpract.models.NotThere;
import com.kireeti.gkpract.models.TrailerFolios;
import com.kireeti.gkpract.models.Trailer_Results;
import com.kireeti.gkpract.models.Valid_Sid;
import com.kireeti.gkpract.networkCall.ApiClient;
import com.kireeti.gkpract.networkCall.ApiInterface;
import com.kireeti.gkpract.models.Warehouse;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotThereActivity extends AppCompatActivity implements View.OnClickListener, SelectNotFolio, DeletePhoto, WareHouseAuto {


    private static final int MY_REQUEST_CODE_LOCATION = 1;
    private static final int MY_REQUEST_CODE_SID = 2;
    private ImageView back_image, lagout_image;
    private TextView not_folio_text, sid_text, transfer_to_location_text;
    private ApiInterface apiService;
    private ProgressDialog notFolioDialog;
    private Call<List<NotThere>> call_not_there;
    List<NotThere> notThere_list = new ArrayList<>();
    private PopupWindow notFolio_popup;
    private String not_Folio_id;
    private AutoCompleteTextView not_folio_auto_complete;
    private ProgressDialog getFoilo_Dialog;
    private String userId;
    private Call<Trailer_Results> get_Folio;
    List<TrailerFolios> get_Folio_aucomplete;
    private ProgressDialog validatesid_Dialog;
    private Call<Valid_Sid> validateSid_Call;
    private EditText sid_editView;
    private ProgressDialog validate_Location_Dialog;
    private EditText location_editView;
    private Button location_Scan_btn, sid_Scan_btn;
    private IntentIntegrator sidScan;
    private IntentIntegrator locationScan;

    private int REQUEST_CAMERA = 0;
    private String sId;
    private String location;
    private int PHOTOS;
    private File destination;
    private int mWidth;
    File rootDir;
    int viewWidth;
    private RoundedImageView rectangular_image;
    private ImageView delete_imageview;
    private RoundedImageView captured_image;
    private HorizontalScrollView horizontal_scrollview;
    ArrayList<ImageFile> AllFilePaths = new ArrayList<>();
    ArrayList<ImageFile> CapturedImage_Path = new ArrayList<>();
    private TouchImageView touchImage;
    private TextView captured_image_txt;
    LinearLayout photo_gallery, photo_list_layout;
    private RelativeLayout main_rl;
    private Dialog toucchimage_Dialog;
    ImageView zoom_remove_photo;
    private Button takephoto;
    private int PHOTOS_NUMBER = 000;
    private int PHOTOS_PATH_NUMBER = 000;
    private RecyclerView not_There_photos;
    private ImageView left_arrow, right_arrow;
    private LinearLayout location_scan_layout, sid_scan_layout;
    private LinearLayout folio_list_layout, aucomplete_text_layout;
    private String location_deatil_Id, sid_detail_Id;
    private Button saveimage_btn;
    private ProgressDialog document_dialog;
    private Call<ResponseBody> call_UploadDoc;
    private TextView not_folio_Settext;
    private InventoryDetails inventoryDetails;

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
    private ProgressDialog save_warehouse_dialog;
    private Call<Integer> call_saveWarehouse;
    private TextView current_warehouse_text;
    private String selected_autocomplete_Warehouse_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_there);
        apiService = ApiClient.getClient(NotThereActivity.this).create(ApiInterface.class);

        Display display = getWindowManager().getDefaultDisplay();
        mWidth = display.getWidth();
        viewWidth = mWidth / 3;
        rootDir = new File(Environment.getExternalStorageDirectory() + "/" + "Cargoquin");
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        selected_autocomplete_Warehouse = Utilities.getPref(NotThereActivity.this, "WarehouseName", "");
        selected_autocomplete_Warehouse_ID = Utilities.getPref(NotThereActivity.this, "WarehouseId", "");

        findViewes();
    }

    private void findViewes() {
        not_folio_text = (TextView) findViewById(R.id.not_folio_text);
        not_folio_text.setOnClickListener(this);

        not_folio_auto_complete = (AutoCompleteTextView) findViewById(R.id.not_folio_auto_complete);

        location_Scan_btn = (Button) findViewById(R.id.location_Scan_btn);
        location_Scan_btn.setOnClickListener(this);

        sid_Scan_btn = (Button) findViewById(R.id.sid_Scan_btn);
        sid_Scan_btn.setOnClickListener(this);

        selction_for_warehouse = (TextView) findViewById(R.id.selction_for_warehouse);
        selction_for_warehouse.setOnClickListener(this);

        back_image = (ImageView) findViewById(R.id.back_image);
        back_image.setOnClickListener(this);

        lagout_image = (ImageView) findViewById(R.id.lagout_image);
        lagout_image.setOnClickListener(this);

        rectangular_image = (RoundedImageView) findViewById(R.id.rectangular_image);
        rectangular_image.setOnClickListener(this);

        takephoto = (Button) findViewById(R.id.takephoto);
        takephoto.setOnClickListener(this);

        not_There_photos = (RecyclerView) findViewById(R.id.not_There_photos);

        left_arrow = (ImageView) findViewById(R.id.left_arrow);
        left_arrow.setOnClickListener(this);

        right_arrow = (ImageView) findViewById(R.id.right_arrow);
        right_arrow.setOnClickListener(this);

        location_scan_layout = (LinearLayout) findViewById(R.id.location_scan_layout);
        sid_scan_layout = (LinearLayout) findViewById(R.id.sid_scan_layout);

        photo_list_layout = (LinearLayout) findViewById(R.id.photo_list_layout);
        photo_gallery = (LinearLayout) findViewById(R.id.photo_gallery);

        saveimage_btn = (Button) findViewById(R.id.saveimage_btn);
        saveimage_btn.setOnClickListener(this);

        folio_list_layout = (LinearLayout) findViewById(R.id.folio_list_layout);
        aucomplete_text_layout = (LinearLayout) findViewById(R.id.aucomplete_text_layout);
        folio_list_layout.setVisibility(View.VISIBLE);
        aucomplete_text_layout.setVisibility(View.VISIBLE);
        not_folio_text.setVisibility(View.VISIBLE);
        not_folio_Settext = (TextView) findViewById(R.id.not_folio_Settext);

        if (Utilities.Pitcure_captured_NotThere) {
            if (Utilities.AutoCompltete_Clicked) {
                not_folio_auto_complete.setEnabled(false);
            } else {
                not_folio_auto_complete.setEnabled(true);
            }

            if (Utilities.Folio_List_Clicked) {
                not_folio_text.setVisibility(View.GONE);
                not_folio_Settext.setText(not_folio_text.getText().toString());
                not_folio_Settext.setVisibility(View.VISIBLE);
            } else {
                not_folio_text.setVisibility(View.VISIBLE);
                not_folio_Settext.setVisibility(View.GONE);
            }

        }


        sid_editView = (EditText) findViewById(R.id.sid_editView);
        sid_editView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (!sid_editView.getText().toString().equalsIgnoreCase("")) {
                        validate_Sid_ServiceCall(sid_editView.getText().toString());
                    } else {
                        Utilities.showToast(NotThereActivity.this, "Please enter or Scan Sid");
                    }
                }
                return false;
            }
        });

        location_editView = (EditText) findViewById(R.id.location_editView);
        location_editView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (!location_editView.getText().toString().equalsIgnoreCase("")) {
                        validate_location_ServiceCall(location_editView.getText().toString());
                    } else {
                        Utilities.showToast(NotThereActivity.this, "Please enter or Scan Location");
                    }
                }
                return false;
            }
        });

        if (!Utilities.getPref(NotThereActivity.this, "UserId", "").equalsIgnoreCase("")) {
            userId = Utilities.getPref(NotThereActivity.this, "UserId", "");
        }
        getnotFolioService();
        getFolio();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
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

            case R.id.not_folio_text:

                if (notFolio_popup != null) {
                    if (!notFolio_popup.isShowing()) {
                        notFolio_Dropdown();
                    } else {
                        notFolio_popup.dismiss();
                    }
                } else {
                    notFolio_Dropdown();
                }

                break;

            case R.id.location_Scan_btn:
                locationScan = new IntentIntegrator(NotThereActivity.this);
                locationScan.setOrientationLocked(true);
                Intent intent = locationScan.createScanIntent();
                startActivityForResult(intent, MY_REQUEST_CODE_LOCATION);
                locationScan.initiateScan();
                break;

            case R.id.sid_Scan_btn:
                sidScan = new IntentIntegrator(NotThereActivity.this);
                sidScan.setOrientationLocked(true);
                Intent intent1 = sidScan.createScanIntent();
                startActivityForResult(intent1, MY_REQUEST_CODE_SID);
                sidScan.initiateScan();
                break;

            case R.id.takephoto:
                try {
                    if (!sid_detail_Id.equalsIgnoreCase("") && !sid_detail_Id.equals(null) && !location_deatil_Id.equalsIgnoreCase("") && !location_deatil_Id.equals(null)) {
                        Intent cam = new Intent(NotThereActivity.this, CamActivity.class);
                        startActivityForResult(cam, 0);
                    } else {
                        Utilities.showToast(NotThereActivity.this, "Please enter or Scan Sid / Location");
                    }
                } catch (NullPointerException e) {
                    Utilities.showToast(NotThereActivity.this, "Please enter or Scan Sid / Location");

                }
                break;

            case R.id.left_arrow:
                if (not_There_photos.getChildAt(0) != null) {
                    if (AllFilePaths.size() > 0) {
                        not_There_photos.smoothScrollBy((int) not_There_photos.getScrollY() - not_There_photos.getChildAt(0).getWidth(), (int) not_There_photos.getScrollX());
                        left_arrow.setImageResource(R.drawable.ic_left_blue);

                    } else {
                        left_arrow.setImageResource(R.drawable.ic_left_gray);
                    }
                }
                break;

            case R.id.right_arrow:
                if (not_There_photos.getChildAt(0) != null) {
                    if (AllFilePaths.size() > 0) {
                        not_There_photos.smoothScrollBy((int) not_There_photos.getScrollX() + not_There_photos.getChildAt(0).getWidth(), (int) not_There_photos.getScrollY());
                        right_arrow.setImageResource(R.drawable.ic_right_blue);
                    } else {
                        right_arrow.setImageResource(R.drawable.ic_right_gray);
                    }
                }
                break;

            case R.id.saveimage_btn:
                if (AllFilePaths.size() > 0) {
                    uploadDocuments();
                } else {
                    Utilities.showToast(NotThereActivity.this, "Please Select or Capture at least one Photo");
                }
                break;

            case R.id.selction_for_warehouse:
                show_Popup_Warehouse();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            addImageToRecycleView(data, requestCode);
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (Utilities.Location) {
                Utilities.Location = false;
                if (result.getContents() == null) {
                    Utilities.showToast(NotThereActivity.this, "Result Not Found");
                } else {
                    location = result.getContents();
                    if (!location.equalsIgnoreCase("")) {
                        location_editView.setText(location);
                        Utilities.callFromTakePhoto = false;
                        validate_location_ServiceCall(location.trim());
                        PHOTOS = 000;
                    }
                }
            } else if (Utilities.Sid) {
                Utilities.Sid = false;
                if (result.getContents() == null) {
                    Utilities.showToast(NotThereActivity.this, "Result Not Found");
                } else {
                    sId = result.getContents();
                    if (!sId.equalsIgnoreCase("")) {
                        sid_editView.setText(sId);
                        Utilities.callFromTakePhoto = false;
                        validate_Sid_ServiceCall(sId.trim());
                        PHOTOS = 000;
                    }
                }
            }

            if (requestCode == MY_REQUEST_CODE_LOCATION) {
                if (result != null) {

                } else {
                    Utilities.Location = true;
                    Utilities.Sid = false;
                    // This is important, otherwise the result will not be passed to the fragment
                    super.onActivityResult(requestCode, resultCode, data);
                }

            } else if (requestCode == MY_REQUEST_CODE_SID) {
                if (result != null) {


                } else {
                    Utilities.Sid = true;
                    Utilities.Location = false;
                    // This is important, otherwise the result will not be passed to the fragment
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    private void getnotFolioService() {
        notFolioDialog = new ProgressDialog(NotThereActivity.this);
        notFolioDialog.setMessage(" Loading ");
        notFolioDialog.setCancelable(false);
        notFolioDialog.setCanceledOnTouchOutside(false);

        if (!notFolioDialog.isShowing()) {
            notFolioDialog.show();
        }
        JsonObject obj = new JsonObject();

        call_not_there = apiService.getNotThere(obj);
        call_not_there.enqueue(new Callback<List<NotThere>>() {
            @Override
            public void onResponse(Call<List<NotThere>> call, Response<List<NotThere>> response) {
                if (notFolioDialog != null && notFolioDialog.isShowing()) {
                    if (notFolioDialog.isShowing()) {
                        notFolioDialog.dismiss();
                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {
                        notThere_list = response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NotThere>> call, Throwable t) {
                if (!NotThereActivity.this.isDestroyed()) {
                    if (notFolioDialog.isShowing()) {
                        notFolioDialog.dismiss();
                    }
                }
                Utilities.showToast(NotThereActivity.this, "Please try again later");
            }
        });
    }

    private void notFolio_Dropdown() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.qafindingfoliolist_popup_lyt, null);
        notFolio_popup = new PopupWindow(popupView, not_folio_text.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        try {
            if (notThere_list.size() > 0 && !notThere_list.isEmpty()) {
                NotThereFolioListAdapter adapter = new NotThereFolioListAdapter(NotThereActivity.this, notThere_list);
                adapter.setCallBack(this);
                RecyclerView warehouse_recycleview = (RecyclerView) popupView.findViewById(R.id.qafindinglist_recycleview);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NotThereActivity.this);
                warehouse_recycleview.setLayoutManager(layoutManager);
                warehouse_recycleview.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "There is no warehouse folios", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            notFolio_popup.showAsDropDown(not_folio_text, 0, 0, Gravity.CENTER);
        }
    }


    @Override
    public void callsetFoliolist(NotThere mModel) {
        if (notFolio_popup.isShowing()) {
            notFolio_popup.dismiss();
        }

        if (mModel != null) {
          //  selected_folio_str=mModel.getNTFolioStr();
            not_folio_text.setText(mModel.getNTFolioStr());
            not_folio_text.setPaintFlags(not_folio_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            if (mModel.getId() != null) {
                not_Folio_id = mModel.getId();
                aucomplete_text_layout.setVisibility(View.GONE);
                location_scan_layout.setVisibility(View.VISIBLE);
                sid_scan_layout.setVisibility(View.VISIBLE);
                AllFilePaths.clear();
                Utilities.Folio_List_Clicked = true;
            }
        }
    }

    @Override
    public void selectednotFolio(TrailerFolios obj) {
       // selected_folio_str=obj.getTrailerFolio();
        not_folio_auto_complete.setText(obj.getTrailerFolio());
        not_folio_auto_complete.dismissDropDown();
        not_Folio_id = obj.getTrailerFolio();

        folio_list_layout.setVisibility(View.GONE);
        location_scan_layout.setVisibility(View.VISIBLE);
        sid_scan_layout.setVisibility(View.VISIBLE);
        AllFilePaths.clear();
        Utilities.AutoCompltete_Clicked = true;

    }

    private void getFolio() {
        getFoilo_Dialog = new ProgressDialog(NotThereActivity.this);
        getFoilo_Dialog.setMessage("Loading...");
        getFoilo_Dialog.setCanceledOnTouchOutside(false);
        getFoilo_Dialog.setCancelable(false);
        if (getFoilo_Dialog != null && !getFoilo_Dialog.isShowing()) {
            getFoilo_Dialog.show();
        }


        get_Folio = apiService.getTrailers(userId, 6, "");
        get_Folio.enqueue(new Callback<Trailer_Results>() {
            @Override
            public void onResponse(Call<Trailer_Results> call, Response<Trailer_Results> response) {
                if (getFoilo_Dialog.isShowing()) {
                    getFoilo_Dialog.dismiss();
                }

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("Success")) {
                        if (response.body().getTrailerList().size() > 0) {
                            get_Folio_aucomplete = response.body().getTrailerList();
                            if (get_Folio_aucomplete.size() > 0) {
                                getFolio_AutoCompleteText();
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Trailer_Results> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(NotThereActivity.this, "Please check your internet connection.");
                    if (getFoilo_Dialog.isShowing()) {
                        getFoilo_Dialog.dismiss();
                    }

                } else {
                    Utilities.showToast(NotThereActivity.this, t.getMessage());
                    if (getFoilo_Dialog.isShowing()) {
                        getFoilo_Dialog.dismiss();
                    }
                }

            }
        });
    }

    private void getFolio_AutoCompleteText() {
        final List<String> newList = new ArrayList<>();
        for (int i = 0; i < get_Folio_aucomplete.size(); i++) {
            newList.add(get_Folio_aucomplete.get(i).getTrailerFolio());
        }
        FolioAdapter adapter = new FolioAdapter(NotThereActivity.this, get_Folio_aucomplete);
        not_folio_auto_complete.setThreshold(1);
        not_folio_auto_complete.setText("");
        adapter.setCallBack(this);
        not_folio_auto_complete.setAdapter(adapter);
    }

    private void validate_Sid_ServiceCall(String sid) {

        validatesid_Dialog = new ProgressDialog(NotThereActivity.this);
        validatesid_Dialog.setMessage("Loading...");
        validatesid_Dialog.setCanceledOnTouchOutside(false);
        validatesid_Dialog.setCancelable(false);
        if (validatesid_Dialog != null && !validatesid_Dialog.isShowing()) {
            validatesid_Dialog.show();
        }
        validateSid_Call = apiService.validateSid(userId, 7, sid);
        validateSid_Call.enqueue(new Callback<Valid_Sid>() {
            @Override
            public void onResponse(Call<Valid_Sid> call, Response<Valid_Sid> response) {
                if (validatesid_Dialog != null && validatesid_Dialog.isShowing()) {
                    validatesid_Dialog.dismiss();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("Success")) {
                            if (response.body().getInventoryDetails() != null) {
                                Utilities.showToast(NotThereActivity.this, "Valid Sid");
                                inventoryDetails = new InventoryDetails();
                                inventoryDetails.setId(response.body().getInventoryDetails().getId());
                            }
                            sid_detail_Id = inventoryDetails.getId();


                        } else {
                            showAlert_SID("Alert", "Please enter Correct SId");
                        }
                    } else {
                        showAlert_SID("Alert", "Please enter Correct SId");
                    }

                } else {
                    Utilities.showToast(NotThereActivity.this, "Please try again later");
                }
            }

            @Override
            public void onFailure(Call<Valid_Sid> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(NotThereActivity.this, "Please check your internet connection.");
                    if (validatesid_Dialog != null && validatesid_Dialog.isShowing()) {
                        validatesid_Dialog.dismiss();
                    }
                } else {
                    Utilities.showToast(NotThereActivity.this, "Please check your internet connection.");
                    Utilities.showToast(NotThereActivity.this, t.getMessage());
                    if (validatesid_Dialog != null && validatesid_Dialog.isShowing()) {
                        validatesid_Dialog.dismiss();
                    }
                }
            }
        });
    }

    private void showAlert_SID(String title_text, String message_text) {
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
                sid_editView.setText("");
            }
        });

        title.setText(title_text);
        message.setText(message_text);
        dialog.show();
    }

    private void validate_location_ServiceCall(String location) {

        validate_Location_Dialog = new ProgressDialog(NotThereActivity.this);
        validate_Location_Dialog.setMessage("Loading...");
        validate_Location_Dialog.setCanceledOnTouchOutside(false);
        validate_Location_Dialog.setCancelable(false);
        if (validate_Location_Dialog != null && !validate_Location_Dialog.isShowing()) {
            validate_Location_Dialog.show();
        }
        validateSid_Call = apiService.validateSid(userId, 6, location);
        validateSid_Call.enqueue(new Callback<Valid_Sid>() {
            @Override
            public void onResponse(Call<Valid_Sid> call, Response<Valid_Sid> response) {
                if (validate_Location_Dialog != null && validate_Location_Dialog.isShowing()) {
                    validate_Location_Dialog.dismiss();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("Success")) {
                            if (response.body().getInventoryDetails() != null) {
                                Utilities.showToast(NotThereActivity.this, "Valid Location");

                                inventoryDetails = new InventoryDetails();
                                inventoryDetails.setId(response.body().getInventoryDetails().getId());
                            }
                            location_deatil_Id = inventoryDetails.getId();

                        } else {
                            showAlert_Location("Alert", "Please enter Correct Location");
                        }
                    } else {
                        showAlert_Location("Alert", "Please enter Correct Location");
                    }

                } else {
                    Utilities.showToast(NotThereActivity.this, "Please try again later");
                }
            }

            @Override
            public void onFailure(Call<Valid_Sid> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(NotThereActivity.this, "Please check your internet connection.");
                    if (validate_Location_Dialog != null && validate_Location_Dialog.isShowing()) {
                        validate_Location_Dialog.dismiss();
                    }
                } else {
                    Utilities.showToast(NotThereActivity.this, "Please check your internet connection.");
                    Utilities.showToast(NotThereActivity.this, t.getMessage());
                    if (validate_Location_Dialog != null && validate_Location_Dialog.isShowing()) {
                        validate_Location_Dialog.dismiss();
                    }
                }
            }
        });
    }

    private void showAlert_Location(String title_text, String message_text) {
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
                location_editView.setText("");

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
                thumbnail = BitmapFactory.decodeFile(String.valueOf(data.getExtras().get("data")));
            } else {
                thumbnail = (Bitmap) data.getExtras().get("data");
            }

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            ImageFile bolObj = new ImageFile();

            if (Utilities.LastPhotoPathNumber == 0) {
                if (PHOTOS_PATH_NUMBER == 000) {
                    destination = new File(rootDir, not_Folio_id + "_001" + ".jpg");
                    PHOTOS_PATH_NUMBER = 001;
                } else {
                    DecimalFormat formatter = new DecimalFormat("000");
                    PHOTOS_PATH_NUMBER = PHOTOS_PATH_NUMBER + 1;
                    String s = formatter.format(PHOTOS_PATH_NUMBER);
                    destination = new File(rootDir, not_Folio_id + "_" + (s) + ".jpg");
                }
            } else {
                DecimalFormat formatter = new DecimalFormat("000");
                Utilities.LastPhotoPathNumber = Utilities.LastPhotoPathNumber + 1;
                String s = formatter.format(Utilities.LastPhotoPathNumber + 1);
                destination = new File(rootDir, not_Folio_id + "_" + (s) + ".jpg");
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

            if (Utilities.LastPhotoNumber == 0) {
                if (PHOTOS_NUMBER == 000) {
                    bolObj.setFileName("NotThere_" + not_Folio_id + "_001" + ".jpg");
                    PHOTOS_NUMBER = 001;
                } else {
                    DecimalFormat formatter = new DecimalFormat("000");
                    PHOTOS_NUMBER = PHOTOS_NUMBER + 1;
                    String s = formatter.format(PHOTOS_NUMBER);
                    bolObj.setFileName("NotThere_" + not_Folio_id + "_" + (s) + ".jpg");
                }
            } else {
                DecimalFormat formatter = new DecimalFormat("000");
                Utilities.LastPhotoNumber = Utilities.LastPhotoNumber + 1;
                String s = formatter.format(Utilities.LastPhotoNumber);
                bolObj.setFileName("NotThere_" + not_Folio_id + "_" + (s) + ".jpg");
            }

            CapturedImage_Path.add(bolObj);
            if (AllFilePaths.size() > 0) {
                photo_list_layout.setVisibility(View.VISIBLE);
            } else {
                photo_list_layout.setVisibility(View.GONE);
            }

            rectangular_image.setVisibility(View.VISIBLE);
            Picasso.with(NotThereActivity.this).load(destination)
                    .placeholder(R.drawable.ic_placeholder)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(rectangular_image);

            AllFilePaths.add(bolObj);
            ImageAdapter mAdaper = new ImageAdapter(NotThereActivity.this, AllFilePaths, false);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(NotThereActivity.this, LinearLayoutManager.HORIZONTAL, false);
            not_There_photos.setLayoutManager(layoutManager2);
            mAdaper.setCallBack(NotThereActivity.this);
            not_There_photos.setAdapter(mAdaper);
            mAdaper.notifyDataSetChanged();
        } else {
            Utilities.showToast(NotThereActivity.this, "Capture Picture Properlly");
        }
    }

    private void zoom_CapturedImage(String imagepath) {

        View view = getLayoutInflater().inflate(R.layout.imagefullscreen, null);
        touchImage = (TouchImageView) view.findViewById(R.id.touchImage);
        zoom_remove_photo = (ImageView) view.findViewById(R.id.zoom_remove_photo);
        toucchimage_Dialog = new Dialog(this, R.style.DialogAnimation);
        toucchimage_Dialog.setContentView(view);
        toucchimage_Dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        //lp.copyFrom(toucchimage_Dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;//(int) (Utilities.screenHeight * 0.85);
        //lp.gravity = Gravity.CENTER_VERTICAL;
        toucchimage_Dialog.getWindow().setAttributes(lp);


        Picasso.with(this).load(new File(imagepath))
                .resize((int) (Utilities.screenWidth * 0.80), (int) (Utilities.screenHeight * 0.80))
                .placeholder(R.drawable.ic_placeholder)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(touchImage);

        zoom_remove_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toucchimage_Dialog.dismiss();
            }
        });

        toucchimage_Dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilities.saveButton) {
            Intent cam = new Intent(NotThereActivity.this, CamActivity.class);
            startActivityForResult(cam, 0);
        }
        if (Utilities.Pitcure_captured_NotThere) {
            if (Utilities.AutoCompltete_Clicked) {
                not_folio_auto_complete.setEnabled(false);
            } else {
                not_folio_auto_complete.setEnabled(true);
            }

            if (Utilities.Folio_List_Clicked) {
                not_folio_text.setVisibility(View.GONE);
                not_folio_Settext.setText(not_folio_text.getText().toString());
                not_folio_Settext.setVisibility(View.VISIBLE);
            } else {
                not_folio_text.setVisibility(View.VISIBLE);
                not_folio_Settext.setVisibility(View.GONE);
            }
        }
        selected_autocomplete_Warehouse = Utilities.getPref(NotThereActivity.this, "WarehouseName", "");
        selected_autocomplete_Warehouse_ID = Utilities.getPref(NotThereActivity.this, "WarehouseId", "");

        selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));
    }

    public void deletePhoto(int i, ImageFile tag) {

    }

    private void uploadDocuments() {

        File file = null;
        RequestBody reqFile = null;
        MultipartBody.Part body = null;
        List<MultipartBody.Part> multipleFiles = new ArrayList<>();

        for (int i = 0; i < CapturedImage_Path.size(); i++) {
            file = new File(CapturedImage_Path.get(i).getFilePath());
            reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData(CapturedImage_Path.get(i).getFileName(), CapturedImage_Path.get(i).getFileName(), reqFile);
            multipleFiles.add(body);
        }

        document_dialog = new ProgressDialog(NotThereActivity.this);
        document_dialog.setMessage("Uploading...");
        document_dialog.setCancelable(false);
        document_dialog.setCanceledOnTouchOutside(false);

        if (!document_dialog.isShowing()) {
            document_dialog.show();
        }

        call_UploadDoc = apiService.uploadMultipleFiles_NotThere(userId, 6, not_Folio_id, location_deatil_Id, multipleFiles);
        call_UploadDoc.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!NotThereActivity.this.isDestroyed()) {
                    if (document_dialog != null && document_dialog.isShowing()) {
                        if (!NotThereActivity.this.isFinishing()) {
                            document_dialog.dismiss();
                        }
                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        try {
                            String json = response.body().string();
                            JSONObject jsonObj = new JSONObject(json);
                            if (jsonObj.getString(("Status")).equalsIgnoreCase("Fail")) {
                                Utilities.showToast(NotThereActivity.this, jsonObj.getString(("message")));

                            } else if (jsonObj.getString(("Status")).equalsIgnoreCase("Success")) {
                                Utilities.showToast(NotThereActivity.this, "Success");
                                Utilities.saveButton = false;

                                CapturedImage_Path.clear();
                                AllFilePaths.clear();
                                sid_editView.setText("");
                                location_editView.setText("");

                                finish();
                            } else {
                                Utilities.showToast(NotThereActivity.this, "Please try again later");
                            }


                        } catch (IOException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    Utilities.showToast(NotThereActivity.this, "Please try again later");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!NotThereActivity.this.isDestroyed()) {
                    if (document_dialog != null && document_dialog.isShowing()) {
                        document_dialog.dismiss();
                    }
                }
                Utilities.showToast(NotThereActivity.this, "Please try again later");

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

        dialog.getWindow().setAttributes(lp);

        current_warehouse_text = (TextView) dialog.findViewById(R.id.current_warehouse_text);
        current_warehouse_text.setText(selected_autocomplete_Warehouse);

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
                    Utilities.savePref(NotThereActivity.this, "WarehouseName", selected_autocomplete_Warehouse);
                    Utilities.savePref(NotThereActivity.this, "WarehouseId", warehouse_id);
                }
                dialog.dismiss();
            }
        });
        getWareHouse();

        dialog.show();
    }

    private void getWareHouse() {
        warehouse_dialog = new ProgressDialog(NotThereActivity.this);
        warehouse_dialog.setMessage("Loading...");
        warehouse_dialog.setCancelable(false);
        warehouse_dialog.setCanceledOnTouchOutside(false);

        if (!warehouse_dialog.isShowing()) {
            warehouse_dialog.show();
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("", "");

        userId=Utilities.getPref(NotThereActivity.this, "UserId", "");

        call_Warehouse = apiService.getWarehousesList("",userId);
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
                if (!NotThereActivity.this.isDestroyed()) {
                    if (warehouse_dialog.isShowing()) {
                        warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(NotThereActivity.this, "Please try again later");
            }
        });
    }

    private void getWarehouse_AutoCompleteText() {
        final List<String> newList = new ArrayList<>();
        for (int i = 0; i < warehouse_list.size(); i++) {
            newList.add(warehouse_list.get(i).getName());
        }
        WareHouseListAdapter adapter = new WareHouseListAdapter(NotThereActivity.this, warehouse_list);
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

    private void saveWarehouse(String wharehouse_id) {
        save_warehouse_dialog = new ProgressDialog(NotThereActivity.this);
        save_warehouse_dialog.setMessage("Loading...");
        save_warehouse_dialog.setCancelable(false);
        save_warehouse_dialog.setCanceledOnTouchOutside(false);

        if (!save_warehouse_dialog.isShowing()) {
            save_warehouse_dialog.show();
        }

        call_saveWarehouse = apiService.saveWarehouse(wharehouse_id, userId);
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
                            //  Utilities.showToast(HomeActivity.this,"Wharehouse saved Successfully");
                            Utilities.showToast(NotThereActivity.this, response.body().toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (!NotThereActivity.this.isDestroyed()) {
                    if (save_warehouse_dialog.isShowing()) {
                        save_warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(NotThereActivity.this, "Please try again later");
            }
        });
    }


}
