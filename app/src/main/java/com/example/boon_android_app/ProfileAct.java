package com.example.boon_android_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boon_android_app.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;

public class ProfileAct extends Fragment {
    Context context;
    View v;

    private TextView history,donate,faq,complaint, aboutUs,EdtFullname,EdtPhone;
    private  EditText EdtAddress,languages,contact,location,payment,recent;
    private ImageView img1, img2, img3, img4, img5, img6;
    private CircleImageView profileImgSettings;
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfileReferenceRefs;
    private String Checker="";
    private Button savetxt,logout;
    private StorageTask uploadTask;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        location = v.findViewById(R.id.Bangalore_txt);
        payment = v.findViewById(R.id.payment_txt);
        recent = v.findViewById(R.id.recent_txt);
        history = v.findViewById(R.id.history_btn);
        donate = v.findViewById(R.id.donate_btn);
        faq = v.findViewById(R.id.faq);
        logout=v.findViewById(R.id.logout);
        complaint = v.findViewById(R.id.complaint_btn);
        aboutUs = v.findViewById(R.id.ABou_us_btn);
        languages = v.findViewById(R.id.language_txt);
        contact = v.findViewById(R.id.contact_txt);
        storageProfileReferenceRefs = FirebaseStorage.getInstance().getReference().child("Profile_Pictures");
        profileImgSettings = (CircleImageView) v.findViewById(R.id.profile_act_userImage);
        savetxt = (Button) v.findViewById(R.id.update_settings);
        EdtFullname = v.findViewById(R.id.profile_act_username);
        EdtPhone = v.findViewById(R.id.profile_act_userEmail); // email of user
        EdtAddress = v.findViewById(R.id.class_txt); // class of user
        EdtFullname.setText(Prevalent.currentOnlineUsers.getName()); // user full name
        EdtPhone.setText(Prevalent.currentOnlineUsers.getEmail());
        img1 = v.findViewById(R.id.edt_img1);
        img2 = v.findViewById(R.id.edt_img2);
        img3 = v.findViewById(R.id.edt_img3);
        img4 = v.findViewById(R.id.edt_img4);
        img5 = v.findViewById(R.id.edt_img5);
        img6 = v.findViewById(R.id.edt_img6);
        EdtAddress.setFocusableInTouchMode(false);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdtAddress.setFocusableInTouchMode(true);
                EdtAddress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            }
        });
        location.setFocusableInTouchMode(false);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setFocusableInTouchMode(true);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent logintent=new Intent(getActivity(),MainActivity.class);
                logintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logintent);
            }
        });
        languages.setFocusableInTouchMode(false);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languages.setFocusableInTouchMode(true);
            }
        });
        contact.setFocusableInTouchMode(false);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setFocusableInTouchMode(true);
            }
        });
        payment.setFocusableInTouchMode(false);
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment.setFocusableInTouchMode(true);
            }
        });
        recent.setFocusableInTouchMode(false);
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recent.setFocusableInTouchMode(true);
            }
        });
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUsers.getPhone());
       userref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists())
               {
                   if (snapshot.child("image").exists()){
                       Picasso.get().load(snapshot.child("image").getValue().toString()).into(profileImgSettings);
                   }
                   EdtAddress.setText(snapshot.child("class").getValue().toString());
                   contact.setText(snapshot.child("phone").getValue().toString());
                   if (snapshot.child("location").exists())
                   {
                       location.setText(snapshot.child("location").getValue().toString());
                   }
                   languages.setText(snapshot.child("language").getValue().toString());
                   if (snapshot.child("payment").exists())
                   {
                       payment.setText(snapshot.child("payment").getValue().toString());
                   }
                   if (snapshot.child("recent").exists())
                   {
                       recent.setText(snapshot.child("recent").getValue().toString());
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        profileImgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checker = "clicked";
                Intent intent = CropImage.activity(imageUri).setAspectRatio(1,1).getIntent(getActivity());
                startActivityForResult(intent,CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        savetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Checker.equals("clicked"))
                {
                    UserInfoSaved();

                }
                else
                {
                    UpdateUserInfo();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== RESULT_OK && data!=null)
        {
            CropImage.ActivityResult activityResult= CropImage.getActivityResult(data);
            imageUri = activityResult.getUri();
            profileImgSettings.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(getActivity(), "Error , Try Again", Toast.LENGTH_SHORT).show();
            Intent setintent=new Intent(getActivity(),HomeActivity.class);
            startActivity(setintent);
        }
    }

    private void UserInfoSaved() {

        uploadImage();

    }

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle(" Update Profile");
        progressDialog.setMessage(" Please Wait, While Profile is getting updated");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (imageUri!= null)
        {
            final StorageReference fileRefs= storageProfileReferenceRefs
                    .child(Prevalent.currentOnlineUsers.getPhone() + ".jpg");
            uploadTask = fileRefs.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRefs.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful())
                            {
                                Uri downloadUri = task.getResult();
                                myUrl=downloadUri.toString();
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users");
                                HashMap<String,Object> userMap = new HashMap<>();
                                userMap.put("language", languages.getText().toString());
                                userMap.put("class",EdtAddress.getText().toString());
                                userMap.put("location",location.getText().toString());
                                userMap.put("phone",contact.getText().toString());
                                userMap.put("payment",payment.getText().toString());
                                userMap.put("image",myUrl);
                                reference.child(Prevalent.currentOnlineUsers.getPhone()).updateChildren(userMap);
                                progressDialog.dismiss();
                                startActivity(new Intent(getActivity(),HomeActivity.class));
                                Toast.makeText(getActivity(), "profile updated", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Error uploading Image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(getActivity(), "Image is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> usermap=new HashMap<>();
        usermap.put("language", languages.getText().toString());
        usermap.put("class",EdtAddress.getText().toString());
        usermap.put("location",location.getText().toString());
        usermap.put("phone",contact.getText().toString());
        usermap.put("payment",payment.getText().toString());
        ref.child(Prevalent.currentOnlineUsers.getPhone()).updateChildren(usermap);
        startActivity(new Intent(getActivity(),HomeActivity.class));
        Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
            public View onCreateView (LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState){
                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.fragment_profile_act, container, false);
            }
        }
