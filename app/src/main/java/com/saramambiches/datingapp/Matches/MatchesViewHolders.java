package com.saramambiches.datingapp.Matches;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saramambiches.datingapp.R;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMatchId;
    public MatchesViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMatchId = (TextView) itemView.findViewById(R.id.Matchid);
    }

    @Override
    public void onClick(View v) {

    }
}
