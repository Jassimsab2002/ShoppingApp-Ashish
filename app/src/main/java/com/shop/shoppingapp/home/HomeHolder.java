package com.shop.shoppingapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shop.shoppingapp.CustomAdapter;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.buy.product_page;
import com.shop.shoppingapp.module.Product;

import java.util.ArrayList;

public class HomeHolder extends AppCompatActivity {

    TextView tTitle ;
    GridView gridView ;
    ImageView iBack ;
    Intent iInfo ;
    String sCategory ;
    CustomAdapter customAdapter ;
    ArrayList<Product> arrayList ;
    LayoutInflater layoutInflater ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_holder);

        //setOnClicks
        tTitle = findViewById(R.id.textView_title);
        gridView = findViewById(R.id.gridView);
        iBack = findViewById(R.id.back);

        //setOnClicks
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               HomeHolder.super.onBackPressed();
            }
        });

        //layoutInflater
        layoutInflater = getLayoutInflater();

        //getInfo
        iInfo = getIntent();
        if (iInfo != null){
            sCategory = iInfo.getStringExtra("Category");
            if(sCategory.equals("Automobiles")) {
                tTitle.setText("Best Selling");
            }else{
                tTitle.setText(sCategory);
            }
        }

        //gridView
        arrayList = new ArrayList<>();
        customAdapter = new CustomAdapter(arrayList ,this,layoutInflater);

        firestore.collection("Product").whereEqualTo("Category",sCategory).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
           if (task.isSuccessful()){
              for (DocumentSnapshot documentSnapshot : task.getResult()){
                  if (documentSnapshot != null) {
                      arrayList.add(new Product(
                                documentSnapshot.get("Title").toString()
                              , documentSnapshot.get("Price").toString()
                              , documentSnapshot.get("StoreName").toString()
                              , documentSnapshot.get("Description").toString()
                              , documentSnapshot.get("ImageUrl").toString()
                              , documentSnapshot.get("Details").toString()
                              ,documentSnapshot.getId()
                      ));
                  }
              }
              gridView.setAdapter(customAdapter);
           }

            }
        });

        customAdapter.setOnClickListener(new CustomAdapter.ClickListener() {
            @Override
            public void onClickListener(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), product_page.class);
                intent.putExtra("Title",arrayList.get(position).getTitle());
                intent.putExtra("StoreName",arrayList.get(position).getStoreName());
                intent.putExtra("Price", arrayList.get(position).getPrice());
                intent.putExtra("Description",arrayList.get(position).getDescription());
                intent.putExtra("ImageUrl",arrayList.get(position).getImageUrl());
                intent.putExtra("Details",arrayList.get(position).getDetails());
                intent.putExtra("Id",arrayList.get(position).getId());
                startActivity(intent);
            }
        });


    }
}