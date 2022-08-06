package com.saramambiches.datingapp.Matches;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;
import com.saramambiches.datingapp.Messages;
import com.saramambiches.datingapp.R;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders> {
    private List<MatchesObject> matchesList;
    private Context context;
    public MatchesAdapter (List<MatchesObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    public MatchesAdapter(List<MatchesObject> dataSetMatches, Messages messages) {
    }

    @NonNull
    @Override
    public MatchesViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View LayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null  , false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutView.setLayoutParams(lp);
        MatchesViewHolders rcv = new MatchesViewHolders(LayoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolders holder, int position) {
        holder.mMatchId.setText(matchesList.get(position).getUserId());

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
