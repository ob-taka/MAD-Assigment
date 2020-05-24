package com.example.mad_assigment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MAdaptor extends RecyclerView.Adapter<MHolder> {
    Context context;
    private ArrayList<Modle> modles;

    public MAdaptor(Context context, ArrayList<Modle> modles) {
        this.context = context;
        this.modles = modles;
    }

    @NonNull
    @Override
    public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);// if on click have issue use parent

        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MHolder holder, int position) {

        holder.mTitle.setText(modles.get(position).getTitle());
        holder.mDes.setText(modles.get(position).getDescription());
        holder.mTime.setText(modles.get(position).getTime());
        holder.mimgV.setImageResource(modles.get(position).getImg());

    }

    @Override
    public int getItemCount() {
        return modles.size();
    }
}
