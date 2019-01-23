package com.kireeti.gkpract.cameraView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kireeti.gkpract.R;
import com.kireeti.gkpract.Utilities;
import com.kireeti.gkpract.networkCall.ApiClient;
import com.kireeti.gkpract.networkCall.ApiInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;

public class CamActivity_QualityFinding extends Activity {

    private final int RC_GET_PICTURE = 301;
    private CamPreview mPreview;
    private RelativeLayout previewParent;
    private LinearLayout blackTop;
    private LinearLayout blackBottom;
    private ImageButton ibFlash;
    private ImageButton ibGrid;
    private ImageView ivGridLines;
    private LinearLayout llGallery;
    private TextView textCamera;
    private SeekBar sbZoom;
    private CaptureUtils captureUtils;
    private boolean flashEnabled = true;
    private int activeCamera = 0;
    private OrientationEventListener orientationListener;
    private int screenOrientation = 90;
    private int THRESHOLD = 30;
    public static boolean isClicked = false;
    private ImageButton ibFlipCamera;
    private View preview_view;
    private Dialog mPreview_dialog;
    private ImageView thumnail_image;
    private Button cancel_btn, ok_btn;
    Button imageButton1;
    private TextView individual_image_coment_text;
    private ProgressDialog nextAvailableQAFinding_dialog;

