package com.shop.shoppingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shop.shoppingapp.buy.product_page;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.module.Product_categories;
import com.shop.shoppingapp.search.Srearch_Activity;
import com.shop.shoppingapp.viewholders.CatagoriesHolder;


import java.util.ArrayList;

public class StorePage extends Fragment {

    GridView grid ;
    CustomAdapter customAdapter ;
    ArrayList<Product> arrayList = new ArrayList<>();
    LayoutInflater layoutInflater ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
    TextView tTitle ;
    EditText eSearch ;
    ChipGroup chipGroup ;
    Chip chip ;
    Animation aDownUp , aLeftRight , aRightLeft ;
    public StorePage() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_store_page, container, false);
        layoutInflater = getLayoutInflater();
        grid = view.findViewById(R.id.gridView);
        chipGroup = view.findViewById(R.id.ChipGroup);
        tTitle = view.findViewById(R.id.title);
        eSearch = view.findViewById(R.id.searching);

        //Animation
        aDownUp = AnimationUtils.loadAnimation(getContext(),R.anim.down_up);
        grid.startAnimation(aDownUp);
        chipGroup.startAnimation(aDownUp);

        aLeftRight = AnimationUtils.loadAnimation(getContext(),R.anim.left_right);
        eSearch.startAnimation(aLeftRight);

        aRightLeft = AnimationUtils.loadAnimation(getContext(),R.anim.right_left);
        tTitle.startAnimation(aRightLeft);
        
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                chip = view.findViewById(chipGroup.getCheckedChipId());
                String s = chip.getText().toString().trim();
                updateGrid(s);

            }
        });

        //RecyclerWork
        updateGrid("All");

        //onClicks
        eSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Srearch_Activity.class);
                startActivity(intent);
            }
        });

        return  view ;
    }

    private void updateGrid(String s) {
        //RecyclerWork
        arrayList = new ArrayList<>();
        customAdapter = new CustomAdapter(arrayList,getContext() , layoutInflater);
        if (s.equals("All")){
            firestore.collection("Product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                            arrayList.add(new Product(
                                    documentSnapshot.get("Title").toString()
                                    ,documentSnapshot.get("Price").toString()
                                    ,documentSnapshot.get("Store").toString()
                                    ,documentSnapshot.get("Description").toString()
                                    ,documentSnapshot.get("ImageUrl").toString()
                                    ,documentSnapshot.get("Details").toString()
                                    ,documentSnapshot.getId()
                            ));
                        }
                        grid.setAdapter(customAdapter);
                        customAdapter.setOnClickListener(new CustomAdapter.ClickListener() {
                            @Override
                            public void onClickListener(View v , int position) {
                                Intent intent = new Intent(getActivity(), product_page.class);
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
                    }else{}

                }
            });
        }else{

            firestore.collection("Product").whereEqualTo("Category",s).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){

                        arrayList.add(new Product(
                                documentSnapshot.get("Title").toString()
                                ,documentSnapshot.get("Price").toString()
                                ,documentSnapshot.get("Store").toString()
                                ,documentSnapshot.get("Description").toString()
                                ,documentSnapshot.get("ImageUrl").toString()
                                ,documentSnapshot.get("Details").toString()
                                ,documentSnapshot.getId()
                        ));

                    }
                    grid.setAdapter(customAdapter);
                }else{}

            }
        });
        }
    }

}