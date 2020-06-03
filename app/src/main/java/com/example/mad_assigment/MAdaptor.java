package com.example.mad_assigment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MAdaptor extends RecyclerView.Adapter<MAdaptor.MHolder> {
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
        holder.detilDes.setText(modles.get(position).getDetailDes());
        holder.dosage.setText(modles.get(position).getDosage());
        holder.mimgV.setImageResource(modles.get(position).getImg());
        boolean isExpanded = modles.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return modles.size();
    }

    class MHolder extends RecyclerView.ViewHolder {

        ImageView mimgV;
        TextView mTitle, mDes, mTime, detilDes, dosage;
        ConstraintLayout expandableLayout;

        public MHolder(@NonNull View itemView) {
            super(itemView);
            this.expandableLayout = itemView.findViewById(R.id.expandableLayout);
            this.detilDes = itemView.findViewById(R.id.detailDes);
            this.dosage = itemView.findViewById(R.id.dosage);
            this.mTitle = itemView.findViewById(R.id.mTitle);
            this.mDes = itemView.findViewById(R.id.mDes);
            this.mimgV = itemView.findViewById(R.id.mImageV);
            this.mTime = itemView.findViewById(R.id.mTime);

            mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Modle modle = modles.get(getAdapterPosition());
                    modle.setExpanded(!modle.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
