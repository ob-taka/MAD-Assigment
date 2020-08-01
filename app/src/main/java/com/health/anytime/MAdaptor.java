package com.health.anytime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class MAdaptor extends FirestoreRecyclerAdapter<Modle, MAdaptor.MHolder> {

    private OnItemClickListener listener;


    public MAdaptor(@NonNull FirestoreRecyclerOptions<Modle> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MHolder holder, int position, @NonNull Modle model) {
        holder.mTitle.setText(model.getTitle());
        holder.mDes.setText(model.getDescription());
        holder.mTime.setText(model.getTime());
        holder.dosage.setText(model.getDosage());
        holder.mUnit.setText(model.getUnit());
        boolean isExpanded  = model.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @NonNull
    @Override
    public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MHolder(v);
    }



    class MHolder extends RecyclerView.ViewHolder {


        TextView mTitle, mDes, mTime, dosage, mUnit;
        ConstraintLayout expandableLayout;

        public MHolder(@NonNull View itemView) {
            super(itemView);
            this.expandableLayout = itemView.findViewById(R.id.expandableLayout);
            this.dosage = itemView.findViewById(R.id.dosage);
            this.mTitle = itemView.findViewById(R.id.mTitle);
            this.mDes = itemView.findViewById(R.id.detailDes);
            this.mTime = itemView.findViewById(R.id.mTime);
            this.mUnit = itemView.findViewById(R.id.unit);
            itemView.setOnClickListener(new View.OnClickListener() {
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
        void onItemClick(DocumentSnapshot dataSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
