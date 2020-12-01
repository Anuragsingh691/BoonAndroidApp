package com.example.boon_android_app.Search;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.boon_android_app.R;

public class SingleChoiceDialog extends DialogFragment {
    int position=0;
    public interface SingleChoiceListener{
        void  onPositiveButtonClicked(String[] list,int position);
        void onNegativeButtonClicked();
    }
    SingleChoiceListener singleChoiceListener;
    @NonNull
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            singleChoiceListener=(SingleChoiceListener) context;
        }
        catch (Exception e)
        {
            throw  new ClassCastException(getActivity().toString()+"SingleChoiceListener must implemented");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final String[] list=getActivity().getResources().getStringArray(R.array.rating_choice);
        builder.setTitle("Rating")
                .setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position=which;

                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                singleChoiceListener.onPositiveButtonClicked(list,position);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        singleChoiceListener.onNegativeButtonClicked();
                    }
                });
        return builder.create();
    }
}
