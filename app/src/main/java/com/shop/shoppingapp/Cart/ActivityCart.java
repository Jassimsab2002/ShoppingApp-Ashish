package com.shop.shoppingapp.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.buy.Checkout_first_step;
import com.shop.shoppingapp.buy.product_page;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.viewholders.ProductHolder;

import java.util.ArrayList;

public class ActivityCart extends AppCompatActivity {

    RecyclerView recyclerView ;
    LinearLayoutManager layoutManager ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query suggestionQuery ;
    TextView title , store , price , tCheckedPrice , tQuantity ;
    ImageView image , iPAdd , iPMinim , iDelete;
    FirestoreRecyclerAdapter<Product, ProductHolder> adapter ;
    AppCompatCheckBox checkBox ;
    int iCheckPrice = 0 ;
    ArrayList<String> arrayList = new ArrayList<>() ;
    Button bBuy ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String sQuantity ;
    int iQuantity = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerview);
        tCheckedPrice = findViewById(R.id.textview_price);
        bBuy = findViewById(R.id.BuyNow);

        //Recycler
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        suggestionQuery = firestore.collection("Product").whereEqualTo("Cart" + firebaseAuth.getUid() ,true);
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
                tQuantity = holder.itemView.findViewById(R.id.product_quality);
                iPAdd = holder.itemView.findViewById(R.id.product_quantity_add);
                iPMinim = holder.itemView.findViewById(R.id.product_quality_minim);
                iDelete = holder.itemView.findViewById(R.id.delete);

                firestore.collection("Product").whereEqualTo("Id" , model.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                              if (documentSnapshot.get("CartQuantity" + firebaseAuth.getUid()) != null) {
                                  sQuantity = documentSnapshot.get("CartQuantity" + firebaseAuth.getUid()).toString();
                                  tQuantity.setText(sQuantity);
                              }
                            }
                        }
                    }
                });



                Glide.with(getApplicationContext()).load(model.getImageUrl()).into(image);
                title.setText(model.getTitle());
                store.setText(model.getPrice());
                price.setText(model.getPrice());

                iPAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sQuantity != null){
                            iPAdd.setAlpha(0.4f);
                            if (iQuantity == 10000) {
                                iQuantity = Integer.valueOf(sQuantity);
                                ++iQuantity;
                                firestore.collection("Product").whereEqualTo("Id" , model.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                                documentSnapshot.getReference().update("CartQuantity" + firebaseAuth.getUid(),String.valueOf(iQuantity)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            iPAdd.setAlpha(1f);
                                                            tQuantity.setText(String.valueOf(iQuantity));
                                                        }else{
                                                            iPAdd.setAlpha(1f);
                                                            Toast.makeText(ActivityCart.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                                }
                                            }
                                    }

                                });
                            }else{
                                ++iQuantity;

                                firestore.collection("Product").whereEqualTo("Id" , model.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                                documentSnapshot.getReference().update("CartQuantity" + firebaseAuth.getUid(),String.valueOf(iQuantity)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            iPAdd.setAlpha(1f);
                                                            tQuantity.setText(String.valueOf(iQuantity));
                                                        }else{
                                                            iPAdd.setAlpha(1f);
                                                            Toast.makeText(ActivityCart.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }

                                });
                            }
                        }else{
                            Toast.makeText(ActivityCart.this, "There is an error please re-add this product to the cart. ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                iPMinim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       iPMinim.setAlpha(0.4f);
                        if (sQuantity != null) {
                            if (iQuantity == 10000) {
                                iQuantity = Integer.valueOf(sQuantity);
                                --iQuantity;

                                firestore.collection("Product").whereEqualTo("Id" , model.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                                documentSnapshot.getReference().update("CartQuantity" + firebaseAuth.getUid(),String.valueOf(iQuantity)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            iPMinim.setAlpha(1f);
                                                            tQuantity.setText(String.valueOf(iQuantity));

                                                        }else{
                                                            iPMinim.setAlpha(1f);
                                                            Toast.makeText(ActivityCart.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }

                                });

                            } else if (iQuantity >= 1){
                                --iQuantity;

                                firestore.collection("Product").whereEqualTo("Id" , model.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                                documentSnapshot.getReference().update("CartQuantity" + firebaseAuth.getUid(),String.valueOf(iQuantity)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            iPMinim.setAlpha(1f);
                                                            tQuantity.setText(String.valueOf(iQuantity));

                                                        }else{
                                                            iPMinim.setAlpha(1f);
                                                            Toast.makeText(ActivityCart.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }

                                });
                            }
                        }else{
                            Toast.makeText(ActivityCart.this, "There is an error please re-add this product to the cart. ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            tCheckedPrice.setText(setPrice(0,Integer.parseInt(model.getPrice()) * Integer.valueOf(sQuantity)));
                            arrayList.add(model.getId());
                        }else{
                            tCheckedPrice.setText(setPrice(5,Integer.parseInt(model.getPrice()) * Integer.valueOf(sQuantity)));
                            arrayList.remove(arrayList.size() - 1 );
                        }
                    }
                });

                iDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firestore.collection("Product").whereEqualTo("Id" , model.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                                        documentSnapshot.getReference().update("Cart" + firebaseAuth.getUid(),false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    iPAdd.setAlpha(1f);
                                                    tQuantity.setText(String.valueOf(iQuantity));
                                                }else{
                                                    iPAdd.setAlpha(1f);
                                                    Toast.makeText(ActivityCart.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    }
                                }
                            }

                        });
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


        //setOnClicks
        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCart.this, Checkout_first_step.class);
                intent.putStringArrayListExtra("Data",arrayList);
                startActivity(intent);
            }
        });

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