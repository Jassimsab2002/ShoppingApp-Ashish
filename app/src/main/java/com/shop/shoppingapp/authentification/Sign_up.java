package com.shop.shoppingapp.authentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shop.shoppingapp.MainActivity;
import com.shop.shoppingapp.R;

import java.util.HashMap;

public class Sign_up extends AppCompatActivity {


    Button Sign_in , Sign_up ;
    EditText eEmail , ePassword , eConfirmPassword , eName , eAdresse ;
    String sEmail , sPassword , sConfirmPassword , sName , sAdresse ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Sign_in = findViewById(R.id.SignUp);
        Sign_up = findViewById(R.id.SignIn);
        eEmail = findViewById(R.id.email);
        ePassword = findViewById(R.id.password);
        eConfirmPassword = findViewById(R.id.confirm_password);
        eName = findViewById(R.id.name);
        eAdresse = findViewById(R.id.Adress);

        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sEmail = eEmail.getText().toString().trim();
                sPassword = ePassword.getText().toString().trim();
                sConfirmPassword = eConfirmPassword.getText().toString().trim();
                sName = eName.getText().toString().trim();
                sAdresse = eAdresse.getText().toString().trim();

                if ( ! sEmail.isEmpty() && ! sPassword.isEmpty() && ! sConfirmPassword.isEmpty() && ! sName.isEmpty() && ! sAdresse.isEmpty()) {
                    if (sPassword.equals(sConfirmPassword)) {
                        firebaseAuth.createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    editor.putString("name",sName);
                                    editor.putString("email",sEmail);
                                    editor.putString("address",sAdresse);
                                    editor.apply();
                                    HashMap<String,Object> hashMap = new HashMap<>();
                                    hashMap.put("Name",sName);
                                    hashMap.put("Address",sAdresse);
                                    hashMap.put("Uid",firebaseAuth.getUid());
                                    firestore.collection("Users").document(firebaseAuth.getUid()).set(hashMap);
                                    Intent intent = new Intent(Sign_up.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Sign_up.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                    }
                }
            }
        });

        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_up.this , Sign_In.class);
                startActivity(intent);
                finish();
            }
        });



    }
}