package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class RegisterAndLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;

    private MaterialButton buttonOnboardingAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);

        layoutOnboardingIndicators= findViewById(R.id.layoutOnboardingIndicators);
        buttonOnboardingAction = findViewById(R.id.buttonOnboardingAction);

        setupOnboardingItems();
        ViewPager2 onboardingViewPager= findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);

        //DETECTA EL CAMBIO DE PAGINA
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        //BOTÓN QUE DESLIZA LOS PASOS Y DIRECCIONA A LA PÁGINA DE REGISTRO
        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onboardingViewPager.getCurrentItem() + 1< onboardingAdapter.getItemCount()){
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem()+1);
                }else{
                    startActivity(new Intent(getApplicationContext(), RegisterPage.class));
                    finish();
                }
            }
        });

        //SI ES QUE ESTÁS LOGUEADO, YA NO ENTRA A REGISTERANDLOGIN
        //Redireccionamiento directo al PrincipalPage
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent i = new Intent(RegisterAndLogin.this, PrincipalPage.class);
                    startActivity(i);
                    finish();
                    return;
                }
            }
        };


        /*btlogin = (Button) findViewById(R.id.bt_login);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterAndLogin.this, RegisterPage.class);
                startActivity(i);
                finish();
            }
        });*/

    }

    //METODO PARA CREAR LOS PASOS DE ONBOARDING
    private void setupOnboardingItems(){
        List<OnboardingItem> onboardingItems= new ArrayList<>();

        OnboardingItem item_1= new OnboardingItem();
        item_1.setTitle("Mismos intereses");
        item_1.setDescription("¿Te gusta escuchar música?, ¿Harry Styles?, ¿Dua Lipa?, ¿Bad bunny?");
        item_1.setImage(R.drawable.im1);

        OnboardingItem item_2= new OnboardingItem();
        item_2.setTitle("¡Hagamos MATCH!");
        item_2.setDescription("Encuentra al Amor de tu vida, a tu Mejor Amig@, o tu choque y fuga ;)");
        item_2.setImage(R.drawable.im2);

        OnboardingItem item_3= new OnboardingItem();
        item_3.setTitle("Muchas Citas");
        item_3.setDescription("Miles de personas para conocer con tan solo deslizar");
        item_3.setImage(R.drawable.im3);

        onboardingItems.add(item_1);
        onboardingItems.add(item_2);
        onboardingItems.add(item_3);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    //METODO DE DE CREAR LOS PUNTOS QUE INDICAN QUE PAGINA ESTÁ
    private void setupOnboardingIndicators(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i=0; i<indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();
        for(int i=0; i< childCount; i++){
            ImageView imageView= (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if(i== index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            }else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }
        if (index ==onboardingAdapter.getItemCount()-1){
            buttonOnboardingAction.setText("Comencemos");
            //buttonOnboardingAction.setIcon(null);
        } else{
            buttonOnboardingAction.setText("Siguiente");
            //buttonOnboardingAction.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_next));


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}