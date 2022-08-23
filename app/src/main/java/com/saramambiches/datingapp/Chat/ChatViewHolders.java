package com.saramambiches.datingapp.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saramambiches.datingapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public TextView oppoMessage;
    public LinearLayout mContainer;
    public LinearLayout oppoContainer;

    public ChatViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMessage=itemView.findViewById(R.id.message);
        oppoMessage=itemView.findViewById(R.id.message_oppo);
        mContainer=itemView.findViewById(R.id.container);
        oppoContainer=itemView.findViewById(R.id.container_oppo);


    }

    @Override
    public void onClick(View v) {
    }
}