    private ApiInterface apiService;
    private Call<JsonObject> Call_nextAvailableQAFinding;
    private String Qa_Folio_id;
    private Button next_pitcure_btn;
    private String Prev_comment;
    private Button text_clear_button;


    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide status-bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide title-bar, must be before setContentView
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cam_quality_finding);
        apiService = ApiClient.getClient(CamActivity_QualityFinding.this).create(ApiInterface.class);
        InitControls();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    void InitControls() {

        previewParent = (RelativeLayout) findViewById(R.id.rlPreview);
        blackTop = (LinearLayout) findViewById(R.id.llBlackTop);
        blackBottom = (LinearLayout) findViewById(R.id.llBlackBottom);
        llGallery = (LinearLayout) findViewById(R.id.llGallery);

        ibFlash = (ImageButton) findViewById(R.id.ibFlash);
        ibGrid = (ImageButton) findViewById(R.id.ibGrid);
        ivGridLines = (ImageView) findViewById(R.id.ivGridLines);
        ibFlipCamera = (ImageButton) findViewById(R.id.ibFlipCamera);
        sbZoom = (SeekBar) findViewById(R.id.sbZoom);

        imageButton1 = (Button) findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!isClicked) {
                        captureUtils.takeShot(jpegCallback);
                        isClicked = true;
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });


        if (Camera.getNumberOfCameras() > 1) {
            ibFlipCamera.setVisibility(View.VISIBLE);
        } else {
            ibFlipCamera.setVisibility(View.GONE);
        }
        orientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_UI) {
            public void onOrientationChanged(int orientation) {
                if (isOrientation(orientation, 0))
                    screenOrientation = 0;
                else if (isOrientation(orientation, 90))
                    screenOrientation = 90;
                else if (isOrientation(orientation, 180))
                    screenOrientation = 180;
                else if (isOrientation(orientation, 270))
                    screenOrientation = 270;


            }
        };
    }


    protected boolean isOrientation(int orientation, int degree) {
        return (degree - THRESHOLD <= orientation && orientation <= degree + THRESHOLD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClicked = false;
//        checkLocationEnabled();
        int currentapiVersion = Build.VERSION.SDK_INT;
        orientationListener.enable();

        setupCamera(activeCamera);

        if (sbZoom.getVisibility() == View.VISIBLE) {
            sbZoom.setProgress(0);
        }

        showLatestPhoto();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
        orientationListener.disable();
    }


    private void setupCamera(final int camera) {
        // Set the second argument by your choice.
        // Usually, 0 for back-facing camera, 1 for front-facing camera.
        // If the OS is pre-gingerbreak, this does not have any effect.
        try {
            mPreview = new CamPreview(this, camera, CamPreview.LayoutMode.NoBlank);// .FitToParent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot Connect to Camera", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // Un-comment below lines to specify the size.

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        previewLayoutParams.height = width;
        previewLayoutParams.width = width;

        // Un-comment below line to specify the position.
        mPreview.setCenterPosition(width / 2, height / 2);
        Camera mCamera = mPreview.getCamera();
        Camera.Parameters parametro = mCamera.getParameters();
        parametro.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(parametro);

        previewParent.addView(mPreview, 0, previewLayoutParams);

        // there is changes in calculations
        // camera preview image centered now to have actual image at center of
        // view
        int delta = height - width;
        int btHeight = 0;// blackTop.getHeight();
        int fix = delta - btHeight;
        int fix2 = 0;// fix / 4;

        FrameLayout.LayoutParams blackBottomParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT/*fix / 2 + fix2*/);
        blackBottomParams.gravity = Gravity.BOTTOM;
        blackBottom.setLayoutParams(blackBottomParams);

        FrameLayout.LayoutParams blackTopParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT/*fix / 2 - fix2*/);
        blackTopParams.gravity = Gravity.TOP;
        blackTop.setLayoutParams(blackTopParams);

        captureUtils = new CaptureUtils(mPreview.getCamera());
        if (captureUtils.isCameraFlashAvailable()) {
            captureUtils.toggleFlash(flashEnabled);
            captureUtils.enableTorch(true);
            ibFlash.setVisibility(View.VISIBLE);
            ibFlash.setImageResource(flashEnabled ? R.drawable.ic_flash_on : R.drawable.ic_flash_off);
        } else {

            ibFlash.setVisibility(View.GONE);
        }
        mPreview.setOnZoomCallback(new CamPreview.ZoomCallback() {
            @Override
            public void onZoomChanged(int progress) {
                sbZoom.setProgress(progress);
            }
        });
        sbZoom.setVisibility(captureUtils.hasAutofocus() ? View.VISIBLE : View.GONE);

        sbZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (mPreview != null) {
                    mPreview.onProgressChanged(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (mPreview != null && seekBar != null) {
                    mPreview.onStopTrackingTouch(seekBar.getProgress());
                }
            }
        });
    }

    private void releaseCamera() {
        if (mPreview != null) {
            mPreview.stop();
            previewParent.removeView(mPreview); // This is necessary.
            mPreview = null;
        }
    }

    @SuppressLint("NewApi")
    public void flipClick(View view) {
        if (Build.VERSION.SDK_INT < 9)
            return;

        if (Camera.getNumberOfCameras() > 1) {

            activeCamera = activeCamera == 0 ? 1 : 0;
            releaseCamera();
            setupCamera(activeCamera);
        }
    }

    public void flashClick(View view) {
        if (!captureUtils.isCameraFlashAvailable())
            return;
        flashEnabled = !flashEnabled;
        captureUtils.toggleFlash(flashEnabled);
        ibFlash.setImageResource(flashEnabled ? R.drawable.ic_flash_on : R.drawable.ic_flash_off);
    }


    public void selectFromGallery(View view) {
        Intent iGetAvatar = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(iGetAvatar, RC_GET_PICTURE);
    }


    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream outStream = null;

            try {
                System.gc();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap finalBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(finalBitmap);
                canvas.drawBitmap(bitmap, 0f, 0f, null);

                String FILENAME = "tmp" + System.currentTimeMillis() + ".jpg";
                outStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.close();
                resetCam();

                String path = getFilesDir() + "/" + FILENAME;
                int orientation = 0;
                int fix = 1;

                if (activeCamera == 0) {
                    ExifInterface ei = new ExifInterface(path);
                    orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    ei.setAttribute(ExifInterface.TAG_ORIENTATION, "90");
                    ei.saveAttributes();
                } else {
                    ExifInterface ei = new ExifInterface(path);
                    orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    ei.setAttribute(ExifInterface.TAG_ORIENTATION, "0");
                    ei.saveAttributes();
                    orientation = ExifInterface.ORIENTATION_ROTATE_270;
                    fix = -1;
                }

                switch (orientation) {
                    case ExifInterface.ORIENTATION_UNDEFINED:
                        BmpUtils.rotateBitmap(path, normalizeRot(90 + screenOrientation));
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        BmpUtils.rotateBitmap(path, normalizeRot(90 + screenOrientation));
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        BmpUtils.rotateBitmap(path, normalizeRot(180 + screenOrientation));
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        BmpUtils.rotateBitmap(path, normalizeRot(270 + fix * screenOrientation));
                        break;
                    case ExifInterface.ORIENTATION_NORMAL:
                        BmpUtils.rotateBitmap(path, normalizeRot(screenOrientation));
                        break;
                }

                selectCategory(path);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }
    };

    protected void selectCategory(final String path) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        preview_view = inflater.inflate(R.layout.clicked_item_qty_finding, null);
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CamActivity_QualityFinding.this);

        mPreview_dialog = new Dialog(this);
        mPreview_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mPreview_dialog.setContentView(preview_view);
        mPreview_dialog.setCanceledOnTouchOutside(false);
        mPreview_dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mPreview_dialog.getWindow().getAttributes());
        lp.width = (int) (Utilities.screenWidth * 0.95);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mPreview_dialog.getWindow().setAttributes(lp);

        text_clear_button=(Button)preview_view.findViewById(R.id.text_clear_button);
        thumnail_image = (ImageView) preview_view.findViewById(R.id.thumnail_image);
        cancel_btn = (Button) preview_view.findViewById(R.id.cancel_btn);
        ok_btn = (Button) preview_view.findViewById(R.id.ok_btn);
        next_pitcure_btn = (Button) preview_view.findViewById(R.id.next_pitcure_btn);
        individual_image_coment_text = (TextView) preview_view.findViewById(R.id.individual_image_coment_text);
        individual_image_coment_text.setImeOptions(EditorInfo.IME_ACTION_DONE);
        individual_image_coment_text.setRawInputType(InputType.TYPE_CLASS_TEXT);

        thumnail_image.setImageURI(Uri.parse(path));

        if(Utilities.getPref(CamActivity_QualityFinding.this,"Prev_comment","")!=null) {
            individual_image_coment_text.setText(Utilities.getPref(CamActivity_QualityFinding.this,"Prev_comment",""));
        }else {
            individual_image_coment_text.setText(Prev_comment);
        }

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prev_comment=individual_image_coment_text.getText().toString();
                mPreview_dialog.dismiss();
                Utilities.savePref(CamActivity_QualityFinding.this, "Prev_comment", Prev_comment);
                Intent iSelect = new Intent();
                iSelect.putExtra("data", path);
                iSelect.putExtra("comment", individual_image_coment_text.getText().toString());
                setResult(RESULT_OK, iSelect);
                Utilities.nextPitcure = false;
                finish();
                Utilities.saveButton = true;
                Utilities.show_Existing_folio_Text_Change=true;
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreview_dialog.dismiss();
                isClicked = false;
                releaseCamera();
                setupCamera(0);
                Utilities.saveButton = true;
                Utilities.show_Existing_folio_Text_Change=true;
            }
        });
        next_pitcure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prev_comment=individual_image_coment_text.getText().toString();
                Utilities.savePref(CamActivity_QualityFinding.this, "Prev_comment", Prev_comment);
                Utilities.nextPitcure = true;
                Intent iSelect = new Intent();
                iSelect.putExtra("data", path);
                iSelect.putExtra("comment", individual_image_coment_text.getText().toString());
                setResult(RESULT_OK, iSelect);
                finish();
                Utilities.saveButton = true;
                Utilities.show_Existing_folio_Text_Change=true;
            }
        });

        text_clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individual_image_coment_text.setText("");
            }
        });

        mPreview_dialog.show();
    }

    protected void resetCam() {
        Camera camera = mPreview.getCamera();
        camera.startPreview();
        showLatestPhoto();
    }

    protected int normalizeRot(int rot) {
        if (rot < 0)
            rot += 360;
        if (rot > 360)
            rot -= 360;
        return rot;
    }


    /**
     * Returns how much we have to rotate
     */
    public int rotationForImage(Uri uri) {
        try {
            if (uri.getScheme().equals("content")) {
                // From the media gallery
                String[] projection = {Images.ImageColumns.ORIENTATION};
                Cursor c = getContentResolver().query(uri, projection, null, null, null);
                if (c.moveToFirst()) {
                    return c.getInt(0);
                }
            } else if (uri.getScheme().equals("file")) {
                // From a file saved by the camera
                ExifInterface exif = new ExifInterface(uri.getPath());
                int rotation = (int) exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL));
                return rotation;
            }
            return 0;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get rotation in degrees
     */
    private static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private void showLatestPhoto() {
        String[] projection = new String[]{Images.ImageColumns._ID, Images.ImageColumns.DATA,
                Images.ImageColumns.BUCKET_DISPLAY_NAME, Images.ImageColumns.DATE_TAKEN, Images.ImageColumns.MIME_TYPE};
        @SuppressWarnings("deprecation") final Cursor cursorE = managedQuery(Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, Images.ImageColumns.DATE_TAKEN
                + " DESC");

        @SuppressWarnings("deprecation") final Cursor cursorI = managedQuery(Images.Media.INTERNAL_CONTENT_URI, projection, null, null, Images.ImageColumns.DATE_TAKEN
                + " DESC");

        String imageLocation = null;
        long udate = 0;

        if (cursorE.moveToFirst()) {
            udate = cursorE.getLong(3);
            imageLocation = cursorE.getString(1);
        }

        if (cursorI.moveToFirst()) {
            long iudate = cursorI.getLong(3);
            if (iudate > udate)
                imageLocation = cursorI.getString(1);
        }

        if (imageLocation == null)
            return;

        final ImageView imageView = (ImageView) findViewById(R.id.ivGallery);
        int rot = rotationForImage(Uri.parse("file://" + imageLocation));
        File imageFile = new File(imageLocation);
        if (imageFile.exists()) {


            Bitmap bm = BmpUtils.getResampledBitmap(imageLocation, 100);
            if (rot != 0)
                bm = BmpUtils.rotateBitmap(bm, rot, bm.getHeight(), bm.getHeight());
            imageView.setImageBitmap(bm);
        }
    }

    public void gridClick(View v) {
        boolean visible = ivGridLines.isShown();
        ivGridLines.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }
}
