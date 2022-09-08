package com.saramambiches.datingapp.UI.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.saramambiches.datingapp.R;
import com.saramambiches.datingapp.SliderImageAdapter;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;


public class DescriptionActivity extends AppCompatActivity {
    TextView name, age, university, description;
    ImageView image;
    ArrayList<String> listImage;
    ViewPager viewPager;
    UnderlinePageIndicator underlinePageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        ItemModel element = (ItemModel) getIntent().getExtras().getSerializable("listItem");

        name = findViewById(R.id.name_info);
        image = findViewById(R.id.image_info);
        age = findViewById(R.id.age_info);
        university = findViewById(R.id.university_info);
        description = findViewById(R.id.description_info);
        underlinePageIndicator = (UnderlinePageIndicator) findViewById(R.id.underline);
        viewPager = findViewById(R.id.view_pager);


        name.setText(element.getName());
        age.setText(element.getAge());
        university.setText(element.getUniversity());
        description.setText(element.getDescription());
        Glide.with(this).load(element.getImage()).into(image);

        listImage = new ArrayList<>();

        listImage.add(element.getImage());
        listImage.add("https://i.pinimg.com/736x/37/8a/27/378a270e775265622393da8c0527417e.jpg");
        listImage.add("https://i.pinimg.com/736x/37/8a/27/378a270e775265622393da8c0527417e.jpg");


        viewPager.setAdapter(new SliderImageAdapter(this, listImage));

        underlinePageIndicator.setViewPager(viewPager);

    }
}