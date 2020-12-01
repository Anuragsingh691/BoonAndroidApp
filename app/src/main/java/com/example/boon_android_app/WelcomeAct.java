package com.example.boon_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boon_android_app.Prevalent.Prevalent;
import com.example.boon_android_app.Users.Users;
import com.example.boon_android_app.model.AddCategory;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import io.paperdb.Paper;

import static com.example.boon_android_app.Prevalent.Prevalent.UserPhoneKey;

public class WelcomeAct extends AppCompatActivity {
    private static final String TAG = WelcomeAct.class.getSimpleName();

private Button login, signup,add;
private EditText number;
private CountryCodePicker ccp;
private String parentDbName="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        login=findViewById(R.id.login_btn);
        signup=findViewById(R.id.signUp_btn);
        number=findViewById(R.id.phone_txt);
        ccp=findViewById(R.id.ccp);
        Paper.init(this);
        ccp.registerCarrierNumberEditText(number);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeAct.this,SignupActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(number.getText().toString().trim()))
                {
                    Toast.makeText(WelcomeAct.this, "Number field is empty, please enter the phone number", Toast.LENGTH_SHORT).show();
                }
                else if (number.length()<10)
                {
                    Toast.makeText(WelcomeAct.this, " please enter the phone number with length of 10 digit", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String ph_no=number.getText().toString().trim();
                    final String getNo = "+"+ccp.getFullNumber();
                    final DatabaseReference Rootref;
                    Rootref= FirebaseDatabase.getInstance().getReference();
                    Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(parentDbName).child(getNo).exists())
                            {
                                Intent intent = new Intent(getApplicationContext(),OtpVerification.class);
                                intent.putExtra("number",getNo);
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                Toast.makeText(WelcomeAct.this, "The phone number does not exists in the database .. please Sign Up", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

        });
    }
}