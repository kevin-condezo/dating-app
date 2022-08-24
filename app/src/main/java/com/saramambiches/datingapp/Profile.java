package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saramambiches.datingapp.Utils.PulsatorLayout;

import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    BottomNavigationView bottomNavigationView;

    private FloatingActionButton bt_setting, bt_photoAdd, bt_edit;
    private CircleImageView mProfileImage;
    private TextView mName;//, mUniversity;
    private String userId, name, profileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();

        bt_setting= (FloatingActionButton) findViewById(R.id.setting);
        bt_photoAdd= (FloatingActionButton) findViewById(R.id.photoAdd);
        bt_edit= (FloatingActionButton) findViewById(R.id.edit);

        mName = findViewById(R.id.txt_nombre);
        //mUniversity = findViewById(R.id.txt_universidad);
        mProfileImage = findViewById(R.id.circle_profile_image);

        mAuth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();

        //Navigation Bar

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_user);

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
                        startActivity(new Intent(getApplicationContext(),Messages.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.nav_user:
                        return true;
                }

                return false;
            }
        });

        //-----------
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this, SettingsActivity.class);
                startActivity(i);
                finish();
            }
        });

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this, EditProfileActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    // Se obtienen los datos del usuario: nombre e imagen de perfil
    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        name = Objects.requireNonNull(map.get("name")).toString();
                        mName.setText(name);
                    }
                    Glide.with(mProfileImage);
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = Objects.requireNonNull(map.get("profileImageUrl")).toString();
                        if ("default".equals(profileImageUrl)) {
                            Glide.with(getApplication()).load("https://zultimate.com/wp-content/uploads/2019/12/default-profile.png").into(mProfileImage);
                        } else {
                            Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(Profile.this, LoginPage.class);
        startActivity(intent);
        finish();
    }
}