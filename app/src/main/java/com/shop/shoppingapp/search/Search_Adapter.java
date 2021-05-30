package com.shop.shoppingapp.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.shoppingapp.R;
import com.shop.shoppingapp.module.Product;

import java.util.ArrayList;

public class Search_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Product> arrayList ;
    TextView tTitle , tBody ;


    public Search_Adapter(ArrayList<Product> arrayList){
        this.arrayList = arrayList ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new Search_Adapter_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        tTitle = holder.itemView.findViewById(R.id.textview_title);
        tBody = holder.itemView.findViewById(R.id.textview_body);
        tTitle.setText(arrayList.get(position).getTitle());
        tBody.setText(arrayList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Search_Adapter_ViewHolder extends RecyclerView.ViewHolder{
        public Search_Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
