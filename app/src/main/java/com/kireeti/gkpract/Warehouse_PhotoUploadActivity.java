package com.kireeti.gkpract;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
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

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kireeti.gkpract.adapter.TrailerAdapter;
import com.kireeti.gkpract.adapter.WareHouseListAdapter;
import com.kireeti.gkpract.cameraView.CamActivity;
import com.kireeti.gkpract.customClass.TouchImageView;
import com.kireeti.gkpract.interfaces.WareHouseAuto;
import com.kireeti.gkpract.models.ImageFile;
import com.kireeti.gkpract.models.InventoryDetails;
import com.kireeti.gkpract.models.Trailer_Results;
import com.kireeti.gkpract.models.Valid_Sid;
import com.kireeti.gkpract.networkCall.ApiClient;
import com.kireeti.gkpract.networkCall.ApiInterface;
import com.kireeti.gkpract.models.Warehouse;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

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

public class Warehouse_PhotoUploadActivity extends AppCompatActivity implements View.OnClickListener, WareHouseAuto {

    private Button validate_btn, Scan_btn;
    private EditText sid_editView;
    private IntentIntegrator qrScan;
    private String sId;
    private ProgressDialog validatesid_Dialog;
    private Call<Valid_Sid> validateSid_Call;
    Button takephoto;
    private int REQUEST_CAMERA = 0;
    private int SELECT_IMAGE = 100;
    private File rootDir;
    private RoundedImageView rectangular_image;
    ArrayList<ImageFile> AllFilePaths = new ArrayList<>();
    ArrayList<ImageFile> CapturedImage_Path = new ArrayList<>();
    private ImageView left_arrow, right_arrow;
    LinearLayout photo_gallery, photo_list_layout;
    private RoundedImageView captured_image;
    private LinearLayout.LayoutParams layout;
    private ImageView back_image, lagout_image;
    private HorizontalScrollView horizontal_scrollview;
    private int mWidth;
    private int viewWidth;
    private ApiInterface apiService;
    private Call<Trailer_Results> get_Trailers;
    private String userId;
    private ProgressDialog getTralers_Dialog;
    private TextView container_no_text;
    private TrailerAdapter adapter;
    private Button saveimage_btn;
    private ProgressDialog document_dialog;
    private Call<ResponseBody> call_UploadDoc;
    private String folio_id = "";
    private TouchImageView touchImage;
    private Dialog toucchimage_Dialog;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    File destination = null;
    private ImageView delete_imageview;
    private RelativeLayout main_rl;
    private TextView captured_image_txt;
    String deatil_Id = "";
    private int PHOTOS = 000;
    private ImageView zoom_remove_photo;
    private LinearLayout sid_details_layout;
    private TextView cq_custtomer, box_quantitty, pallet_quantity, total_quantity, location, transfer;
    private LinearLayout expandable_button;
    private ExpandableLayout expandable_layout;
    private TextView sid_text_title;
    private InventoryDetails inventoryDetails;

