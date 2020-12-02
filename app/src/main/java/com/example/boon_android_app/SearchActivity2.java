package com.example.boon_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boon_android_app.model.TutorVH;
import com.example.boon_android_app.model.tutors;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity2 extends AppCompatActivity {
    View v;
    RecyclerView recyclerViewSu,recyclerView;
    private DatabaseReference TutorRefs;
    private RecyclerView.LayoutManager layoutManager,layoutManagerO;
    private ImageView searchImage,searchFilter;
    private String searchInput;
    private EditText searchEdit;
    private FloatingActionButton chatFab;
    Dialog dialogLoc;
    private TextView Dcurr,DchangeLoc,twelveth,tenth,nineth,nineEng,tenthHin,twelveBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        chatFab=findViewById(R.id.chat_fab_search2);
        chatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity2.this,ChatBotAct.class));
            }
        });
        recyclerViewSu=findViewById(R.id.suggested_rv);
        recyclerViewSu.setHasFixedSize(true);
        recyclerView=findViewById(R.id.offline_rv);
        recyclerView.setHasFixedSize(true);
        searchFilter=findViewById(R.id.filter_search2);
        dialogLoc=new Dialog(SearchActivity2.this);
        dialogLoc.setContentView(R.layout.loc_dialog);
        twelveth=findViewById(R.id.twelve_txt);
        tenth=findViewById(R.id.tenth_txt);
        nineEng=findViewById(R.id.eng2_txt);
        nineth=findViewById(R.id.nineth_txt);
        tenthHin=findViewById(R.id.hin2_txt);
        twelveBio=findViewById(R.id.bio_txt);
        twelveth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("XII");
            }
        });
        tenth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("X");
            }
        });
        nineth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("IX");
            }
        });
        nineEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("IX");
            }
        });
        tenthHin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("IX");
            }
        });
        twelveBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("XII");
            }
        });
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            dialogLoc.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loc_dialog_bg));
        }
        Window window=dialogLoc.getWindow();
        WindowManager.LayoutParams wlp=window.getAttributes();
        wlp.gravity= Gravity.TOP;
        window.setAttributes(wlp);
        dialogLoc.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        Dcurr=dialogLoc.findViewById(R.id.curr_loc_txt);
        DchangeLoc=dialogLoc.findViewById(R.id.change_loc_txt);
        Dcurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLoc.dismiss();
            }
        });
        DchangeLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchActivity2.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        searchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLoc.show();
            }
        });
        TutorRefs= FirebaseDatabase.getInstance().getReference().child("Tutors");
        searchEdit=findViewById(R.id.search_edt2);
        layoutManager= new LinearLayoutManager(SearchActivity2.this, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerO= new LinearLayoutManager(SearchActivity2.this, LinearLayoutManager.HORIZONTAL, false);
        getOffline();
        searchImage=findViewById(R.id.search_img2);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput=searchEdit.getText().toString();
                onStart();
            }
        });
    }

   private void getOffline(){
       FirebaseRecyclerOptions<tutors> options=new FirebaseRecyclerOptions.Builder<tutors>()
               .setQuery(TutorRefs,tutors.class)
               .build();
       FirebaseRecyclerAdapter<tutors, TutorVH> adapter=new FirebaseRecyclerAdapter<tutors, TutorVH>(options) {
           @Override
           protected void onBindViewHolder(@NonNull TutorVH tutorVH, int i, @NonNull final tutors tutors) {
               tutorVH.Name.setText(tutors.getName());
               tutorVH.class1.setText(tutors.getClass1());
               tutorVH.rating.setText(tutors.getRating());
               tutorVH.sub.setText(tutors.getSub());
               Picasso.get().load(tutors.getImage()).into(tutorVH.Timage);
               tutorVH.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent detintent=new Intent(SearchActivity2.this, TutorDetailActivity.class);
                       detintent.putExtra("pid",tutors.getPid());
                       startActivity(detintent);
                   }
               });

           }
           @NonNull
           @Override
           public TutorVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_item,parent,false);
               TutorVH holder=new TutorVH(view);
               return holder;
           }
       };
       recyclerView.setLayoutManager(layoutManagerO);
       adapter.startListening();
       recyclerView.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<tutors> options=new FirebaseRecyclerOptions.Builder<tutors>()
                .setQuery(TutorRefs.orderByChild("class1").startAt(searchInput),tutors.class)
                .build();
        FirebaseRecyclerAdapter<tutors, TutorVH> adapter=new FirebaseRecyclerAdapter<tutors, TutorVH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TutorVH tutorVH, int i, @NonNull final tutors tutors) {
                tutorVH.Name.setText(tutors.getName());
                tutorVH.class1.setText(tutors.getClass1());
                tutorVH.rating.setText(tutors.getRating());
                tutorVH.sub.setText(tutors.getSub());
                Picasso.get().load(tutors.getImage()).into(tutorVH.Timage);
                tutorVH.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detintent=new Intent(SearchActivity2.this, TutorDetailActivity.class);
                        detintent.putExtra("pid",tutors.getPid());
                        startActivity(detintent);
                    }
                });

            }
            @NonNull
            @Override
            public TutorVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_item,parent,false);
                TutorVH holder=new TutorVH(view);
                return holder;
            }
        };
        recyclerViewSu.setLayoutManager(layoutManager);
        adapter.startListening();
        recyclerViewSu.setAdapter(adapter);
    }
}