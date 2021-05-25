package com.shop.shoppingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.core.InFilter;
import com.shop.shoppingapp.lists.Favorite;
import com.shop.shoppingapp.module.Product;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    ArrayList<Product> arrayList ;
    Context context ;
    LayoutInflater inflater ;
    TextView tTitle , tStoreName , tPrice ;
    ImageView iImage ;

    public CustomAdapter(ArrayList<Product> arrayList , Context context , LayoutInflater inflater){
        this.arrayList = arrayList ;
        this.context = context ;
        this.inflater = inflater ;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = inflater.inflate(R.layout.item_store,parent,false);
        tTitle = view.findViewById(R.id.product_title);
        tStoreName = view.findViewById(R.id.product_store);
        tPrice = view.findViewById(R.id.product_price);
        iImage = view.findViewById(R.id.product_image);
        tPrice.setText(arrayList.get(position).getPrice());
        tStoreName.setText(arrayList.get(position).getStoreName());
        tTitle.setText(arrayList.get(position).getTitle());
        Glide.with(context).load(arrayList.get(position).getImageUrl()).into(iImage);

        return view;
    }

}
