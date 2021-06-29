package com.shop.shoppingapp.authentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.shop.shoppingapp.MainActivity;
import com.shop.shoppingapp.R;

public class Sign_In extends AppCompatActivity {

    Button Sign_up , Sign_in ;
    EditText ePassword , eEmail ;
    String email , password ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in);

        Sign_up = findViewById(R.id.SignUp);
        Sign_in = findViewById(R.id.SignIn);
        ePassword = findViewById(R.id.password);
        eEmail = findViewById(R.id.email);
        progressBar = findViewById(R.id.progress_bar);


        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                email = eEmail.getText().toString().trim();
                password = ePassword.getText().toString().trim();

                if ( ! email.isEmpty() && ! password.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                       if (task.isSuccessful()){
                           Intent intent = new Intent(Sign_In.this , MainActivity.class);
                           startActivity(intent);
                           finish();
                       }else{
                           try {
                               throw task.getException();
                           } catch(FirebaseAuthWeakPasswordException e) {
                               ePassword.setError(task.getException().getMessage());
                               ePassword.requestFocus();
                           } catch(FirebaseAuthInvalidCredentialsException e) {
                               eEmail.setError(task.getException().getMessage());
                               eEmail.requestFocus();
                           } catch(FirebaseAuthUserCollisionException e) {
                               eEmail.setError(task.getException().getMessage());
                               eEmail.requestFocus();
                           } catch(Exception e) {
                               Toast.makeText(Sign_In.this, "" + task.getException() , Toast.LENGTH_SHORT).show();
                           }
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