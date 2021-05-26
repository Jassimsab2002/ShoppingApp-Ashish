package com.shop.shoppingapp.buy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shop.shoppingapp.R;

import java.util.ArrayList;

public class product_page extends AppCompatActivity {

    Intent intent ;
    TextView tStoreName , tProductTitle , tProductPrice , tProductDescription , tProductDetails ,tAddToCart;
    String sStoreName , sProductTitle , sProductPrice , sProductDescription , sProductImage , sProductDetails;
    ImageView iSend , iFavorite , iBack ;
    CardView cAddToCart , cBuyNow;
    String sProductShare = "";
    ViewPager vProductImages ;
    ProductImagesAdapter productImagesAdapter ;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        //findViews
        tStoreName = findViewById(R.id.store_name);
        tProductTitle = findViewById(R.id.product_title);
        tProductDescription = findViewById(R.id.product_description);
        tProductPrice = findViewById(R.id.product_price);
        tProductDetails = findViewById(R.id.product_details);
        vProductImages = findViewById(R.id.product_image);
        iSend = findViewById(R.id.Send);
        iFavorite = findViewById(R.id.favorite);
        iBack = findViewById(R.id.back);
        cAddToCart = findViewById(R.id.cardView2);
        cBuyNow = findViewById(R.id.cardView);
        tAddToCart = findViewById(R.id.addToCart);


        //onClicks
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_page.super.onBackPressed();
            }
        });

        cAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Product").whereEqualTo("Title",sProductTitle).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()){
                       for (DocumentSnapshot documentSnapshot : task.getResult()){
                           documentSnapshot.getReference().update("Cart",true);
                       }
                   }else{}
                    }
                });
            }
        });

        cBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,sProductShare);
                intent.setType("text/plain");
                Intent.createChooser(intent,"Share Via");
                startActivity(intent);
            }
        });
        iFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Product").whereEqualTo("Title",sProductTitle).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()){
                       for (DocumentSnapshot documentSnapshot : task.getResult()){
                          update(documentSnapshot,"Favorite",true);
                       }
                   }else{}

                    }
                });
            }
        });

        //intent
        intent = getIntent() ;
        fetchData();
    }

    private void update(DocumentSnapshot documentSnapshot, String favorite , final boolean value) {
        documentSnapshot.getReference().update(favorite,value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if (task.isSuccessful()){
              if(value){
                  iFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
              }else{
                  iFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
              }
           }else{

           }
            }
        });
    }

    public void fetchData(){

        if (intent != null){
           ArrayList<String> images =  new ArrayList<>();
           sStoreName = intent.getStringExtra("StoreName");
           sProductTitle = intent.getStringExtra("Title");
           sProductPrice = intent.getStringExtra("Price");
           sProductDescription = intent.getStringExtra("Description");
           sProductDetails = intent.getStringExtra("Details");
           sProductImage = intent.getStringExtra("ImageUrl");
           images.add(sProductImage);
           productImagesAdapter = new ProductImagesAdapter(this,images);
           vProductImages.setAdapter(productImagesAdapter);
           tProductDetails.setText(sProductDetails);
           tStoreName.setText(sStoreName);
           tProductTitle.setText(sProductTitle);
           tProductDescription.setText(sProductDescription);
           tProductPrice.setText(sProductPrice);
        }

    }
}