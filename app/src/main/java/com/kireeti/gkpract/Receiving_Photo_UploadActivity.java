package com.kireeti.gkpract;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.JsonObject;
import com.kireeti.gkpract.adapter.ImageAdapter;
import com.kireeti.gkpract.adapter.TrailerAdapter;
import com.kireeti.gkpract.adapter.WareHouseListAdapter;
import com.kireeti.gkpract.cameraView.CamActivity;
import com.kireeti.gkpract.customClass.TouchImageView;
import com.kireeti.gkpract.interfaces.DeletePhoto;
import com.kireeti.gkpract.interfaces.SelectedPholioId;
import com.kireeti.gkpract.interfaces.WareHouseAuto;
import com.kireeti.gkpract.models.ImageFile;
import com.kireeti.gkpract.models.PhotoResults;
import com.kireeti.gkpract.models.TrailerFolios;
import com.kireeti.gkpract.models.Trailer_Results;
import com.kireeti.gkpract.models.Warehouse;
import com.kireeti.gkpract.networkCall.ApiClient;
import com.kireeti.gkpract.networkCall.ApiInterface;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

public class Receiving_Photo_UploadActivity extends AppCompatActivity implements View.OnClickListener, SelectedPholioId,
        DeletePhoto, WareHouseAuto {
    Button takephoto;
    private int REQUEST_CAMERA = 0;
    private int SELECT_IMAGE = 100;
    private File rootDir;
    private RoundedImageView rectangular_image;
    ArrayList<ImageFile> AllFilePaths = new ArrayList<>();

    ArrayList<ImageFile> CapturedImage_Path = new ArrayList<>();
    private ImageView left_arrow, right_arrow;
    LinearLayout photo_list_layout;
    private RoundedImageView captured_image;
    private ImageView back_image, lagout_image;
    private int mWidth;
    private int viewWidth;
    private ApiInterface apiService;
    private Call<Trailer_Results> get_Trailers;
    private String userId;
    private AutoCompleteTextView trailer_folio_autocomplete;
    private ProgressDialog getTralers_Dialog;
    private TextView container_no_text;
    private TrailerAdapter adapter;
    private Button saveimage_btn;
    private ProgressDialog document_dialog;
    private Call<JsonObject> call_UploadDoc;
    private String folio_id, id;
    private ArrayList<TrailerFolios> TrailerFolios = new ArrayList<TrailerFolios>();
    private TouchImageView touchImage;
    private Dialog toucchimage_Dialog;
    File destination = null;
    private ImageView delete_imageview;
    private RelativeLayout main_rl;
    private TextView captured_image_txt;
    private int PHOTOS_NUMBER = 000;
    private int PHOTOS_PATH_NUMBER = 000;
    private Call<PhotoResults> get_FolioPhotos;
    RecyclerView receiving_photos;
    PhotoResults listOfPhotos = new PhotoResults();
    private ProgressDialog getPhoto_dialog;
    private ProgressDialog delete_Dialog;
    private Call<JsonObject> delete_Picture;
    private ImageView zoom_remove_photo;

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
    private String selected_autocomplete_Warehouse_ID;
    private TextView current_warehouse_text;
    private ProgressDialog save_warehouse_dialog;
    private Call<Integer> call_saveWarehouse;

   private List<MultipartBody.Part> multipleFiles;
    public static String TAG="REciving Responce";
    private SimpleDateFormat format;
    private SimpleDateFormat format1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);

        Date today = new Date();
        format = new SimpleDateFormat("hh:mm:ss.SSS");
       // "yyyy-MM-dd HH:mm:ss.SSS"
         format.format(today);


        Date today1 = new Date();
        format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        // "yyyy-MM-dd HH:mm:ss.SSS"
        format1.format(today1);

        apiService = ApiClient.getClient(Receiving_Photo_UploadActivity.this).create(ApiInterface.class);
        Display display = getWindowManager().getDefaultDisplay();
        mWidth = display.getWidth(); // deprecated
        viewWidth = mWidth / 3;

        selected_autocomplete_Warehouse = Utilities.getPref(Receiving_Photo_UploadActivity.this, "WarehouseName", "");
        selected_autocomplete_Warehouse_ID = Utilities.getPref(Receiving_Photo_UploadActivity.this, "WarehouseId", "");

        findViews();

        rootDir = new File(Environment.getExternalStorageDirectory() + "/" + "Cargoquin");
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void findViews() {
        photo_list_layout = (LinearLayout) findViewById(R.id.photo_list_layout);

        saveimage_btn = (Button) findViewById(R.id.saveimage_btn);
        saveimage_btn.setOnClickListener(this);

        container_no_text = (TextView) findViewById(R.id.container_no_text);

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

        selction_for_warehouse = (TextView) findViewById(R.id.selction_for_warehouse);
        selction_for_warehouse.setOnClickListener(this);
        selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));

        receiving_photos = (RecyclerView) findViewById(R.id.receiving_photos);
        trailer_folio_autocomplete = (AutoCompleteTextView) findViewById(R.id.trailer_folio_autocomplete);
        trailer_folio_autocomplete.setThreshold(1);

        trailer_folio_autocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (trailer_folio_autocomplete.getText().toString().length() == 0) {

                    AllFilePaths.clear();
                    container_no_text.setText("");
                    container_no_text.setVisibility(View.GONE);
                    photo_list_layout.setVisibility(View.GONE);
                    rectangular_image.setImageResource(android.R.color.transparent);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!Utilities.getPref(Receiving_Photo_UploadActivity.this, "UserId", "").equalsIgnoreCase("")) {
            userId = Utilities.getPref(Receiving_Photo_UploadActivity.this, "UserId", "");
        }

        getTrailers();
    }

    private void getTrailers() {
        getTralers_Dialog = new ProgressDialog(Receiving_Photo_UploadActivity.this);
        getTralers_Dialog.setMessage("Loading...");
        getTralers_Dialog.setCanceledOnTouchOutside(false);
        getTralers_Dialog.setCancelable(false);
        if (getTralers_Dialog != null && !getTralers_Dialog.isShowing()) {
            getTralers_Dialog.show();
        }


        get_Trailers = apiService.getTrailers(userId, 1, "");
        get_Trailers.enqueue(new Callback<Trailer_Results>() {
            @Override
            public void onResponse(Call<Trailer_Results> call, Response<Trailer_Results> response) {
                if (getTralers_Dialog.isShowing()) {
                    getTralers_Dialog.dismiss();
                }

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("Success")) {
                        if (response.body().getTrailerList().size() > 0) {
                            TrailerFolios = response.body().getTrailerList();
                            if (TrailerFolios.size() > 0) {
                                getTraiolrFolio_AutoCompleteText(TrailerFolios);
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Trailer_Results> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please check your internet connection.");
                    if (getTralers_Dialog.isShowing()) {
                        getTralers_Dialog.dismiss();
                    }

                } else {
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, t.getMessage());
                    if (getTralers_Dialog.isShowing()) {
                        getTralers_Dialog.dismiss();
                    }
                }

            }
        });
    }

    private void getTraiolrFolio_AutoCompleteText(ArrayList<TrailerFolios> trailers) {
        final List<String> newList = new ArrayList<>();
        for (int i = 0; i < trailers.size(); i++) {
            newList.add(trailers.get(i).getContainer());
        }

        TrailerAdapter adapter = new TrailerAdapter(Receiving_Photo_UploadActivity.this, trailers);
        trailer_folio_autocomplete.setThreshold(1);
        trailer_folio_autocomplete.setText("");
        adapter.setCallBack(this);
        trailer_folio_autocomplete.setAdapter(adapter);
        PHOTOS_NUMBER = 000;
        PHOTOS_PATH_NUMBER = 000;
        rectangular_image.setVisibility(View.INVISIBLE);
    }

