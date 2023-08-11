package com.example.goods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class driver_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
    }

    public void signIn(View view) {
        Intent intent = new Intent(this, driver_register.class);
        startActivity(intent);
    }
}