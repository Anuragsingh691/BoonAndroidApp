package com.example.boon_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class SignupActivity extends AppCompatActivity {

    private Button  signup;
    private EditText number;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup=findViewById(R.id.signUp_btn);
        number=findViewById(R.id.phone_txt);
        ccp=findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(number);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(number.getText().toString().trim()))
                {
                    Toast.makeText(SignupActivity.this, "Number field is empty, please enter the phone number", Toast.LENGTH_SHORT).show();
                }
                else if (number.length()<10)
                {
                    Toast.makeText(SignupActivity.this, " please enter the phone number with length of 10 digit", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String ph_no=number.getText().toString().trim();
                    String getNo = "+"+ccp.getFullNumber();
                    Intent intent = new Intent(getApplicationContext(),OtpVerification2.class);
                    intent.putExtra("number",getNo);
                    startActivity(intent);
                }
            }
        });
    }
}