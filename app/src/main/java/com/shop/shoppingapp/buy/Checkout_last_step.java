package com.shop.shoppingapp.buy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shop.shoppingapp.MainActivity;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.module.Orders;
import com.shop.shoppingapp.profile.MyOrders_Change;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

public class Checkout_last_step extends AppCompatActivity {

    TextView tPPrice , tPShipping , tPTotal ,tAddress ;
    CardInputWidget cardInputWidget ;
    private static final String BACKEND_URL = "https://stripe-jassem-test-78.herokuapp.com";
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret ;
    private Stripe stripe ;
    static ProgressBar progressBar ;
    FrameLayout fPay ;
    static String sAddress , sTotalPrice , sProductPrice , sShippingPrice , sId , sTitle , sImage , sUserId , sCountry;
    Intent intent ;
    static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<Orders> arrayListOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_last_step);

        //findViews
        tPPrice = findViewById(R.id.textview_Product_Price);
        tPShipping = findViewById(R.id.textview_Product_shipping);
        tAddress = findViewById(R.id.textview_address);
        tPTotal = findViewById(R.id.textview_Product_Total);
        fPay = findViewById(R.id.pay);
        progressBar = findViewById(R.id.progress_bar);
        cardInputWidget = findViewById(R.id.card);

        progressBar.setVisibility(View.VISIBLE);

        //Intent
        intent = getIntent();
        getData(Objects.requireNonNull(intent.getStringArrayListExtra("Data")));
        sCountry = intent.getStringExtra("Country");
        sAddress = intent.getStringExtra("Address");
          /*
        sProductPrice = intent.getStringExtra("Price");
        sId = intent.getStringExtra("Id");
        sTitle = intent.getStringExtra("Title");
        sImage = intent.getStringExtra("Image");
         */

        //User Info
        sUserId = firebaseAuth.getUid();

        //Stripe Work
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51IxrgNIs1nmQac7AujyYLOSm73ZDYKyfyiP09iTS4lte1Ti0VoWabbjhTENgoCRxBmwNwZ5mwebcVZz9giexfFVW00ApsIJwy1")
        );

        startCheckout();

        //setOnClicks
        fPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
                if (params != null){
                    ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(params,paymentIntentClientSecret);
                    stripe.confirmPayment(Checkout_last_step.this, confirmParams);
                }
            }
        });
    }

    private void getData(ArrayList<String> arrayList) {
        for (String id : arrayList){
            firestore.collection("Product").whereEqualTo("Id",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                            sProductPrice = documentSnapshot.get("Price").toString();
                            sImage = documentSnapshot.get("ImageUrl").toString();
                            sTitle = documentSnapshot.get("Title").toString();
                            sId = documentSnapshot.get("Id").toString();
                            getShipping(documentSnapshot.getReference());

                            tAddress.setText(sAddress);
                            tPPrice.setText(sProductPrice + "$");

                            arrayListOrders.add(new Orders(sImage,sUserId,sTitle,null,sProductPrice,generateId(),documentSnapshot.get("Id").toString()));
                        }
                    }else{
                        Toast.makeText(Checkout_last_step.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void getShipping(DocumentReference reference) {
        reference.collection("Country").whereEqualTo("Country",sCountry).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        sShippingPrice = documentSnapshot.get("Shipping").toString();
                        Toast.makeText(Checkout_last_step.this, "suc " + sShippingPrice, Toast.LENGTH_SHORT).show();

                        tPShipping.setText(sShippingPrice + "$");
                        String pTotal = String.valueOf( Integer.valueOf(sProductPrice) + Integer.valueOf(sShippingPrice));
                        tPTotal.setText(pTotal);
                    }
                }else{
                    Toast.makeText(Checkout_last_step.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startCheckout() {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        String json = "{}";

/*

        double amount = 05.25 ;
        Map<String,Object> payMap = new HashMap<>();
        Map<String,Object> itemMap = new HashMap<>();
        List<Map<String,Object>> itemList = new ArrayList<>();
        payMap.put("currency","usd");
        itemMap.put("id","photo_subscription");
        itemMap.put("amount",amount);
        itemList.add(itemMap);
        payMap.put("items",itemList);
        String json = new Gson().toJson(payMap);

 */

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "/create-payment-intent")
                .post(body)
                .build();

       /*
       httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
                */
        httpClient.newCall(request)
                .enqueue(new Checkout_last_step.PayCallBack(Checkout_last_step.this));
        progressBar.setVisibility(View.INVISIBLE);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        stripe.onPaymentResult(requestCode,data,new Checkout_last_step.PaymentResultCallback(Checkout_last_step.this));
    }

    private final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        private final WeakReference<Checkout_last_step> activityRef ;
        PaymentResultCallback(Checkout_last_step activityRef){
            this.activityRef = new WeakReference<>(activityRef);
        }
        @Override
        public void onError(@NotNull Exception e) {

        }

        @Override
        public void onSuccess(PaymentIntentResult paymentIntentResult) {
            final Checkout_last_step activity = activityRef.get();
            if (activity == null){
                return;
            }
            PaymentIntent paymentIntent = paymentIntentResult.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();

            if (status == PaymentIntent.Status.Succeeded){
            /*
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
                String datetime = ft.format(dNow);
             */
                for (Orders model : arrayListOrders){
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("Address",sAddress);
                    hashMap.put("Shipping",sShippingPrice);
                    hashMap.put("Image",model.getImage());
                    hashMap.put("ProductId",model.getOrderId());
                    hashMap.put("BuyerId",sUserId);
                    hashMap.put("Title",model.getTitle());
                    hashMap.put("Price",model.getPrice());
                    hashMap.put("OrderId",model.getOrderId());
                    firestore.collection("Orders").document().set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(Checkout_last_step.this, MyOrders_Change.class);
                                startActivity(intent);
                                finish();
                            }else{}
                        }
                    });
                }

             /*
                HashMap<String,Object> hashMap = new HashMap<>();
                       hashMap.put("Address",sAddress);
                       hashMap.put("Shipping",sShippingPrice);
                       hashMap.put("Image",sImage);
                       hashMap.put("ProductId",sId);
                       hashMap.put("BuyerId",sUserId);
                       hashMap.put("Title",sTitle);
                       hashMap.put("Price",sProductPrice);
                       hashMap.put("OrderId",datetime);

                     firestore.collection("Orders").document().set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){
                                   Intent intent = new Intent(Checkout_last_step.this, MyOrders_Change.class);
                                   startActivity(intent);
                                   finish();
                               }else{}
                           }
                       });

              */


                   }
                    }

            }

    private static final class PayCallBack implements Callback {

        @NonNull
        private final WeakReference<Checkout_last_step> activityRef;
        public PayCallBack(Checkout_last_step checkout_first_step) {
            activityRef = new WeakReference<>(checkout_first_step);
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull final IOException e) {
            final Checkout_last_step activity = activityRef.get();
            if (activity == null){
                return;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,"Error CallBack Failure: " + e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull final Response response)
                throws IOException {

            final Checkout_last_step activity = activityRef.get();
            if (activity == null){
                return;
            }

            if (!response.isSuccessful()){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Error response not successful: " + response.message() , Toast.LENGTH_SHORT).show();
                    }
                });

            }else{
                activity.onPaymentSuccess(response);
            }
        }
    }

    private void onPaymentSuccess(Response response) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String,String>>(){}.getType();
        Map<String,String> responseMap = null;
        try {
            responseMap = gson.fromJson(
                    Objects.requireNonNull(response.body()).string(),
                    type
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        paymentIntentClientSecret = responseMap.get("clientSecret");

    }

    private String generateId (){
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        return datetime ;
    }
}