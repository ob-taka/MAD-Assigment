package com.example.mad_assigment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedicineCardHolder extends RecyclerView.ViewHolder{
    TextView medicineName;
    ImageView medicineIcon;

    public MedicineCardHolder(@NonNull View itemView) {
        super(itemView);

        this.medicineName = itemView.findViewById(R.id.mTitle);
        this.medicineIcon = itemView.findViewById(R.id.mImageV);
    }
}
