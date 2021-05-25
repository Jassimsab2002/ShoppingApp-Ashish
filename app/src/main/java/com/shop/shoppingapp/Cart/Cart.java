package com.shop.shoppingapp.Cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.viewholders.ProductHolder;

public class Cart extends Fragment {

    public Cart() {

    }

    RecyclerView recyclerView ;
    LinearLayoutManager layoutManager ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query suggestionQuery ;
    TextView title , store , price ;
    ImageView image ;
    FirestoreRecyclerAdapter<Product,ProductHolder> adapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);

        //Recycler
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        suggestionQuery = firestore.collection("Product").whereEqualTo("Cart",true);
        FirestoreRecyclerOptions<Product> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(suggestionQuery,Product.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Product, ProductHolder>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Product model) {

                title = holder.itemView.findViewById(R.id.product_title);
                store = holder.itemView.findViewById(R.id.product_store);
                price = holder.itemView.findViewById(R.id.product_price);
                image = holder.itemView.findViewById(R.id.product_image);

                Glide.with(getContext()).load(model.getImageUrl()).into(image);
                title.setText(model.getTitle());
                store.setText(model.getPrice());
                price.setText(model.getPrice());

            }

            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
                return new ProductHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        return view ;

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}