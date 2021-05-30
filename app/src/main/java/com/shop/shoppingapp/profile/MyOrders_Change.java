package com.shop.shoppingapp.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.shop.shoppingapp.R;

public class MyOrders_Change extends AppCompatActivity {

    Button bGetToNextLevel ;
    ImageView iBack ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders__change);

        //findViews
        iBack = findViewById(R.id.back);
        bGetToNextLevel = findViewById(R.id.button);

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
}