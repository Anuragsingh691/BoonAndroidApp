package com.example.boon_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boon_android_app.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class PaymentGateway extends AppCompatActivity {
    private ActivityMainBinding binding;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    EditText noteT;
    LinearLayout pay;
    String status;
    TextView amountT ,  nameT, upivirtualid;
    Uri uri;

    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        pay=findViewById(R.id.send);
        nameT=findViewById(R.id.acc_name);
        amountT=findViewById(R.id.acc_amount);
        upivirtualid=findViewById(R.id.acc_upi_Id);
        noteT=findViewById(R.id.note);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = amountT.getText().toString();
                String note = noteT.getText().toString();
                String name ="Shubhankar Arya";
                String upiId = "shubhankar4444.arya@okaxis.com";
                if (!amount.isEmpty()) {
                    uri = getUpiPaymentUri(name, upiId, note, amount);
                    payWithGPay();
                } else {
                    Toast.makeText(PaymentGateway.this, "All the information is required", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(PaymentGateway.this, "Please Install GPay", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        if ((RESULT_OK == resultCode) && status.equals("success")) {
            Toast.makeText(PaymentGateway.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PaymentGateway.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        }
    }


    
}