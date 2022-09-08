package com.saramambiches.datingapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SliderImageAdapter extends PagerAdapter {

    private ArrayList<String> list;
    private Context context;

    public SliderImageAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View rootView = View.inflate(context, R.layout.item_slider_layout, null);

        ImageView imageView = rootView.findViewById(R.id.image);
        Glide.with(context).load(list.get(position)).into(imageView);
        container.addView(rootView);

        return rootView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
