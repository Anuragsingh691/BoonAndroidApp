package com.example.boon_android_app.model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.boon_android_app.HomeActivity;
import com.example.boon_android_app.R;

public class AddCategory extends AppCompatActivity {
    private ImageView tshirts,SportTshirts,femaleDress,sweaters;
    private ImageView glasses,hats,purse,shoes;
    private ImageView headphones,laptop,watches,mobiles;
    private Button homebtn,logoutBtn,checkOrders,maintainBtn,ApproveBtn;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        homebtn=(Button)findViewById(R.id.home_btn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddCategory.this, HomeActivity.class));
                finish();
            }
        });

        tshirts=(ImageView)findViewById(R.id.cat_tshirts);
        SportTshirts=(ImageView)findViewById(R.id.cat_sports_tshirts);
        femaleDress=(ImageView)findViewById(R.id.cat_femaleDress);
        sweaters=(ImageView)findViewById(R.id.cat_sweaters);
        glasses=(ImageView)findViewById(R.id.cat_glasses);
        hats=(ImageView)findViewById(R.id.cat_hats);
        purse=(ImageView)findViewById(R.id.cat_purse);
        shoes=(ImageView)findViewById(R.id.cat_shoes);
        headphones=(ImageView)findViewById(R.id.cat_headphones);
        laptop=(ImageView)findViewById(R.id.cat_laptops);
        watches=(ImageView)findViewById(R.id.cat_watches);
        mobiles=(ImageView)findViewById(R.id.cat_mobiles);
        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Tshirts");
                startActivity(intent);
                finish();
            }
        });
        SportTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Sports tshirts");
                startActivity(intent);
                finish();
            }
        });
        femaleDress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Female Dress");
                startActivity(intent);
                finish();
            }
        });
        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Sweaters ");
                startActivity(intent);
                finish();
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Glasses");
                startActivity(intent);
                finish();
            }
        });
        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Hats");
                startActivity(intent);
                finish();
            }
        });
        purse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Purse");
                startActivity(intent);
                finish();
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Shoes");
                startActivity(intent);
                finish();
            }
        });
        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","HeadPhone");
                startActivity(intent);
                finish();
            }
        });
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Laptop");
                startActivity(intent);
                finish();
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Watches");
                startActivity(intent);
                finish();
            }
        });
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddCategory.this, AddTutor.class);
                intent.putExtra("Category","Mobiles");
                startActivity(intent);
                finish();
            }
        });
    }
}