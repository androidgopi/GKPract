package com.kireeti.gkpract;

import android.app.Activity;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kireeti.gkpract.adapter.ImageAdapter;
import com.kireeti.gkpract.adapter.QAFindingFolioAdapter;
import com.kireeti.gkpract.adapter.QAFindingFolioListSelectAdapter;
import com.kireeti.gkpract.adapter.WareHouseListAdapter;
import com.kireeti.gkpract.adapter.WarehouseSelectAdapter;
import com.kireeti.gkpract.cameraView.CamActivity_QualityFinding;
import com.kireeti.gkpract.customClass.TouchImageView;
import com.kireeti.gkpract.interfaces.DeletePhoto;
import com.kireeti.gkpract.interfaces.QAFindingAuto;
import com.kireeti.gkpract.interfaces.SelectQAFindingFolioList;
import com.kireeti.gkpract.interfaces.SelectWarehouse;
import com.kireeti.gkpract.interfaces.WareHouseAuto;
import com.kireeti.gkpract.models.ImageFile;
import com.kireeti.gkpract.models.PhotoResults;
import com.kireeti.gkpract.models.QAFindingFolio;
import com.kireeti.gkpract.models.QAFindingFolioList;
import com.kireeti.gkpract.models.WarehouseResult;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Quality_FindingActivity extends AppCompatActivity implements View.OnClickListener, SelectWarehouse,
        DeletePhoto, QAFindingAuto, SelectQAFindingFolioList, WareHouseAuto {

    private TextView warehouse_selection, Details_txt;
    private ApiInterface apiService;
    private Call<List<WarehouseResult>> call_Warehouse;
    private Call<List<QAFindingFolioList>> call_QAFindingFolio_list;
    List<WarehouseResult> warehouse_list = new ArrayList<>();
    List<QAFindingFolioList> QAFindingFolio_list = new ArrayList<QAFindingFolioList>();
    private PopupWindow warehuse_popup;
    ImageView back_image, lagout_image;
    EditText comment_Txt;
    private int mWidth;
    private int viewWidth;
    private File rootDir;
    private Button saveimage_btn;
    private Button takephoto;
    private RoundedImageView rectangular_image;
    private ImageView left_arrow;
    private ImageView right_arrow;
    private RecyclerView recycle_photos;
    ArrayList<ImageFile> AllFilePaths = new ArrayList<>();
    ArrayList<ImageFile> CapturedImage_Path = new ArrayList<>();
    private int REQUEST_CAMERA = 0;
    private int PHOTOS_NUMBER = 000;
    private int PHOTOS_PATH_NUMBER = 000;
    private File destination;
    private String warehouse_selction_id = "", warehouse_code = "";
    LinearLayout photo_list_layout;
    private View new_existing_view;
    private Dialog new_existing_dialog;
    private ProgressDialog delete_Dialog;
    private Call<JsonObject> delete_Picture;
    private ProgressDialog document_dialog;
    private String userId;
    private Call<ResponseBody> call_UploadDoc;
    ProgressDialog nextAvailableQAFinding_dialog;
    private Call<JsonObject> Call_nextAvailableQAFinding;
    AutoCompleteTextView quality_finding_folio;
    ProgressDialog getQAFindingFolio_Diolog;
    String RefId;
    private ProgressDialog warehouse_dialog;
    ImageView change_popup_img;
    String Qa_Folio_id = "";
    private Call<List<QAFindingFolio>> get_QAFindings;
    private List<QAFindingFolio> QAFindings_list = new ArrayList<>();
    private ProgressDialog getPhoto_dialog;
    private Call<PhotoResults> get_FolioPhotos;
    PhotoResults listOfPhotos = new PhotoResults();
    private TouchImageView touchImage;
    private Dialog toucchimage_Dialog;
    private String individual_Image_Comment;
    List<String> commentlist = new ArrayList<String>();
    private String comments;
    private String comment_FileName;
    private Button resend_btn, close_btn;
    private String getFolioBasedPhoto_ID;
    private Integer addedBy;
    private String QAFCount;
    private String isClosed;
    private Dialog close_button_dialog;
    private View close_Dialog_view;
    private String close_btn_reason;
    private String comment_main;
    private String s1;
    private String Warehose_id;
    private ImageView zoom_remove_photo;
    private TextView zoom_comment_text;
    private String Rarecase_Id;
    private int QAFCount_int;
    private TextView qafinding_folio_selection_text;
    private ProgressDialog qafindingfoliList_dialog;
    private String fromDate, toDate;
    //   private ApiInterface apiService_another;
    private PopupWindow qafindinglist_popup;
    private String qualityFinding_folio_id;
    private String warehouse_aselect_id;
    private TextView quality_finding_folio_settxt;
    private String Email;

    private AutoCompleteTextView warehouse_autocomplete;
    private ProgressDialog warehouse_dialog_main;
    private Call<List<Warehouse>> call_Warehouse_main;
    List<Warehouse> warehouse_list_main = new ArrayList<>();
    private PopupWindow warehuse_popup_main;
    private String warehouse_id;
    private TextView selction_for_warehouse;
    private ImageView cancel_popup;
    Button save_button;
    private String selected_autocomplete_Warehouse;
    private ProgressDialog save_warehouse_dialog;
    private Call<Integer> call_saveWarehouse;
    private String selected_autocomplete_Warehouse_ID;
    private TextView current_warehouse_text;
    private String isRcUpload = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_finding);
        apiService = ApiClient.getClient(Quality_FindingActivity.this).create(ApiInterface.class);
        //  apiService_another = ApiClient.getAnotherClient(Quality_FindingActivity.this).create(ApiInterface.class);
        Display display = getWindowManager().getDefaultDisplay();
        mWidth = display.getWidth(); // deprecated
        viewWidth = mWidth / 3;
        Utilities.closeButton = false;
        Utilities.Resend_button_status = false;
        Utilities.nextPitcure = false;

        selected_autocomplete_Warehouse = Utilities.getPref(Quality_FindingActivity.this, "WarehouseName", "");
        selected_autocomplete_Warehouse_ID = Utilities.getPref(Quality_FindingActivity.this, "WarehouseId", "");


        findViews();

        rootDir = new File(Environment.getExternalStorageDirectory() + "/" + "Cargoquin");
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }


    }

    private void findViews() {

        warehouse_selection = (TextView) findViewById(R.id.warehouse_selection);
        warehouse_selection.setOnClickListener(this);

        back_image = (ImageView) findViewById(R.id.back_image);
        back_image.setOnClickListener(this);

        lagout_image = (ImageView) findViewById(R.id.lagout_image);
        lagout_image.setOnClickListener(this);

        comment_Txt = (EditText) findViewById(R.id.comment_Txt);
        comment_Txt.setCursorVisible(false);

        saveimage_btn = (Button) findViewById(R.id.saveimage_btn);
        saveimage_btn.setOnClickListener(this);

        selction_for_warehouse = (TextView) findViewById(R.id.selction_for_warehouse);
        selction_for_warehouse.setOnClickListener(this);
        selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));

        takephoto = (Button) findViewById(R.id.takephoto);
        takephoto.setOnClickListener(this);

        rectangular_image = (RoundedImageView) findViewById(R.id.rectangular_image);
        rectangular_image.setOnClickListener(this);

        quality_finding_folio_settxt = (TextView) findViewById(R.id.quality_finding_folio_settxt);


        LinearLayout.LayoutParams charParams = new LinearLayout.LayoutParams((int) (Utilities.screenWidth * 0.55), (int) (Utilities.screenHeight * 0.22));
        charParams.setMargins(0, 0, 0, 10);
        charParams.gravity = Gravity.CENTER;
        rectangular_image.setLayoutParams(charParams);

        left_arrow = (ImageView) findViewById(R.id.left_arrow);
        left_arrow.setOnClickListener(this);

        right_arrow = (ImageView) findViewById(R.id.right_arrow);
        right_arrow.setOnClickListener(this);

        Details_txt = (TextView) findViewById(R.id.Details_txt);
        Details_txt.setOnClickListener(this);

        recycle_photos = (RecyclerView) findViewById(R.id.recycle_photos);
        photo_list_layout = (LinearLayout) findViewById(R.id.photo_list_layout);

        change_popup_img = (ImageView) findViewById(R.id.change_popup_img);
        change_popup_img.setOnClickListener(this);

        resend_btn = (Button) findViewById(R.id.resend_btn);
        resend_btn.setOnClickListener(this);

        close_btn = (Button) findViewById(R.id.close_btn);
        close_btn.setOnClickListener(this);

        qafinding_folio_selection_text = (TextView) findViewById(R.id.quality_finding_folio_selection);
        qafinding_folio_selection_text.setOnClickListener(this);

        if (!Utilities.getPref(Quality_FindingActivity.this, "UserId", "").equalsIgnoreCase("")) {
            userId = Utilities.getPref(Quality_FindingActivity.this, "UserId", "");
        }

        quality_finding_folio = (AutoCompleteTextView) findViewById(R.id.quality_finding_folio);
        quality_finding_folio.setThreshold(1);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        toDate = df.format(c);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -15);
        Date newDate = calendar.getTime();
        fromDate = df.format(newDate);


        getWareHouse();
        NeworExisting_Popup();

        if (Utilities.newFolio) {

        } else {
            if (Utilities.show_Existing_folio_Text_Change) {
                quality_finding_folio_settxt.setText(qafinding_folio_selection_text.getText().toString());
                qafinding_folio_selection_text.setVisibility(View.GONE);
                quality_finding_folio_settxt.setVisibility(View.VISIBLE);
            } else {
                qafinding_folio_selection_text.setText("");
                qafinding_folio_selection_text.setVisibility(View.VISIBLE);
                quality_finding_folio_settxt.setVisibility(View.GONE);
            }
        }
    }

    private void getQAFindingFolio(String id) {
        getQAFindingFolio_Diolog = new ProgressDialog(Quality_FindingActivity.this);
        getQAFindingFolio_Diolog.setMessage("Loading...");
        getQAFindingFolio_Diolog.setCancelable(false);
        getQAFindingFolio_Diolog.setCanceledOnTouchOutside(false);

        if (!getQAFindingFolio_Diolog.isShowing()) {
            getQAFindingFolio_Diolog.show();
        }

        get_QAFindings = apiService.getQAFindingFolio(id, "");
        get_QAFindings.enqueue(new Callback<List<QAFindingFolio>>() {
            @Override
            public void onResponse(Call<List<QAFindingFolio>> call, Response<List<QAFindingFolio>> response) {

                if (getQAFindingFolio_Diolog.isShowing()) {
                    getQAFindingFolio_Diolog.dismiss();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        QAFindings_list = response.body();
                        if (QAFindings_list.size() > 0) {

                            getQAFolio_AutoCompleteText();

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<QAFindingFolio>> call, Throwable t) {

                if (!Quality_FindingActivity.this.isDestroyed()) {
                    if (getQAFindingFolio_Diolog.isShowing()) {
                        getQAFindingFolio_Diolog.dismiss();
                    }
                }
                Utilities.showToast(Quality_FindingActivity.this, "Please try again later");

            }
        });
    }

    private void getQAFolio_AutoCompleteText() {
        resend_btn.setVisibility(View.GONE);
        close_btn.setVisibility(View.GONE);

        final List<String> newList = new ArrayList<>();
        for (int i = 0; i < QAFindings_list.size(); i++) {
            newList.add(QAFindings_list.get(i).getQFFolio());
        }
        QAFindingFolioAdapter adapter = new QAFindingFolioAdapter(Quality_FindingActivity.this, QAFindings_list);
        quality_finding_folio.setThreshold(1);
        //quality_finding_folio.setText("");
        adapter.setCallBack(this);
        quality_finding_folio.setAdapter(adapter);

    }

    private void getWareHouse() {

        Utilities.savePref(Quality_FindingActivity.this, "ExistingWarehouseCode", "");

        warehouse_dialog = new ProgressDialog(Quality_FindingActivity.this);
        warehouse_dialog.setMessage("Loading...");
        warehouse_dialog.setCancelable(false);
        warehouse_dialog.setCanceledOnTouchOutside(false);

        if (!warehouse_dialog.isShowing()) {
            warehouse_dialog.show();
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        call_Warehouse = apiService.getWarehouse(obj);
        call_Warehouse.enqueue(new Callback<List<WarehouseResult>>() {
            @Override
            public void onResponse(Call<List<WarehouseResult>> call, Response<List<WarehouseResult>> response) {
                if (warehouse_dialog != null && warehouse_dialog.isShowing()) {
                    if (warehouse_dialog.isShowing()) {
                        warehouse_dialog.dismiss();
                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {
                        warehouse_list = response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<WarehouseResult>> call, Throwable t) {
                if (!Quality_FindingActivity.this.isDestroyed()) {
                    if (warehouse_dialog.isShowing()) {
                        warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(Quality_FindingActivity.this, "Please try again later");
            }
        });
    }

    private void NeworExisting_Popup() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        new_existing_view = inflater.inflate(R.layout.new_existing_popup, null);
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Quality_FindingActivity.this);

        new_existing_dialog = new Dialog(this);
        new_existing_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        new_existing_dialog.setContentView(new_existing_view);
        new_existing_dialog.setCanceledOnTouchOutside(false);
        new_existing_dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(new_existing_dialog.getWindow().getAttributes());
        lp.width = (int) (Utilities.screenWidth * 0.90);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        new_existing_dialog.getWindow().setAttributes(lp);


        final Button newuser_btn = (Button) new_existing_view.findViewById(R.id.newuser_btn);
        final Button existing_btn = (Button) new_existing_view.findViewById(R.id.existing_btn);


        newuser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.newFolio = true;
                new_existing_dialog.dismiss();
                resend_btn.setVisibility(View.GONE);
                quality_finding_folio.setVisibility(View.INVISIBLE);
                qafinding_folio_selection_text.setVisibility(View.GONE);
                quality_finding_folio_settxt.setVisibility(View.GONE);

            }
        });

        existing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.newFolio = false;
                new_existing_dialog.dismiss();
                quality_finding_folio.setEnabled(true);
                quality_finding_folio.setVisibility(View.VISIBLE);
                qafinding_folio_selection_text.setVisibility(View.VISIBLE);
            }
        });


        new_existing_dialog.show();
    }

    private void getNextAvailableQAFindingFolio(String id) {

        nextAvailableQAFinding_dialog = new ProgressDialog(Quality_FindingActivity.this);
        nextAvailableQAFinding_dialog.setMessage("Loading...");
        nextAvailableQAFinding_dialog.setCanceledOnTouchOutside(false);
        nextAvailableQAFinding_dialog.setCancelable(false);
        if (nextAvailableQAFinding_dialog != null && !nextAvailableQAFinding_dialog.isShowing()) {
            nextAvailableQAFinding_dialog.show();
        }

        Call_nextAvailableQAFinding = apiService.nextAvailableQAFinding(id);
        Call_nextAvailableQAFinding.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (nextAvailableQAFinding_dialog.isShowing()) {
                    nextAvailableQAFinding_dialog.dismiss();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().get("Status").getAsString().equalsIgnoreCase("Success")) {

                            Qa_Folio_id = response.body().get("Folio").getAsString();
                            quality_finding_folio.setEnabled(false);
                            if (Utilities.newFolio) {

                            } else {
                                //  qafinding_folio_selection_text.setVisibility(View.VISIBLE);
                            }
                            //"QF" + warehouse_code + "_" + Qa_Folio_id
                            quality_finding_folio.setText("QF" + warehouse_code + "_" + Qa_Folio_id); //Qa_Folio_id
                            quality_finding_folio.setVisibility(View.VISIBLE);
                            Utilities.saveButton = true;
                            Intent cam = new Intent(Quality_FindingActivity.this, CamActivity_QualityFinding.class);
                            startActivityForResult(cam, 0);
                            // Utilities.showToast(Quality_FindingActivity.this,response.body().get("Folio").getAsString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (!Quality_FindingActivity.this.isDestroyed()) {
                    if (nextAvailableQAFinding_dialog != null && nextAvailableQAFinding_dialog.isShowing()) {
                        nextAvailableQAFinding_dialog.dismiss();
                    }
                }
                Utilities.showToast(Quality_FindingActivity.this, "Please try again later");

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilities.nextPitcure) {
            Intent cam = new Intent(Quality_FindingActivity.this, CamActivity_QualityFinding.class);
            startActivityForResult(cam, 0);

            selected_autocomplete_Warehouse = Utilities.getPref(Quality_FindingActivity.this, "WarehouseName", "");
            selected_autocomplete_Warehouse_ID = Utilities.getPref(Quality_FindingActivity.this, "WarehouseId", "");

            selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));

        }
        if (Utilities.newFolio) {

        } else {
            if (Utilities.show_Existing_folio_Text_Change) {
                quality_finding_folio_settxt.setText(qafinding_folio_selection_text.getText().toString());
                qafinding_folio_selection_text.setVisibility(View.GONE);
                quality_finding_folio_settxt.setVisibility(View.VISIBLE);
            } else {
                qafinding_folio_selection_text.setText("");
                qafinding_folio_selection_text.setVisibility(View.VISIBLE);
                quality_finding_folio_settxt.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.warehouse_selection:
                if (Utilities.qafindingFolioPopup) {
                    if (qafindinglist_popup.isShowing()) {
                        qafindinglist_popup.dismiss();
                    }
                }
                if (warehuse_popup != null) {
                    if (!warehuse_popup.isShowing()) {
                        warehuse_Dropdown();
                    } else {
                        warehuse_popup.dismiss();
                    }
                } else {
                    warehuse_Dropdown();
                }

                break;

            case R.id.quality_finding_folio_selection:

                if (warehouse_aselect_id == null && warehouse_selction_id.equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), "Please select Warehouse", Toast.LENGTH_SHORT).show();

                } else {
                    if (qafindinglist_popup != null) {
                        if (!qafindinglist_popup.isShowing()) {
                            Utilities.hideKeyboard(quality_finding_folio);
                            qafindingFolio_Dropdown();
                        } else {
                            qafindinglist_popup.dismiss();
                        }
                    } else {
                        Utilities.hideKeyboard(quality_finding_folio);
                        qafindingFolio_Dropdown();
                    }
                }


                break;

            case R.id.back_image:
                finish();
                Utilities.newFolio = false;

                PHOTOS_NUMBER = 000;
                PHOTOS_PATH_NUMBER = 000;

                Utilities.LastPhotoNumber = 000;
                Utilities.LastPhotoPathNumber = 000;

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

            case R.id.takephoto:
                if (warehouse_selection.getText().length() > 0) {

                    if (Utilities.saveButton) {

                        Intent cam = new Intent(Quality_FindingActivity.this, CamActivity_QualityFinding.class);
                        startActivityForResult(cam, 0);

                    } else {
                        if (Warehose_id != null) {
                            getNextAvailableQAFindingFolio(Warehose_id);
                            RefId = String.valueOf(0);
                            Utilities.LastPhotoNumber = 000;
                        } else if (Rarecase_Id != null) {

                            Intent cam = new Intent(Quality_FindingActivity.this, CamActivity_QualityFinding.class);
                            startActivityForResult(cam, 0);
                        } else {

                        }

                    }

                } else {
                    Utilities.showToast(Quality_FindingActivity.this, "Please Select a Warehouse");
                }
                break;

            case R.id.left_arrow:
                if (recycle_photos.getChildAt(0) != null) {
                    if (AllFilePaths.size() > 0) {
                        recycle_photos.smoothScrollBy((int) recycle_photos.getScrollY() - recycle_photos.getChildAt(0).getWidth(), (int) recycle_photos.getScrollX());
                        left_arrow.setImageResource(R.drawable.ic_left_blue);
                    } else {
                        left_arrow.setImageResource(R.drawable.ic_left_gray);
                    }
                }

                break;

            case R.id.right_arrow:
                if (recycle_photos.getChildAt(0) != null) {
                    if (AllFilePaths.size() > 0) {
                        recycle_photos.smoothScrollBy((int) recycle_photos.getScrollX() + recycle_photos.getChildAt(0).getWidth(), (int) recycle_photos.getScrollY());
                        right_arrow.setImageResource(R.drawable.ic_right_blue);
                    } else {
                        right_arrow.setImageResource(R.drawable.ic_right_gray);
                    }
                }
                break;

            case R.id.saveimage_btn:
                if (AllFilePaths.size() > 0) {
                    if (!quality_finding_folio.getText().toString().equalsIgnoreCase("") || !qafinding_folio_selection_text.toString().equalsIgnoreCase("")) {
                        if (!comment_Txt.getText().toString().equalsIgnoreCase("")) {
                            uploadDocuments();
                        } else {
                            Utilities.showToast(Quality_FindingActivity.this, "Please Type Comment About photos");
                        }
                    } else {
                        Utilities.showToast(Quality_FindingActivity.this, "Please Select Quality Finding Folio");
                    }
                } else {
                    Utilities.showToast(Quality_FindingActivity.this, "Please Select or Capture at least one Photo.");
                }
                break;

            case R.id.change_popup_img:
                NeworExisting_Popup();
                break;

            case R.id.rectangular_image:
                if (AllFilePaths.size() > 0) {
                    if (!rectangular_image.getTag().toString().equalsIgnoreCase("")) {
                        photo_list_layout.setVisibility(View.VISIBLE);
                        zoom_CapturedImage(rectangular_image.getTag().toString());
                    }
                } else {
                    photo_list_layout.setVisibility(View.GONE);
                    Utilities.showToast(Quality_FindingActivity.this, "Please Select or Capture at least one Photo");
                }
                break;

            case R.id.resend_btn:
                Utilities.Resend_button_status = true;
                Utilities.Reopened_count_one_time = true;
                Utilities.saveButton = true;
                quality_finding_folio.setEnabled(false);
                takephoto.setVisibility(View.VISIBLE);
                if (AllFilePaths.size() > 0) {
                    Utilities.Resend_button_status = true;
                    quality_finding_folio.setEnabled(false);
                    AllFilePaths.clear();
                    rectangular_image.setVisibility(View.INVISIBLE);
                    resend_btn.setVisibility(View.GONE);
                    //takephoto.setPadding(30, 20, 40, 20);
                    ImageAdapter mAdaper = new ImageAdapter(Quality_FindingActivity.this, AllFilePaths, true);
                    RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Quality_FindingActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recycle_photos.setLayoutManager(layoutManager2);
                    mAdaper.quality_setCallBack(Quality_FindingActivity.this);
                    recycle_photos.setAdapter(mAdaper);
                }
                break;

            case R.id.close_btn:
