package com.example.boon_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.boon_android_app.Prevalent.Prevalent;
import com.example.boon_android_app.Search.BestRatingFragment;
import com.example.boon_android_app.Search.MaxExpFragment;
import com.example.boon_android_app.Search.TrendingSearchFragment;
import com.example.boon_android_app.homeFragments.TrendingFrag;
import com.example.boon_android_app.model.TutorVH;
import com.example.boon_android_app.model.tutors;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private ImageView searchImg,back;
    private EditText searchEdt;
    FrameLayout frameLayout;
    private RecyclerView searchRv;
    private RecyclerView.LayoutManager layoutManager;
    private String searchInput;
    private FloatingActionButton chatFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.frameLayout2);
        chatFab=findViewById(R.id.chat_fab_search);
        chatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this,ChatBotAct.class));
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout2,new BestRatingFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomMethod2);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomMethod2 =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId())
                    {
                        case R.id.bottom_best_rating:
                            fragment=new BestRatingFragment();
                            break;
                        case R.id.bottom_nav_trending_search:
                            fragment=new TrendingSearchFragment();
                            break;
                        case R.id.bottom_nav_max_search:
                            fragment=new MaxExpFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout2,fragment).commit();
                    return true;
                }
            };
}