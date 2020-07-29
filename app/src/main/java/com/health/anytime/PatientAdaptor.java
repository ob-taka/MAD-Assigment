package com.health.anytime;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PatientAdaptor extends RecyclerView.Adapter<PatientCardHolder> {
  private Context context;
  private ArrayList<PatientModel> patientData;
  private  ArrayList<String> patientpic;

  public PatientAdaptor(Context context, ArrayList<PatientModel> data , ArrayList<String> pic) {
    this.context = context;
    this.patientData = data;
    this.patientpic = pic;

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
          @NonNull final PatientCardHolder holder,
          final int position
  ) {

    holder.patientName.setText(patientData.get(position).getPatientName());
    // finds image and download image from firebase storage by image path and binds it to view holder
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://quickmad-e4016.appspot.com/");
    // get image path
    StorageReference storageRef = storage.getReference().child( "ProfilePicture/" + patientpic.get(position));
    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
      @Override
      public void onSuccess(Uri uri) {
        Glide.with(context).load(uri).into(holder.patientPic); // uses Gilde , a framework to load and download files in android
      }
    });

    holder.patientName.setOnClickListener(
      new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          // move to view Patient activity
          Intent nextActivity = new Intent(context, ViewPatient.class);
          nextActivity.putExtra("patientname" , patientData.get(position).getPatientName());
          nextActivity.putExtra("patientpic" , patientpic.get(position));
          nextActivity.putExtra("medKey" , patientData.get(position).getMedid());
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
