package com.example.goods;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class driver_register extends AppCompatActivity {
    private EditText password, cPassword, name, email, mobile, address;
    private ImageView id, DL;
    private TextView pError, cError;
    private String Name, Email, Mobile, Add, Pass, cPass;
    private Uri proofImage;
    private LottieAnimationView load;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);

        name = findViewById(R.id.driverR_name);
        email = findViewById(R.id.driverR_email);
        mobile = findViewById(R.id.driverR_mobile);
        address = findViewById(R.id.driverR_address);
        password = findViewById(R.id.driverR_password);
        cPassword = findViewById(R.id.driverR_Cpassword);

        id = findViewById(R.id.driverR_id);
        DL = findViewById(R.id.driverR_dl);

        pError = findViewById(R.id.driverR_p_error);
        cError = findViewById(R.id.driverR_c_error);

        load = findViewById(R.id.DriverR_loading);

        btn = findViewById(R.id.driverR_save_btn);
    }
    public void loadProof(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(i, "select Image"), 100);
    }
    public void loadDL(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(i, "select Image"), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == 100){
                assert data != null;
                proofImage = data.getData();
                if (proofImage != null)
                    id.setImageURI(proofImage);
            }
            if(requestCode == 200){
                assert data != null;
                Uri dlImage = data.getData();
                if(dlImage != null)
                    DL.setImageURI(dlImage);
            }
        }
    }

    public void save(View view) {
        btn.setVisibility(View.GONE);
        load.setVisibility(View.VISIBLE);

        Name = name.getText().toString();
        Email = email.getText().toString();
        Mobile = mobile.getText().toString();
        Add = address.getText().toString();
        Pass = password.getText().toString();
        cPass = cPassword.getText().toString();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference();
        DatabaseReference myRef = reference.child("Driver");

        myRef.orderByChild("Email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    btn.setVisibility(View.VISIBLE);
                    load.setVisibility(View.GONE);
                    Toast.makeText(driver_register.this, "Email is already registered.",
                            Toast.LENGTH_LONG).show();
                }
                else userNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                btn.setVisibility(View.VISIBLE);
                load.setVisibility(View.GONE);
                Toast.makeText(driver_register.this, "something went wrong. try again after sometime.",
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    private void userNotFound(){
        if(!Pass.equals(cPass) || Pass.length() < 8 || Pass.length() > 16 ||
                (!Pass.matches(".*[a-z].*") || !Pass.matches(".*[A-Z].*") ||
                        !Pass.matches(".*[0-9].*") || !Pass.matches(".*[@#$&].*"))){

            btn.setVisibility(View.VISIBLE);
            load.setVisibility(View.GONE);

            if(!Pass.equals(cPass)) cError.setText("password does not match.");

            if(Pass.length() < 8 || Pass.length() > 16) pError.setText("password must be 8-16 in length.");

            if(!Pass.matches(".*[a-z].*") || !Pass.matches(".*[A-Z].*") ||
                    !Pass.matches(".*[0-9].*") || !Pass.matches(".*[@#$&].*"))
                pError.setText("password must contain at least one(Uppercase, Lowercase, number, special" +
                        " character i.e. @, #, $, &");
        }
        else{
            pError.setText("");
            cError.setText("");

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference reference = db.getReference().child("Driver");
            //DatabaseReference reference1 = db.getReference().child("Driver_image");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //converting image to bitmap.
            BitmapDrawable drawable = (BitmapDrawable) id.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            //compressing and storing the image in bytearray.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] idData =  baos.toByteArray();

            BitmapDrawable drawable1 = (BitmapDrawable) DL.getDrawable();
            Bitmap bitmap1 = drawable1.getBitmap();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dlData = baos.toByteArray();

            Map<String, Object> hs = new HashMap<>();
            hs.put("Name", Name);
            hs.put("Email", Email);
            hs.put("Mobile", Mobile);
            hs.put("Address", Add);
            hs.put("Password", Pass);

            String idImage = Base64.encodeToString(idData, Base64.DEFAULT);
            String dlImage = Base64.encodeToString(dlData, Base64.DEFAULT);
            hs.put("id", idImage);
            hs.put("DL", idImage);

            reference.push().setValue(hs).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                btn.setVisibility(View.VISIBLE);
                                load.setVisibility(View.GONE);
                                Toast.makeText(driver_register.this, "uploaded", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(driver_register.this, driver_login.class));
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    btn.setVisibility(View.VISIBLE);
                    load.setVisibility(View.GONE);
                    Toast.makeText(driver_register.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}