package com.saramambiches.datingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CardsAdapter extends ArrayAdapter<Cards> {

    public CardsAdapter(Context context, int resourceId, List<Cards> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(card_item.getName());

        Glide.with(image);
        if ("default".equals(card_item.getProfileImageUrl())) {
            // TODO: Reemplazar con imagen de usuario por defecto
            Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
        } else {
            Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
        }

        return convertView;
    }
}
