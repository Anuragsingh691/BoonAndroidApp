package com.example.boon_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.agrawalsuneet.dotsloader.loaders.LinearDotsLoader;

public class LoadActivity extends AppCompatActivity {
    private static  int SPLASH_SCREEN=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        LinearLayout containerLL = (LinearLayout) findViewById(R.id.layout);
        LinearDotsLoader loader = new LinearDotsLoader(this);
        loader.setDefaultColor(ContextCompat.getColor(this, R.color.loader_defalut));
        loader.setSelectedColor(ContextCompat.getColor(this, R.color.loader_selected));
        loader.setSingleDir(true);
        loader.setNoOfDots(3);
        loader.setSelRadius(40);
        loader.setExpandOnSelect(true);
        loader.setRadius(30);
        loader.setDotsDistance(20);
        loader.setAnimDur(300);
        loader.setShowRunningShadow(true);
        loader.setFirstShadowColor(ContextCompat.getColor(this, R.color.blue_selected));
        loader.setSecondShadowColor(ContextCompat.getColor(this, R.color.blue_default));
        containerLL.addView(loader);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash = new Intent(LoadActivity.this, HomeActivity.class);
                startActivity(splash);
                finish();
            }
        },SPLASH_SCREEN);
    }
}