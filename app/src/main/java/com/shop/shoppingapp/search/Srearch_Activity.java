package com.shop.shoppingapp.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.module.Product;

import java.util.ArrayList;

public class Srearch_Activity extends AppCompatActivity {

    RecyclerView recyclerView ;
    ImageView iBack ;
    LinearLayoutManager layoutManager ;
    EditText eSearch ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    TextWatcher textWatcher ;
    ArrayList<Product> arrayList ;
    Search_Adapter search_adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srearch_);

        //findViews
        recyclerView = findViewById(R.id.recyclerview);
        iBack = findViewById(R.id.back);
        eSearch = findViewById(R.id.searching);

        //Recycler Work
        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        //EditText Work
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayList = new ArrayList<>();
                search(eSearch.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        eSearch.addTextChangedListener(textWatcher);


        //onClicks
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Srearch_Activity.super.onBackPressed();
            }
        });

    }

    private void search(String text) {
        firestore.collection("Product").whereArrayContains("Keyword",text.toLowerCase()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        arrayList.add(new Product(
                                documentSnapshot.get("Title").toString()
                                ,documentSnapshot.get("Price").toString()
                                ,documentSnapshot.get("StoreName").toString()
                                ,documentSnapshot.get("Description").toString()
                                ,documentSnapshot.get("ImageUrl").toString()
                                ,documentSnapshot.get("Details").toString()
                                ,documentSnapshot.getId()
                        ));
                    }
                    search_adapter = new Search_Adapter(arrayList,Srearch_Activity.this);
                    recyclerView.setAdapter(search_adapter);
                }else{}
            }
        });
    }
}