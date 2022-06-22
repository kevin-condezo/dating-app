package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Messages extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

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
                        return true;
                    case R.id.nav_sms:
                        return true;
                    case R.id.nav_user:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;

                }

                return false;
            }
        });

        //-----------
    }
}