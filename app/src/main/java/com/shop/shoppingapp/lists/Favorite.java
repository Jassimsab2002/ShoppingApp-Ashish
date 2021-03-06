package com.shop.shoppingapp.lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shop.shoppingapp.CustomAdapter;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.buy.product_page;
import com.shop.shoppingapp.module.Product;

import java.util.ArrayList;
import java.util.Objects;

public class Favorite extends AppCompatActivity {

    GridView gridView ;
    ImageView iBack ;
    CustomAdapter adapter ;
    ArrayList<Product> arrayList ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        gridView = findViewById(R.id.gridView);
        iBack = findViewById(R.id.back);

        //OnClicks
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favorite.super.onBackPressed();
            }
        });

        //GridView
        arrayList =  new ArrayList<>();
        adapter = new CustomAdapter(arrayList,this,getLayoutInflater());
        firestore.collection("Product").whereEqualTo("Favorite" + firebaseUser.getUid() ,true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        if (documentSnapshot != null) {
                            arrayList.add(new Product(
                                    documentSnapshot.get("Title").toString()
                                    , documentSnapshot.get("Price").toString()
                                    , documentSnapshot.get("StoreName").toString()
                                    , documentSnapshot.get("Description").toString()
                                    , documentSnapshot.get("ImageUrl").toString()
                                    , documentSnapshot.get("Details").toString()
                                    , documentSnapshot.getId()
                            ));
                        }
                    }
                gridView.setAdapter(adapter);
                    adapter.setOnClickListener(new CustomAdapter.ClickListener() {
                        @Override
                        public void onClickListener(View v , int position) {

                            Intent intent = new Intent(Favorite.this, product_page.class);
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

                }else{

                }
            }
        });

    }
}