package com.shop.shoppingapp.buy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shop.shoppingapp.R;

import java.util.HashMap;

public class AddressActivity extends AppCompatActivity {

    EditText eAddress ;
    ImageView iBack ;
    Button bSubmit ;
    String sAddress ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        eAddress = findViewById(R.id.address);
        iBack = findViewById(R.id.back);
        bSubmit = findViewById(R.id.submit);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bSubmit.setAlpha(0.1f);
                sAddress = eAddress.getText().toString().trim();
                if (!sAddress.isEmpty()){
                    HashMap<String,Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("Address",sAddress);
                    firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Address").document().set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                bSubmit.setAlpha(1f);
                                AddressActivity.super.onBackPressed();
                            }else{
                                bSubmit.setAlpha(1f);
                                eAddress.setError(task.getException().getLocalizedMessage());

                            }
                        }
                    });
                }else{
                    eAddress.setError("Field is empty");
                }
            }
        });

        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}