package com.shop.shoppingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.shop.shoppingapp.module.Product;

import java.util.ArrayList;

public class StorePage extends Fragment {
    GridView grid ;
    CustomAdapter customAdapter ;
    ArrayList<Product> arrayList = new ArrayList<>();
    LayoutInflater layoutInflater ;

    public StorePage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_page, container, false);

        layoutInflater = getLayoutInflater();
        grid = view.findViewById(R.id.gridView);
        arrayList.add(new Product("kjhjk","khgsdflms","kjkjb","klj","jhbjb"));
        customAdapter = new CustomAdapter(arrayList,getContext() , layoutInflater);
        grid.setAdapter(customAdapter);

        return  view ;

    }
}