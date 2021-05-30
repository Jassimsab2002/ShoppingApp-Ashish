package com.shop.shoppingapp.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shop.shoppingapp.R;

import java.util.HashMap;

public class Settings_Profile extends AppCompatActivity {

    EditText eName , eEmail , eAdress ;
    Button bSubmit ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    ImageView iBack ;
    String sName , sEmail , sAddress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__profile);

        //findviews
        eName = findViewById(R.id.edittext_name);
        bSubmit = findViewById(R.id.button_submit);
        eAdress = findViewById(R.id.edittext_email);
        iBack = findViewById(R.id.back);

        //getData
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sName = sharedPreferences.getString("name","You need to resign in we lost the data.");
        sAddress = sharedPreferences.getString("address","You need to resign in we lost the data");

        //setData
        eName.setText(sName);
        eAdress.setText(sAddress);

        //setOnClicks
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings_Profile.super.onBackPressed();
            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName = eName.getText().toString();
                editor.remove("name");
                editor.putString("name" , sName);
                editor.remove("Address");
                editor.putString("Address",sAddress);
                editor.apply();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("Name",sName);
                hashMap.put("Address",sAddress);
                firestore.collection("User").document(firebaseAuth.getUid()).update(hashMap);

            }
        });

    }
}