//                if(isRcUpload.equalsIgnoreCase("true")) {
//                    Utilities.closeButton = false;
//                    close_Button_Popup();
//                }else{
                Utilities.closeButton = false;
                close_Button_Popup();
                //  Toast.makeText(Quality_FindingActivity.this,"Rc Upload file Missing",Toast.LENGTH_LONG).show();
                //  }
                break;

            case R.id.selction_for_warehouse:
                show_Popup_Warehouse();
                break;

        }
    }

    private void close_Button_Popup() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        close_Dialog_view = inflater.inflate(R.layout.close_buton_popup, null);
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Quality_FindingActivity.this);

        close_button_dialog = new Dialog(this);
        close_button_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        close_button_dialog.setContentView(close_Dialog_view);
        close_button_dialog.setCanceledOnTouchOutside(false);
        close_button_dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(close_button_dialog.getWindow().getAttributes());
        lp.width = (int) (Utilities.screenWidth * 0.90);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        close_button_dialog.getWindow().setAttributes(lp);


        final Button save_btn = (Button) close_Dialog_view.findViewById(R.id.save_btn);
        final Button cancel_btn = (Button) close_Dialog_view.findViewById(R.id.cancel_btn);
        final EditText close_btn_comment_Txt = (EditText) close_Dialog_view.findViewById(R.id.close_btn_comment_Txt);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!close_btn_comment_Txt.getText().toString().equalsIgnoreCase("")) {

                    close_btn_reason = close_btn_comment_Txt.getText().toString();
                    close_button_dialog.dismiss();
                    Utilities.closeButton = true;

                    uploadDocuments();

                } else {
                    Utilities.showToast(Quality_FindingActivity.this, "Please Type Reason");
                }


            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_button_dialog.dismiss();


            }
        });
        close_button_dialog.show();
    }

    private void uploadDocuments() {

        File file = null;
        RequestBody reqFile = null;
        MultipartBody.Part body = null;
        List<MultipartBody.Part> multipleFiles = new ArrayList<>();

        if (Utilities.closeButton) {
            isClosed = "true";
            comment_main = close_btn_reason;

            if (CapturedImage_Path.size() == 0) {
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.applogo);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                largeIcon.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                ImageFile bolObj1 = new ImageFile();

                File destination1 = new File(rootDir, "_dummeyImage" + ".jpg");
                String bol_filePath = destination1.getAbsolutePath();
                bolObj1.setFilePath(bol_filePath);

                rectangular_image.setTag(destination1);
                FileOutputStream fo;
                try {
                    destination1.createNewFile();
                    fo = new FileOutputStream(destination1);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                file = new File(bolObj1.getFilePath());
                reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                body = MultipartBody.Part.createFormData("dummy_image", "dummy_image", reqFile);
                multipleFiles.add(body);

            }

        } else {
            isClosed = "false";
            comment_main = comment_Txt.getText().toString();

            for (int i = 0; i < CapturedImage_Path.size(); i++) {
                file = new File(CapturedImage_Path.get(i).getFilePath());
                reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                body = MultipartBody.Part.createFormData(CapturedImage_Path.get(i).getFileName(), CapturedImage_Path.get(i).getFileName(), reqFile);
                multipleFiles.add(body);
            }

        }


        document_dialog = new ProgressDialog(Quality_FindingActivity.this);
        document_dialog.setMessage("Uploading...");
        document_dialog.setCancelable(false);
        document_dialog.setCanceledOnTouchOutside(false);

        if (!document_dialog.isShowing()) {
            document_dialog.show();
        }
        for (int i = 0; i < commentlist.size(); i++) {

            comments = comments + commentlist.get(i);
        }

        if (comments != null) {
            comments = comments.replace("null", "");
        }
        if (QAFCount == null) {
            QAFCount = "0";
        }

        if (Utilities.Resend_button_status) {

            call_UploadDoc = apiService.uploadfile_QualityFinding(userId, 5, getFolioBasedPhoto_ID, Qa_Folio_id, warehouse_selction_id, warehouse_code, comment_main, comments, QAFCount, isClosed, Email, multipleFiles);

        } else {
            call_UploadDoc = apiService.uploadfile_QualityFinding(userId, 5, RefId, Qa_Folio_id, warehouse_selction_id, warehouse_code, comment_main, comments, QAFCount, isClosed, Email, multipleFiles);

        }

        call_UploadDoc.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!Quality_FindingActivity.this.isDestroyed()) {
                    if (document_dialog != null && document_dialog.isShowing()) {
                        if (!Quality_FindingActivity.this.isFinishing()) {
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
                                Utilities.showToast(Quality_FindingActivity.this, jsonObj.getString(("message")));

                            } else if (jsonObj.getString(("Status")).equalsIgnoreCase("Success")) {
                                Utilities.showToast(Quality_FindingActivity.this, "Success");
                                AllFilePaths.clear();
                                Utilities.newFolio = false;
                                Utilities.closeButton = false;
                                close_btn.setVisibility(View.GONE);
                                Utilities.saveButton = false;
                                Utilities.show_Existing_folio_Text_Change = false;
                                finish();
                            } else {
                                Utilities.showToast(Quality_FindingActivity.this, "Please try again later");
                            }
                            CapturedImage_Path.clear();
                            // AllFilePaths.clear();

                        } catch (IOException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    Utilities.showToast(Quality_FindingActivity.this, "Please try again later");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!Quality_FindingActivity.this.isDestroyed()) {
                    if (document_dialog != null && document_dialog.isShowing()) {
                        document_dialog.dismiss();
                    }
                }
                Utilities.showToast(Quality_FindingActivity.this, "Please try again later");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {

                    individual_Image_Comment = (String) data.getExtras().get("comment");
                    addImageToRecycleView(data, requestCode);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addImageToRecycleView(Intent data, int requestCode) {

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
                destination = new File(rootDir, warehouse_code + "_001" + ".jpg");
                PHOTOS_PATH_NUMBER = 001;
            } else {
                DecimalFormat formatter = new DecimalFormat("000");
                PHOTOS_PATH_NUMBER = PHOTOS_PATH_NUMBER + 1;
                String s = formatter.format(PHOTOS_PATH_NUMBER);
                destination = new File(rootDir, warehouse_code + "_" + (s) + ".jpg");
            }
        } else {
            DecimalFormat formatter = new DecimalFormat("000");
            Utilities.LastPhotoPathNumber = Utilities.LastPhotoPathNumber + 1;
            String s = formatter.format(Utilities.LastPhotoPathNumber + 1);
            destination = new File(rootDir, warehouse_code + "_" + (s) + ".jpg");
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

        if (Utilities.Resend_button_status) {

            if (Utilities.LastPhotoNumber == 0) {
                if (PHOTOS_NUMBER == 000) {
                    bolObj.setFileName("QF" + warehouse_code + "_" + Qa_Folio_id + "_001" + ".jpg");
                    comment_FileName = "QF" + warehouse_code + "_" + Qa_Folio_id + "_001" + ".jpg";
                    bolObj.setComments(individual_Image_Comment);
                    PHOTOS_NUMBER = 001;
                } else {
                    DecimalFormat formatter = new DecimalFormat("000");
                    PHOTOS_NUMBER = PHOTOS_NUMBER + 1;
                    String s = formatter.format(PHOTOS_NUMBER);
                    bolObj.setFileName("QF" + warehouse_code + "_" + Qa_Folio_id + "_" + (s) + ".jpg");
                    comment_FileName = "QF" + warehouse_code + "_" + Qa_Folio_id + "_" + (s) + ".jpg";
                    bolObj.setComments(individual_Image_Comment);
                }
            } else {

                int re_btn_count = 1;
                if (Utilities.Resend_button_count >= re_btn_count) {

                    if (Utilities.Reopened_count_one_time) {
                        Utilities.Reopened_count_one_time = false;
                        DecimalFormat formatter = new DecimalFormat("00");
                        Utilities.Resend_button_count = Utilities.Resend_button_count + 1;
                        s1 = formatter.format(Utilities.Resend_button_count);
                        bolObj.setComments(individual_Image_Comment);
                    } else {
                        DecimalFormat formatter = new DecimalFormat("00");
                        s1 = formatter.format(Utilities.Resend_button_count);
                        bolObj.setComments(individual_Image_Comment);
                    }

                    DecimalFormat formatter1 = new DecimalFormat("000");
                    Utilities.LastPhotoNumber = Utilities.LastPhotoNumber + 1;
                    String s = formatter1.format(Utilities.LastPhotoNumber);

                    bolObj.setFileName("QF" + warehouse_code + "_" + Qa_Folio_id + "_" + (s) + "_D" + (s1) + ".jpg");
                    comment_FileName = "QF" + warehouse_code + "_" + Qa_Folio_id + "_" + (s) + "_D" + (s1) + ".jpg";
                    bolObj.setComments(individual_Image_Comment);
                } else {
                    DecimalFormat formatter = new DecimalFormat("000");
                    Utilities.LastPhotoNumber = Utilities.LastPhotoNumber + 1;
                    String s = formatter.format(Utilities.LastPhotoNumber);
                    bolObj.setFileName("QF" + warehouse_code + "_" + Qa_Folio_id + "_" + (s) + "_D01" + ".jpg");
                    comment_FileName = "QF" + warehouse_code + "_" + Qa_Folio_id + "_" + (s) + "_D01" + ".jpg";
                    bolObj.setComments(individual_Image_Comment);
                }
            }

        } else {

            if (Utilities.LastPhotoNumber == 0) {
                if (PHOTOS_NUMBER == 000) {
                    bolObj.setFileName("QF" + warehouse_code + "_" + Qa_Folio_id + "_001" + ".jpg");
                    comment_FileName = "QF" + warehouse_code + "_" + Qa_Folio_id + "_001" + ".jpg";
                    PHOTOS_NUMBER = 001;
                    bolObj.setComments(individual_Image_Comment);
                } else {
                    DecimalFormat formatter = new DecimalFormat("000");
                    PHOTOS_NUMBER = PHOTOS_NUMBER + 1;
                    String s = formatter.format(PHOTOS_NUMBER);
                    bolObj.setFileName("QF" + warehouse_code + "_" + Qa_Folio_id + "_" + (s) + ".jpg");
                    comment_FileName = "QF" + warehouse_code + "_" + Qa_Folio_id + "_" + (s) + ".jpg";
                    bolObj.setComments(individual_Image_Comment);
                }
            } else {
                DecimalFormat formatter = new DecimalFormat("000");
                Utilities.LastPhotoNumber = Utilities.LastPhotoNumber + 1;
                String s = formatter.format(Utilities.LastPhotoNumber);
                bolObj.setFileName("QF" + warehouse_code + "_" + Qa_Folio_id + "_" + (s) + ".jpg");
                comment_FileName = "QF" + warehouse_code + "_" + Qa_Folio_id + "_" + (s) + ".jpg";
                bolObj.setComments(individual_Image_Comment);
            }
        }
        commentlist.add(comment_FileName + "@@" + individual_Image_Comment + "##");

        CapturedImage_Path.add(bolObj);

        if (AllFilePaths.size() > 0) {
            photo_list_layout.setVisibility(View.VISIBLE);
        } else {
            photo_list_layout.setVisibility(View.GONE);

        }
        rectangular_image.setVisibility(View.VISIBLE);
        Picasso.with(Quality_FindingActivity.this).load(destination)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(rectangular_image);

        final LinearLayout it = new LinearLayout(this);
        it.setOrientation(LinearLayout.VERTICAL);
        AllFilePaths.add(bolObj);
        ImageAdapter mAdaper = new ImageAdapter(Quality_FindingActivity.this, AllFilePaths, false);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Quality_FindingActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recycle_photos.setLayoutManager(layoutManager2);
        mAdaper.quality_setCallBack(Quality_FindingActivity.this);
        recycle_photos.setAdapter(mAdaper);
        mAdaper.notifyDataSetChanged();
    }


    private void warehuse_Dropdown() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.warehouse_popup_lyt, null);
        warehuse_popup = new PopupWindow(popupView, warehouse_selection.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        try {
            if (warehouse_list.size() > 0 && !warehouse_list.isEmpty()) {
                WarehouseSelectAdapter adapter = new WarehouseSelectAdapter(Quality_FindingActivity.this, warehouse_list);
                adapter.setCallBack(this);
                RecyclerView warehouse_recycleview = (RecyclerView) popupView.findViewById(R.id.warehouse_recycleview);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Quality_FindingActivity.this);
                warehouse_recycleview.setLayoutManager(layoutManager);
                warehouse_recycleview.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "there is no Warehouse", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            warehuse_popup.showAsDropDown(warehouse_selection, 0, 0, Gravity.CENTER);
        }
    }

    @Override
    public void callsetData(WarehouseResult mModel) {
        if (warehuse_popup.isShowing()) {
            warehuse_popup.dismiss();
        }
        if (mModel != null) {
            warehouse_selection.setText(mModel.getWarehouseName());
            if (mModel.getId() != null) {
                warehouse_selction_id = mModel.getId();
            }

            if (mModel.getWarehouseCode() != null) {
                warehouse_code = mModel.getWarehouseCode();
            }

            if (Utilities.newFolio) {
                Warehose_id = mModel.getId();
                Email = mModel.getSentTo();
            } else {
                warehouse_aselect_id = mModel.getId();
                Email = mModel.getSentTo();

                PHOTOS_NUMBER = 000;
                PHOTOS_PATH_NUMBER = 000;
                QAFindingFolio_list.clear();
                qafinding_folio_selection_text.setText("");
                QAFindings_list.clear();
                quality_finding_folio.setText("");
                quality_finding_folio.setVisibility(View.VISIBLE);
                qafinding_folio_selection_text.setVisibility(View.VISIBLE);
                AllFilePaths.clear();
                quality_finding_folio.setEnabled(true);
                Utilities.show_Existing_folio_Text_Change = false;
                Utilities.saveButton = false;
                qafinding_folio_selection_text.setVisibility(View.VISIBLE);
                quality_finding_folio_settxt.setVisibility(View.GONE);

                getQAFindingFolio(warehouse_aselect_id);
                getQAFindingFolioList(warehouse_aselect_id);
                quality_finding_folio.setText("");
                Utilities.Resend_button_count = 00;
                Utilities.LastPhotoNumber = 000;

                // previous data clear
                AllFilePaths.clear();
                ImageAdapter mAdaper = new ImageAdapter(Quality_FindingActivity.this, AllFilePaths, true);
                RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Quality_FindingActivity.this, LinearLayoutManager.HORIZONTAL, false);
                recycle_photos.setLayoutManager(layoutManager2);
                mAdaper.quality_setCallBack(Quality_FindingActivity.this);
                recycle_photos.setAdapter(mAdaper);
                if (AllFilePaths.size() == 0) {
                    rectangular_image.setVisibility(View.INVISIBLE);
                }

            }
        }
    }

    private void getQAFindingFolioList(String id) {

        qafindingfoliList_dialog = new ProgressDialog(Quality_FindingActivity.this);
        qafindingfoliList_dialog.setMessage("Loading...");
        qafindingfoliList_dialog.setCancelable(false);
        qafindingfoliList_dialog.setCanceledOnTouchOutside(false);

        if (!qafindingfoliList_dialog.isShowing()) {
            qafindingfoliList_dialog.show();
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("FromDate", fromDate);
        obj.addProperty("ToDate", toDate);

        JsonArray productArray = new JsonArray();
        JsonObject singleProduct = new JsonObject();

        singleProduct.addProperty("Id", id);
        singleProduct.addProperty("Type", "3");
        productArray.add(singleProduct);

        obj.add("WHRes", productArray);

        call_QAFindingFolio_list = apiService.getQAFindingFolioList(obj);
        call_QAFindingFolio_list.enqueue(new Callback<List<QAFindingFolioList>>() {
            @Override
            public void onResponse(Call<List<QAFindingFolioList>> call, Response<List<QAFindingFolioList>> response) {
                if (qafindingfoliList_dialog != null && qafindingfoliList_dialog.isShowing()) {
                    if (qafindingfoliList_dialog.isShowing()) {
                        qafindingfoliList_dialog.dismiss();
                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {
                        QAFindingFolio_list = response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<QAFindingFolioList>> call, Throwable t) {
                if (!Quality_FindingActivity.this.isDestroyed()) {
                    if (qafindingfoliList_dialog.isShowing()) {
                        qafindingfoliList_dialog.dismiss();
                    }
                }
                Utilities.showToast(Quality_FindingActivity.this, "Please try again later");
            }
        });
    }

    private void qafindingFolio_Dropdown() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.qafindingfoliolist_popup_lyt, null);
        qafindinglist_popup = new PopupWindow(popupView, qafinding_folio_selection_text.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        try {

            if (QAFindingFolio_list.size() > 0 && !QAFindingFolio_list.isEmpty()) {
                Utilities.qafindingFolioPopup = true;
                QAFindingFolioListSelectAdapter adapter = new QAFindingFolioListSelectAdapter(Quality_FindingActivity.this, QAFindingFolio_list);
                adapter.setCallBack(this);
                RecyclerView warehouse_recycleview = (RecyclerView) popupView.findViewById(R.id.qafindinglist_recycleview);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Quality_FindingActivity.this);
                warehouse_recycleview.setLayoutManager(layoutManager);
                warehouse_recycleview.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "There is no warehouse folios", Toast.LENGTH_SHORT).show();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            qafindinglist_popup.showAsDropDown(qafinding_folio_selection_text, 0, 0, Gravity.CENTER);
        }
    }


    @Override
    public void deletePhoto(int position, ImageFile mModel) {
        if (mModel.getId() != null && !mModel.getId().equalsIgnoreCase("")) {
            deletePhoto_Service(mModel.getId(), position, mModel.getFileName());
        } else {
            AllFilePaths.remove(position);
            ImageAdapter mAdaper = new ImageAdapter(Quality_FindingActivity.this, AllFilePaths, false);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Quality_FindingActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recycle_photos.setLayoutManager(layoutManager2);
            mAdaper.quality_setCallBack(Quality_FindingActivity.this);
            recycle_photos.setAdapter(mAdaper);
            mAdaper.notifyDataSetChanged();
        }

        if (position > 0) {

            rectangular_image.setTag(AllFilePaths.get(position - 1).getFilePath());
            Picasso.with(Quality_FindingActivity.this).load(AllFilePaths.get(position - 1).getFilePath())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(rectangular_image);
        } else {
            rectangular_image.setVisibility(View.INVISIBLE);
        }
    }

    public void deletePhoto_Service(String id, final int position, String filenamne) {
        delete_Dialog = new ProgressDialog(Quality_FindingActivity.this);
        delete_Dialog.setMessage("Loading...");
        delete_Dialog.setCanceledOnTouchOutside(false);
        delete_Dialog.setCancelable(false);
        if (delete_Dialog != null && !delete_Dialog.isShowing()) {
            delete_Dialog.show();
        }
        delete_Picture = apiService.deletePhoto(id, filenamne);
        delete_Picture.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (delete_Dialog.isShowing()) {
                    delete_Dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Utilities.showToast(Quality_FindingActivity.this, response.body().get("Status").getAsString());
                        AllFilePaths.remove(position);
                        ImageAdapter mAdaper = new ImageAdapter(Quality_FindingActivity.this, AllFilePaths, false);
                        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Quality_FindingActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recycle_photos.setLayoutManager(layoutManager2);
                        mAdaper.quality_setCallBack(Quality_FindingActivity.this);
                        recycle_photos.setAdapter(mAdaper);
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
                    Utilities.showToast(Quality_FindingActivity.this, "Please check your internet connection.");
                } else {
                    Utilities.showToast(Quality_FindingActivity.this, t.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void qaFindingAuto(QAFindingFolio mModel) {


        Qa_Folio_id = mModel.getQFFolio();
        RefId = mModel.getId();
        QAFCount = mModel.getQAFCount();
        QAFCount_int = Integer.parseInt(mModel.getQAFCount());
        quality_finding_folio.setVisibility(View.VISIBLE);
        quality_finding_folio.setText("QF" + warehouse_code + mModel.getQFFolio()); //"QF" + warehouse_code +
        quality_finding_folio.dismissDropDown();
        PHOTOS_NUMBER = 000;
        PHOTOS_PATH_NUMBER = 000;
        Utilities.hideKeyboard(quality_finding_folio);
        Utilities.LastPhotoNumber = 000;
        Utilities.LastPhotoPathNumber = 000;
        AllFilePaths.clear();
        addedBy = mModel.getAddedBy();
        getFolioBasedPhoto(mModel.getId());
        Rarecase_Id = mModel.getId();
        getFolioBasedPhoto_ID = mModel.getId();
        Utilities.Resend_button_status = false;
    }

    private void getFolioBasedPhoto(String id) {
        getPhoto_dialog = new ProgressDialog(Quality_FindingActivity.this);
        getPhoto_dialog.setMessage("Loading...");
        getPhoto_dialog.setCancelable(false);
        getPhoto_dialog.setCanceledOnTouchOutside(false);

        if (!getPhoto_dialog.isShowing()) {
            getPhoto_dialog.show();
        }

        get_FolioPhotos = apiService.getFolioPhotos(id, 5);
        get_FolioPhotos.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {

                if (getPhoto_dialog.isShowing()) {
                    getPhoto_dialog.dismiss();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("Success")) {

                            if (response.body().getPhotoList().size() > 0) {

                                if (QAFCount_int == 1) {
                                    resend_btn.setText(R.string.done);
                                } else if (QAFCount_int > 1) {
                                    resend_btn.setText(R.string.reopen);
                                } else {
                                    resend_btn.setText(R.string.done);
                                }
                                resend_btn.setVisibility(View.VISIBLE);
                                takephoto.setVisibility(View.GONE);
                                quality_finding_folio.setEnabled(true);
                                //
                                if (addedBy == Integer.parseInt(Utilities.getPref(Quality_FindingActivity.this, "UserId", "")) || Utilities.getPref(Quality_FindingActivity.this, "IsQAFUser", "").equalsIgnoreCase("true")) {
                                    close_btn.setVisibility(View.VISIBLE);
                                } else {
                                    close_btn.setVisibility(View.GONE);
                                }
                                //takephoto.setPadding(10, 2, 3, 3);
                                listOfPhotos.setPhotoList(response.body().getPhotoList());
                                AllFilePaths.clear();
                                for (int i = 0; i < listOfPhotos.getPhotoList().size(); i++) {

                                    if (listOfPhotos.getPhotoList().get(i).getPath().contains("_D_")) {
                                        Utilities.Resend_button_count = Integer.parseInt(listOfPhotos.getPhotoList().get(i).getPath().split("_d_")[1]);
                                    }

                                    ImageFile mModel = new ImageFile();
                                    mModel.setId(listOfPhotos.getPhotoList().get(i).getId());
                                    mModel.setFileName(listOfPhotos.getPhotoList().get(i).getPath().split("\\.")[0] + ".jpg");
                                    mModel.setComments(listOfPhotos.getPhotoList().get(i).getComments());
                                    mModel.setFilePath(ApiClient.PICTURE_SHOW_URL+"Content/Uploads/Photos/QAFindingPhotos/" + listOfPhotos.getPhotoList().get(i).getPath());
                                    AllFilePaths.add(mModel);

                                    rectangular_image.setVisibility(View.VISIBLE);
                                    Picasso.with(Quality_FindingActivity.this).load(mModel.getFilePath())
                                            .placeholder(R.drawable.ic_placeholder)
                                            .networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE).into(rectangular_image);

                                    rectangular_image.setTag(mModel.getFilePath());

                                    photo_list_layout.setVisibility(View.VISIBLE);
                                    ImageAdapter mAdaper = new ImageAdapter(Quality_FindingActivity.this, AllFilePaths, true);
                                    RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Quality_FindingActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    recycle_photos.setLayoutManager(layoutManager2);
                                    mAdaper.quality_setCallBack(Quality_FindingActivity.this);
                                    recycle_photos.setAdapter(mAdaper);
                                }
                            } else {
                                rectangular_image.setVisibility(View.INVISIBLE);
                                resend_btn.setVisibility(View.GONE);
                                //  takephoto.setPadding(20, 10, 30, 10);
                            }
                        } else {
                            Utilities.showToast(Quality_FindingActivity.this, response.body().getStatus());
                            rectangular_image.setVisibility(View.INVISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {

                if (getPhoto_dialog.isShowing()) {
                    getPhoto_dialog.dismiss();
                }
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(Quality_FindingActivity.this, "Please check your internet connection.");
                } else {
                    Utilities.showToast(Quality_FindingActivity.this, t.getMessage());

                }
            }
        });

    }


    private void zoom_CapturedImage(String imagepath) {
        View view = getLayoutInflater().inflate(R.layout.imagefullscreen, null);
        touchImage = (TouchImageView) view.findViewById(R.id.touchImage);
        zoom_remove_photo = (ImageView) view.findViewById(R.id.zoom_remove_photo);
        zoom_comment_text = (TextView) view.findViewById(R.id.zoom_comment_text);
        toucchimage_Dialog = new Dialog(this, R.style.animationdialog);
        toucchimage_Dialog.setContentView(view);
        toucchimage_Dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(toucchimage_Dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; //(int) (Utilities.screenWidth * 0.90)
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER_VERTICAL;
        toucchimage_Dialog.getWindow().setAttributes(lp);

        if (imagepath.contains("http")) {
            Picasso.with(this)
                    .load(imagepath)
                    .resize((int) (Utilities.screenWidth * 0.80), (int) (Utilities.screenHeight * 0.80))
                    .placeholder(R.drawable.ic_placeholder)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(touchImage);
        } else {
            Picasso.with(this).load(new File(imagepath))
                    .resize((int) (Utilities.screenWidth * 0.80), (int) (Utilities.screenHeight * 0.80))
                    .placeholder(R.drawable.ic_placeholder)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(touchImage);
        }

        zoom_remove_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toucchimage_Dialog.dismiss();
            }
        });

        toucchimage_Dialog.show();
    }


    @Override
    public void callsetData1(QAFindingFolioList mModel) {
        if (qafindinglist_popup.isShowing()) {
            qafindinglist_popup.dismiss();
        }
        if (mModel != null) {
            qafinding_folio_selection_text.setText(mModel.getQFFOlioStr());
            qafinding_folio_selection_text.setPaintFlags(qafinding_folio_selection_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            if (mModel.getId() != null) {
                Qa_Folio_id = mModel.getQFFolio();
                RefId = mModel.getId();
                QAFCount = mModel.getQAFCount();
                QAFCount_int = Integer.parseInt(mModel.getQAFCount());
                quality_finding_folio.setVisibility(View.GONE);
                //  quality_finding_folio.setText("QF" + warehouse_code + mModel.getQFFolio()); //"QF" + warehouse_code +
                // quality_finding_folio.dismissDropDown();
                PHOTOS_NUMBER = 000;
                PHOTOS_PATH_NUMBER = 000;
                Utilities.hideKeyboard(quality_finding_folio);
                Utilities.LastPhotoNumber = 000;
                Utilities.LastPhotoPathNumber = 000;
                AllFilePaths.clear();
                addedBy = mModel.getAddedBy();
                getFolioBasedPhoto(mModel.getId());
                Rarecase_Id = mModel.getId();
                getFolioBasedPhoto_ID = mModel.getId();
                Utilities.Resend_button_status = false;
            }
        }
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
                    Utilities.savePref(Quality_FindingActivity.this, "WarehouseName", selected_autocomplete_Warehouse);
                    Utilities.savePref(Quality_FindingActivity.this, "WarehouseId", warehouse_id);
                }
                dialog.dismiss();
            }
        });
        getWareHouse_main();
        dialog.show();
    }

    private void getWareHouse_main() {
        warehouse_dialog = new ProgressDialog(Quality_FindingActivity.this);
        warehouse_dialog.setMessage("Loading...");
        warehouse_dialog.setCancelable(false);
        warehouse_dialog.setCanceledOnTouchOutside(false);

        if (!warehouse_dialog.isShowing()) {
            warehouse_dialog.show();
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("", "");

        userId = Utilities.getPref(Quality_FindingActivity.this, "UserId", "");

        call_Warehouse_main = apiService.getWarehousesList("", userId);
        call_Warehouse_main.enqueue(new Callback<List<Warehouse>>() {
            @Override
            public void onResponse(Call<List<Warehouse>> call, Response<List<Warehouse>> response) {
                if (warehouse_dialog != null && warehouse_dialog.isShowing()) {
                    if (warehouse_dialog.isShowing()) {
                        warehouse_dialog.dismiss();
                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {
                        warehouse_list_main = response.body();
                        if (warehouse_list.size() > 0) {
                            getWarehouse_AutoCompleteText();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Warehouse>> call, Throwable t) {
                if (!Quality_FindingActivity.this.isDestroyed()) {
                    if (warehouse_dialog.isShowing()) {
                        warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(Quality_FindingActivity.this, "Please try again later");
            }
        });
    }

    private void getWarehouse_AutoCompleteText() {
        final List<String> newList = new ArrayList<>();
        for (int i = 0; i < warehouse_list_main.size(); i++) {
            newList.add(warehouse_list_main.get(i).getName());
        }
        WareHouseListAdapter adapter = new WareHouseListAdapter(Quality_FindingActivity.this, warehouse_list_main);
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
        save_warehouse_dialog = new ProgressDialog(Quality_FindingActivity.this);
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
                            Utilities.showToast(Quality_FindingActivity.this, "Wharehouse saved Successfully");
                            // Utilities.showToast(Quality_FindingActivity.this, response.body().toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (!Quality_FindingActivity.this.isDestroyed()) {
                    if (save_warehouse_dialog.isShowing()) {
                        save_warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(Quality_FindingActivity.this, "Please try again later");
            }
        });
    }
}
