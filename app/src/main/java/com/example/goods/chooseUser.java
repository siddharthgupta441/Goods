package com.example.goods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class chooseUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
    }

    public void cRedirect(View view) {
        finish();
        Intent intent = new Intent(this, costumer_login.class);
        startActivity(intent);
    }

    public void dRedirect(View view) {
        finish();
        Intent intent = new Intent(this, driver_login.class);
        startActivity(intent);
    }
}