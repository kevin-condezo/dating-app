package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Messages extends AppCompatActivity {
    private FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        String userSex = getIntent().getExtras().getString("sex");

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
                        //startActivity(new Intent(getApplicationContext(),Profile.class));
                        Intent i = new Intent(Messages.this, Profile.class);
                        i.putExtra("sex", userSex);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }

                return false;
            }
        });

        //-----------
    }
}