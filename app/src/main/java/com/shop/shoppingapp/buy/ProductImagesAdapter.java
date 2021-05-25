package com.shop.shoppingapp.buy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductImagesAdapter extends PagerAdapter {

    ArrayList<String>images ;
    Context context ;

    public ProductImagesAdapter(Context context , ArrayList<String> images){
        this.images = images ;
        this.context = context ;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ImageView) object ;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(images.get(position)).into(imageView);
        ((ViewPager) container).addView(imageView,0);
        return imageView;
    }

}
