package com.shop.shoppingapp.search;

import android.app.Activity;
import android.content.Intent;
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

    ArrayList<Product> arrayList = new ArrayList<>();
    TextView tTitle , tBody ;
    Activity activity ;


    public Search_Adapter(ArrayList<Product> arrayList , Activity activity){
        this.arrayList = arrayList ;
        this.activity = activity ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new Search_Adapter_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        tTitle = holder.itemView.findViewById(R.id.textview_title);
        tBody = holder.itemView.findViewById(R.id.textview_body);
        tTitle.setText(arrayList.get(position).getTitle());
        tBody.setText(arrayList.get(position).getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,Search_Result.class);
                intent.putExtra("Title",arrayList.get(position).getTitle());
                activity.startActivity(intent);

            }
        });

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
