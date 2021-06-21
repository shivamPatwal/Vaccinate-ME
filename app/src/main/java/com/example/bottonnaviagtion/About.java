package com.example.bottonnaviagtion;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    private ActionBar toolbar;
    TextView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app);
        toolbar = getSupportActionBar();
        ImageView iconImage = findViewById(R.id.appimage);
        TextView heading = findViewById(R.id.heading);
        about = findViewById(R.id.about);
        toolbar.setTitle("About");


    }


}
