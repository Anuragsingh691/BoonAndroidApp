package com.example.boon_android_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.boon_android_app.Prevalent.Prevalent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    TextView title,eng,hin;
    private RelativeLayout searchLayout,home;
    private ImageView logo;
    FloatingActionButton chatFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logo=findViewById(R.id.pg_logo);
        eng=findViewById(R.id.eng_text);
        hin=findViewById(R.id.hin_text);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        title=findViewById(R.id.advaitya);
        home=findViewById(R.id.language_24);
        searchLayout=(RelativeLayout)findViewById(R.id.search_layout);
        chatFab=findViewById(R.id.chat_fab);
        chatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ChatBotAct.class));
            }
        });
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SearchActivity2.class));
            }
        });
        if (Prevalent.currentOnlineUsers.getName()!=null)
        {
            title.setText(Prevalent.currentOnlineUsers.getName());
        }
        else
        {
            title.setText("UserName");
        }
        frameLayout = findViewById(R.id.frameLayout);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Home2Act()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomMethod);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener bottomMethod =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId())
                    {
                        case R.id.bottom_nav_home:
                            logo.setVisibility(View.VISIBLE);
                            title.setVisibility(View.VISIBLE);
                            home.setVisibility(View.VISIBLE);
                            eng.setVisibility(View.VISIBLE);
                            hin.setVisibility(View.VISIBLE);
                            searchLayout.setVisibility(View.VISIBLE);
                            fragment=new Home2Act();
                            title.setText(Prevalent.currentOnlineUsers.getName());
                            break;
                        case R.id.bottom_nav_profile:
                            logo.setVisibility(View.INVISIBLE);
                            title.setVisibility(View.INVISIBLE);
                            home.setVisibility(View.INVISIBLE);
                            eng.setVisibility(View.INVISIBLE);
                            hin.setVisibility(View.INVISIBLE);
                            searchLayout.setVisibility(View.INVISIBLE);

                            fragment=new ProfileAct();
                            break;
                        case R.id.bottom_nav_tutor:
                            logo.setVisibility(View.VISIBLE);
                            title.setVisibility(View.VISIBLE);
                            home.setVisibility(View.VISIBLE);
                            eng.setVisibility(View.VISIBLE);
                            hin.setVisibility(View.VISIBLE);
                            searchLayout.setVisibility(View.VISIBLE);

                            fragment=new TutorAct();
                            title.setText("Tutors");
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();

                    return true;
                }
            };

}