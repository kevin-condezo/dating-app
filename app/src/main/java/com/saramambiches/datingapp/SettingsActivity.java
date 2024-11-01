package com.saramambiches.datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;


public class SettingsActivity extends AppCompatActivity {

    ImageButton back;
    SeekBar distance;
    TextView distance_text, age_range;
    RangeSeekBar rangeSeekBar;
    Button SignOut;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.back);
        distance = findViewById(R.id.distance);
        distance_text = findViewById(R.id.distance_text);
        rangeSeekBar = findViewById(R.id.rangeSeekbar);
        age_range = findViewById(R.id.age_range);
        SignOut = findViewById(R.id.sign_out);

        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, Profile.class);
            startActivity(intent);
            finish();
        });

        SignOut.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginPage.class);
            startActivity(intent);
            finish();
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

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                age_range.setText(minValue + " - " + maxValue);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingsActivity.this, Profile.class);
        startActivity(intent);
        finish();
    }
}