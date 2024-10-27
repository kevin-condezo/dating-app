package com.saramambiches.datingapp.register;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.saramambiches.datingapp.R;

public class OrientationSelect extends AppCompatActivity {
    private RadioGroup r_RadioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_select);
        r_RadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

    }

}