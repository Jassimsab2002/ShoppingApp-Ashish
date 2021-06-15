package com.shop.shoppingapp.buy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shop.shoppingapp.R;

import java.util.HashMap;

public class Order_Received extends AppCompatActivity {

    EditText eComment, eRating ;
    FrameLayout fSave ;
    ImageView iBack ;
    String sComment , sRating , sId , sName , sTitle ;
    Intent intent ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
    SharedPreferences sharedPreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__received);

        //findViews
        eComment = findViewById(R.id.edittext_comment);
        eRating = findViewById(R.id.edittext_rating);
        fSave = findViewById(R.id.save);
        iBack = findViewById(R.id.back);

        //setInfo
        intent = getIntent();
        sId = intent.getStringExtra("Id");
        sTitle = intent.getStringExtra("Title");
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        sName = sharedPreferences.getString("name","Private User");

        //setOnClicks
        fSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fSave.setAlpha(0.3f);
                sComment = eComment.getText().toString().trim();
                sRating = eRating.getText().toString().trim();
                if ( ! sRating.isEmpty() && ! sComment.isEmpty()){
                    if (Integer.valueOf(sRating) < 5){
                        firestore.collection("Product").whereEqualTo("Title",sTitle).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("Name",sName);
                                    hashMap.put("Comment",sComment);
                                    hashMap.put("Rating",sRating);
                                    documentSnapshot.getReference().collection("Reviews").document().set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Order_Received.this, "Comment added , thank you !", Toast.LENGTH_SHORT).show();
                                            firestore.collection("Orders").whereEqualTo("OrderId",sId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                                        documentSnapshot.getReference().update("BuyerId",null);
                                                        fSave.setAlpha(1f);
                                                        Order_Received.super.onBackPressed();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }else{
                        Toast.makeText(Order_Received.this, "Please rate out of 1", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Order_Received.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order_Received.super.onBackPressed();
            }
        });




    }
}