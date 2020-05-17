package com.example.mad_assigment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MHolder extends RecyclerView.ViewHolder {

    ImageView mimgV;
    TextView mTitle, mDes, mTime;

    public MHolder(@NonNull View itemView) {
        super(itemView);

        this.mTitle = itemView.findViewById(R.id.mTitle);
        this.mDes = itemView.findViewById(R.id.mDes);
        this.mimgV = itemView.findViewById(R.id.mImageV);
        this.mTime = itemView.findViewById(R.id.mTime);
    }
}
