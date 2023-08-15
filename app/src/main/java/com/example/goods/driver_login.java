package com.example.goods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class driver_login extends AppCompatActivity {
    private EditText email, password;
    private TextView email_error, password_error;
    private LottieAnimationView loading;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        email = findViewById(R.id.driverL_email);
        password = findViewById(R.id.driverL_password);
        email_error = findViewById(R.id.driverL_error);
        password_error = findViewById(R.id.driverL_pass_error);
        loading = findViewById(R.id.driverL_loading);
        btn = findViewById(R.id.driverL_login_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });
    }

    public void signIn(View view) {
        Intent intent = new Intent(this, driver_register.class);
        startActivity(intent);
    }
    private void checkUser(){
        btn.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        String sEmail = email.getText().toString();
        String sPassword = password.getText().toString();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference();
        DatabaseReference myRef = reference.child("Driver");

        myRef.orderByChild("Email").equalTo(sEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean userStatus = false;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    String storedPassword = dataSnapshot.child("Password").getValue(String.class);
                    if (storedPassword != null && storedPassword.equals(sPassword)) {
                        userStatus = true;
                        break;
                    }
                }
                if(userStatus) success();
                else failed();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                    password_error.setText("something went wrong. try again after sometime.");
            }
        });
    }
    private void success(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                password_error.setText("");
                Toast.makeText(driver_login.this, "success", Toast.LENGTH_SHORT).show();
            }
        }, 500);
    }
    private void failed(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                password_error.setText("wrong password or email. try again");
            }
        }, 500);
    }

    public void forgot(View view) {
        startActivity(new Intent(this, driver_forgot.class));
    }
}