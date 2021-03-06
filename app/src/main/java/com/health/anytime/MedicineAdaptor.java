package com.health.anytime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MedicineAdaptor extends RecyclerView.Adapter<MedicineAdaptor.MedicineCardHolder>{
    private ArrayList<MedicineModel> MedicineData;
    private OnCardListener cardListener;
    Context context;

    public MedicineAdaptor(Context context , ArrayList<MedicineModel> data , OnCardListener cardListener) {
        this.MedicineData = data;
        this.context = context;
        this.cardListener = cardListener;
    }

    @NonNull
    @Override
    public MedicineCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicinecard, parent , false);
        return new MedicineCardHolder(view , cardListener );
    }

    @Override
    public void onBindViewHolder(@NonNull final MedicineCardHolder holder, int position) {
        holder.medicineName.setText(MedicineData.get(position).getMedicineTitle());
        // change color of card when it is low on supplies
        if (MedicineData.get(position).getQuantity() < 10){
            holder.cardView.setBackgroundColor(Color.parseColor("#cc0000"));//red
            holder.medicineName.setTextColor(Color.WHITE);
        }
        // finds image and download image from firebase storage by image path and binds it to view holder
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://quickmad-e4016.appspot.com/");
        // get image path
        StorageReference storageRef = storage.getReference().child(MedicineData.get(position).getMedicineImg());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.medicineIcon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MedicineData.size();
    }

    public class MedicineCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView medicineName;
        ImageView medicineIcon;
        OnCardListener cardListener;
        View cardView;
        public MedicineCardHolder(@NonNull View itemView , OnCardListener cardListener) {
            super(itemView);
            this.cardView = itemView.getRootView();
            this.medicineName = itemView.findViewById(R.id.med_title);
            this.medicineIcon = itemView.findViewById(R.id.med_image);
            this.cardListener = cardListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            cardListener.onCardClick(getAdapterPosition());
        }
    }
    public  interface OnCardListener{
        void  onCardClick(int position);
    }
}
