package com.example.boon_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LinearDotsLoader;
import com.chaos.view.PinView;
import com.example.boon_android_app.Prevalent.Prevalent;
import com.example.boon_android_app.Users.Users;
import com.example.boon_android_app.model.AddCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

import static com.example.boon_android_app.Prevalent.Prevalent.UserPhoneKey;

public class OtpVerification extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = OtpVerification.class.getSimpleName();
     Button verify,resend,changeNo,pay;
     TextView waittext,WelcomeTxt,retry;
     PinView pinView;
    LinearLayout containerLL;
    String codeBySystem;
    String phone_number;
    private String parentDbName="Users";
    final DatabaseReference TutorRefsDets= FirebaseDatabase.getInstance().getReference().child("Users");
    PhoneAuthProvider.ForceResendingToken token ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        verify=(Button) findViewById(R.id.verify_btn);
        Paper.init(this);
        pay=findViewById(R.id.payment);
        resend=(Button) findViewById(R.id.resend_btn);
         phone_number = getIntent().getStringExtra("number");
        retry = findViewById(R.id.retry_text);
        WelcomeTxt = findViewById(R.id.welcome_txt);
        changeNo=(Button) findViewById(R.id.change_no);
        changeNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(OtpVerification.this,WelcomeAct.class));
                    finish();

            }
        });
        waittext = (TextView) findViewById(R.id.wait_text);
        pinView = findViewById(R.id.firstPinView);
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userdata=snapshot.child(parentDbName).child(phone_number).getValue(Users.class);
                if (snapshot.child(parentDbName).child(phone_number).exists())
                {
                    sendVerificationCode(phone_number);
                    Paper.book().write(UserPhoneKey,phone_number);
                    Prevalent.currentOnlineUsers=userdata;
                }
                else
                {
                    Toast.makeText(OtpVerification.this, "The phone number does not exists in the database .. please Sign Up", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    resendVerificationCode(phone_number,token);
                    Toast.makeText(OtpVerification.this, "Wait While resending the otp", Toast.LENGTH_SHORT).show();

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
                LinearDotsLoader loader = new LinearDotsLoader(OtpVerification.this);
                loader.setDefaultColor(ContextCompat.getColor(OtpVerification.this, R.color.loader_defalut));
                loader.setSelectedColor(ContextCompat.getColor(OtpVerification.this, R.color.blue_default));
                loader.setSingleDir(true);
                loader.setNoOfDots(3);
                loader.setSelRadius(30);
                loader.setExpandOnSelect(true);
                loader.setRadius(20);
                loader.setDotsDistance(30);
                loader.setAnimDur(300);
                loader.setShowRunningShadow(true);
                loader.setFirstShadowColor(ContextCompat.getColor(OtpVerification.this, R.color.blue_selected));
                loader.setSecondShadowColor(ContextCompat.getColor(OtpVerification.this, R.color.blue_default));
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
                    Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUsers.getPhone());
                            userref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                    {
                                        if (snapshot.child("TeacherBooked").exists())
                                        {
                                            String teacher=snapshot.child("TeacherBooked").getValue().toString();
                                            if (!teacher.isEmpty()){
                                                Toast.makeText(OtpVerification.this, "Payment is yet to be done", Toast.LENGTH_SHORT).show();
                                                pay.setVisibility(View.VISIBLE);
                                                pay.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        startActivity(new Intent(OtpVerification.this,HomeActivity.class));
                                                        finish();
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                Toast.makeText(OtpVerification.this, "Welcome ", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(OtpVerification.this,HomeActivity.class));
                                                finish();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(OtpVerification.this, "Welcome ", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(OtpVerification.this,HomeActivity.class));
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
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
    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Boon ");
            options.put("description", "Tution charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(final String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            final DatabaseReference userrefP = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUsers.getPhone());
            userrefP.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        if (snapshot.child("TeacherBooked").exists()){
                            userrefP.child("TeacherBooked").removeValue();
                            HashMap<String,Object> userMap = new HashMap<>();
                            userMap.put("Payment-ID",razorpayPaymentID);
                            userrefP.child("Payments").updateChildren(userMap);
                            startActivity(new Intent(OtpVerification.this, HomeActivity.class));
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

}