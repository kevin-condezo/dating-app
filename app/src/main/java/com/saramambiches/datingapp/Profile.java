package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.saramambiches.datingapp.Utils.PulsatorLayout;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;

    private FloatingActionButton bt_setting, bt_photoAdd, bt_edit;

    private String userSex;

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


    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(Profile.this, LoginPage.class);
        startActivity(intent);
        finish();
    }
}