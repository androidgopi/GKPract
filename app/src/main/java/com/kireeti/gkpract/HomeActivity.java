package com.kireeti.gkpract;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.kireeti.gkpract.adapter.WareHouseListAdapter;
import com.kireeti.gkpract.interfaces.WareHouseAuto;
import com.kireeti.gkpract.models.UserScreens;
import com.kireeti.gkpract.models.Warehouse;
import com.kireeti.gkpract.networkCall.ApiClient;
import com.kireeti.gkpract.networkCall.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, WareHouseAuto {

    private MenuItem editmenu;
    private LinearLayout receiving_layout, damaged_layout, warehouse_layout, shipping_layout, qualityfinding_layout;
    private TextView username_txt;
    private Toolbar toolbar;
    ApiInterface apiService;
    private ProgressDialog userScreen_Diolog;
    private Call<List<UserScreens>> get_UserScreens;
    private List<UserScreens> get_UserScreens_list;
    String id;
    private LinearLayout notThere_layout;
    private LinearLayout gate_pass_layout;
    private TextView selction_for_warehouse;
    private ImageView cancel_popup;
    private Button save_button;
    private View customView;
    private Dialog warehouse_Dialog;
    private AutoCompleteTextView warehouse_autocomplete;
    private ProgressDialog warehouse_dialog;
    private Call<List<Warehouse>> call_Warehouse;
    List<Warehouse> warehouse_list = new ArrayList<>();
    private PopupWindow warehuse_popup;
    private String warehouse_id;
    private String selected_autocomplete_Warehouse;
    private String selected_autocomplete_Warehouse_ID;
    private TextView current_warehouse_text;
    private ProgressDialog save_warehouse_dialog;
    private Call<Integer> call_saveWarehouse;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        apiService = ApiClient.getClient(HomeActivity.this).create(ApiInterface.class);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      //  id = Utilities.getPref(HomeActivity.this, "UserId", "");

        findViews();
        selected_autocomplete_Warehouse = Utilities.getPref(HomeActivity.this, "WarehouseName", "");
        selected_autocomplete_Warehouse_ID = Utilities.getPref(HomeActivity.this, "WarehouseId", "");
        if (!Utilities.getPref(HomeActivity.this, "UserName", "").equalsIgnoreCase("")) {
            username_txt.setText(Utilities.getPref(HomeActivity.this, "UserName", ""));
            userId=Utilities.getPref(HomeActivity.this, "UserId", "");
        }

        getUserScreen();
    }

    private void getWareHouse() {
        warehouse_dialog = new ProgressDialog(HomeActivity.this);
        warehouse_dialog.setMessage("Loading...");
        warehouse_dialog.setCancelable(false);
        warehouse_dialog.setCanceledOnTouchOutside(false);

        if (!warehouse_dialog.isShowing()) {
            warehouse_dialog.show();
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("", "");

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
                if (!HomeActivity.this.isDestroyed()) {
                    if (warehouse_dialog.isShowing()) {
                        warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(HomeActivity.this, "Please try again later");
            }
        });
    }


    private void getUserScreen() {
        userScreen_Diolog = new ProgressDialog(HomeActivity.this);
        userScreen_Diolog.setMessage("Loading...");
        userScreen_Diolog.setCancelable(false);
        userScreen_Diolog.setCanceledOnTouchOutside(false);

        if (!userScreen_Diolog.isShowing()) {
            userScreen_Diolog.show();
        }

        get_UserScreens = apiService.getUserScreens(userId, "en-Us", "4");
        get_UserScreens.enqueue(new Callback<List<UserScreens>>() {
            @Override
            public void onResponse(Call<List<UserScreens>> call, Response<List<UserScreens>> response) {

                if (userScreen_Diolog.isShowing()) {
                    userScreen_Diolog.dismiss();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        get_UserScreens_list = response.body();
                        if (get_UserScreens_list.size() > 0) {

                            for (int i = 0; i < get_UserScreens_list.size(); i++) {

                                if (get_UserScreens_list.get(i).getScreenName() != null
                                        && !get_UserScreens_list.get(i).getScreenName().equalsIgnoreCase("")) {
                                    switch (get_UserScreens_list.get(i).getScreenName()) {
                                        case "Receiving":
                                            receiving_layout = (LinearLayout) findViewById(R.id.receiving_layout);
                                            receiving_layout.setVisibility(View.VISIBLE);
                                            break;

                                        case "Damaged":
                                            damaged_layout = (LinearLayout) findViewById(R.id.damaged_layout);
                                            damaged_layout.setVisibility(View.VISIBLE);
                                            break;

                                        case "Warehouse":
                                            warehouse_layout = (LinearLayout) findViewById(R.id.warehouse_layout);
                                            warehouse_layout.setVisibility(View.VISIBLE);
                                            break;

                                        case "Shipping":
                                            shipping_layout = (LinearLayout) findViewById(R.id.shipping_layout);
                                            shipping_layout.setVisibility(View.VISIBLE);
                                            break;

                                        case "Quality Findings":
                                            qualityfinding_layout = (LinearLayout) findViewById(R.id.qualityfinding_layout);
                                            qualityfinding_layout.setVisibility(View.VISIBLE);
                                            break;

                                        case "NOT THERE":
                                            notThere_layout = (LinearLayout) findViewById(R.id.notThere_layout);
                                            notThere_layout.setVisibility(View.VISIBLE);
                                            break;

                                        case "Gate Pass":
                                            gate_pass_layout = (LinearLayout) findViewById(R.id.gate_pass_layout);
                                            gate_pass_layout.setVisibility(View.VISIBLE);
                                            Utilities.SecondGatePassAPI=false;
                                            break;
                                    }
                                }
                            }
                        } else {
                            Utilities.savebooleanPref(getApplicationContext(), "HasLogged_In", false);
                            Utilities.showToast(HomeActivity.this, "User does not have any screen(s) permission(s) for this APP ");
                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserScreens>> call, Throwable t) {

                if (!HomeActivity.this.isDestroyed()) {
                    if (userScreen_Diolog.isShowing()) {
                        userScreen_Diolog.dismiss();
                    }
                }
                Utilities.showToast(HomeActivity.this, "Please try again later");
            }
        });
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(null);
        setSupportActionBar(toolbar);
        displayActionBar();
        ActionBar avy = getSupportActionBar();
        if (avy != null) {
            avy.setTitle(null);
        }
        selction_for_warehouse = (TextView) findViewById(R.id.selction_for_warehouse);
        selction_for_warehouse.setOnClickListener(this);
        selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));

        toolbar.setNavigationIcon(null);
        receiving_layout = (LinearLayout) findViewById(R.id.receiving_layout);
        receiving_layout.setOnClickListener(this);

        damaged_layout = (LinearLayout) findViewById(R.id.damaged_layout);
        damaged_layout.setOnClickListener(this);

        warehouse_layout = (LinearLayout) findViewById(R.id.warehouse_layout);
        warehouse_layout.setOnClickListener(this);

        shipping_layout = (LinearLayout) findViewById(R.id.shipping_layout);
        shipping_layout.setOnClickListener(this);

        qualityfinding_layout = (LinearLayout) findViewById(R.id.qualityfinding_layout);
        qualityfinding_layout.setOnClickListener(this);

        notThere_layout = (LinearLayout) findViewById(R.id.notThere_layout);
        notThere_layout.setOnClickListener(this);

        gate_pass_layout = (LinearLayout) findViewById(R.id.gate_pass_layout);
        gate_pass_layout.setOnClickListener(this);

        username_txt = (TextView) findViewById(R.id.username_txt);

    }

    public void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        editmenu = menu.findItem(R.id.logout);
        editmenu.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Utilities.savebooleanPref(getApplicationContext(), "HasLogged_In", false);
                Intent returnTOLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(returnTOLogin);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.receiving_layout:
                Utilities.saveButton = false;
                Intent rev_in = new Intent(HomeActivity.this, Receiving_Photo_UploadActivity.class);
                startActivity(rev_in);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                break;

            case R.id.shipping_layout:
                Utilities.saveButton = false;
                Intent ship_in = new Intent(HomeActivity.this, Shipping_Photo_UploadActivity.class);
                startActivity(ship_in);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                break;

            case R.id.damaged_layout:
                Utilities.saveButton = false;
                Intent damaged_in = new Intent(HomeActivity.this, Damaged_PhotoUploadActivity.class);
                startActivity(damaged_in);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                break;

            case R.id.warehouse_layout:
                Utilities.saveButton = false;
                Intent warehouse_in = new Intent(HomeActivity.this, Warehouse_PhotoUploadActivity.class);
                startActivity(warehouse_in);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                break;

            case R.id.qualityfinding_layout:
                Intent quality_finding = new Intent(HomeActivity.this, Quality_FindingActivity.class);
                startActivity(quality_finding);
                Utilities.saveButton = false;
                Utilities.show_Existing_folio_Text_Change = false;
                Utilities.qafindingFolioPopup = false;
                Utilities.savePref(HomeActivity.this, "Prev_comment", "");
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                break;

            case R.id.notThere_layout:
                Intent notThere = new Intent(HomeActivity.this, NotThereActivity.class);
                startActivity(notThere);
                Utilities.saveButton = false;
                Utilities.Folio_List_Clicked = false;
                Utilities.AutoCompltete_Clicked = false;
                Utilities.Pitcure_captured_NotThere = false;
                break;

            case R.id.gate_pass_layout:
                Intent i = new Intent(HomeActivity.this, GatePassActivity.class);
                startActivity(i);
                Utilities.saveButton = false;
                Utilities.Loaded_Out_Photoes=false;
                Utilities.Pitcure_captured_NotThere = false;
                break;

            case R.id.selction_for_warehouse:
                show_Popup_Warehouse();
                break;

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
                    Utilities.savePref(HomeActivity.this, "WarehouseName", selected_autocomplete_Warehouse);
                    Utilities.savePref(HomeActivity.this, "WarehouseId", warehouse_id);
                }
                dialog.dismiss();
            }
        });
        getWareHouse();

        dialog.show();

    }

    private void getWarehouse_AutoCompleteText() {

        final List<String> newList = new ArrayList<>();
        for (int i = 0; i < warehouse_list.size(); i++) {
            newList.add(warehouse_list.get(i).getName());
        }
        WareHouseListAdapter adapter = new WareHouseListAdapter(HomeActivity.this, warehouse_list);
        warehouse_autocomplete.setThreshold(1);
        adapter.setCallBack(this);
        warehouse_autocomplete.setAdapter(adapter);
    }

    @Override
    public void wareHouseAuto(Warehouse mModel) {
        warehouse_id = mModel.getId();
        selected_autocomplete_Warehouse = mModel.getName();
        warehouse_autocomplete.setText(mModel.getName());

    }

    @Override
    protected void onResume() {
        super.onResume();

        selected_autocomplete_Warehouse = Utilities.getPref(HomeActivity.this, "WarehouseName", "");
        selected_autocomplete_Warehouse_ID = Utilities.getPref(HomeActivity.this, "WarehouseId", "");

        selction_for_warehouse.setText(Html.fromHtml("<u>" + selected_autocomplete_Warehouse + "</u>"));

    }

    private void saveWarehouse(String wharehouse_id) {
        save_warehouse_dialog = new ProgressDialog(HomeActivity.this);
        save_warehouse_dialog.setMessage("Loading...");
        save_warehouse_dialog.setCancelable(false);
        save_warehouse_dialog.setCanceledOnTouchOutside(false);

        if (!save_warehouse_dialog.isShowing()) {
            save_warehouse_dialog.show();
        }

        call_saveWarehouse = apiService.saveWarehouse(wharehouse_id,userId);
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
                       if(Integer.parseInt(response.body().toString())>0) {
                           //  Utilities.showToast(HomeActivity.this,"Wharehouse saved Successfully");
                           Utilities.showToast(HomeActivity.this, "Wharehouse saved Successfully");
                       }
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (!HomeActivity.this.isDestroyed()) {
                    if (save_warehouse_dialog.isShowing()) {
                        save_warehouse_dialog.dismiss();
                    }
                }
                Utilities.showToast(HomeActivity.this, "Please try again later");
            }
        });
    }

}
