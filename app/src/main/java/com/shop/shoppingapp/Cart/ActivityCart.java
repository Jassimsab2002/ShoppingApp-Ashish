package com.shop.shoppingapp.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.buy.product_page;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.viewholders.ProductHolder;

public class ActivityCart extends AppCompatActivity {

    RecyclerView recyclerView ;
    LinearLayoutManager layoutManager ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query suggestionQuery ;
    TextView title , store , price , tCheckedPrice ;
    ImageView image ;
    FirestoreRecyclerAdapter<Product, ProductHolder> adapter ;
    AppCompatCheckBox checkBox ;
    int iCheckPrice = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerview);
        tCheckedPrice = findViewById(R.id.textview_price);

        //Recycler
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        suggestionQuery = firestore.collection("Product").whereEqualTo("Cart",true);
        FirestoreRecyclerOptions<Product> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(suggestionQuery,Product.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Product, ProductHolder>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Product model) {

                title = holder.itemView.findViewById(R.id.product_title);
                store = holder.itemView.findViewById(R.id.product_store);
                price = holder.itemView.findViewById(R.id.product_price);
                image = holder.itemView.findViewById(R.id.product_image);
                checkBox = holder.itemView.findViewById(R.id.appCompatCheckBox);

                Glide.with(getApplicationContext()).load(model.getImageUrl()).into(image);
                title.setText(model.getTitle());
                store.setText(model.getPrice());
                price.setText(model.getPrice());

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            tCheckedPrice.setText(setPrice(0,Integer.parseInt(model.getPrice())));
                        }else{
                            tCheckedPrice.setText(setPrice(5,Integer.parseInt(model.getPrice())));
                        }
                    }
                });

                holder.setOnClickListener(new ProductHolder.ClickListener() {
                    @Override
                    public void onClickListener(View v) {

                        Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityCart.this, product_page.class);
                        intent.putExtra("Title",model.getTitle());
                        intent.putExtra("StoreName",model.getStoreName());
                        intent.putExtra("Price", model.getPrice());
                        intent.putExtra("Description",model.getDescription());
                        intent.putExtra("ImageUrl",model.getImageUrl());
                        intent.putExtra("Details",model.getDetails());
                        intent.putExtra("Id",model.getId());
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
                return new ProductHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public String setPrice(int type , int price){
        if (type == 0){
            iCheckPrice += price ;
        }else{
            iCheckPrice -= price ;
        }
        return String.valueOf(iCheckPrice) + "$";
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}