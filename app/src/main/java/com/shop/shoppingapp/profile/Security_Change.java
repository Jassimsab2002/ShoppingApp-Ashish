package com.shop.shoppingapp.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.shop.shoppingapp.R;

public class Security_Change extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Button bGetEmail ;
    SharedPreferences sharedPreferences ;
    String sEmail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security__change);

        //findViews
        bGetEmail = findViewById(R.id.button);

        //getData
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        sEmail = sharedPreferences.getString("email","null");

        //setOnClicks
        bGetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(sEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Security_Change.this, "Email Sent !", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Security_Change.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}