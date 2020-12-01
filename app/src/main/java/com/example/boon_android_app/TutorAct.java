package com.example.boon_android_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boon_android_app.model.TutorVH;
import com.example.boon_android_app.model.tutors;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class TutorAct extends Fragment {
    View v;
    RecyclerView recyclerView;
    private DatabaseReference TutorRefs;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        recyclerView=v.findViewById(R.id.tutor_fragment_rv);
        recyclerView.setHasFixedSize(true);
        TutorRefs= FirebaseDatabase.getInstance().getReference().child("Tutors");
    }


    @Override
    public void onStart() {
        super.onStart();
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
                        Intent detintent=new Intent(getActivity(), TutorDetailActivity.class);
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutor_act, container, false);
    }

}