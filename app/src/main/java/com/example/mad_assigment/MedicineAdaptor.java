package com.example.mad_assigment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedicineAdaptor extends RecyclerView.Adapter<MedicineCardHolder>{
    private ArrayList<MedicineModel> MedicineData;

    public MedicineAdaptor(ArrayList<MedicineModel> data) {
        this.MedicineData = data;
    }

    @NonNull
    @Override
    public MedicineCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicinerow, parent , false);
        return new MedicineCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineCardHolder holder, int position) {
        holder.medicineName.setText(MedicineData.get(position).getMedicineTitle());
        holder.medicineIcon.setImageResource(MedicineData.get(position).getMedicineImg());
    }

    @Override
    public int getItemCount() {
        return MedicineData.size();
    }

}
