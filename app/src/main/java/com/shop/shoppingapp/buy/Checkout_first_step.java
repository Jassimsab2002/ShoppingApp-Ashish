package com.shop.shoppingapp.buy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shop.shoppingapp.R;
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

        //Intent
        intent = getIntent();
        arrayList = intent.getStringArrayListExtra("Data");



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
}