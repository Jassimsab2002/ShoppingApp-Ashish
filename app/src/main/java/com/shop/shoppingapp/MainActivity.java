package com.shop.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.shop.shoppingapp.authentification.Sign_In;
import com.shop.shoppingapp.home.HomePage;
import com.shop.shoppingapp.profile.Profile_Fragment;


public class MainActivity extends AppCompatActivity {

    FragmentTransaction transaction ;
    HomePage homePage ;
    StorePage storePage ;
    BottomNavigationView bottomNavigationView ;
    SharedPreferences sharedPreferences ;
    Profile_Fragment profile_fragment ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (firebaseAuth.getCurrentUser() != null) {
            sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
            homePage = new HomePage(sharedPreferences);
            storePage = new StorePage();
            profile_fragment = new Profile_Fragment(sharedPreferences);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, homePage).commit();
            bottomNavigationView = findViewById(R.id.bottom_navigation);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {

                        case R.id.home:
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.addToBackStack("sfd");
                            transaction.replace(R.id.container, homePage).commit();
                            break;

                        case R.id.shop:
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.addToBackStack("sfd");
                            transaction.replace(R.id.container, storePage).commit();
                            break;

                        case R.id.profile:
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.addToBackStack("sfd");
                            transaction.replace(R.id.container, profile_fragment).commit();
                            break;

                    }
                    return false;
                }
            });

        }else{

            Intent iIntent = new Intent(MainActivity.this, Sign_In.class);
            startActivity(iIntent);
            finish();

        }

    }

}