    private AutoCompleteTextView warehouse_autocomplete;
    private ProgressDialog warehouse_dialog;
    private Call<List<Warehouse>> call_Warehouse;
    private List<Warehouse> warehouse_list = new ArrayList<>();
    private PopupWindow warehuse_popup;
    private String warehouse_id;
    private TextView selction_for_warehouse;
    private ImageView cancel_popup;
    Button save_button;
    private String selected_autocomplete_Warehouse;
    private ProgressDialog save_warehouse_dialog;
    private Call<Integer> call_saveWarehouse;
    private String selected_autocomplete_Warehouse_ID;
    private TextView current_warehouse_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_photo_upload);

        apiService = ApiClient.getClient(Warehouse_PhotoUploadActivity.this).create(ApiInterface.class);


        selected_autocomplete_Warehouse = Utilities.getPref(Warehouse_PhotoUploadActivity.this, "WarehouseName", "");
        selected_autocomplete_Warehouse_ID = Utilities.getPref(Warehouse_PhotoUploadActivity.this, "WarehouseId", "");

        if (!Utilities.getPref(Warehouse_PhotoUploadActivity.this, "UserId", "").equalsIgnoreCase("")) {
            userId = Utilities.getPref(Warehouse_PhotoUploadActivity.this, "UserId", "");
        }

        findViews();
        Display display = getWindowManager().getDefaultDisplay();
        mWidth = display.getWidth(); // deprecated
        viewWidth = mWidth / 3;

        rootDir = new File(Environment.getExternalStorageDirectory() + "/" + "Cargoquin");
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
    }

    private void findViews() {

        validate_btn = (Button) findViewById(R.id.validate_btn);
        validate_btn.setOnClickListener(this);

        Scan_btn = (Button) findViewById(R.id.Scan_btn);
        Scan_btn.setOnClickListener(this);

        expandable_button = (LinearLayout) findViewById(R.id.expandable_button);
        expandable_button.setOnClickListener(this);

        expandable_layout = (ExpandableLayout) findViewById(R.id.expandable_layout);
        sid_text_title = (TextView) findViewById(R.id.sid_text_title);

        selction_for_warehouse = (TextView) findViewById(R.id.selction_for_warehouse);
        selction_for_warehouse.setOnClickListener(this);
        selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));

        sid_details_layout = (LinearLayout) findViewById(R.id.sid_details_layout);
        cq_custtomer = (TextView) findViewById(R.id.cq_custtomer);
        box_quantitty = (TextView) findViewById(R.id.box_quantitty);
        pallet_quantity = (TextView) findViewById(R.id.pallet_quantity);
        total_quantity = (TextView) findViewById(R.id.total_quantity);
        location = (TextView) findViewById(R.id.location);
        transfer = (TextView) findViewById(R.id.transfer);

        sid_editView = (EditText) findViewById(R.id.sid_editView);
        sid_editView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sid_text_title.setText("");
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Utilities.callFromTakePhoto = false;
                    if (!sid_editView.getText().toString().equalsIgnoreCase("")) {
                        validate_Sid_ServiceCall(sid_editView.getText().toString());
                    } else {
                        Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please enter or Scan Sid");
                    }
                }
                return false;
            }
        });

        container_no_text = (TextView) findViewById(R.id.container_no_text);

        photo_list_layout = (LinearLayout) findViewById(R.id.photo_list_layout);

        saveimage_btn = (Button) findViewById(R.id.saveimage_btn);
        saveimage_btn.setOnClickListener(this);

        container_no_text = (TextView) findViewById(R.id.container_no_text);
        horizontal_scrollview = (HorizontalScrollView) findViewById(R.id.horizontal_layout);
        horizontal_scrollview.setHorizontalScrollBarEnabled(false);

        back_image = (ImageView) findViewById(R.id.back_image);
        back_image.setOnClickListener(this);

        lagout_image = (ImageView) findViewById(R.id.lagout_image);
        lagout_image.setOnClickListener(this);

        takephoto = (Button) findViewById(R.id.takephoto);
        takephoto.setOnClickListener(this);

        rectangular_image = (RoundedImageView) findViewById(R.id.rectangular_image);
        rectangular_image.setOnClickListener(this);

        left_arrow = (ImageView) findViewById(R.id.left_arrow);
        left_arrow.setOnClickListener(this);

        right_arrow = (ImageView) findViewById(R.id.right_arrow);
        right_arrow.setOnClickListener(this);

        photo_gallery = (LinearLayout) findViewById(R.id.photo_gallery);

        sid_editView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                deatil_Id = "";

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.expandable_button:
                expandable_layout.toggle();
                break;

            case R.id.left_arrow:
                if (horizontal_scrollview.getChildAt(0) != null) {
                    if (AllFilePaths.size() > 0) {
                        horizontal_scrollview.smoothScrollBy((int) horizontal_scrollview.getScrollY() - viewWidth, (int) horizontal_scrollview.getScrollX());
                        left_arrow.setImageResource(R.drawable.ic_left_blue);
                    } else {
                        left_arrow.setImageResource(R.drawable.ic_left_gray);
                    }
                }
                break;
            case R.id.right_arrow:
                if (horizontal_scrollview.getChildAt(0) != null) {
                    if (AllFilePaths.size() > 0) {
                        horizontal_scrollview.smoothScrollBy((int) horizontal_scrollview.getScrollX() + viewWidth, (int) horizontal_scrollview.getScrollY());
                        right_arrow.setImageResource(R.drawable.ic_right_blue);
                    } else {
                        right_arrow.setImageResource(R.drawable.ic_right_gray);
                    }
                }
                break;
            case R.id.Scan_btn:

                expandable_layout.setVisibility(View.GONE);
                expandable_button.setVisibility(View.GONE);
                sid_editView.setText("");
                qrScan = new IntentIntegrator(Warehouse_PhotoUploadActivity.this);
                qrScan.setOrientationLocked(true);
                qrScan.initiateScan();
                break;

            case R.id.validate_btn:

                break;

            case R.id.takephoto:
                if (deatil_Id.length() > 0) {
                    if (!sid_editView.getText().toString().equalsIgnoreCase("")) {
                        Intent cam = new Intent(Warehouse_PhotoUploadActivity.this, CamActivity.class);
                        startActivityForResult(cam, 0);
                    } else {
                        Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please enter or Scan Sid");

                    }
                } else {
                    Utilities.callFromTakePhoto = true;
                    if (!sid_editView.getText().toString().equalsIgnoreCase("")) {
                        validate_Sid_ServiceCall(sid_editView.getText().toString());
                    } else {
                        Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please enter or Scan Sid");
                    }
                }
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
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.saveimage_btn:
                if (deatil_Id.length() > 0) {
                    if (AllFilePaths.size() > 0) {
                        uploadDocuments();
                    } else {
                        Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please Select or Capture at least one Photo");
                    }
                } else {
                    Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please validate Sid");
                }
                break;

            case R.id.rectangular_image:
                if (AllFilePaths.size() > 0) {
                    if (!rectangular_image.getTag().toString().equalsIgnoreCase("")) {
                        photo_list_layout.setVisibility(View.VISIBLE);
                        zoom_CapturedImage(rectangular_image.getTag().toString());
                    }
                } else {
                    photo_list_layout.setVisibility(View.GONE);
                    Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please Select or Capture at least one Photo");
                }
                break;

            case R.id.selction_for_warehouse:
                show_Popup_Warehouse();
                break;
        }
    }

    private void validate_Sid_ServiceCall(final String sid) {

        validatesid_Dialog = new ProgressDialog(Warehouse_PhotoUploadActivity.this);
        validatesid_Dialog.setMessage("Loading...");
        validatesid_Dialog.setCanceledOnTouchOutside(false);
        validatesid_Dialog.setCancelable(false);
        if (validatesid_Dialog != null && !validatesid_Dialog.isShowing()) {
            validatesid_Dialog.show();
        }
        validateSid_Call = apiService.validateSid(userId, 2, sid);
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
                                Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Validation Successful");

                                expandable_layout.setVisibility(View.VISIBLE);
                                expandable_button.setVisibility(View.VISIBLE);
                                sid_text_title.setText(sid);

                                inventoryDetails = new InventoryDetails();
                                inventoryDetails.setEtiqMaster(response.body().getInventoryDetails().getEtiqMaster());
                                inventoryDetails.setLocationName(response.body().getInventoryDetails().getLocationName());
                                inventoryDetails.setPallets(response.body().getInventoryDetails().getPallets());
                                inventoryDetails.setTotalPieces(response.body().getInventoryDetails().getTotalPieces());
                                inventoryDetails.setWorkOrder(response.body().getInventoryDetails().getWorkOrder());
                                inventoryDetails.setBoxes(response.body().getInventoryDetails().getBoxes());
                                inventoryDetails.setId(response.body().getInventoryDetails().getId());
                            }
                            transfer.setText(inventoryDetails.getEtiqMaster());
                            location.setText(inventoryDetails.getLocationName());
                            pallet_quantity.setText(inventoryDetails.getPallets());
                            total_quantity.setText(inventoryDetails.getTotalPieces());
                            cq_custtomer.setText(inventoryDetails.getWorkOrder());
                            box_quantitty.setText(inventoryDetails.getBoxes());

                            if (Utilities.callFromTakePhoto) {
                                deatil_Id = inventoryDetails.getId();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, REQUEST_CAMERA);
                            } else {
                                deatil_Id = inventoryDetails.getId();
                                folio_id = sid_editView.getText().toString();
                            }

                        } else {
                            showAlert("Alert", "Please enter Correct SId");
                        }
                    } else {
                        showAlert("Alert", "Please enter Correct SId");
                    }

                } else {
                    Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please try again later");
                }

            }

            @Override
            public void onFailure(Call<Valid_Sid> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please check your internet connection.");
                    if (validatesid_Dialog != null && validatesid_Dialog.isShowing()) {
                        validatesid_Dialog.dismiss();
                    }
                } else {
                    Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please check your internet connection.");
                    Utilities.showToast(Warehouse_PhotoUploadActivity.this, t.getMessage());
                    if (validatesid_Dialog != null && validatesid_Dialog.isShowing()) {
                        validatesid_Dialog.dismiss();
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {
                addImageToRecycleView(data, requestCode);
            } else {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() == null) {
                        Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Result Not Found");
                    } else {
                        sId = result.getContents();

                        if (!sId.equalsIgnoreCase("")) {
                            sid_editView.setText(sId);
                            Utilities.callFromTakePhoto = false;
                            validate_Sid_ServiceCall(sId.trim());
                            PHOTOS = 000;
                        }
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private void addImageToRecycleView(Intent data, int requestCode) {


        Bitmap thumbnail = null;
        if (requestCode == 0) {
            thumbnail = BitmapFactory.decodeFile(String.valueOf(data.getExtras().get("data")));
        } else {
            thumbnail = (Bitmap) data.getExtras().get("data");
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        ImageFile bolObj = new ImageFile();


        if (PHOTOS == 000) {
            destination = new File(rootDir, folio_id + "_001" + ".jpg");
        } else {
            DecimalFormat formatter = new DecimalFormat("000");
            String s = formatter.format(PHOTOS + 1);
            destination = new File(rootDir, folio_id + "_" + (s) + ".jpg");
        }
       /* if (Utilities.getPref(Warehouse_PhotoUploadActivity.this, "Warehs_Last_photo_number", "").equalsIgnoreCase("")) {
            destination = new File(rootDir, folio_id + "_001" + ".jpg");
            bolObj.setFileName("Warehouse_" + folio_id + "_001" + ".jpg");
            Utilities.savePref(Warehouse_PhotoUploadActivity.this, "Warehs_Last_photo_number", "001");
        } else {
            DecimalFormat formatter = new DecimalFormat("000");
            String current_Photo_Number = formatter.format(Integer.parseInt(Utilities.getPref(Warehouse_PhotoUploadActivity.this, "Warehs_Last_photo_number", "")) + 1);
            Utilities.savePref(Warehouse_PhotoUploadActivity.this, "Warehs_Last_photo_number", current_Photo_Number);
            destination = new File(rootDir, folio_id + "_" + (current_Photo_Number) + ".jpg");
            bolObj.setFileName("Warehouse_" + folio_id + "_" + (current_Photo_Number) + ".jpg");
        }*/

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
        String bol_filePath = destination.getAbsolutePath();
        bolObj.setFilePath(bol_filePath);

        if (PHOTOS == 000) {
            bolObj.setFileName("Warehouse_" + folio_id + "_001" + ".jpg");
        } else {
            DecimalFormat formatter = new DecimalFormat("000");
            String s = formatter.format(PHOTOS + 1);
            bolObj.setFileName("Warehouse_" + folio_id + "_" + (s) + ".jpg");

        }

        CapturedImage_Path.add(bolObj);
        PHOTOS = PHOTOS + CapturedImage_Path.size();
        AllFilePaths.addAll(CapturedImage_Path);
        CapturedImage_Path.clear();

        if (AllFilePaths.size() > 0) {
            photo_list_layout.setVisibility(View.VISIBLE);
        } else {
            photo_list_layout.setVisibility(View.GONE);

        }
        Picasso.with(Warehouse_PhotoUploadActivity.this).load(destination)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(rectangular_image);

        final LinearLayout it = new LinearLayout(this);
        it.setOrientation(LinearLayout.VERTICAL);

        captured_image = new RoundedImageView(this);

        LinearLayout.LayoutParams img_Param = new LinearLayout.LayoutParams((int) (Utilities.screenWidth * 0.35), (int) (Utilities.screenWidth * 0.25));
        img_Param.setMargins(10, 10, 10, 10);
        captured_image.setLayoutParams(img_Param);
        captured_image.setScaleType(ImageView.ScaleType.FIT_XY);
        captured_image.setRadius(30);
        captured_image.setImageBitmap(thumbnail);
        it.setTag(bolObj.getFilePath());
        it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AllFilePaths.size() > 0) {
                    if (!view.getTag().toString().equalsIgnoreCase("")) {
                        zoom_CapturedImage(view.getTag().toString());
                    }
                } else {
                    Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please Select or Capture at least one Photo");

                }
            }
        });

        main_rl = new RelativeLayout(this);

        LinearLayout.LayoutParams it_param = new LinearLayout.LayoutParams
                ((int) (Utilities.screenWidth * 0.36), LinearLayout.LayoutParams.WRAP_CONTENT);
        main_rl.setLayoutParams(it_param);

        captured_image_txt = new TextView(this);
        captured_image_txt.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams tex_Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tex_Param.setMargins(15, 5, 5, 0);
        captured_image_txt.setTextColor(R.color.color_black);
        captured_image_txt.setLayoutParams(tex_Param);
        captured_image_txt.setTextSize(10);
        captured_image_txt.setText(bolObj.getFileName());
        delete_imageview = new ImageView(this);
        delete_imageview.setBackgroundResource(R.drawable.ic_cancel_btn);

        it.addView(captured_image);
        it.addView(captured_image_txt);
        delete_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < photo_gallery.getChildCount(); i++) {
                    if (view.getTag().toString().equals(photo_gallery.getChildAt(i).getTag().toString())) {
                        photo_gallery.removeViewAt(i);
                    }
                }
            }
        });
        main_rl.addView(it);
        RelativeLayout.LayoutParams relative_param = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relative_param.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.ALIGN_PARENT_RIGHT);
        relative_param.setMargins(10, 20, 10, 0);

        delete_imageview.setLayoutParams(relative_param);
        main_rl.addView(delete_imageview);

        delete_imageview.setTag(bolObj.getFileName());
        main_rl.setTag(bolObj.getFileName());
        photo_gallery.addView(main_rl);
    }

    public String getPath(Uri uri, String type) {
        String document_id = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        }
        String path = "";
        if (type.equalsIgnoreCase("Photo")) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(uri,
                    filePathColumn, null, null, null);
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        } else {
            cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Video.Media._ID + " = ? ", new String[]{document_id}, null);
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                cursor.close();
            }
        }

        if (path.equalsIgnoreCase("")) {
            path = document_id;
        }

        return path;
    }

    private void uploadDocuments() {
        File file = null;
        RequestBody reqFile = null;
        MultipartBody.Part body = null;
        List<MultipartBody.Part> multipleFiles = new ArrayList<>();
        for (int i = 0; i < AllFilePaths.size(); i++) {
            file = new File(AllFilePaths.get(i).getFilePath());
            reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData(AllFilePaths.get(i).getFileName(), AllFilePaths.get(i).getFileName(), reqFile);
            multipleFiles.add(body);

        }

        document_dialog = new ProgressDialog(Warehouse_PhotoUploadActivity.this);
        document_dialog.setMessage("Uploading...");
        document_dialog.setCancelable(false);
        document_dialog.setCanceledOnTouchOutside(false);

        if (!document_dialog.isShowing()) {
            document_dialog.show();
        }
        call_UploadDoc = apiService.uploadMultipleFiles(userId, 3, deatil_Id, multipleFiles);
        call_UploadDoc.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!Warehouse_PhotoUploadActivity.this.isDestroyed()) {
                    if (document_dialog != null && document_dialog.isShowing()) {
                        if (!Warehouse_PhotoUploadActivity.this.isFinishing()) {
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
                                Utilities.showToast(Warehouse_PhotoUploadActivity.this, jsonObj.getString(("message")));

                            } else if (jsonObj.getString(("Status")).equalsIgnoreCase("Success")) {
                                Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Success");
                                //  sid_details_layout.setVisibility(View.GONE);
                                Utilities.saveButton = false;
                                finish();
                            } else {
                                Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please try again later");
                            }
                            AllFilePaths.clear();
                            photo_gallery.removeAllViews();
                            container_no_text.setText("");
                            container_no_text.setVisibility(View.GONE);
                            rectangular_image.setImageResource(android.R.color.transparent);

                        } catch (IOException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please try again later");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!Warehouse_PhotoUploadActivity.this.isDestroyed()) {
                    if (document_dialog != null && document_dialog.isShowing()) {
                        document_dialog.dismiss();
                    }
                }
                Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please try again later");

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilities.saveButton) {
            Intent cam = new Intent(Warehouse_PhotoUploadActivity.this, CamActivity.class);
            startActivityForResult(cam, 0);

            selected_autocomplete_Warehouse = Utilities.getPref(Warehouse_PhotoUploadActivity.this, "WarehouseName", "");
            selected_autocomplete_Warehouse_ID = Utilities.getPref(Warehouse_PhotoUploadActivity.this, "WarehouseId", "");
            selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));

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
        lp.copyFrom(toucchimage_Dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;//(int) (Utilities.screenHeight * 0.85);
        lp.gravity = Gravity.CENTER_VERTICAL;
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

    private void showAlert(String title_text, String message_text) {
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
                container_no_text.setText("");
                sid_editView.setText("");
            }
        });

        title.setText(title_text);
        message.setText(message_text);
        dialog.show();
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
                    Utilities.savePref(Warehouse_PhotoUploadActivity.this, "WarehouseName", selected_autocomplete_Warehouse);
                    Utilities.savePref(Warehouse_PhotoUploadActivity.this, "WarehouseId", warehouse_id);
                }
                dialog.dismiss();
            }
        });
        getWareHouse();

        dialog.show();

    }


    private void getWareHouse() {
        warehouse_dialog = new ProgressDialog(Warehouse_PhotoUploadActivity.this);
        warehouse_dialog.setMessage("Loading...");
        warehouse_dialog.setCancelable(false);
        warehouse_dialog.setCanceledOnTouchOutside(false);

        if (!warehouse_dialog.isShowing()) {
            warehouse_dialog.show();
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("", "");
        userId=Utilities.getPref(Warehouse_PhotoUploadActivity.this, "UserId", "");

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
                if (!Warehouse_PhotoUploadActivity.this.isDestroyed()) {
                    if (warehouse_dialog.isShowing()) {
                        warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please try again later");
            }
        });
    }

    private void getWarehouse_AutoCompleteText() {
        final List<String> newList = new ArrayList<>();
        for (int i = 0; i < warehouse_list.size(); i++) {
            newList.add(warehouse_list.get(i).getName());
        }
        WareHouseListAdapter adapter = new WareHouseListAdapter(Warehouse_PhotoUploadActivity.this, warehouse_list);
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
        save_warehouse_dialog = new ProgressDialog(Warehouse_PhotoUploadActivity.this);
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
                             Utilities.showToast(Warehouse_PhotoUploadActivity.this,"Wharehouse saved Successfully");
                          //  Utilities.showToast(Warehouse_PhotoUploadActivity.this, response.body().toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (!Warehouse_PhotoUploadActivity.this.isDestroyed()) {
                    if (save_warehouse_dialog.isShowing()) {
                        save_warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(Warehouse_PhotoUploadActivity.this, "Please try again later");
            }
        });
    }


}
