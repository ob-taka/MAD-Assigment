package com.example.mad_assigment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PatientAdaptor extends RecyclerView.Adapter<PatientCardHolder> implements Filterable {
    private Context context;
    private ArrayList<PatientModel> patientData;
    private ArrayList<PatientModel> patientDataFull;

    public PatientAdaptor(Context context, ArrayList<PatientModel> data) {
        this.context = context;
        this.patientData = data;
        this.patientDataFull = new ArrayList<>(data); //making a copy of patient list , used for filtering search results
    }

    @NonNull
    @Override
    public PatientCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patientrow, parent , false);
        return new PatientCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientCardHolder holder, int position) {
        holder.patientName.setText(patientData.get(position).getPatientName());
        holder.patientPic.setImageResource(patientData.get(position).getPatientProfilepic());

        holder.patientName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(context  , ViewPatient.class );
                context.startActivity(nextActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return patientData.size();
    }

    // used to filter search results
    private Filter exampleFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // list that fits filter
            ArrayList<PatientModel> filterList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0 ){
                filterList.addAll(patientDataFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                // loops to find names that have filtered name
                for (PatientModel patient : patientDataFull){
                    if (patient.getPatientName().toLowerCase().contains(filterPattern)){
                        filterList.add(patient);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;
            Log.d("d" , "flitering");
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            patientData.clear();
            patientData.addAll( (List<PatientModel>) results.values);
            notifyDataSetChanged();
        }
    };
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

}
