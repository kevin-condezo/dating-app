package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saramambiches.datingapp.Matches.MatchesAdapter;
import com.saramambiches.datingapp.Matches.MatchesAdapterHorizontal;
import com.saramambiches.datingapp.Matches.MatchesObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Messages extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView, mRecyclerViewHorizontal;
    private RecyclerView.Adapter mMatchesAdapter,mMatchesAdapterHorizontal;
    private RecyclerView.LayoutManager mMatchesLayoutManager,mMatchesLayoutManagerHorizontal;
    private String currentUserId; //= mAuth.getCurrentUser().getUid(); // get current user id
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(Messages.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), Messages.this);
        mRecyclerView.setAdapter(mMatchesAdapter);

        //Recycler View Horizontal
        mRecyclerViewHorizontal = findViewById(R.id.recyclerViewHorizontal);
        mRecyclerViewHorizontal.setNestedScrollingEnabled(false);
        mRecyclerViewHorizontal.setHasFixedSize(true);
        mMatchesLayoutManagerHorizontal = new LinearLayoutManager(Messages.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewHorizontal.setLayoutManager(mMatchesLayoutManagerHorizontal);
        mMatchesAdapterHorizontal = new MatchesAdapterHorizontal(getDataSetMatches(), Messages.this);
        mRecyclerViewHorizontal.setAdapter(mMatchesAdapterHorizontal);//llamamos al adapter


        getUserMatchId();

        /*
        MatchesObject obj = new MatchesObject("asd");
        resultsMatches.add(obj);
        */


       // mMatchesAdapter.notifyDataSetChanged();

        //Navigation Bar

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_sms);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),PrincipalPage.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.nav_sms:
                        return true;
                    case R.id.nav_user:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }

                return false;
            }
        });


        //-----------
    }

    private void getUserMatchId() {

        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot match : snapshot.getChildren()){
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userId = snapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";

                    if(snapshot.child("name").getValue()!=null){
                        name = snapshot.child("name").getValue().toString();
                    }
                    if(snapshot.child("profileImageUrl").getValue()!=null){
                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }
                    MatchesObject obj = new MatchesObject(userId, name, profileImageUrl);
                    resultsMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                    mMatchesAdapterHorizontal.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private LinkedList<MatchesObject> resultsMatches = new LinkedList<>();
    private List<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }

    private LinkedList<MatchesObject> resultsChats = new LinkedList<MatchesObject>();
    private List<MatchesObject> getDataSetChats() {
        return resultsChats;
    }
}