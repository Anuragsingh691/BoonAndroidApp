package com.example.boon_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Profilesignup extends AppCompatActivity {
    Button btn;
    private EditText name, email;
    private CheckBox engish, hindi;
    private Slider slider;
    private float sliderValue;
    private String phone;
    private Boolean EngBool, HindBool;
    private String lang1=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesignup);
        btn=findViewById(R.id.save_btn);
        name=findViewById(R.id.name_edtText);
        email=findViewById(R.id.mail_edtText);
        engish=findViewById(R.id.checkbox_eng);
        phone=getIntent().getStringExtra("number");
        hindi=findViewById(R.id.checkbox_hindi);
        slider=findViewById(R.id.slider);
        engish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EngBool=isChecked;
                lang1="English";
            }
        });
        hindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HindBool=isChecked;
                lang1="Hindi";
            }
        });

        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                sliderValue=slider.getValue();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (TextUtils.isEmpty(name.getText().toString()))
               {
                   Toast.makeText(Profilesignup.this, "Please enter the user name", Toast.LENGTH_SHORT).show();
               }
               else if (TextUtils.isEmpty(email.getText().toString()))
               {
                   Toast.makeText(Profilesignup.this, "Please enter the user email", Toast.LENGTH_SHORT).show();
               }else if (!engish.isChecked() && !hindi.isChecked())
               {
                   Toast.makeText(Profilesignup.this, "Please select atleast one language", Toast.LENGTH_SHORT).show();
               }
               else if(engish.isChecked() && hindi.isChecked())
               {
                   Toast.makeText(Profilesignup.this, "Please select one language, you can't select two language at a time", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   String nametxt=name.getText().toString();
                   String mailTxt=email.getText().toString();
                   StoreDetails(phone,nametxt,mailTxt,EngBool,HindBool,sliderValue,lang1);
               }
            }
        });
    }

    private void StoreDetails(final String phone,final String name, final String email, final Boolean english,final Boolean hindi,final float sliderval,final String lang1) {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();
        Toast.makeText(this, "number = "+phone+" ref -" + Rootref, Toast.LENGTH_SHORT).show();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child("Users").child(phone).exists())
                {
                    HashMap<String,Object> userdatamap=new HashMap<>();
                    userdatamap.put("phone",phone);
                    userdatamap.put("name",name);
                    userdatamap.put("email",email);
                    userdatamap.put("language",lang1);
                    userdatamap.put("hindi",hindi);
                    userdatamap.put("class",sliderval);
                    Rootref.child("Users").child(phone).updateChildren(userdatamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(Profilesignup.this, "Congratulations Your Account has been created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Profilesignup.this,HomeActivity.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Profilesignup.this, "Sorry , Registration is not Complete ,Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Profilesignup.this, "Phone Number " + phone + " Exists Already", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Profilesignup.this, "Please Use different Phone number", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}