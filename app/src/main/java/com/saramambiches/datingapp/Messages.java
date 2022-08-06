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
import com.saramambiches.datingapp.Matches.MatchesAdapter;
import com.saramambiches.datingapp.Matches.MatchesObject;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(Messages.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), Messages.this);
        mRecyclerView.setAdapter(mMatchesAdapter);
        /*
        MatchesObject obj = new MatchesObject("asd");
        resultsMatches.add(obj);
        resultsMatches.add(obj);
        resultsMatches.add(obj);
        resultsMatches.add(obj);
        */

        for(int i=0; i<100; i++){
            MatchesObject obj = new MatchesObject(Integer.toString(i));
            resultsMatches.add(obj);
        }
        mMatchesAdapter.notifyDataSetChanged();

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

    private ArrayList<MatchesObject> resultsMatches = new ArrayList<MatchesObject>();
    private List<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }
}