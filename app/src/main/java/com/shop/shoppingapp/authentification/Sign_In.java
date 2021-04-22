package com.shop.shoppingapp.authentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shop.shoppingapp.R;

public class Sign_In extends AppCompatActivity {

    Button Sign_up , Sign_in ;
    EditText ePassword , eEmail ;
    String email , password ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in);

        Sign_up = findViewById(R.id.SignUp);
        Sign_in = findViewById(R.id.SignIn);
        ePassword = findViewById(R.id.password);
        eEmail = findViewById(R.id.email);


        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = eEmail.getText().toString().trim();
                password = ePassword.getText().toString().trim();

                if ( ! email.isEmpty() && ! password.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           Intent intent = new Intent(Sign_In.this , Sign_up.class);
                           startActivity(intent);
                           finish();
                       }
                        }
                    });
                }

            }
        });

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sign_In.this, com.shop.shoppingapp.authentification.Sign_up.class);
                startActivity(intent);
                finish();

            }
        });

    }
}