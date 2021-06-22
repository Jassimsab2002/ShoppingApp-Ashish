package com.shop.shoppingapp.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shop.shoppingapp.Cart.ActivityCart;
import com.shop.shoppingapp.MainActivity;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.authentification.Sign_In;
import com.shop.shoppingapp.buy.ProductImagesAdapter;
import com.shop.shoppingapp.buy.product_page;
import com.shop.shoppingapp.lists.Favorite;
import com.shop.shoppingapp.lists.Notification_List;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.profile.Settings_Profile;
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
    }

    Animation aDownUp , aLeftRight , aRightLeft , aRepeating;
    FirebaseUser user ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    TextView tUserName , tSeeShoes , tSeeApparelMen , tSeeApparelWomen, tSeeWatches , tHello , tSuggestions , tBest , tSell_All_Best_Selling  , tAds;
    String sName ;
    RecyclerView recyclerView , shoesRecyclerView , menRecyclerView ,womanRecyclerView ,watchRecyclerView ;
    LinearLayoutManager layoutManager ,layoutManagerSuggestions ,menLayoutManager , womanLayoutManager ,watchesLayoutManager ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
    Query shoesQuery , menQuery , womanQuery ,watchesQuery ;
    FirestoreRecyclerAdapter adapter , shoesAdapter , menAdapter , womanAdapter , watchesAdapter ;
    TextView title , store , price ;
    ImageView image , iAds , iAds2 ;
    DrawerLayout nMenu ;
    FrameLayout fMenu , fCart ;
    FragmentTransaction transaction ;
    ProductImagesAdapter productImagesAdapter ;
    NavigationView navigationView ;
    EditText eSearch ;
    CardView cardView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        sName = sharedPreferences.getString("name",null);
        recyclerView = view.findViewById(R.id.recyclerview);
        shoesRecyclerView = view.findViewById(R.id.BestSelling_recyclerview);
        menRecyclerView = view.findViewById(R.id.forMen);
        tUserName = view.findViewById(R.id.user_name);
        womanRecyclerView = view.findViewById(R.id.forWomenRecyclerView);
        watchRecyclerView = view.findViewById(R.id.WatchesRecyclerView);
        fMenu = view.findViewById(R.id.menu);
        nMenu = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        fCart = view.findViewById(R.id.cart);
        iAds = view.findViewById(R.id.image_ads);
        iAds2 = view.findViewById(R.id.image_ads2);
        navigationView = view.findViewById(R.id.nvView);
        tSeeApparelMen = view.findViewById(R.id.see_all_menAppral);
        tSeeApparelWomen = view.findViewById(R.id.see_all_apparelwomen);
        tSeeShoes = view.findViewById(R.id.see_all_shoes);
        tSeeWatches = view.findViewById(R.id.see_all_watches);
        eSearch = view.findViewById(R.id.searching);
        tHello = view.findViewById(R.id.Hello);
        cardView = view.findViewById(R.id.card);
        tSuggestions = view.findViewById(R.id.suggestions);
        tBest= view.findViewById(R.id.best);
        tSell_All_Best_Selling = view.findViewById(R.id.see_all_bestSelling);
        tAds = view.findViewById(R.id.text_ads);

        setOnClicks();
        tUserName.setText(sName);

        //Animation
        aDownUp = AnimationUtils.loadAnimation(getContext(),R.anim.down_up);
        tHello.startAnimation(aDownUp);
        tUserName.startAnimation(aDownUp);
        eSearch.startAnimation(aDownUp);
        cardView.startAnimation(aDownUp);

        aLeftRight = AnimationUtils.loadAnimation(getContext(),R.anim.left_right);
        fMenu.startAnimation(aLeftRight);
        tBest.startAnimation(aLeftRight);
        recyclerView.startAnimation(aLeftRight);
        menRecyclerView.startAnimation(aLeftRight);
        womanRecyclerView.startAnimation(aLeftRight);
        shoesRecyclerView.startAnimation(aLeftRight);
        tSuggestions.startAnimation(aLeftRight);
        aRightLeft = AnimationUtils.loadAnimation(getContext(),R.anim.right_left);
        fCart.startAnimation(aRightLeft);
        tSell_All_Best_Selling.startAnimation(aRightLeft);

        aRepeating = AnimationUtils.loadAnimation(getContext(),R.anim.ads_repeating);
        tAds.startAnimation(aRepeating);

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

        firestore.collection("Ad" +
                "s").document("2").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
        shoesRecyclerView.setLayoutManager(layoutManagerSuggestions);

        menLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        menRecyclerView.setLayoutManager(menLayoutManager);

        womanLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        womanRecyclerView.setLayoutManager(womanLayoutManager);

        shoesQuery = firestore.collection("Product").whereEqualTo("Category","Automobiles");
        menQuery = firestore.collection("Product").whereEqualTo("Sex","Men");
        womanQuery = firestore.collection("Product").whereEqualTo("Sex","Woman");
        watchesQuery = firestore.collection("Product").whereEqualTo("Category","Watches");


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.wishlist:
                    Intent intent = new Intent(getActivity(), Favorite.class);
                    startActivity(intent);
                        break;

                    case R.id.account:
                        Intent dIntent =  new Intent(getActivity(), Settings_Profile.class);
                        startActivity(dIntent);
                        break;

                    case R.id.notification :
                        Intent nIntent =  new Intent(getActivity(), Notification_List.class);
                        startActivity(nIntent);
                        break;

                    case R.id.AllCategories :
                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                        intent2.putExtra("Type","Store");
                        startActivity(intent2);
                        break;

                }
                return true;
            }
        });

        FirestoreRecyclerOptions<Product> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(shoesQuery,Product.class)
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
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Product model) {

                title = holder.itemView.findViewById(R.id.product_title);
                store = holder.itemView.findViewById(R.id.product_store);
                price = holder.itemView.findViewById(R.id.product_price);
                image = holder.itemView.findViewById(R.id.product_image);

                Glide.with(getContext()).load(model.getImageUrl()).into(image);
                title.setText(model.getTitle());
                store.setText(model.getStoreName());
                price.setText(model.getPrice() + "$");
                holder.setOnClickListener(new ProductHolder.ClickListener() {
                    @Override
                    public void onClickListener(View v) {

                        Intent intent = new Intent(getActivity(), product_page.class);
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
                return new ProductHolder(view);
            }
        };

        shoesAdapter = new FirestoreRecyclerAdapter<Product,ProductHolder>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Product model) {

                title = holder.itemView.findViewById(R.id.product_title);
                store = holder.itemView.findViewById(R.id.product_store);
                price = holder.itemView.findViewById(R.id.product_price);
                image = holder.itemView.findViewById(R.id.product_image);

                Glide.with(getContext()).load(model.getImageUrl()).into(image);
                title.setText(model.getTitle());
                store.setText(model.getStoreName());
                price.setText(model.getPrice() + "$");
                holder.setOnClickListener(new ProductHolder.ClickListener() {
                    @Override
                    public void onClickListener(View v) {

                        Intent intent = new Intent(getActivity(), product_page.class);
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store,parent,false);
                return new ProductHolder(view);
            }
        };

        menAdapter = new FirestoreRecyclerAdapter<Product,ProductHolder>(firestoreRecyclerOptionsMen) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Product model) {

                title = holder.itemView.findViewById(R.id.product_title);
                store = holder.itemView.findViewById(R.id.product_store);
                price = holder.itemView.findViewById(R.id.product_price);
                image = holder.itemView.findViewById(R.id.product_image);

                Glide.with(getContext()).load(model.getImageUrl()).into(image);
                title.setText(model.getTitle());
                store.setText(model.getStoreName());
                price.setText(model.getPrice() + "$");
                holder.setOnClickListener(new ProductHolder.ClickListener() {
                    @Override
                    public void onClickListener(View v) {
                        Intent intent = new Intent(getActivity(), product_page.class);
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store,parent,false);
                return new ProductHolder(view);
            }
        };

        womanAdapter = new FirestoreRecyclerAdapter<Product,ProductHolder>(firestoreRecyclerOptionsWoman) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Product model) {

                title = holder.itemView.findViewById(R.id.product_title);
                store = holder.itemView.findViewById(R.id.product_store);
                price = holder.itemView.findViewById(R.id.product_price);
                image = holder.itemView.findViewById(R.id.product_image);

                Glide.with(getContext()).load(model.getImageUrl()).into(image);
                title.setText(model.getTitle());
                store.setText(model.getStoreName());
                price.setText(model.getPrice() + "$");
                holder.setOnClickListener(new ProductHolder.ClickListener() {
                    @Override
                    public void onClickListener(View v) {

                        Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), product_page.class);
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store,parent,false);
                return new ProductHolder(view);
            }
        };
        watchesAdapter = new FirestoreRecyclerAdapter<Product,ProductHolder>(firestoreRecyclerOptionsWatch) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Product model) {

                title = holder.itemView.findViewById(R.id.product_title);
                store = holder.itemView.findViewById(R.id.product_store);
                price = holder.itemView.findViewById(R.id.product_price);
                image = holder.itemView.findViewById(R.id.product_image);

                Glide.with(getContext()).load(model.getImageUrl()).into(image);
                title.setText(model.getTitle());
                store.setText(model.getStoreName());
                price.setText(model.getPrice() + "$");
                holder.setOnClickListener(new ProductHolder.ClickListener() {
                    @Override
                    public void onClickListener(View v) {

                        Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), product_page.class);
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store,parent,false);
                return new ProductHolder(view);
            }
        };

        watchRecyclerView.setAdapter(watchesAdapter);
        menRecyclerView.setAdapter(menAdapter);
        shoesRecyclerView.setAdapter(shoesAdapter);
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
              Intent intent = new Intent(getActivity(), ActivityCart.class);
              startActivity(intent);
            }
        });

        tSeeWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct("Glasses");
            }
        });
        tSeeShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct("Automobiles");
            }
        });

        tSeeApparelWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct("Apparel For Women");
            }
        });

        tSell_All_Best_Selling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct("Best Selling");
            }
        });
        tSeeApparelMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct("Apparel For Men");

            }
        });

    }

    public void startAct(String category){
        Intent intent = new Intent(getActivity(), HomeHolder.class);
        intent.putExtra("Category",category);
        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getActivity(), Sign_In.class);
            startActivity(intent);
            getActivity().finish();
        }
        shoesAdapter.startListening();
        menAdapter.startListening();
        womanAdapter.startListening();
        adapter.startListening();
        watchesAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        shoesAdapter.stopListening();
        menAdapter.stopListening();
        womanAdapter.stopListening();
        watchesAdapter.stopListening();
    }
}