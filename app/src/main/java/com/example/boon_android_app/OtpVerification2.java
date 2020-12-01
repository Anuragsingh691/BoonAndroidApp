package com.example.boon_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LinearDotsLoader;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerification2 extends AppCompatActivity {
    Button verify,resend,changeNo;
    TextView waittext,WelcomeTxt,retry;
    PinView pinView;
    LinearLayout containerLL;
    String codeBySystem;
    PhoneAuthProvider.ForceResendingToken token ;
     String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification2);
        verify=(Button) findViewById(R.id.verify_btn);
        resend=(Button) findViewById(R.id.resend_btn);
        phone_number = getIntent().getStringExtra("number");
        retry = findViewById(R.id.retry_text);
        WelcomeTxt = findViewById(R.id.welcome_txt);
        changeNo=(Button) findViewById(R.id.change_no);
        changeNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OtpVerification2.this,SignupActivity.class));
                finish();

            }
        });
        waittext = (TextView) findViewById(R.id.wait_text);
        pinView = findViewById(R.id.firstPinView);
        sendVerificationCode(phone_number);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phone_number,token);
                Toast.makeText(OtpVerification2.this, "Wait While resending the otp", Toast.LENGTH_SHORT).show();

            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Otp = pinView.getText().toString();

                if (!Otp.isEmpty())
                {
                    verifyCode(Otp);
                }

                verify.setVisibility(v.INVISIBLE);
                resend.setVisibility(v.INVISIBLE);
                changeNo.setVisibility(v.INVISIBLE);
                waittext.setVisibility(v.VISIBLE);
                containerLL = (LinearLayout) findViewById(R.id.layout);
                LinearDotsLoader loader = new LinearDotsLoader(OtpVerification2.this);
                loader.setDefaultColor(ContextCompat.getColor(OtpVerification2.this, R.color.loader_defalut));
                loader.setSelectedColor(ContextCompat.getColor(OtpVerification2.this, R.color.blue_default));
                loader.setSingleDir(true);
                loader.setNoOfDots(3);
                loader.setSelRadius(30);
                loader.setExpandOnSelect(true);
                loader.setRadius(20);
                loader.setDotsDistance(30);
                loader.setAnimDur(300);
                loader.setShowRunningShadow(true);
                loader.setFirstShadowColor(ContextCompat.getColor(OtpVerification2.this, R.color.blue_selected));
                loader.setSecondShadowColor(ContextCompat.getColor(OtpVerification2.this, R.color.blue_default));
                containerLL.addView(loader);

            }

        });
    }
    private void sendVerificationCode(String phone_number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone_number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                    token = forceResendingToken;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code!=null)
                    {
                        pinView.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    WelcomeTxt.setText("Otp didn't match");
                    retry.setVisibility(View.VISIBLE);
                    Toast.makeText(OtpVerification2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OtpVerification2.this, "Welcome", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(OtpVerification2.this,Profilesignup.class);
                            intent.putExtra("number",phone_number);
                            startActivity(intent);
                            finish();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                WelcomeTxt.setText("Otp didn't match");
                                retry.setVisibility(View.VISIBLE);
                                containerLL.setVisibility(View.INVISIBLE);

                            }
                        }
                    }
                });
    }
}