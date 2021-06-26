package com.example.findlost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CreatePostActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.createPostToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Share Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}