//    private void getFolio_AutoCompleteText(List<Trailer_Results> director) {
//        final List<String> newList = new ArrayList<>();
//        for (int i = 0; i < director.size(); i++) {
//            newList.add(director.get(i).ge);
//        }
//        AutoListAdapter adapter = new AutoListAdapter(ReceiptEntryActivity.this, director);
//        agmtSeries.setThreshold(1);
//        agmtSeries.setText("");
//        adapter.setCallBack(this);
//        agmtSeries.setAdapter(adapter);
//    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.takephoto:
                if (container_no_text.getText().length() > 0) {
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);*/
                    Intent cam = new Intent(Receiving_Photo_UploadActivity.this, CamActivity.class);
                    startActivityForResult(cam, 0);

                } else {
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please Select Trailer Number");
                }
                break;

            case R.id.left_arrow:
                if (receiving_photos.getChildAt(0) != null) {
                    if (AllFilePaths.size() > 0) {
                        receiving_photos.smoothScrollBy((int) receiving_photos.getScrollY() - receiving_photos.getChildAt(0).getWidth(), (int) receiving_photos.getScrollX());
                        left_arrow.setImageResource(R.drawable.ic_left_blue);

                    } else {
                        left_arrow.setImageResource(R.drawable.ic_left_gray);
                    }
                }

                break;

            case R.id.right_arrow:
                if (receiving_photos.getChildAt(0) != null) {
                    if (AllFilePaths.size() > 0) {
                        receiving_photos.smoothScrollBy((int) receiving_photos.getScrollX() + receiving_photos.getChildAt(0).getWidth(), (int) receiving_photos.getScrollY());
                        right_arrow.setImageResource(R.drawable.ic_right_blue);
                    } else {
                        right_arrow.setImageResource(R.drawable.ic_right_gray);
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
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.saveimage_btn:
                if (AllFilePaths.size() > 0) {
                    uploadDocuments();
                } else {
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please Select or Capture at least one Photo");
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
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please Select or Capture at least one Photo");

                }
                break;

            case R.id.selction_for_warehouse:
                show_Popup_Warehouse();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {

                    addImageToRecycleView(data, requestCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            //    Bitmap bitmapImage = BitmapFactory.decodeFile("Your path");
            int nh = (int) (thumbnail.getHeight() * (512.0 / thumbnail.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(thumbnail, 512, nh, true);
            // your_imageview.setImageBitmap(scaled);


            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            ImageFile bolObj = new ImageFile();

            if (Utilities.LastPhotoPathNumber == 0) {
                if (PHOTOS_PATH_NUMBER == 000) {
                    destination = new File(rootDir, folio_id + "_001" + ".jpg");
                    PHOTOS_PATH_NUMBER = 001;
                } else {
                    DecimalFormat formatter = new DecimalFormat("000");
                    PHOTOS_PATH_NUMBER = PHOTOS_PATH_NUMBER + 1;
                    String s = formatter.format(PHOTOS_PATH_NUMBER);
                    destination = new File(rootDir, folio_id + "_" + (s) + ".jpg");
                }
            } else {
                DecimalFormat formatter = new DecimalFormat("000");
                Utilities.LastPhotoPathNumber = Utilities.LastPhotoPathNumber + 1;
                String s = formatter.format(Utilities.LastPhotoPathNumber + 1);
                destination = new File(rootDir, folio_id + "_" + (s) + ".jpg");
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
                    bolObj.setFileName("Receiving_" + folio_id + "_001" + ".jpg");
                    PHOTOS_NUMBER = 001;
                } else {
                    DecimalFormat formatter = new DecimalFormat("000");
                    PHOTOS_NUMBER = PHOTOS_NUMBER + 1;
                    String s = formatter.format(PHOTOS_NUMBER);
                    bolObj.setFileName("Receiving_" + folio_id + "_" + (s) + ".jpg");
                }
            } else {
                DecimalFormat formatter = new DecimalFormat("000");
                Utilities.LastPhotoNumber = Utilities.LastPhotoNumber + 1;
                String s = formatter.format(Utilities.LastPhotoNumber);
                bolObj.setFileName("Receiving_" + folio_id + "_" + (s) + ".jpg");
            }

            CapturedImage_Path.add(bolObj);

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

            rectangular_image.setVisibility(View.VISIBLE);
            Picasso.with(Receiving_Photo_UploadActivity.this).load(destination)
                    .placeholder(R.drawable.ic_placeholder)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(rectangular_image);

            AllFilePaths.add(bolObj);
            ImageAdapter mAdaper = new ImageAdapter(Receiving_Photo_UploadActivity.this, AllFilePaths, false);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Receiving_Photo_UploadActivity.this, LinearLayoutManager.HORIZONTAL, false);
            receiving_photos.setLayoutManager(layoutManager2);
            mAdaper.setCallBack(Receiving_Photo_UploadActivity.this);
            receiving_photos.setAdapter(mAdaper);
            mAdaper.notifyDataSetChanged();
        } else {
            Utilities.showToast(Receiving_Photo_UploadActivity.this, "Capture Picture Properlly");
        }
    }

    private void uploadDocuments() {
      //  Log.i(TAG, "Reciived_Start Time for progressbar : " +  format.format(System.currentTimeMillis()));
        writeToFile(" " +  "/n");
        writeToFile("Toatl Images: " +  CapturedImage_Path.size());
        writeToFile("Reciived_Start Time for progressbar: " +  format1.format(System.currentTimeMillis()));
        final long startTime = System.currentTimeMillis();
        document_dialog = new ProgressDialog(Receiving_Photo_UploadActivity.this);
        document_dialog.setMessage("Uploading...");
        document_dialog.setCancelable(false);
        document_dialog.setCanceledOnTouchOutside(false);

        if (!document_dialog.isShowing()) {
            document_dialog.show();
        }
       // Log.i(TAG, "Reciived_Showing for progressbar : " + format.format(System.currentTimeMillis()));
        writeToFile("Reciived_Showing for progressbar: " + format.format(System.currentTimeMillis()));
        call_UploadDoc = apiService.uploadMultipleFiles1(userId, 1, id, multipleFiles);
      //  Log.i(TAG, "Reciived_Send data to server : " + format.format(System.currentTimeMillis()));
        writeToFile("Reciived_Send data to server: " + format.format(System.currentTimeMillis()));
        call_UploadDoc.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!Receiving_Photo_UploadActivity.this.isDestroyed()) {
                //    Log.i(TAG, "Reciived_Revive the Responce  : " + format.format(System.currentTimeMillis()));
                    long difference = System.currentTimeMillis() - startTime;
                    writeToFile("Reciived_Revive the Responce: " + format.format(System.currentTimeMillis()));
                  long  time_taken= difference/1000;

                    if(time_taken>60){
                        long different_min= time_taken/60;
                        writeToFile("Time Taken: " + different_min +" min");
                    }else{
                        writeToFile("Time Taken: " + time_taken +" Sec");
                    }

                    if (document_dialog != null && document_dialog.isShowing()) {
                        if (!Receiving_Photo_UploadActivity.this.isFinishing()) {
                            document_dialog.dismiss();
                        }
                    }
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                     //   Log.i(TAG, "Reciived_Succes Message Reciverd from Server  : " + format.format(System.currentTimeMillis()));
                        writeToFile("Reciived_Success Message Reciverd from Server: " + format.format(System.currentTimeMillis()));
                        if (response.body().get("Status").getAsString().equalsIgnoreCase("Fail")) {
                            Utilities.showToast(Receiving_Photo_UploadActivity.this, response.body().get("message").getAsString());
                        } else if (response.body().get("Status").getAsString().equalsIgnoreCase("Success")) {
                            Utilities.showToast(Receiving_Photo_UploadActivity.this, "Success");
                            Utilities.saveButton = false;
                                finish();
                            } else {
                                Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please try again later");
                            }
                            CapturedImage_Path.clear();
                            AllFilePaths.clear();
                            container_no_text.setText("");
                            container_no_text.setVisibility(View.GONE);
                            trailer_folio_autocomplete.setText("");

                    }
                } else {
               //     Log.i(TAG, "Reciived_responce Null : " + format.format(System.currentTimeMillis()));
                    writeToFile("Reciived_responce Null: " + format.format(System.currentTimeMillis()));
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please try again later");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    if (document_dialog != null && document_dialog.isShowing()) {
                        document_dialog.dismiss();
                    }
                 //   Log.i(TAG, "Reciived_Internet check: " + format.format(System.currentTimeMillis()));
                    writeToFile("Reciived_Internet check: " + format.format(System.currentTimeMillis()));
                    long difference = System.currentTimeMillis() - startTime;
                    writeToFile("Time Taken: " + difference/1000 +" Sec");
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please check your internet connection.");

                } else {
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, t.getMessage());
                    if (document_dialog != null && document_dialog.isShowing()) {
                        document_dialog.dismiss();
                    }
                 //   Log.i(TAG, "Reciived_failes API: " + format.format(System.currentTimeMillis()));
                    long difference = System.currentTimeMillis() - startTime;
                    writeToFile("Time Taken: " + difference/1000 +" Sec");
                    writeToFile("Reciived_failes API: " + format.format(System.currentTimeMillis()));
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please try again later");
                }
            }

        });

    }

    private static void writeToFile(String message) {
        File logFile = new File("sdcard/CQ Stage_log.file");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(message);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zoom_CapturedImage(String imagepath) {

        View view = getLayoutInflater().inflate(R.layout.imagefullscreen, null);
        touchImage = (TouchImageView) view.findViewById(R.id.touchImage);
        zoom_remove_photo = (ImageView) view.findViewById(R.id.zoom_remove_photo);
        toucchimage_Dialog = new Dialog(this, R.style.animationdialog);
        toucchimage_Dialog.setContentView(view);
        toucchimage_Dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(toucchimage_Dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
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
    public void selectPholio(TrailerFolios obj) {
        folio_id = obj.getTrailerFolio();
        id = obj.getId();
        container_no_text.setVisibility(View.VISIBLE);
        container_no_text.setText("Trailer Folio : " + obj.getTrailerFolio());
        trailer_folio_autocomplete.setText(obj.getContainer());
        trailer_folio_autocomplete.dismissDropDown();
        PHOTOS_NUMBER = 000;
        PHOTOS_PATH_NUMBER = 000;
        getFolioBasedPhoto(obj.getId());
        Utilities.hideKeyboard(trailer_folio_autocomplete);
        Utilities.LastPhotoNumber = 000;
        Utilities.LastPhotoPathNumber = 000;
        AllFilePaths.clear();
    }

    private void getFolioBasedPhoto(String id) {

        getPhoto_dialog = new ProgressDialog(Receiving_Photo_UploadActivity.this);
        getPhoto_dialog.setMessage("Loading...");
        getPhoto_dialog.setCancelable(false);
        getPhoto_dialog.setCanceledOnTouchOutside(false);

        if (!getPhoto_dialog.isShowing()) {
            getPhoto_dialog.show();
        }

        get_FolioPhotos = apiService.getFolioPhotos(id, 1);
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
                                listOfPhotos.setPhotoList(response.body().getPhotoList());
                                AllFilePaths.clear();
                                for (int i = 0; i < listOfPhotos.getPhotoList().size(); i++) {

                                    ImageFile mModel = new ImageFile();
                                    mModel.setId(listOfPhotos.getPhotoList().get(i).getId());
                                    mModel.setFileName(listOfPhotos.getPhotoList().get(i).getPath().split("\\.")[0] + ".jpg");
                                    mModel.setFilePath(ApiClient.PICTURE_SHOW_URL+"Content/Uploads/Photos/ReceviedPhotos/" + listOfPhotos.getPhotoList().get(i).getPath());

                                    AllFilePaths.add(mModel);

                                    rectangular_image.setVisibility(View.VISIBLE);
                                    Picasso.with(Receiving_Photo_UploadActivity.this).load(mModel.getFilePath())
                                            .placeholder(R.drawable.ic_placeholder)
                                            .networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE).into(rectangular_image);

                                    rectangular_image.setTag(mModel.getFilePath());

                                    photo_list_layout.setVisibility(View.VISIBLE);
                                    ImageAdapter mAdaper = new ImageAdapter(Receiving_Photo_UploadActivity.this, AllFilePaths, true);
                                    RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Receiving_Photo_UploadActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    receiving_photos.setLayoutManager(layoutManager2);
                                    mAdaper.setCallBack(Receiving_Photo_UploadActivity.this);
                                    receiving_photos.setAdapter(mAdaper);

                                }
                            } else {
                                rectangular_image.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Utilities.showToast(Receiving_Photo_UploadActivity.this, response.body().getStatus());
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
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please check your internet connection.");
                } else {
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, t.getMessage());

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilities.saveButton) {
            Intent cam = new Intent(Receiving_Photo_UploadActivity.this, CamActivity.class);
            startActivityForResult(cam, 0);
        }
        selected_autocomplete_Warehouse = Utilities.getPref(Receiving_Photo_UploadActivity.this, "WarehouseName", "");
        selected_autocomplete_Warehouse_ID = Utilities.getPref(Receiving_Photo_UploadActivity.this, "WarehouseId", "");

        selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));
    }

    @Override
    public void deletePhoto(int position, ImageFile mModel) {

        if (mModel.getId() != null && !mModel.getId().equalsIgnoreCase("")) {
            deletePhoto_Service(mModel.getId(), position, mModel.getFileName());
        } else {

            if(CapturedImage_Path.contains(AllFilePaths.get(position))){
                CapturedImage_Path.remove(AllFilePaths.get(position));
            }
            AllFilePaths.remove(position);

            ImageAdapter mAdaper = new ImageAdapter(Receiving_Photo_UploadActivity.this, AllFilePaths, false);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Receiving_Photo_UploadActivity.this, LinearLayoutManager.HORIZONTAL, false);
            receiving_photos.setLayoutManager(layoutManager2);
            mAdaper.setCallBack(Receiving_Photo_UploadActivity.this);
            receiving_photos.setAdapter(mAdaper);
            mAdaper.notifyDataSetChanged();
        }

        if (position > 0) {
            rectangular_image.setTag(AllFilePaths.get(position - 1).getFilePath());
            if (AllFilePaths.get(position - 1).getFilePath().contains("https")) {
                Picasso.with(Receiving_Photo_UploadActivity.this)
                        .load(AllFilePaths.get(position - 1).getFilePath())
                        .placeholder(R.drawable.ic_placeholder)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(rectangular_image);
            } else {

                if (AllFilePaths.size() > 0) {
                    Picasso.with(Receiving_Photo_UploadActivity.this)
                            .load(new File(AllFilePaths.get(position - 1).getFilePath()))
                            .placeholder(R.drawable.ic_placeholder)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(rectangular_image);
                } else {
                    rectangular_image.setVisibility(View.INVISIBLE);

                }
            }
        } else {
            rectangular_image.setVisibility(View.INVISIBLE);
        }

    }

    public void deletePhoto_Service(String id, final int position, String filenamne) {
        delete_Dialog = new ProgressDialog(Receiving_Photo_UploadActivity.this);
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

                        Utilities.showToast(Receiving_Photo_UploadActivity.this, response.body().get("Status").getAsString());

                        AllFilePaths.remove(position);
                        ImageAdapter mAdaper = new ImageAdapter(Receiving_Photo_UploadActivity.this, AllFilePaths, false);
                        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Receiving_Photo_UploadActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        receiving_photos.setLayoutManager(layoutManager2);
                        mAdaper.setCallBack(Receiving_Photo_UploadActivity.this);
                        receiving_photos.setAdapter(mAdaper);
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
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please check your internet connection.");
                } else {
                    Utilities.showToast(Receiving_Photo_UploadActivity.this, t.getMessage());
                }
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
                    Utilities.savePref(Receiving_Photo_UploadActivity.this, "WarehouseName", selected_autocomplete_Warehouse);
                    Utilities.savePref(Receiving_Photo_UploadActivity.this, "WarehouseId", warehouse_id);
                }
                dialog.dismiss();
            }
        });
        getWareHouse();

        dialog.show();

    }


    private void getWareHouse() {
        warehouse_dialog = new ProgressDialog(Receiving_Photo_UploadActivity.this);
        warehouse_dialog.setMessage("Loading...");
        warehouse_dialog.setCancelable(false);
        warehouse_dialog.setCanceledOnTouchOutside(false);

        if (!warehouse_dialog.isShowing()) {
            warehouse_dialog.show();
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("", "");

        userId=Utilities.getPref(Receiving_Photo_UploadActivity.this, "UserId", "");

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
                if (!Receiving_Photo_UploadActivity.this.isDestroyed()) {
                    if (warehouse_dialog.isShowing()) {
                        warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please try again later");
            }
        });
    }

    private void getWarehouse_AutoCompleteText() {

        final List<String> newList = new ArrayList<>();
        for (int i = 0; i < warehouse_list.size(); i++) {
            newList.add(warehouse_list.get(i).getName());
        }
        WareHouseListAdapter adapter = new WareHouseListAdapter(Receiving_Photo_UploadActivity.this, warehouse_list);
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
        save_warehouse_dialog = new ProgressDialog(Receiving_Photo_UploadActivity.this);
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
                            Utilities.showToast(Receiving_Photo_UploadActivity.this, "Wharehouse saved Successfully");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (!Receiving_Photo_UploadActivity.this.isDestroyed()) {
                    if (save_warehouse_dialog.isShowing()) {
                        save_warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(Receiving_Photo_UploadActivity.this, "Please try again later");
            }
        });
    }

}
