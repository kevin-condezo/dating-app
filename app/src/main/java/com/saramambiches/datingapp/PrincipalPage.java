package com.saramambiches.datingapp;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrincipalPage extends AppCompatActivity {
    private cards cards_data[];
    private arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private String currentUId;
    private DatabaseReference usersDb;

    private FloatingActionButton bt_back, bt_skip, bt_like;
    BottomNavigationView bottomNavigationView;

    private Button layoutHide;
    private LinearLayout layoutMatch;

    ListView listView;
    List<cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);


        //Navigation Bar

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_sms:
                        startActivity(new Intent(getApplicationContext(), Messages.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.nav_user:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        //-----------

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId= mAuth.getCurrentUser().getUid();


        checkUserSex();

        rowItems = new ArrayList<cards>();


        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards object = (cards) dataObject;
                String userId = object.getUserId();
                usersDb.child(userId).child("connections").child("skip").child(currentUId).setValue(true);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards object = (cards) dataObject;
                String userId = object.getUserId();
                usersDb.child(userId).child("connections").child("like").child(currentUId).setValue(true);
                isConnectionMatch(userId);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                //mensaje al entrar a la pantalla
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Darle click
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                    Toast.makeText(PrincipalPage.this, "Info", Toast.LENGTH_SHORT).show();
                    //View match = flingContainer.getSelectedView();
                    //match.findViewById(R.id.item_match).setAlpha(1);
            }
        });

        //Botones flotantes

        init();

        /*bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimarFab(bt_back);
                Toast.makeText(PrincipalPage.this, "Back", Toast.LENGTH_SHORT).show();
            }
        });*/

        bt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimarFab(bt_skip);
                flingContainer.getTopCardListener().selectLeft();
            }
        });


        bt_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimarFab(bt_like);
                flingContainer.getTopCardListener().selectRight();
            }
        });

        layoutHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutMatch.setVisibility(View.INVISIBLE);
            }
        });
        //--------------
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("like").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){

                    //Show layout of Match
                    layoutMatch.setVisibility(View.VISIBLE);

                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).setValue(true);
                    usersDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void init() {
        //this.bt_back = findViewById(R.id.bt_back);
        this.bt_skip = findViewById(R.id.bt_skip);
        this.bt_like = findViewById(R.id.bt_like);
        this.layoutHide= findViewById(R.id.hideLayout);
        this.layoutMatch= findViewById(R.id.matchLayout);
    }
    private void AnimarFab(final FloatingActionButton fab){
        fab.animate().scaleX(0.7f).scaleY(0.7f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                fab.animate().scaleX(1f).scaleY(1f);
            }
        });
    }
/*
    private String userSex;
    private String oppositeUserSex;
    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //male
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals(user.getUid())) {
                    if(snapshot.exists()){
                       if(snapshot.child("sex") !=null){
                           userSex = snapshot.child("sex").getValue().toString();
                           oppositeUserSex = "Female";
                           switch (userSex){
                               case "Male":
                                   oppositeUserSex = "Female";
                                   break;
                               case "Female":
                                   oppositeUserSex = "Male";
                                   break;
                           }
                           getOppositeUserSex();
                       }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //female

    }
*/

    private String userSex;
    private String oppositeUserSex;
    public void checkUserSex(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                    if(snapshot.child("sex").getValue() != null) {
                        userSex = snapshot.child("sex").getValue().toString();
                        switch (userSex) {
                            case "Male":
                                oppositeUserSex = "Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }
                        getOppositeUserSex();
                    }

               }
            }
            /*
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            */

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void getOppositeUserSex() {
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() && !snapshot.child("connections").child("skip").hasChild(currentUId) && !snapshot.child("connections").child("like").hasChild(currentUId) && !snapshot.child("sex").getValue().toString().equals(oppositeUserSex )) {
                    String imageUrl = "default";
                    if (!snapshot.child("profileImageUrl").getValue().equals("default")) {
                        imageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }
                    cards Item = new cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), imageUrl);
                    rowItems.add(Item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}