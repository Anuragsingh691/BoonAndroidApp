package com.example.boon_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import io.paperdb.Paper;

public class ConfirmedActivity extends AppCompatActivity {
    private static  int SPLASH_SCREEN=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed);
        Paper.init(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash = new Intent(ConfirmedActivity.this, WelcomeAct.class);
                splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                splash.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Paper.book().destroy();
                startActivity(splash);
                finish();
            }
        },SPLASH_SCREEN);
    }
}