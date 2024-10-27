package com.saramambiches.datingapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.saramambiches.datingapp.R;

public class GenderSelect extends AppCompatActivity {
    private RadioGroup r_RadioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_select);
        r_RadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

    }

}