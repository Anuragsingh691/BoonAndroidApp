package com.example.boon_android_app.model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boon_android_app.R;
import com.example.boon_android_app.interfaces.ItemClickListener;

public class OffersVH extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView heading, desc;
    public ItemClickListener itemClickListener;

    public OffersVH(@NonNull View itemView) {
        super(itemView);
        heading=(TextView) itemView.findViewById(R.id.offers_title);
        desc=(TextView)itemView.findViewById(R.id.offers_desc);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.itemClickListener=listener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v , getAdapterPosition(),false);
    }
}
