package com.example.mad_assigment;

import android.content.Context;
import android.media.midi.MidiOutputPort;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class MAdaptor extends FirebaseRecyclerAdapter<Modle, MAdaptor.MHolder> {

    private OnItemClickListener listener;
    public MAdaptor(@NonNull FirebaseRecyclerOptions<Modle> optiions){
        super(optiions);
    }

    @NonNull
    @Override
    public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);// if on click have issue use parent

        return new MHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MHolder holder, int position, @NonNull Modle model) {
        holder.mTitle.setText(model.getTitle());
        holder.mDes.setText(model.getDescription());
        holder.mTime.setText(model.getTime());
        holder.detilDes.setText(model.getDetailDes());
        holder.dosage.setText(model.getDosage());
        //holder.mimgV.setImageResource(R.drawable.pill);
        boolean isExpanded  = model.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    class MHolder extends RecyclerView.ViewHolder {

        //ImageView mimgV;
        TextView mTitle, mDes, mTime, detilDes, dosage;
        ConstraintLayout expandableLayout;

        public MHolder(@NonNull View itemView) {
            super(itemView);
            this.expandableLayout = itemView.findViewById(R.id.expandableLayout);
            this.detilDes = itemView.findViewById(R.id.detailDes);
            this.dosage = itemView.findViewById(R.id.dosage);
            this.mTitle = itemView.findViewById(R.id.mTitle);
            this.mDes = itemView.findViewById(R.id.mDes);
            //this.mimgV = itemView.findViewById(R.id.mImageV);
            this.mTime = itemView.findViewById(R.id.mTime);
            mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);

                    }
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(DataSnapshot dataSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
