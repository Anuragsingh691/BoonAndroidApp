package com.example.boon_android_app.model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boon_android_app.R;
import com.example.boon_android_app.interfaces.ItemClickListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorVH extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView Name,rating,class1,sub;
    public CircleImageView Timage;
    public ItemClickListener itemClickListener;
    public TutorVH(@NonNull View itemView) {
        super(itemView);
        Name=(TextView)itemView.findViewById(R.id.tutor_name);
        rating=(TextView)itemView.findViewById(R.id.userRating);
        class1=(TextView)itemView.findViewById(R.id.tutor_cls);
        sub=(TextView)itemView.findViewById(R.id.tutor_sub);
        Timage=(CircleImageView)itemView.findViewById(R.id.tutor_img);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.itemClickListener=listener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view , getAdapterPosition(),false);
    }
}
