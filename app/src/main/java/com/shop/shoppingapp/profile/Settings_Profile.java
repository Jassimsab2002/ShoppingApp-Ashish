package com.shop.shoppingapp.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shop.shoppingapp.FragmentInterface;
import com.shop.shoppingapp.MainActivity;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.buy.AddressActivity;
import com.shop.shoppingapp.home.HomePage;
import com.shop.shoppingapp.module.Address;
import com.shop.shoppingapp.viewholders.ProductHolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Settings_Profile extends AppCompatActivity {

    EditText eName  , eNumber ;
    Button bSubmit , iAddress ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    ImageView iBack , iEdit , iClose;
    String sName , sImage , sAddress , sNumber , sENumber  ;
    FragmentInterface fragmentInterface ;
    HomePage homePage ;
    MainActivity mainActivity ;
    ImageView iProfile ;
    final int REQUEST_OPTION = 22 ;
    Uri filePath ;
    FirebaseStorage storageReference = FirebaseStorage.getInstance();
    FrameLayout fThanks ;
    TextView tThanks , tAddress;
    FirestoreRecyclerAdapter firestoreRecyclerAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__profile);

        //findviews
        eName = findViewById(R.id.edittext_name);
        bSubmit = findViewById(R.id.button_submit);
        eNumber = findViewById(R.id.edittext_number);
        iAddress = findViewById(R.id.button_address);

        iBack = findViewById(R.id.back);
        iProfile = findViewById(R.id.profile_image);

        //getData
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sName = sharedPreferences.getString("name","You need to resign in we lost the data.");
        sAddress = sharedPreferences.getString("address","You need to resign in we lost the data");
        sImage = sharedPreferences.getString("image",null);
        sNumber = sharedPreferences.getString("number" , null);


        if (sImage != null){
            Uri uri = Uri.parse(sImage);
            Glide.with(getApplicationContext()).load(uri).into(iProfile);
        }


        //Fragment
        mainActivity = new MainActivity();
        homePage = new HomePage();

        //setData
        if (sNumber != null){
            eNumber.setText(sNumber);
        }

        eName.setText(sName);
        /*
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

         */

        //setOnClicks
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         startAct(1);
            }
        });

        iAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchAlertDialog();
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
                sENumber = eNumber.getText().toString();
                editor.remove("name");
                editor.putString("name" , sName);
                editor.remove("Address");
                editor.putString("Address",sAddress);
                if (sNumber != null){
                    editor.remove("number");
                }
                editor.putString("number" , sENumber);

                if (filePath != null){
                    editor.remove("image");
                    editor.putString("image" , filePath.toString());
                }

                editor.apply();

                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         bSubmit.setAlpha(1f);
                     }
                 },4000);
              /*
                firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i("PHOTOWORKING", "onComplete: ");
                            if (filePath != null) {
                                Toast.makeText(Settings_Profile.this, "Okay", Toast.LENGTH_SHORT).show();
                                // uploadImage(filePath);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bSubmit.setAlpha(1);
                                }
                            },6000);
                        }else{
                            bSubmit.setAlpha(1);
                            Log.i("PHOTOERROR", "onComplete: " + task.getException().getMessage());
                        }
                    }
                });

               */

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
            Log.i("PHOTO/", "onActivityResult: " + filePath);
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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update("ProfileImage" , ref.getDownloadUrl());
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Settings_Profile.this, "Image uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Settings_Profile.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot
                        .getTotalByteCount());
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
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
    public void lunchAlertDialog (){
        // create an alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Saved Addresses");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.address_alertdialog, null);
        builder.setView(customLayout);
        // create and show the alert dialog
        final RecyclerView recyclerView = customLayout.findViewById(R.id.recyclerview);

        builder.setPositiveButton("Add New Address", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings_Profile.this, AddressActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        //firebase
        Query query = firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Address");

        FirestoreRecyclerOptions<Address> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Address>()
                .setQuery(query,Address.class)
                .build();


        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Address, ProductHolder>(firestoreRecyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Address model) {
                Log.i("Test", "onBindViewHolder: " + "good");
                iEdit = holder.itemView.findViewById(R.id.image_edit);
                tAddress = holder.itemView.findViewById(R.id.text_address);
                iClose = holder.itemView.findViewById(R.id.check_box);
                iEdit.setVisibility(View.INVISIBLE);
                tAddress.setText(model.getAddress());

                iClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Address").whereEqualTo("Address",model.getAddress()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    documentSnapshot.getReference().delete();
                                }
                            }
                        });
                    }
                });
                holder.setOnClickListener(new ProductHolder.ClickListener() {
                    @Override
                    public void onClickListener(View v) {

                    }
                });


            }

            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent,false);
                return new ProductHolder(view);
            }
        };


        firestoreRecyclerAdapter.startListening();
        recyclerView.setAdapter(firestoreRecyclerAdapter);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}