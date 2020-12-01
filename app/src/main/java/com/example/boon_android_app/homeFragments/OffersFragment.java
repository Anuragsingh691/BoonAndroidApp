package com.example.boon_android_app.homeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boon_android_app.R;
import com.example.boon_android_app.model.Offers;
import com.example.boon_android_app.model.OffersVH;
import com.example.boon_android_app.model.TutorVH;
import com.example.boon_android_app.model.tutors;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OffersFragment extends Fragment {

    View v;
    RecyclerView recyclerView;
    private DatabaseReference TutorRefs;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        recyclerView=v.findViewById(R.id.offers_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        TutorRefs= FirebaseDatabase.getInstance().getReference().child("Offers");
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Offers> options=new FirebaseRecyclerOptions.Builder<Offers>()
                .setQuery(TutorRefs,Offers.class)
                .build();
        FirebaseRecyclerAdapter<Offers, OffersVH> adapter=new FirebaseRecyclerAdapter<Offers,OffersVH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OffersVH tutorVH, int i, @NonNull Offers tutors) {
                tutorVH.heading.setText(tutors.getHeading());
                tutorVH.desc.setText(tutors.getDesc());

            }

            @NonNull
            @Override
            public OffersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                OffersVH holder=new OffersVH(view);
                return holder;
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offers, container, false);
    }
}