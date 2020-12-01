package com.example.boon_android_app.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boon_android_app.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddOffers extends AppCompatActivity {

    private EditText heading, desc;
    private Button add;
    private String Oheading,Odesc;
    private String productRandomKey,downloadImageUrl;
    private StorageReference offersImageRefs;
    private static final int gallerypic=1;
    private DatabaseReference ProductsRefs,UsersRefs;
    private ProgressDialog mProProgress;
    private String sName,sPhone,sEmail,saveCurrentDate,saveCurrentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offers);
        heading = findViewById(R.id.select_offers_name);
        desc=findViewById(R.id.select_offers_desc);
        add=findViewById(R.id.add_new_offer_btn);
        mProProgress=new ProgressDialog(this);
        UsersRefs= FirebaseDatabase.getInstance().getReference().child("Users");
        ProductsRefs=FirebaseDatabase.getInstance().getReference().child("Offers");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
        UsersRefs.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            sName = dataSnapshot.child("name").getValue().toString();
                            sPhone = dataSnapshot.child("phone").getValue().toString();
                            sEmail = dataSnapshot.child("email").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void ValidateProductData() {
        Oheading=heading.getText().toString();
        Odesc=desc.getText().toString();
        if (TextUtils.isEmpty(Oheading))
        {
            Toast.makeText(this, "Offer name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Odesc))
        {
            Toast.makeText(this, "Please , Write the description of the offer", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInfo();
        }
    }
    private void StoreProductInfo() {
        mProProgress.setTitle("Adding New Offer");
        mProProgress.setMessage("Please wait while we are adding new tutor !");
        mProProgress.setCanceledOnTouchOutside(false);
        mProProgress.show();
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        productRandomKey=saveCurrentDate+saveCurrentTime;
        saveProductInfoToDatabase();
    }

    private void saveProductInfoToDatabase() {
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("heading",Oheading);
        productMap.put("desc",Odesc);
        productMap.put("UserName",sName);
        productMap.put("UserEmail",sEmail);
        productMap.put("SellerPhone",sPhone);
        ProductsRefs.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    startActivity(new Intent(AddOffers.this, OffersAddCategory.class));
                    mProProgress.dismiss();
                    Toast.makeText(AddOffers.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProProgress.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(AddOffers.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}