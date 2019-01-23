package com.kireeti.gkpract;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Utilities.callMove = false;
        Utilities.callPopBackStack = false;

        DisplayMetrics dm = new DisplayMetrics();
        SplashActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Utilities.screenWidth = dm.widthPixels;
        Utilities.screenHeight = dm.heightPixels;


        new Thread() {

            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(1000);
                       if(Utilities.getBooleanPref(SplashActivity.this, "HasLogged_In", false)){
                            Intent login = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(login);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }else {
                           Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                           startActivity(login);
                           finish();
                           overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                       }
                    }
                } catch (InterruptedException ex) {

                }
            }
        }.start();

    }


}
