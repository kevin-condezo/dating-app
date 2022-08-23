package com.saramambiches.datingapp.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saramambiches.datingapp.Chat.ChatActivity;
import com.saramambiches.datingapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mLastMessage, mMatchName;
    public CircleImageView mMatchImage;
    public MatchesViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mLastMessage = (TextView) itemView.findViewById(R.id.lastMessage);
        mMatchName = (TextView) itemView.findViewById(R.id.MatchName);
        mMatchImage = (CircleImageView) itemView.findViewById(R.id.MatchImage);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ChatActivity.class);
        Bundle b= new Bundle();
        b.putString("matchId", mLastMessage.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
