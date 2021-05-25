package com.shop.shoppingapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.module.Product_categories;
import com.shop.shoppingapp.viewholders.CatagoriesHolder;


import java.util.ArrayList;

public class StorePage extends Fragment {

    GridView grid ;
    CustomAdapter customAdapter ;
    ArrayList<Product> arrayList = new ArrayList<>();
    LayoutInflater layoutInflater ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
    RecyclerView rCategories ;
    LinearLayoutManager layoutManager ;
    Query catQuery ;
    FirestoreRecyclerAdapter fSuggestionsAdapter ;
    TextView tCategory ;

    public StorePage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_page, container, false);
        layoutInflater = getLayoutInflater();
        grid = view.findViewById(R.id.gridView);

        //RecyclerWork
        rCategories = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rCategories.setLayoutManager(layoutManager);
        customAdapter = new CustomAdapter(arrayList,getContext() , layoutInflater);
        catQuery = firestore.collection("Product").whereEqualTo("Category","Suggestions");

        FirestoreRecyclerOptions<Product_categories> firestoreRecyclerOptiones = new FirestoreRecyclerOptions.Builder<Product_categories>()
                .setQuery(catQuery,Product_categories.class)
                .build();

        fSuggestionsAdapter = new FirestoreRecyclerAdapter<Product_categories, CatagoriesHolder>(firestoreRecyclerOptiones) {
            @Override
            protected void onBindViewHolder(@NonNull CatagoriesHolder holder, int position, @NonNull Product_categories model) {
                tCategory = holder.itemView.findViewById(R.id.title);
                tCategory.setBackgroundResource(R.drawable.menu_select);
                tCategory.setText(model.getTitle());
                if (model.getTitle() == "Clothes"){
                    tCategory.setBackgroundResource(R.drawable.menu_selected);
                }

            }

            @NonNull
            @Override
            public CatagoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category ,parent,false);
                return new CatagoriesHolder(view);
            }
        };

        rCategories.setAdapter(fSuggestionsAdapter);

        firestore.collection("Product").whereEqualTo("Category","Clothes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    arrayList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                     arrayList.add(new Product(
                             documentSnapshot.get("Title").toString()
                             ,documentSnapshot.get("Price").toString()
                             ,documentSnapshot.get("Store").toString()
                             ,documentSnapshot.get("Description").toString()
                             ,documentSnapshot.get("ImageUrl").toString()
                             ,documentSnapshot.get("Details").toString()
                     ));
                    }
                    grid.setAdapter(customAdapter);
                }else{}
            }
        });
        return  view ;
    }

    @Override
    public void onStop() {
        super.onStop();
        fSuggestionsAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        fSuggestionsAdapter.startListening();
    }
}