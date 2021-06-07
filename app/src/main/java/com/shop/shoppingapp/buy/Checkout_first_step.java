package com.shop.shoppingapp.buy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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
    private static final String BACKEND_URL = "https://stripe-pipay.herokuapp.com";
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret ;
    private Stripe stripe ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_first_step);

        //findViews
        sCountry = findViewById(R.id.spinner_country);
        fSave = findViewById(R.id.save);
        cardInputWidget = findViewById(R.id.card);

        //Spinner Work
        arrayAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_dropdown_item_1line,sItems);
        sCountry.setAdapter(arrayAdapter);

        //Stripe Work
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51IxrgNIs1nmQac7AujyYLOSm73ZDYKyfyiP09iTS4lte1Ti0VoWabbjhTENgoCRxBmwNwZ5mwebcVZz9giexfFVW00ApsIJwy1")

        );

         startCheckout();

        //setOnClicks
        fSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

                if (params != null){

                    ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(params,paymentIntentClientSecret);
                    stripe.confirmPayment(Checkout_first_step.this, confirmParams);

                }
            }
        });
    }

    private void startCheckout() {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");


        /*
        String json = "{"
                +"\"currency\":\"usd\","
                +"\"items\":["
                +"\"id\":\"photo_subscription\"}"
                +"]"
                +"}";
*/




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


        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "/create-payment-intent")
                .post(body)
                .build();

        httpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);

        httpClient.newCall(request)
                .enqueue(new PayCallBack(this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        stripe.onPaymentResult(requestCode,data,new PaymentResultCallback(this));
    }

    private static final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult>{
        private final WeakReference<Checkout_first_step> activityRef ;
        PaymentResultCallback( Checkout_first_step activityRef){
            this.activityRef = new WeakReference<>(activityRef);
        }
        @Override
        public void onError(@NotNull Exception e) {

        }

        @Override
        public void onSuccess(PaymentIntentResult paymentIntentResult) {
            final Checkout_first_step activity = activityRef.get();
            if (activity == null){
                return;
            }
            PaymentIntent paymentIntent = paymentIntentResult.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();

            if (status == PaymentIntent.Status.Succeeded){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

            }


        }
    }
    private static final class PayCallBack implements Callback{

        @NonNull private final WeakReference<Checkout_first_step> activityRef;
        public PayCallBack(Checkout_first_step checkout_first_step) {
            activityRef = new WeakReference<>(checkout_first_step);
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull final IOException e) {
            final Checkout_first_step activity = activityRef.get();
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

                final Checkout_first_step activity = activityRef.get();
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
}