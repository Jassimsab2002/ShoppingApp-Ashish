package com.shop.shoppingapp.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.model.Document;
import com.shop.shoppingapp.Cart.Cart;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.authentification.Sign_In;
import com.shop.shoppingapp.buy.product_page;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.viewholders.ProductHolder;

public class HomePage extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    SharedPreferences sharedPreferences ;

    public HomePage(SharedPreferences sharedPreferences) {
        // Required empty public constructor
        this.sharedPreferences = sharedPreferences ;
    }

    public HomePage() {
        // Required empty public constructor
    }

    FirebaseUser user ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    TextView tUserName ;
    String sName ;
    RecyclerView recyclerView , best_selling , menRecyclerView ,womanRecyclerView ,watchRecyclerView ;
    LinearLayoutManager layoutManager ,layoutManagerSuggestions ,menLayoutManager , womanLayoutManager ,watchesLayoutManager ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
    Query suggestionQuery ,menQuery ,womanQuery ,watchesQuery ;
    FirestoreRecyclerAdapter adapter , suggestionAdapter , menAdapter , womanAdapter , watchesAdapter ;
    TextView title , store , price ;
    ImageView image , iAds , iAds2 ;
    DrawerLayout nMenu ;
    FrameLayout fMenu , fCart ;
    FragmentTransaction transaction ;
    Cart cart ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        sName = sharedPreferences.getString("name",null);

        recyclerView = view.findViewById(R.id.recyclerview);
        best_selling = view.findViewById(R.id.BestSelling_recyclerview);
        menRecyclerView = view.findViewById(R.id.forMen);
        tUserName = view.findViewById(R.id.user_name);
        womanRecyclerView = view.findViewById(R.id.forWomenRecyclerView);
        watchRecyclerView = view.findViewById(R.id.WatchesRecyclerView);
        fMenu = view.findViewById(R.id.menu);
        nMenu = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        fCart = view.findViewById(R.id.cart);
        iAds = view.findViewById(R.id.image_ads);
        iAds2 = view.findViewById(R.id.image_ads2);

        cart = new Cart();
        setOnClicks();

        tUserName.setText(sName);

        //Ads
        firestore.collection("Ads").document("1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           if (task.isSuccessful()){
               DocumentSnapshot document = task.getResult();
               Glide.with(getContext()).load(document.get("Image")).into(iAds);
           }else {
               Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
           }
            }
        });
        firestore.collection("Ads").document("2").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Glide.with(getContext()).load(document.get("Image")).into(iAds2);
                }else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        watchesLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        watchRecyclerView.setLayoutManager(watchesLayoutManager);

        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        layoutManagerSuggestions = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        best_selling.setLayoutManager(layoutManagerSuggestions);

        menLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        menRecyclerView.setLayoutManager(menLayoutManager);

        womanLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        womanRecyclerView.setLayoutManager(womanLayoutManager);

        suggestionQuery = firestore.collection("Product");
        menQuery = firestore.collection("Product").whereEqualTo("Sex","Men");
        womanQuery = firestore.collection("Product").whereEqualTo("Sex","Woman");
        watchesQuery = firestore.collection("Product").whereEqualTo("Category","Watches");

        FirestoreRecyclerOptions<Product> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(suggestionQuery,Product.class)
                .build();
        FirestoreRecyclerOptions<Product> firestoreRecyclerOptionsMen = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(menQuery ,Product.class)
                .build();
        FirestoreRecyclerOptions<Product> firestoreRecyclerOptionsWoman = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(womanQuery ,Product.class)
                .build();

        FirestoreRecyclerOptions<Product> firestoreRecyclerOptionsWatch = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(watchesQuery ,Product.class)
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
                return new ProductHolder(view);
            }
        };

        suggestionAdapter = new FirestoreRecyclerAdapter<Product,ProductHolder>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Product model) {

                title = holder.itemView.findViewById(R.id.product_title);
                store = holder.itemView.findViewById(R.id.product_store);
                price = holder.itemView.findViewById(R.id.product_price);
                image = holder.itemView.findViewById(R.id.product_image);

                Glide.with(getContext()).load(model.getImageUrl()).into(image);
                title.setText(model.getTitle());
                store.setText(model.getPrice());
                price.setText(model.getPrice());
                holder.setOnClickListener(new ProductHolder.ClickListener() {
                    @Override
                    public void onClickListener(View v) {

                        Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), product_page.class);
                        intent.putExtra("Title",model.getTitle());
                        intent.putExtra("StoreName",model.getStoreName());
                        intent.putExtra("Price", model.getPrice());
                        intent.putExtra("Description",model.getDescription());
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion,parent,false);
                return new ProductHolder(view);
            }
        };

        menAdapter = new FirestoreRecyclerAdapter<Product,ProductHolder>(firestoreRecyclerOptionsMen) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion,parent,false);
                return new ProductHolder(view);
            }
        };

        womanAdapter = new FirestoreRecyclerAdapter<Product,ProductHolder>(firestoreRecyclerOptionsWoman) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion,parent,false);
                return new ProductHolder(view);
            }
        };
        watchesAdapter = new FirestoreRecyclerAdapter<Product,ProductHolder>(firestoreRecyclerOptionsWatch) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
                return new ProductHolder(view);
            }
        };

        watchRecyclerView.setAdapter(watchesAdapter);
        menRecyclerView.setAdapter(menAdapter);
        best_selling.setAdapter(suggestionAdapter);
        recyclerView.setAdapter(adapter);
        womanRecyclerView.setAdapter(womanAdapter);
        return  view ;
    }

    private void setOnClicks() {

        fMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nMenu.openDrawer(GravityCompat.START);
            }
        });

        fCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, cart).commit();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        suggestionAdapter.startListening();
        menAdapter.startListening();
        womanAdapter.startListening();
        adapter.startListening();
        watchesAdapter.startListening();
        if (user == null){
            Intent intent = new Intent(getActivity(), Sign_In.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        suggestionAdapter.stopListening();
        menAdapter.stopListening();
        womanAdapter.stopListening();
        watchesAdapter.stopListening();
    }
}