package com.shop.shoppingapp.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.shoppingapp.R;
import com.shop.shoppingapp.module.Product;

public class ProductHolder extends RecyclerView.ViewHolder
{

    View view ;
    public ProductHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView ;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClickListener(v);
            }
        });

    }

    private ProductHolder.ClickListener mClickListener ;
    public interface ClickListener{
        public void onClickListener(View v);
    }

    public void setOnClickListener(ProductHolder.ClickListener clickListener){
        mClickListener = clickListener ;
    }


}
