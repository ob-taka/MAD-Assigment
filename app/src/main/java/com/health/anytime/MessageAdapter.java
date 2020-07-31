package com.health.anytime;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
{
//Declaration of attributes
    private List<Message> msgList;
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;

    public MessageAdapter (List<Message> msgList){
        this.msgList = msgList;
    }

//Declare the attributes ID in custom layout and assign to a variable
    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfPic;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText = itemView.findViewById(R.id.sender_messsage_text);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
            receiverProfPic = itemView.findViewById(R.id.message_profile_image);
        }
    }

//Message view holder will be set to a custom chat message layout
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chatmessage_layout, parent,false);
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        return new MessageViewHolder(view);
    }

//Sets sent and received messages and profile pic to this view holder
    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        String senderUID = auth.getCurrentUser().getUid();
        Message message = msgList.get(position);
        String fromUserID = message.getFrom();
        String fromMessageType = message.getType();

        ref = FirebaseDatabase.getInstance().getReference().child("User").child(fromUserID);
//Access firebase storage to set the profile picture in view holder
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String receiverProfImg = snapshot.child("patientProfilepic").getValue().toString();
                storageRef = firebaseStorage.getReference().child("ProfilePicture/" + receiverProfImg);
                storageRef.getDownloadUrl()
                        .addOnSuccessListener(
                                new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(holder.itemView.getContext()).load(uri).into(holder.receiverProfPic);
                                    }
                                }
                        );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

/*
*Display the sent or received messages using visibility depending on the sender or receiver.
*The sender should not be able to see the receiver's received message and vise versa for the receiver.
*/
        if (fromMessageType.equals("text"))
        {

            if (fromUserID.equals(senderUID))
            {
                holder.receiverMessageText.setVisibility(View.INVISIBLE);
                holder.receiverProfPic.setVisibility(View.INVISIBLE);
                holder.senderMessageText.setBackgroundResource(R.drawable.sendermessage_layout);
                holder.senderMessageText.setTextColor(Color.BLACK);
                holder.senderMessageText.setText(message.getMessage());
            }
            else
            {
                holder.senderMessageText.setVisibility(View.INVISIBLE);
                holder.receiverProfPic.setVisibility(View.VISIBLE);
                holder.receiverProfPic.setVisibility(View.VISIBLE);

                holder.receiverProfPic.setBackgroundResource(R.drawable.receivermessage_layout);
                holder.receiverMessageText.setTextColor(Color.BLACK);
                holder.receiverMessageText.setText(message.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

}
