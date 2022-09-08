package com.saramambiches.datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener;
import com.mohammedalaa.seekbar.DoubleValueSeekBarView;
import com.saramambiches.datingapp.UI.Profile.Profile;


public class SettingsActivity extends AppCompatActivity {

    ImageButton back;
    SeekBar distance;
    TextView distance_text, age_range;
    DoubleValueSeekBarView doubleValueSeekBarView;
    Button SignOut;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.back);
        distance = findViewById(R.id.distance);
        distance_text = findViewById(R.id.distance_text);
        doubleValueSeekBarView = findViewById(R.id.double_range_seekbar);
        age_range = findViewById(R.id.age_range);
        SignOut = findViewById(R.id.sign_out);

        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(v -> {
            finish();
        });

        SignOut.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginPage.class);
            startActivity(intent);
            finish();
        });

        doubleValueSeekBarView.setOnRangeSeekBarViewChangeListener(new OnDoubleValueSeekBarChangeListener() {
            @Override
            public void onValueChanged(DoubleValueSeekBarView doubleValueSeekBarView, int i, int i1, boolean b) {
                age_range.setText(i + " - " + i1);
            }

            @Override
            public void onStopTrackingTouch(DoubleValueSeekBarView doubleValueSeekBarView, int i, int i1) {

            }

            @Override
            public void onStartTrackingTouch(DoubleValueSeekBarView doubleValueSeekBarView, int i, int i1) {

            }
        });



        distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance_text.setText(progress + " Km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

}