package com.example.boon_android_app.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddTutor extends AppCompatActivity {

    private EditText name, email ,  language1, language2,Uclass,rating,sub;
    private String CategoryName,Temail,Tlanguage1,Name,saveCurrentDate,saveCurrentTime,Tlanguage2,TClass,Trating,Tsub;
    private ImageView image;
    private Button saveBtn;
    private StorageReference ProductImageRefs;
    private static final int gallerypic=1;
    private Uri ImageUri;
    private String productRandomKey,downloadImageUrl;
    private DatabaseReference ProductsRefs,UsersRefs;
    private ProgressDialog mProProgress;
    private String sName,sPhone,sEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tutor);
        name=findViewById(R.id.select_product_name);
        email=findViewById(R.id.select_product_desc);
        mProProgress=new ProgressDialog(this);
        image=findViewById(R.id.select_product_img);
        saveBtn=findViewById(R.id.add_new_product_btn);
        sub=findViewById(R.id.sub);
        ProductImageRefs= FirebaseStorage.getInstance().getReference().child("Product Images");
        UsersRefs= FirebaseDatabase.getInstance().getReference().child("Users");
        ProductsRefs=FirebaseDatabase.getInstance().getReference().child("Tutors");
        language1=findViewById(R.id.eng_text);
        Uclass=findViewById(R.id.Uclass);
        rating=findViewById(R.id.select_product_price);
        language2=findViewById(R.id.language2);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
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
    private void openGallery() {
        Intent galleryintent=new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,gallerypic);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==gallerypic && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            image.setImageURI(ImageUri);
        }
    }
    private void ValidateProductData() {
        Temail=email.getText().toString();
        Trating=rating.getText().toString();
        Name=name.getText().toString();
        Tsub=sub.getText().toString();
        Tlanguage1=language1.getText().toString();
        Tlanguage2=language2.getText().toString();
        TClass=Uclass.getText().toString();
        if (ImageUri==null)
        {
            Toast.makeText(this, "Tutor Image is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Temail))
        {
            Toast.makeText(this, "Please , Write the image", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Trating))
        {
            Toast.makeText(this, "Please , Write the Price", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Name))
        {
            Toast.makeText(this, "Please , Write the Tutor Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Tlanguage1))
        {
            Toast.makeText(this, "Please , Write the Language 1 of tutor", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Tlanguage2))
        {
            Toast.makeText(this, "Please , Write the Language 2 of tutor", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(TClass))
        {
            Toast.makeText(this, "Please , Write the Tutor class", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Tsub))
        {
            Toast.makeText(this, "Please , Write the subject of the tutor", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInfo();
        }
    }
    private void StoreProductInfo() {
        mProProgress.setTitle("Adding New Tutor");
        mProProgress.setMessage("Please wait while we are adding new tutor !");
        mProProgress.setCanceledOnTouchOutside(false);
        mProProgress.show();
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        productRandomKey=saveCurrentDate+saveCurrentTime;
        final StorageReference filepath=ProductImageRefs.child(ImageUri.getLastPathSegment()+ productRandomKey + ".jpg" );
        final UploadTask uploadTask=filepath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message= e.toString();
                Toast.makeText(AddTutor.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                mProProgress.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddTutor.this, "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }
                        downloadImageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(AddTutor.this, "got Product Image Saved to Database Successfully", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("Email",Temail);
        productMap.put("class1",TClass);
        productMap.put("image",downloadImageUrl);
        productMap.put("rating",Trating);
        productMap.put("sub",Tsub);
        productMap.put("Name",Name);
        productMap.put("UserName",sName);
        productMap.put("UserEmail",sEmail);
        productMap.put("SellerPhone",sPhone);
        ProductsRefs.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    startActivity(new Intent(AddTutor.this, AddCategory.class));
                    mProProgress.dismiss();
                    Toast.makeText(AddTutor.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProProgress.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(AddTutor.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}