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

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class RegisterAndLogin extends AppCompatActivity {
    Button btlogin, btregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);

        ImageSlider Imagenes=findViewById(R.id.slider);

        List<SlideModel> slideModels=new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.im1));
        slideModels.add(new SlideModel(R.drawable.im2));
        slideModels.add(new SlideModel(R.drawable.im3));
        slideModels.add(new SlideModel(R.drawable.im4));
        slideModels.add(new SlideModel(R.drawable.im5));
        slideModels.add(new SlideModel(R.drawable.im6));

        Imagenes.setImageList(slideModels, true);


        btlogin= (Button) findViewById(R.id.bt_login);
        btregister= (Button) findViewById(R.id.bt_register);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterAndLogin.this, LoginPage.class);
                startActivity(i);
                finish();
            }
        });
        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterAndLogin.this, RegisterPage.class);
                startActivity(i);
                finish();
            }
        });
    }

}