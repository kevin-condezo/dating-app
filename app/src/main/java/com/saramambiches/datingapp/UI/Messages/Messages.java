package com.saramambiches.datingapp.UI.Messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saramambiches.datingapp.Chat.ChatAdapter;
import com.saramambiches.datingapp.Chat.ChatObject;
import com.saramambiches.datingapp.Matches.MatchesAdapter;
import com.saramambiches.datingapp.Matches.MatchesAdapterHorizontal;
import com.saramambiches.datingapp.Matches.MatchesObject;
import com.saramambiches.datingapp.UI.Profile.Profile;
import com.saramambiches.datingapp.R;
import com.saramambiches.datingapp.UI.Home.PrincipalPage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messages extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView, mRecyclerViewHorizontal;
    private ChatAdapter mChatAdapter;
    private RecyclerView.Adapter mMatchesAdapter,mMatchesAdapterHorizontal;
    private RecyclerView.LayoutManager mMatchesLayoutManager,mMatchesLayoutManagerHorizontal;
    private String currentUserId; //= mAuth.getCurrentUser().getUid(); // get current user id
    private String chatId;
    String profileImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Se declara a los matches en la sección Mensajes
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(Messages.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDataSetChats(), Messages.this);
        mRecyclerView.setAdapter(mMatchesAdapter);

        //Se declara a los matches en la sección Nuevos matches
        mRecyclerViewHorizontal = findViewById(R.id.recyclerViewHorizontal);
        mRecyclerViewHorizontal.setNestedScrollingEnabled(false);
        mRecyclerViewHorizontal.setHasFixedSize(true);
        mMatchesLayoutManagerHorizontal = new LinearLayoutManager(Messages.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewHorizontal.setLayoutManager(mMatchesLayoutManagerHorizontal);
        mMatchesAdapterHorizontal = new MatchesAdapterHorizontal(getDataSetMatches(), Messages.this);
        mRecyclerViewHorizontal.setAdapter(mMatchesAdapterHorizontal);//llamamos al adapter

        mChatAdapter = new ChatAdapter(getDataSetChat(), Messages.this);


        getUserMatchId();

        /*
        MatchesObject obj = new MatchesObject("asd");
        resultsMatches.add(obj);
        */


       // mMatchesAdapter.notifyDataSetChanged();


    }

    private void getUserMatchId() { //Detecta a los usuarios con los que se ha hecho un match

        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot match : snapshot.getChildren()){
                        FetchMatchInformation(match.getKey());
                        resultsChat.size();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchMatchInformation(String key) { //Obtiene la información de los usuarios con los que se ha hecho un match
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
                    resultsChats.add(obj);
                    mMatchesAdapterHorizontal.notifyDataSetChanged();
                    resultsMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchChatInformation(String key) { //Proximamente
        DatabaseReference chatDb = FirebaseDatabase.getInstance().getReference().child("User").child(key).child("connections").child("matches").child(chatId);
        final String chatId = chatDb.push().getKey();
        chatDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userId = snapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    //Lista enlazada
    private LinkedList<MatchesObject> resultsMatches = new LinkedList<MatchesObject>();
    private LinkedList<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }

    private LinkedList<MatchesObject> resultsChats = new LinkedList<MatchesObject>();
    private LinkedList<MatchesObject> getDataSetChats() {
        return resultsChats;
    }

    //por implementear
    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }
}