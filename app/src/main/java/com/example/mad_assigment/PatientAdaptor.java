package com.example.mad_assigment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientAdaptor extends RecyclerView.Adapter{
    Context context;
    private ArrayList<patientModel> PatientData;

    public PatientAdaptor(Context context, ArrayList<patientModel> data) {
        this.context = context;
        this.PatientData = data;
    }

    @NonNull
    @Override
    public PatientCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patientrow, parent , false);

        return new PatientCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientCardHolder holder, int position) {
        holder.patientName.setText(PatientData.get(position).getPatientName());
        holder.patientPic.setImageResource(PatientData.get(position).getPatientProfilepic());
    }

    @Override
    public int getItemCount() {
        return PatientData.size();
    }
}
