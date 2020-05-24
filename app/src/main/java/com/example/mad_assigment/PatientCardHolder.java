package com.example.mad_assigment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientCardHolder extends RecyclerView.ViewHolder{
    TextView patientName;
    ImageView patientPic;

    public PatientCardHolder(@NonNull View itemView) {
        super(itemView);

        this.patientName = itemView.findViewById(R.id.PatientName);
        this.patientPic = itemView.findViewById(R.id.profile_image);

    }
}
