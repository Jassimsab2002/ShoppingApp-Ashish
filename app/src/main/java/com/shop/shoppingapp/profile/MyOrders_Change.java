package com.shop.shoppingapp.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.buy.Order_Received;
import com.shop.shoppingapp.buy.product_page;
import com.shop.shoppingapp.module.Orders;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.viewholders.ProductHolder;

import java.util.ArrayList;

public class MyOrders_Change extends AppCompatActivity {

    Button bGetToNextLevel ;
    ImageView iBack ;
    RecyclerView recyclerView ;
    LinearLayoutManager linearLayoutManager ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
    FirebaseAuth auth = FirebaseAuth.getInstance() ;
    String sUserId ;
    Query query ;
    FirestoreRecyclerAdapter firestoreRecyclerAdapter ;
    ImageView iProduct ;
    Button bConfirm ;
    TextView tTitle , tPrice , tArrive , tPageTitle , tSubTitle  , sPrice , sTitle , sImage ;
    ArrayList<Orders> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders__change);

        //findViews
        iBack = findViewById(R.id.back);
        bGetToNextLevel = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recyclerview);
        tPageTitle = findViewById(R.id.textview_title);
        tSubTitle = findViewById(R.id.textview_subtitle);


        //User Info
        sUserId = auth.getUid();

        //Recycler work
        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        query = firestore.collection("Orders").whereEqualTo("BuyerId",sUserId);
            FirestoreRecyclerOptions<Orders> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Orders>()
                    .setQuery(query, Orders.class)
                    .build();

            firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Orders, ProductHolder>(firestoreRecyclerOptions) {
                @Override
                protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Orders model) {
                    arrayList.add(model);
                    tTitle = holder.itemView.findViewById(R.id.title);
                    tPrice = holder.itemView.findViewById(R.id.price);
                    tArrive = holder.itemView.findViewById(R.id.num_unit);
                    iProduct = holder.itemView.findViewById(R.id.image);
                    bConfirm = holder.itemView.findViewById(R.id.confirm_receive);

                    Glide.with(MyOrders_Change.this).load(model.getImage()).into(iProduct);
                    tTitle.setText(model.getTitle());
                    tPrice.setText(model.getPrice());
                    tArrive.setText(model.getArrive());

                    holder.setOnClickListener(new ProductHolder.ClickListener() {
                        @Override
                        public void onClickListener(View v) {
                        }
                    });

                    bConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MyOrders_Change.this, Order_Received.class);
                            intent.putExtra("Id",model.getOrderId());
                            intent.putExtra("Title",model.getTitle());
                            startActivity(intent);
                        }
                    });

                }

                @NonNull
                @Override
                public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders, parent, false);
                    return new ProductHolder(view);
                }
            };

            recyclerView.setAdapter(firestoreRecyclerAdapter);
     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {

             if (arrayList.size() == 0) {
                 tPageTitle.setVisibility(View.VISIBLE);
                 tSubTitle.setVisibility(View.VISIBLE);
                 bGetToNextLevel.setVisibility(View.VISIBLE);
             }

         }
     },4000);

        //setOnClicks
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrders_Change.super.onBackPressed();
            }
        });

        bGetToNextLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrders_Change.super.onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firestoreRecyclerAdapter.stopListening();
    }
}