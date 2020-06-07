package com.example.mad_assigment;

import android.content.Context;
import android.media.midi.MidiOutputPort;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MAdaptor extends FirebaseRecyclerAdapter<Modle, MAdaptor.MHolder> {
    private Context context;
    ArrayList<String> medicinepic;
    private OnItemClickListener listener; // create a instance of a OnClickListener that I have created
    public MAdaptor(Context context , @NonNull FirebaseRecyclerOptions<Modle> optiions , ArrayList<String> medicinepic){
        super(optiions);
        this.context = context;
        this.medicinepic = medicinepic;
    }

    @NonNull
    @Override
    public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);// if on click have issue use parent

        return new MHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MHolder holder, int position, @NonNull Modle model) {
        holder.mTitle.setText(model.getTitle());
        holder.mDes.setText(model.getDescription());
        holder.mTime.setText(model.getTime());
        holder.detilDes.setText(model.getDetailDes());
        holder.dosage.setText(model.getDosage());
        //holder.mimgV.setImageResource(R.drawable.pill);
        // finds image and download image from firebase storage by image path and binds it to view holder
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://quickmad-e4016.appspot.com/");
        // get image path
        StorageReference storageRef = storage.getReference().child( "ProfilePicture/" + medicinepic.get(position));
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.mimgV); // uses Gilde , a framework to load and download files in android
            }
        });
        boolean isExpanded  = model.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE); // if the layout is expanded set to viable else set to gone so to not take up space
    }

    /**
     * Model holder that sets the items in the view to display the information
     * setOnClickListener bind to mTitle so when user clicks on the title the even will be triggered
     */
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
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);

                    }
                }
            });

        }
    }

    /**
     * custom interface to be overeaten later according to our needs
     */
    public interface OnItemClickListener{
        void onItemClick(DataSnapshot dataSnapshot, int position);
    }

    /**
     * constructor for the listener
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
