package com.example.user.healthymate.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.healthymate.R;

public class AnimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        getSupportActionBar().hide();

        String name = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("description");
        String image = getIntent().getExtras().getString("background");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar);



        TextView textView1 = findViewById(R.id.description);
        ImageView imageView = findViewById(R.id.image_b);

        collapsingToolbarLayout.setTitle(name);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

        textView1.setText(description);

        Glide.with(this).load(image).apply(requestOptions).into(imageView);


    }

}
