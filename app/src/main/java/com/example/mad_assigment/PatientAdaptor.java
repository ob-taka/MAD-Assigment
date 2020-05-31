package com.example.mad_assigment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PatientAdaptor extends RecyclerView.Adapter<PatientCardHolder> {
  private Context context;
  private ArrayList<String> patientData;

  public PatientAdaptor(Context context, ArrayList<String> data) {
    this.context = context;
    this.patientData = data;
  }

  @NonNull
  @Override
  public PatientCardHolder onCreateViewHolder(
    @NonNull ViewGroup parent,
    int viewType
  ) {
    View view = LayoutInflater
      .from(parent.getContext())
      .inflate(R.layout.patientrow, parent, false);
    return new PatientCardHolder(view);
  }

  @Override
  public void onBindViewHolder(
          @NonNull PatientCardHolder holder,
          final int position
  ) {
    holder.patientName.setText(patientData.get(position));
    //holder.patientPic.setImageResource(patientData.get(position).getPatientProfilepic());

    holder.patientName.setOnClickListener(
      new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent nextActivity = new Intent(context, ViewPatient.class);
          nextActivity.putExtra("patientname" , patientData.get(position));
          context.startActivity(nextActivity);
        }
      }
    );
  }

  @Override
  public int getItemCount() {
    return patientData.size();
  }

}
