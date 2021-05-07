package com.shop.shoppingapp.buy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.shop.shoppingapp.R;

public class product_page extends AppCompatActivity {

    Intent intent ;
    TextView tStoreName , tProductTitle , tProductPrice , tProductDescription ;
    String sStoreName , sProductTitle , sProductPrice , sProductDescription ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        //findViews
        tStoreName = findViewById(R.id.store_name);
        tProductTitle = findViewById(R.id.product_title);
        tProductDescription = findViewById(R.id.product_description);
        tProductPrice = findViewById(R.id.product_price);

        //intent
        intent = getIntent() ;
        fetchData();


    }
    public void fetchData(){

        if (intent != null){
           sStoreName = intent.getStringExtra("StoreName");
           sProductTitle = intent.getStringExtra("Title");
           sProductPrice = intent.getStringExtra("Price");
           sProductDescription = intent.getStringExtra("Description");
        }

    }
}