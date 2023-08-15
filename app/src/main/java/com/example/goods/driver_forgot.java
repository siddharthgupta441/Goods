package com.example.goods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class driver_forgot extends AppCompatActivity {
    private EditText email;
    private TextView text;
    private LottieAnimationView loading;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_forgot);
        email = findViewById(R.id.driverF_email);
        text = findViewById(R.id.driverF_text);
        loading = findViewById(R.id.driverF_loading);
        btn = findViewById(R.id.driverF_btn);
    }

    public void reset(View view) {
        String Email = email.getText().toString();

        btn.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    text.setText("reset link has been sent to your email.");
                    loading.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(driver_forgot.this, "email not found.", Toast.LENGTH_SHORT).show();
                    btn.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }
}