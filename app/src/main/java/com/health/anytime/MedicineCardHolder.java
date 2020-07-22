package com.health.anytime;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedicineCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView medicineName;
    ImageView medicineIcon;
    TextView medicineqty;

    public MedicineCardHolder(@NonNull View itemView ) {
        super(itemView);

        this.medicineName = itemView.findViewById(R.id.med_title);
        this.medicineIcon = itemView.findViewById(R.id.med_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
