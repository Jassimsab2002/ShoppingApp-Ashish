package com.shop.shoppingapp.buy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.module.Address;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.module.Reviews;
import com.shop.shoppingapp.viewholders.ProductHolder;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.StripeIntent;
import com.stripe.android.view.CardInputWidget;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Checkout_first_step extends AppCompatActivity {

    FrameLayout fSave ;
    CardInputWidget cardInputWidget ;
    Spinner sCountry ;
    ArrayAdapter<CharSequence> arrayAdapter ;
    String[] sItems = new String[]{"India","US","Morocco"};
    ProgressBar progressBar ;
    EditText eName , eNumber ,  eAddress , eZipCode ;
    String sName , sNumber ,  sAddress , sZipCode , sSCountry , sPrice , sId , sImage , sTitle ;
    Intent intent ;
    ArrayList<String> arrayList ;
    SharedPreferences sharedPreferences ;
    ImageView iBack , iEdit , iClose ;
    TextView tChooseFromExisting , tAddress ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter firestoreRecyclerAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_first_step);

        //findViews
        sCountry = findViewById(R.id.spinner_country);
        fSave = findViewById(R.id.save);
        progressBar = findViewById(R.id.progress_bar);
        eName = findViewById(R.id.edittext_name);
        eNumber = findViewById(R.id.edittext_number);
        eAddress = findViewById(R.id.edittext_location);
        eZipCode = findViewById(R.id.edittext_zipcode);
        iBack = findViewById(R.id.back);
        tChooseFromExisting = findViewById(R.id.text_choose);
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Checkout_first_step.super.onBackPressed();
            }
        });

        //Intent
        intent = getIntent();
        arrayList = intent.getStringArrayListExtra("Data");

        //getData
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        sName = sharedPreferences.getString("name","Please add the name of the receiver");
        sAddress = sharedPreferences.getString("address","Please add the Address of the receiver");
        sNumber = sharedPreferences.getString("number", null);
        eAddress.setText(sAddress);
        eName.setText(sName);

        if (sNumber != null){
            eNumber.setText(sNumber);
        }


        /*
        sPrice = intent.getStringExtra("Price");
        sId = intent.getStringExtra("Id");
        sImage = intent.getStringExtra("Image");
        sTitle = intent.getStringExtra("Title");
        */

        //Progress
        progressBar.setVisibility(View.INVISIBLE);

        //Spinner Work
        arrayAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_dropdown_item_1line,sItems);
        sCountry.setAdapter(arrayAdapter);
        sCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sSCountry = sItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sSCountry = null ;
            }
        });

        //setOnClicks
        tChooseFromExisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchAlertDialog();
            }
        });

        fSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                sName = eName.getText().toString().trim();
                sNumber = eNumber.getText().toString().trim();
                sAddress = eAddress.getText().toString().trim();
                sZipCode = eZipCode.getText().toString().trim();

                if(! sNumber.isEmpty() && ! sName.isEmpty() && ! sAddress.isEmpty() && ! sZipCode.isEmpty() && ! sSCountry.isEmpty()){

                   Intent intent = new Intent(Checkout_first_step.this ,Checkout_last_step.class);
                   intent.putExtra("Country",sSCountry);
                   intent.putExtra("Address",sAddress);
                   intent.putExtra("Name" , sName);
                   intent.putExtra("Number",sNumber);

                   /*
                   intent.putExtra("Price",sPrice);
                   intent.putExtra("Id",sId);
                   intent.putExtra("Image",sImage);
                   intent.putExtra("Title",sTitle);
                  */

                    intent.putStringArrayListExtra("Data",arrayList);
                    startActivity(intent);
                    finish();

                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Checkout_first_step.this, "One of the fields is empty.", Toast.LENGTH_LONG).show();
                }
            }
        });
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        //firebase
        Query query = firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Address");

        FirestoreRecyclerOptions<Address> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Address>()
                .setQuery(query,Address.class)
                .build();

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Address,ProductHolder>(firestoreRecyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Address model) {
                Log.i("Test", "onBindViewHolder: " + "good");
                iEdit = holder.itemView.findViewById(R.id.image_edit);
                tAddress = holder.itemView.findViewById(R.id.text_address);
                iClose = holder.itemView.findViewById(R.id.check_box);
                iEdit.setVisibility(View.INVISIBLE);
                iClose.setVisibility(View.INVISIBLE);
                tAddress.setText(model.getAddress());

                holder.setOnClickListener(new ProductHolder.ClickListener() {
                    @Override
                    public void onClickListener(View v) {
                        eAddress.setText(model.getAddress());
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firestoreRecyclerAdapter != null) {
            firestoreRecyclerAdapter.stopListening();
        }
    }
}