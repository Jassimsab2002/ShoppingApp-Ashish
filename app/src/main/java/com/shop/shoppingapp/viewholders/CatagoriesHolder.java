package com.shop.shoppingapp.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CatagoriesHolder extends RecyclerView.ViewHolder {
    View view ;
    public CatagoriesHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView ;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClickListener(v);
            }
        });
    }

    private CatagoriesHolder.ClickListener mClickListener ;
    public interface ClickListener{
        public void onClickListener(View v);
    }

    public void setOnClickListener(CatagoriesHolder.ClickListener clickListener){
        mClickListener = clickListener ;
    }

}
