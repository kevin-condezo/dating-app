package com.saramambiches.datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class RegisterAndLogin extends AppCompatActivity {
    Button btlogin;
    TextView btredirect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);


        btredirect=(TextView)findViewById(R.id.bt_redirect);
        btlogin= (Button) findViewById(R.id.bt_login);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterAndLogin.this, PrincipalPage.class);
                startActivity(i);
                finish();
            }
        });
        btredirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterAndLogin.this, RegisterPage.class);
                startActivity(i);
                finish();
            }
        });
    }

}