package com.example.goods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

public class chooseUser extends AppCompatActivity {
    private LottieAnimationView cLoading, dLoading;
    private Button btn_cutomer, btn_driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
        cLoading = findViewById(R.id.chooseUser_cLoading);
        dLoading = findViewById(R.id.chooseUser_dLoading);
        btn_cutomer = findViewById(R.id.btn_customer);
        btn_driver = findViewById(R.id.btn_driver);
    }

    public void cRedirect(View view) {
        btn_cutomer.setVisibility(View.GONE);
        cLoading.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_cutomer.setVisibility(View.VISIBLE);
                cLoading.setVisibility(View.GONE);
                Intent intent = new Intent(chooseUser.this, costumer_login.class);
                startActivity(intent);
            }
        }, 500);
    }

    public void dRedirect(View view) {
        btn_driver.setVisibility(View.GONE);
        dLoading.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_driver.setVisibility(View.VISIBLE);
                dLoading.setVisibility(View.GONE);
                Intent intent = new Intent(chooseUser.this, driver_login.class);
                startActivity(intent);
            }
        }, 500);
    }
}