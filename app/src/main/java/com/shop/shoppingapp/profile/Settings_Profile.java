package com.shop.shoppingapp.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shop.shoppingapp.FragmentInterface;
import com.shop.shoppingapp.MainActivity;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.home.HomePage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Settings_Profile extends AppCompatActivity {

    EditText eName , eEmail , eAdress ;
    Button bSubmit ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    ImageView iBack ;
    String sName , sEmail , sAddress ;
    FragmentInterface fragmentInterface ;
    HomePage homePage ;
    MainActivity mainActivity ;
    ImageView iProfile ;
    final int REQUEST_OPTION = 22 ;
    Uri filePath ;
    FirebaseStorage storageReference = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__profile);

        //findviews
        eName = findViewById(R.id.edittext_name);
        bSubmit = findViewById(R.id.button_submit);
        eAdress = findViewById(R.id.edittext_email);
        iBack = findViewById(R.id.back);
        iProfile = findViewById(R.id.profile_image);

        //getData
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sName = sharedPreferences.getString("name","You need to resign in we lost the data.");
        sAddress = sharedPreferences.getString("address","You need to resign in we lost the data");

        //Fragment
        mainActivity = new MainActivity();
        homePage = new HomePage();

        //setData

        eName.setText(sName);
        eAdress.setText(sAddress);
        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().get("ProfileImage") != null) {
                        String imageUrl = task.getResult().get("ProfileImage").toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(iProfile);
                    }
                    }
            }
        });

        //setOnClicks
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         startAct(1);
            }
        });

        iProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bSubmit.setAlpha(0.2f);
                sName = eName.getText().toString();
                editor.remove("name");
                editor.putString("name" , sName);
                editor.remove("Address");
                editor.putString("Address",sAddress);
                editor.apply();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("Name",sName);
                hashMap.put("Address",sAddress);
                firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bSubmit.setAlpha(1);
                                    if (filePath != null) {
                                        uploadImage(filePath);
                                        Toast.makeText(Settings_Profile.this, "Okay", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },6000);
                        }else{
                            bSubmit.setAlpha(1);
                        }
                    }
                });

            }
        });

    }

    private void getImage() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Choose profile image." ) , REQUEST_OPTION);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPTION
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                iProfile.setImageBitmap(bitmap);
                

            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Uri filePath) {

        final StorageReference ref
                = storageReference.getReference().child(
                        "images/*"
                                + UUID.randomUUID().toString());
        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update("ProfileImage" , ref.getDownloadUrl());
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Settings_Profile.this, "Image uploaded", Toast.LENGTH_SHORT).show();

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Settings_Profile.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startAct(0);
    }

    private void startAct(int s) {
        Intent intent = new Intent(Settings_Profile.this,MainActivity.class);
        startActivity(intent);
    }

}