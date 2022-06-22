package com.saramambiches.datingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyCallback;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class LoginPage extends AppCompatActivity {
    Button btloginf;
    FloatingActionButton btFB, btGOOGLE;
    TextView btredirectR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        btloginf= (Button) findViewById(R.id.bt_login_f);
        btredirectR=(TextView) findViewById(R.id.text_redirect_r);
        btFB=(FloatingActionButton) findViewById(R.id.bt_fb);
        btGOOGLE=(FloatingActionButton) findViewById(R.id.bt_google);

        //Funciones al presionar los botones
        btloginf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginPage.this, PrincipalPage.class);
                startActivity(i);
                finish();
            }
        });
        btredirectR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent i = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(i);
                finish();
            }
        });
        btFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginPage.this,"Aqui redirecciona a Facebook",Toast.LENGTH_LONG).show();
            }
        });
        btGOOGLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginPage.this,"Aqui redirecciona a google",Toast.LENGTH_LONG).show();
            }
        });
    }
    //Redireccionamiento de pagina
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LoginPage.this, RegisterAndLogin.class);
        startActivity(i);
        finish();
    }
}