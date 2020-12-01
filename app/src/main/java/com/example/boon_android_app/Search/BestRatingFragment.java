package com.example.boon_android_app.Search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.boon_android_app.HomeActivity;
import com.example.boon_android_app.R;
import com.example.boon_android_app.TutorDetailActivity;
import com.example.boon_android_app.model.TutorVH;
import com.example.boon_android_app.model.tutors;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BestRatingFragment extends Fragment {
    View v;
    private ImageView searchImg,back;
    private EditText searchEdt;
    FrameLayout frameLayout;
    private RecyclerView searchRv;
    RelativeLayout layout_slider;
    private TextView rating, cost,offline,online,covid,pop;
    private RecyclerView.LayoutManager layoutManager;
    private String searchInput;
    String[] listitems;
    Slider slider2;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        back=(ImageView)v.findViewById(R.id.back_arrow);
        rating=v.findViewById(R.id.rating_txt);
        cost=v.findViewById(R.id.cost_txt);
        offline=v.findViewById(R.id.offline_txt_rating);
        online=v.findViewById(R.id.online_txt_rating);
        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
        cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
        covid=v.findViewById(R.id.covidIn_txt);
        pop=v.findViewById(R.id.popularity_txt);
        covid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listitems = new String[]{"2+","3+","4+","5+"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Rating");
                mBuilder.setIcon(R.drawable.ic_list_filter);
                mBuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rating.setText("Rating "+listitems[which]);
                        dialog.dismiss();
                        String ratingS=listitems[which].toString();
                        DatabaseReference searchref = FirebaseDatabase.getInstance().getReference().child("Tutors");
                        FirebaseRecyclerOptions<tutors> options = new FirebaseRecyclerOptions.Builder<tutors>()
                                .setQuery(searchref.orderByChild("rating").startAt(ratingS),tutors.class)
                                .build();
                        FirebaseRecyclerAdapter<tutors, TutorVH> adapter = new FirebaseRecyclerAdapter<tutors, TutorVH>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull TutorVH productViewHolder, int i, @NonNull final tutors products) {
                                productViewHolder.Name.setText(products.getName());
                                productViewHolder.rating.setText(products.getRating());
                                productViewHolder.class1.setText(products.getClass1());
                                productViewHolder.sub.setText(products.getSub());
                                Picasso.get().load(products.getImage()).into(productViewHolder.Timage);
                                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent detintent=new Intent(getActivity(), TutorDetailActivity.class);
                                        detintent.putExtra("pid",products.getPid());
                                        startActivity(detintent);
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public TutorVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card,parent,false);
                                TutorVH holder=new TutorVH(view);
                                return holder;
                            }
                        };
                        searchRv.setAdapter(adapter);
                        adapter.startListening();
                    }
                });
                mBuilder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog mDialog=mBuilder.create();
                mDialog.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }
        });
        searchImg=(ImageView)v.findViewById(R.id.search_img);
        searchEdt=(EditText)v.findViewById(R.id.search_edt);
        searchRv=(RecyclerView)v.findViewById(R.id.order_search_list_rv);
        searchRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput=searchEdt.getText().toString();
                onStart();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference searchref = FirebaseDatabase.getInstance().getReference().child("Tutors");
        FirebaseRecyclerOptions<tutors> options = new FirebaseRecyclerOptions.Builder<tutors>()
                .setQuery(searchref.orderByChild("rating").startAt(searchInput),tutors.class)
                .build();
        FirebaseRecyclerAdapter<tutors, TutorVH> adapter = new FirebaseRecyclerAdapter<tutors, TutorVH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TutorVH productViewHolder, int i, @NonNull final tutors products) {
                productViewHolder.Name.setText(products.getName());
                productViewHolder.rating.setText(products.getRating());
                productViewHolder.class1.setText(products.getClass1());
                productViewHolder.sub.setText(products.getSub());
                Picasso.get().load(products.getImage()).into(productViewHolder.Timage);
                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detintent=new Intent(getActivity(), TutorDetailActivity.class);
                        detintent.putExtra("pid",products.getPid());
                        startActivity(detintent);
                    }
                });
            }

            @NonNull
            @Override
            public TutorVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card,parent,false);
                TutorVH holder=new TutorVH(view);
                return holder;
            }
        };
        searchRv.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_best_rating, container, false);
    }
}