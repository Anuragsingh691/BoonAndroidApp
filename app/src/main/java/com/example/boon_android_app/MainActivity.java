package com.example.boon_android_app;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.boon_android_app.Prevalent.Prevalent;
import com.example.boon_android_app.Users.Users;
import com.example.boon_android_app.model.AddCategory;
import com.example.boon_android_app.model.AddOffers;
import com.example.boon_android_app.model.AddTutor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
    private String userPhone="";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static  int SPLASH_SCREEN=2000;
    FirebaseUser firebaseUser;
    private ProgressDialog mLogProgress;
    private FirebaseAuth mAuth;
    String UserPhoneKey;
    Dialog dialogPay;
    public String status;
    Uri uri;
    String sAmount= "10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        dialogPay=new Dialog(MainActivity.this);
        dialogPay.setContentView(R.layout.payment_dialog);
        dialogPay.setCancelable(false);
        dialogPay.getWindow().getAttributes().windowAnimations = R.style.animation;
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            dialogPay.getWindow().setBackgroundDrawable(getDrawable(R.drawable.card_bg));
        }
        final int amount=Math.round(Float.parseFloat(sAmount)*100);
        Button confirm=dialogPay.findViewById(R.id.confirm_btn2);
        Checkout.preload(getApplicationContext());
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
                dialogPay.dismiss();
            }
        });
        firebaseUser=mAuth.getCurrentUser();
        Paper.init(this);
        UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        userPhone=UserPhoneKey;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            status = data.getStringExtra("status").toLowerCase();
        }
        if (RESULT_OK == resultCode && status.equals("success")){
            Toast.makeText(this, "Transaction Successful"+data.getStringExtra("ApprovalRefNo"), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser==null)
        {
            Intent splash = new Intent(MainActivity.this, WelcomeAct.class);
            splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(splash);
            finish();
        }
        else
        {
            if (UserPhoneKey!=null)
            {
                Allowacess(UserPhoneKey);
            }
            else
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent splash = new Intent(MainActivity.this, WelcomeAct.class);
                        splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(splash);
                        finish();
                    }
                },SPLASH_SCREEN);
            }

        }
    }
    private void Allowacess(final String phone) {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    try {
                        final Users userdata=dataSnapshot.child("Users").child(phone).getValue(Users.class);
                        if (userdata.getPhone().equals(phone))
                        {
                            DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
                            userref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                    {
                                        if (snapshot.child("TeacherBooked").exists())
                                        {
                                            String teacher=snapshot.child("TeacherBooked").getValue().toString();
                                            if (!teacher.isEmpty()){
                                               dialogPay.show();
                                            }
                                            else
                                            {
                                                Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                                Intent startintent=new Intent(MainActivity.this, HomeActivity.class);
                                                Prevalent.currentOnlineUsers=userdata;
                                                startActivity(startintent);
                                                finish();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                            Intent startintent=new Intent(MainActivity.this, HomeActivity.class);
                                            Prevalent.currentOnlineUsers=userdata;
                                            startActivity(startintent);
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    } catch (Exception e)
                    {
                        Toast.makeText(MainActivity.this, " Exception "+e, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, WelcomeAct.class));
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Account with the given phone number doesn't exist", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, WelcomeAct.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(final String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            final DatabaseReference userrefP = FirebaseDatabase.getInstance().getReference().child("Users").child(userPhone);
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
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
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