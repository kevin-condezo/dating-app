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

import com.saramambiches.datingapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public LinearLayout mContainer;
    public CircleImageView mProfileImage;

    public ChatViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mProfileImage = itemView.findViewById(R.id.profile_image_match);
        mMessage=itemView.findViewById(R.id.message);
        mContainer=itemView.findViewById(R.id.container);
    }

    @Override
    public void onClick(View v) {
    }
}
