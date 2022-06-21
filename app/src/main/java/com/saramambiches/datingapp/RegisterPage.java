package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RegisterPage extends AppCompatActivity {
    Button btregisterf;
    FloatingActionButton btFB, btGOOGLE;
    TextView btredirectl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_page);
        btregisterf= (Button) findViewById(R.id.bt_register_f);
        btredirectl=(TextView) findViewById(R.id.text_redirect_l);
        btFB=(FloatingActionButton) findViewById(R.id.bt_fb);
        btGOOGLE=(FloatingActionButton) findViewById(R.id.bt_google);

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

        btFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterPage.this,"Aqui redirecciona a Facebook",Toast.LENGTH_LONG).show();
            }
        });
        btGOOGLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterPage.this,"Aqui redirecciona a google",Toast.LENGTH_LONG).show();
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