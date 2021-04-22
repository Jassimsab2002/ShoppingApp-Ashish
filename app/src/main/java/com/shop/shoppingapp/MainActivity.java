package com.shop.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shop.shoppingapp.Cart.Cart;
import com.shop.shoppingapp.authentification.Sign_In;
import com.shop.shoppingapp.home.HomePage;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.viewholders.ProductHolder;

public class MainActivity extends AppCompatActivity {

    FragmentTransaction transaction ;
    HomePage homePage ;
    StorePage storePage ;
    BottomNavigationView bottomNavigationView ;
    Cart cart ;
    SharedPreferences sharedPreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        homePage = new HomePage(sharedPreferences);
        storePage = new StorePage();
        cart = new Cart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container,homePage).commit();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home :
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container , homePage).commit();
                        break;
                    case R.id.shop :
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container , storePage).commit();
                        break;

                }
                return false;
            }
        });



    }

}