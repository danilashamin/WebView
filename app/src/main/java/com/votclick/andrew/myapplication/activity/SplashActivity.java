package com.votclick.andrew.myapplication.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.votclick.andrew.myapplication.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            Intent homeIntent = new Intent (SplashActivity.this, MainActivity.class);
            startActivity(homeIntent);

            finish();
        }, SPLASH_TIME_OUT);
    }
}
