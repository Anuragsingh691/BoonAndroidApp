package com.example.boon_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boon_android_app.Prevalent.Prevalent;
import com.example.boon_android_app.model.tutors;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorDetailActivity extends AppCompatActivity {

    CircleImageView TutorImg;
    TextView TnameD,rate,reviews,tutions,level,subjects,expertise,experience;
    RatingBar ratingBar;
    private String pId="",downloadImageUrl;
    Button proceed;
    Dialog dialog;
    Integer t=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail);
        dialog=new Dialog(TutorDetailActivity.this);
        dialog.setContentView(R.layout.custom_dialog_confirm);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.card_bg));
        }
        Button confirm=dialog.findViewById(R.id.confirm_btn2);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TutorDetailActivity.this, "Confirm clicked", Toast.LENGTH_SHORT).show();
                final DatabaseReference TutorRefsDets= FirebaseDatabase.getInstance().getReference().child("Users");
                HashMap<String,Object> userMap = new HashMap<>();
                userMap.put("TeacherBooked",pId);
                TutorRefsDets.child(Prevalent.currentOnlineUsers.getPhone()).updateChildren(userMap);
                startActivity(new Intent(TutorDetailActivity.this, ConfirmedActivity.class));
                finish();
                dialog.dismiss();
            }
        });
        proceed=findViewById(R.id.proceed_btn_details);
        pId=getIntent().getStringExtra("pid");
        TutorImg=findViewById(R.id.tutor_img_details);
        TnameD=findViewById(R.id.tutor_name_details);
        rate=findViewById(R.id.tutor_rate_details);
        ratingBar=findViewById(R.id.details_ratingBar);
        reviews=findViewById(R.id.tutor_reviews_details);
        tutions=findViewById(R.id.tutions_txt);
        level=findViewById(R.id.Level_txt_details);
        subjects=findViewById(R.id.Subjects_txt_details);
        experience=findViewById(R.id.Exp_txt_details);
        expertise=findViewById(R.id.Expertise_txt_details);
        getProductsDetails(pId);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }
    private void getProductsDetails(final String pId) {
        final DatabaseReference TutorRefsDets= FirebaseDatabase.getInstance().getReference().child("Tutors");
        TutorRefsDets.child(pId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    tutors products=dataSnapshot.getValue(tutors.class);
                    // ddsds//
                    Picasso.get().load(products.getImage()).into(TutorImg);
                    TnameD.setText(products.getName());
                    level.setText(products.getClass1()+"th");
                    subjects.setText(products.getSub());
                    String ratings=dataSnapshot.child("rating").getValue().toString();
                    Float ratingFloat=Float.parseFloat(ratings);
                    ratingBar.setRating(ratingFloat);
                    CircleImageView dialogImg= dialog.findViewById(R.id.tutor_confirmation_image);
                    TextView dialogName= dialog.findViewById(R.id.confirmation_tutor_title_txt);
                    Picasso.get().load(products.getImage()).into(dialogImg);
                    dialogName.setText(products.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}