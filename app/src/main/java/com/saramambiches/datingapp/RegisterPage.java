package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterPage extends AppCompatActivity {
    Button btregisterf;
    TextView btredirectl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_page);
        btregisterf= (Button) findViewById(R.id.bt_register_f);
        btredirectl=(TextView) findViewById(R.id.text_redirect_l);

        btregisterf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterPage.this, PrincipalPage.class);
                startActivity(i);
                finish();
            }
        });
        btredirectl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent i = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(i);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RegisterPage.this, RegisterAndLogin.class);
        startActivity(i);
        finish();
    }
}