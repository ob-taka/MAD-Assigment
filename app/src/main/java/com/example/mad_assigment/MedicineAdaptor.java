package com.example.mad_assigment;

import android.content.Context;
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

public class MedicineAdaptor extends RecyclerView.Adapter<MedicineCardHolder>{
    private ArrayList<Modle> MedicineData;
    Context context;
    public MedicineAdaptor(Context context , ArrayList<Modle> data) {
        this.MedicineData = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MedicineCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicinerow, parent , false);
        return new MedicineCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MedicineCardHolder holder, int position) {
        holder.medicineName.setText(MedicineData.get(position).getTitle());
        // finds image and download image from firebase storage by image path and binds it to view holder
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://quickmad-e4016.appspot.com/");
        // get image path
        StorageReference storageRef = storage.getReference().child(MedicineData.get(position).getImg());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.medicineIcon); // uses Gilde , a framework to load and download files in android
            }
        });
    }

    @Override
    public int getItemCount() {
        return MedicineData.size();
    }

}
