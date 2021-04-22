package com.shop.shoppingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.firestore.core.InFilter;
import com.shop.shoppingapp.module.Product;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    ArrayList<Product> arrayList ;
    Context context ;
    LayoutInflater inflater ;

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

        return view;
    }

}
