package com.kireeti.gkpract;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.kireeti.gkpract.networkCall.ApiClient;
import com.kireeti.gkpract.networkCall.ApiInterface;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private int PERMISSION_ALL = 5;
    private Button login_btn;
    EditText emailText, password_editText;
    private ProgressDialog login_Dialog;
    private ApiInterface apiService;
    private Call<JsonObject> login_Call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        apiService = ApiClient.getClient(LoginActivity.this).create(ApiInterface.class);
        findViews();

        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            //startService(new Intent(getBaseContext(), MyLocationService.class));
        }
    }

    private void findViews() {

        emailText = (EditText) findViewById(R.id.emailText);
        password_editText = (EditText) findViewById(R.id.password_editText);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                } else {
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.login_btn:
                callLoginService();
                break;
        }
    }

    private void callLoginService() {
        if (!emailText.getText().toString().equalsIgnoreCase("") && !password_editText.getText().toString().equalsIgnoreCase("")) {
            login_Dialog = new ProgressDialog(LoginActivity.this);
            login_Dialog.setMessage("Authenticating...");
            login_Dialog.setCanceledOnTouchOutside(false);
            login_Dialog.setCancelable(false);
            if (login_Dialog != null && !login_Dialog.isShowing()) {
                login_Dialog.show();
            }
            login_Call = apiService.loginAction(emailText.getText().toString(), Utilities.convertPassMd5(password_editText.getText().toString()));
            login_Call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (login_Dialog.isShowing()) {
                        login_Dialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().get("Status").getAsString().equalsIgnoreCase("Success")) {
                                Utilities.savePref(LoginActivity.this, "UserName", response.body().get("UserName").getAsString());
                                Utilities.savePref(LoginActivity.this, "UserId", response.body().get("UserId").getAsString());
                                Utilities.savePref(LoginActivity.this, "IsQAFUser", response.body().get("IsQAFUser").getAsString());
                                Utilities.savePref(LoginActivity.this, "WarehouseName", response.body().get("WarehouseName").getAsString());
                                Utilities.savePref(LoginActivity.this, "WarehouseId", response.body().get("WarehouseId").getAsString());

                                Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(home);
                                finish();

                                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                                Utilities.isLoged_In = true;
                                Utilities.savebooleanPref(LoginActivity.this, "HasLogged_In", true);
                            } else {
                                if (login_Dialog.isShowing()) {
                                    login_Dialog.dismiss();
                                }
                                Utilities.showToast(LoginActivity.this, "Invalid username or password.");
                            }
                        } else {
                            if (login_Dialog.isShowing()) {
                                login_Dialog.dismiss();
                            }
                            Utilities.showToast(LoginActivity.this, "The username or password is incorrect");
                        }
                    } else {
                        Utilities.showToast(LoginActivity.this, "The username or password is incorrect");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        Utilities.showToast(LoginActivity.this, "Please check your internet connection.");
                        callLoginService();
                    } else {
                        Utilities.showToast(LoginActivity.this, t.getMessage());
                        if (login_Dialog.isShowing()) {
                            login_Dialog.dismiss();
                        }
                    }
                }
            });
        } else {
            Utilities.showToast(LoginActivity.this, "Please enter Credentials");
        }
    }

}
