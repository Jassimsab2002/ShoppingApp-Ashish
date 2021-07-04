package com.shop.shoppingapp.lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.module.Notification;
import com.shop.shoppingapp.viewholders.ProductHolder;

public class Notification_List extends AppCompatActivity {

    RecyclerView recyclerView ;
    LinearLayoutManager linearLayoutManager ;
    FirestoreRecyclerOptions firestoreRecyclerOptions ;
    FirestoreRecyclerAdapter firestoreRecyclerAdapter ;
    Query qNotification ;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    TextView tTitle , tBody ;
    ImageView iBack ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //findViews
        recyclerView = findViewById(R.id.recyclerview);
        iBack = findViewById(R.id.back);


        linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        qNotification = firestore.collection("Notification");

        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification_List.super.onBackPressed();
            }
        });

        firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Notification>()
                .setQuery(qNotification, Notification.class)
                .build();

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Notification, ProductHolder>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Notification model) {

                tTitle = holder.itemView.findViewById(R.id.textview_title);
                tBody = holder.itemView.findViewById(R.id.textview_body);
                tTitle.setText(model.getTitle());
                tBody.setText(model.getBody());
                holder.setOnClickListener(new ProductHolder.ClickListener() {
                                              @Override
                                              public void onClickListener(View v) {

                                              }
                                          });
            }

            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
                return new ProductHolder(view);
            }
        };
        recyclerView.setAdapter(firestoreRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firestoreRecyclerAdapter.stopListening();
    }
}