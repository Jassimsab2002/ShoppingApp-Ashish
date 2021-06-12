package com.shop.shoppingapp.buy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shop.shoppingapp.R;
import com.shop.shoppingapp.module.Product;
import com.shop.shoppingapp.module.Reviews;
import com.shop.shoppingapp.viewholders.ProductHolder;

import java.util.ArrayList;

public class product_page extends AppCompatActivity {

    Intent intent ;
    TextView tStoreName , tProductTitle , tProductPrice , tProductDetails ,tAddToCart , tReviewTitle , tReviewDescription ,tProductDiscount;
    String sStoreName , sProductTitle , sProductPrice , sProductDescription , sProductImage , sProductDetails , sId;
    ImageView iSend , iFavorite , iBack , iLeft , iRight;
    CardView cAddToCart , cBuyNow;
    String sProductShare = "";
    ViewPager vProductImages ;
    ProductImagesAdapter productImagesAdapter ;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    RecyclerView rReview ;
    LinearLayoutManager layoutManager ;
    Query qReviews ;
    FirestoreRecyclerOptions<Reviews> fReviews ;
    FirestoreRecyclerAdapter faReviews ;
    RatingBar ratingBar ;
    Animation aDown_Up , aLeft_Right , aRight_Left ;
    FirebaseUser firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String> aImages ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        //findViews
        tStoreName = findViewById(R.id.store_name);
        tProductPrice = findViewById(R.id.product_title);
        tProductTitle = findViewById(R.id.product_description);
        tProductDiscount = findViewById(R.id.product_price);
        tProductDetails = findViewById(R.id.product_details);
        vProductImages = findViewById(R.id.product_image);
        iSend = findViewById(R.id.Send);
        iFavorite = findViewById(R.id.favorite);
        iRight = findViewById(R.id.right);
        iLeft = findViewById(R.id.left);
        iBack = findViewById(R.id.back);
        cAddToCart = findViewById(R.id.cardView2);
        cBuyNow = findViewById(R.id.cardView);
        tAddToCart = findViewById(R.id.addToCart);
        rReview = findViewById(R.id.recyclerview_reviews);

        //Animation
        aDown_Up = AnimationUtils.loadAnimation(this,R.anim.down_up);
        tStoreName.startAnimation(aDown_Up);
        tAddToCart.startAnimation(aDown_Up);
        tProductPrice.startAnimation(aDown_Up);
        tAddToCart.startAnimation(aDown_Up);
        tProductDetails.startAnimation(aDown_Up);
        tProductDiscount.startAnimation(aDown_Up);
        vProductImages.startAnimation(aDown_Up);
        tProductTitle.startAnimation(aDown_Up );

        aLeft_Right = AnimationUtils.loadAnimation(this,R.anim.left_right);
        iFavorite.startAnimation(aLeft_Right);
        iSend.startAnimation(aLeft_Right);

        aRight_Left = AnimationUtils.loadAnimation(this,R.anim.right_left);
        iBack.startAnimation(aRight_Left);

        //onClicks
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_page.super.onBackPressed();
            }
        });

        cAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cAddToCart.setAlpha(0.5f);
                firebaseFirestore.collection("Product").whereEqualTo("Title",sProductTitle).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()){
                       for (DocumentSnapshot documentSnapshot : task.getResult()){
                           documentSnapshot.getReference().update("Cart" + firebaseAuth.getUid(),true).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   cAddToCart.setAlpha(1f);
                                   Toast.makeText(product_page.this, "Product Added To cart", Toast.LENGTH_SHORT).show();
                               }
                           });
                       }
                   }else{}
                    }
                });
            }
        });

        cBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(product_page.this,Checkout_first_step.class);
                intent.putExtra("Price",sProductPrice);
                intent.putExtra("Id",sId);
                startActivity(intent);
            }
        });

        iSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,sProductShare);
                intent.setType("text/plain");
                Intent.createChooser(intent,"Share Via");
                startActivity(intent);
            }
        });
        iFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iFavorite.setAlpha(0.2f);
                firebaseFirestore.collection("Product").whereEqualTo("Title",sProductTitle).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()){
                       for (DocumentSnapshot documentSnapshot : task.getResult()){
                          update(documentSnapshot,"Favorite" + firebaseAuth.getUid(),true);
                       }
                   }else{}

                    }
                });
            }
        });

        //intent
        intent = getIntent() ;
        fetchData();

        //Reviews Work
        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rReview.setLayoutManager(layoutManager);
        Log.i("Test", "onCreate: " + sId);
        qReviews = firebaseFirestore.collection("Product").document(sId).collection("Reviews");

        fReviews = new FirestoreRecyclerOptions.Builder<Reviews>()
                .setQuery(qReviews,Reviews.class)
                .build();

        faReviews = new FirestoreRecyclerAdapter<Reviews, ProductHolder>(fReviews) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Reviews model) {

                tReviewTitle = holder.itemView.findViewById(R.id.textview_name);
                tReviewDescription = holder.itemView.findViewById(R.id.textview_comment);
                ratingBar = holder.itemView.findViewById(R.id.ratingbar);
                ratingBar.setRating(Float.parseFloat(model.getRating()));
                tReviewTitle.setText(model.getName());
                tReviewDescription.setText(model.getComment());

            }

            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reviews,parent,false);
                return new ProductHolder(view);
            }
        };
        rReview.setAdapter(faReviews);

        //ImageView
        aImages = new ArrayList<>();
        firebaseFirestore.collection("Product").whereEqualTo("Title",sProductTitle).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    getData(documentSnapshot.getReference());
                }
            }
        });

    }

    private void getData(DocumentReference documentSnapshot) {
        documentSnapshot.collection("Images")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot dDocumentSnapshot : queryDocumentSnapshots){
                            aImages.add((String) dDocumentSnapshot.get("Image"));
                            productImagesAdapter = new ProductImagesAdapter(product_page.this,aImages);
                            vProductImages.setAdapter(productImagesAdapter);
                        }
                        if (aImages.size() != 0){
                            iLeft.setVisibility(View.VISIBLE);
                            iRight.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void update(DocumentSnapshot documentSnapshot, String favorite , final boolean value) {
        documentSnapshot.getReference().update(favorite,value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if (task.isSuccessful()){
               iFavorite.setAlpha(1f);
               Toast.makeText(product_page.this, "Product Added To WishList", Toast.LENGTH_SHORT).show();
               if(value){
                  iFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
              }else{
                  iFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
              }
           }else{
           }
            }
        });
    }

    public void fetchData(){

        if (intent != null){

           sStoreName = intent.getStringExtra("StoreName");
           sProductTitle = intent.getStringExtra("Title");
           sProductPrice = intent.getStringExtra("Price");
           sProductDescription = intent.getStringExtra("Description");
           sProductDetails = intent.getStringExtra("Details");
           sId = intent.getStringExtra("Id");
           tProductDetails.setText(sProductDetails);
           tStoreName.setText(sStoreName);
           tProductTitle.setText(sProductTitle);
           tProductPrice.setText(sProductPrice + "$");

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        faReviews.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        faReviews.stopListening();
    }
}