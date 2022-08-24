package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrincipalPage extends AppCompatActivity {
    private CardsAdapter adaptadorItems;

    private FirebaseAuth mAuth;
    private String currentUId;
    private DatabaseReference usersDb;

    private FloatingActionButton bt_back, bt_skip, bt_like;
    BottomNavigationView bottomNavigationView;

    private Button layoutHide;
    private Button bt_smsRedirect;
    private LinearLayout layoutMatch;

    private CircleImageView myImage, matchImage;
    private String myImageUrl, matchImageUrl;

    Queue<Cards> colaItems;

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

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId= mAuth.getCurrentUser().getUid();

        checkUserSex();

        colaItems = new LinkedList<Cards>();

        adaptadorItems = new CardsAdapter(this, R.layout.item, (List<Cards>) colaItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(adaptadorItems);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("colaItems", "item eliminado");
                // se elimina (desencola) el primer elemento de la cola de items
                colaItems.remove();
                adaptadorItems.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Cards object = (Cards) dataObject;
                String userId = object.getUserId();
                usersDb.child(userId).child("connections").child("skip").child(currentUId).setValue(true);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards object = (Cards) dataObject;
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
            }
        });

        //Botones flotantes

        init();

        bt_smsRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //redireccionar a la pagina de mensajes
                startActivity(new Intent(getApplicationContext(), Messages.class));
                finish();
            }
        });

        bt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimarFab(bt_skip);
                try {
                    flingContainer.getTopCardListener().selectLeft();
                } catch (Exception e) {
                    Toast.makeText(PrincipalPage.this, "No hay más usuarios", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bt_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimarFab(bt_like);
                try {
                    flingContainer.getTopCardListener().selectRight();
                } catch (Exception e) {
                    Toast.makeText(PrincipalPage.this, "No hay más usuarios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        layoutHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutMatch.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("like").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).child("ChatId").setValue(key);

                    // Se cargan las imagenes de perfil de ambos usuarios
                    usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                            Glide.with(myImage);
                            myImageUrl = datasnapshot.child(currentUId).child("profileImageUrl").getValue().toString();
                            if ("default".equals(myImageUrl)) {
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(myImage);
                            } else {
                                Glide.with(getApplication()).load(myImageUrl).into(myImage);
                            }

                            Glide.with(matchImage);
                            matchImageUrl = datasnapshot.child(snapshot.getKey()).child("profileImageUrl").getValue().toString();
                            if ("default".equals(matchImageUrl)) {
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(matchImage);
                            } else {
                                Glide.with(getApplication()).load(matchImageUrl).into(matchImage);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    //Show layout of Match
                    layoutMatch.setVisibility(View.VISIBLE);
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
        this.bt_smsRedirect= findViewById(R.id.sms_redirect);

        this.myImage = findViewById(R.id.profile_image_you);
        this.matchImage = findViewById(R.id.profile_image_match);
    }
    private void AnimarFab(final FloatingActionButton fab){
        fab.animate().scaleX(0.7f).scaleY(0.7f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                fab.animate().scaleX(1f).scaleY(1f);
            }
        });
    }

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void getOppositeUserSex() {
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.child("sex").getValue() !=null) {
                    if (snapshot.exists() && !snapshot.child("connections").child("skip").hasChild(currentUId) && !snapshot.child("connections").child("like").hasChild(currentUId) && snapshot.child("sex").getValue().toString().equals(oppositeUserSex) && !snapshot.getKey().equals(currentUId)) {
                        String imageUrl = "default";
                        if (!snapshot.child("profileImageUrl").getValue().equals("default")) {
                            imageUrl = snapshot.child("profileImageUrl").getValue().toString();
                        }
                        Cards Item = new Cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), imageUrl);
                        colaItems.add(Item); // se agrega el item a la cola
                        adaptadorItems.notifyDataSetChanged(); // se actualiza la vista
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
    }
}