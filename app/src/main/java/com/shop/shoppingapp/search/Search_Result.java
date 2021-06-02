package com.shop.shoppingapp.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.shop.shoppingapp.lists.Favorite;
import com.shop.shoppingapp.module.Product;

import java.util.ArrayList;
import java.util.Objects;

public class Search_Result extends AppCompatActivity {

    ImageView iBack ;
    TextView tTitle ;
    GridView gSearch ;
    CustomAdapter adapter ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ArrayList<Product> arrayList ;
    Intent iSearch ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__result);

        //findViews
        iBack = findViewById(R.id.back);
        tTitle = findViewById(R.id.textview_search);0
        gSearch = findViewById(R.id.gridView);

        //get-setData
        iSearch = getIntent();
        tTitle.setText(iSearch.getStringExtra("Title"));

        //OnClicks
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search_Result.super.onBackPressed();
            }
        });

        //GridView
        arrayList =  new ArrayList<>();
        adapter = new CustomAdapter(arrayList,this,getLayoutInflater());
        firestore.collection("Product").whereArrayContains("Title",iSearch.getStringExtra("Title")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        if (documentSnapshot != null) {
                            arrayList.add(new Product(
                                    documentSnapshot.get("Title").toString()
                                    , documentSnapshot.get("Price").toString()
                                    , documentSnapshot.get("Store").toString()
                                    , documentSnapshot.get("Description").toString()
                                    , documentSnapshot.get("ImageUrl").toString()
                                    , documentSnapshot.get("Details").toString()
                                    , documentSnapshot.getId()
                            ));
                        }
                    }
                    gSearch.setAdapter(adapter);

                }else{

                }
            }
        });


    }
}