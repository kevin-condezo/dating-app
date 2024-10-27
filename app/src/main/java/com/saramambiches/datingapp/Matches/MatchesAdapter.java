package com.saramambiches.datingapp.Matches;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saramambiches.datingapp.R;

import java.util.LinkedList;
import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders> {
    private LinkedList<MatchesObject> matchesList;
    private Context context;
    public MatchesAdapter (LinkedList<MatchesObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

//    public MatchesAdapter(List<MatchesObject> dataSetMatches, Messages messages) {
//    }

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
        holder.mLastMessage.setText(matchesList.get(position).getUserId());
        holder.mMatchName.setText(matchesList.get(position).getName());
        if (!matchesList.get(position).getProfileImageUrl().equals("default")) {
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }else{
            Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/date-app-e56a0.appspot.com/o/profileImages%2Fimage_profile.jpg?alt=media&token=f1c8e41a-bcf5-40de-adfd-195ce5ef7b0a").into(holder.mMatchImage);
        }

    }


    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }
}
