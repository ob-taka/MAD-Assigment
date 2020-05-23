package com.example.mad_assigment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class medicineAdaptor extends RecyclerView.Adapter<medicineCardHolder>{
    Context context;
    private ArrayList<medicineModel> cardData;

    public medicineAdaptor(Context context, ArrayList<medicineModel> data) {
        this.context = context;
        this.cardData = data;
    }

    @NonNull
    @Override
    public medicineCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicinerow, parent , false);

        return new medicineCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull medicineCardHolder holder, int position) {
        holder.medicineName.setText(cardData.get(position).getTitle());
        holder.medicineIcon.setImageResource(cardData.get(position).getImg());

    }

    @Override
    public int getItemCount() {
        return cardData.size();
    }
}
