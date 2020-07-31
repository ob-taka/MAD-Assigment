package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
//Declaration of attributes
    private String receiverName, receiverPic, receiverUID, msg, senderUID;
    private TextView userName;
    private EditText enterMSG;
    private ImageButton sendMSG;
    private CircleImageView profPic;
    private Toolbar chatTB;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    private StorageReference storageRef;
    private FirebaseStorage firebaseStorage;
    private List<Message> msgList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance("gs://quickmad-e4016.appspot.com/");
        receiverUID = getIntent().getExtras().getString("UID");
        receiverName = getIntent().getExtras().getString("Name");
        receiverPic = getIntent().getExtras().getString("Pic");

        senderUID = auth.getCurrentUser().getUid();

        chatTB = findViewById(R.id.chatActivity_toolbar);
        setSupportActionBar(chatTB);
        //Create instance of ActionBar and display the action bar. This is for the custom chat bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_bar, null);
        actionBar.setCustomView(actionBarView);

        enterMSG = findViewById(R.id.editText_inputChatMSG);
        sendMSG = findViewById(R.id.btn_sendMSG);
        userName = findViewById(R.id.custom_profName);
        profPic = findViewById(R.id.custom_profPic);

        messageAdapter = new MessageAdapter(msgList);
        messagesList = findViewById(R.id.RV_ChatMessageList);
        linearLayoutManager = new LinearLayoutManager(this);
        messagesList.setLayoutManager(linearLayoutManager);
        messagesList.setAdapter(messageAdapter);
//Accesses firebase storage to get profile pic and set it to the profile pic in the custom action bar
        userName.setText(receiverName);
        storageRef = firebaseStorage.getReference().child("ProfilePicture/" + receiverPic);
        storageRef.getDownloadUrl()
                .addOnSuccessListener(
                        new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ChatActivity.this).load(uri).into(profPic);
                            }
                        }
                );
//Toggles the visibility on and off for the send button
        enterMSG.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(buttonDisplay()){
                    sendMSG.setVisibility(View.INVISIBLE);
                }
            }
        });

        sendMSG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!buttonDisplay()){
                    sendMSG.setVisibility(View.INVISIBLE);
                }
                sendMessage();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//Accesses the firebase database to retrieve messages and set it to msglist list
        rootRef.child("Messages").child(senderUID).child(receiverUID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                msgList.add(message);
                messageAdapter.notifyDataSetChanged();
                messagesList.smoothScrollToPosition(messagesList.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//Method to send message to the receiver or other user. It accesses the firebase database and push the message, senderUID as well as the receiverUID.
    private void sendMessage(){
        sendMSG.setVisibility(View.GONE);
        String msg = enterMSG.getText().toString();
        if (!TextUtils.isEmpty(msg)){
            String messageSenderRef = "Messages/" + senderUID + "/" + receiverUID;
            String messageReceiverRef = "Messages/" + receiverUID + "/" + senderUID;

            DatabaseReference userMessageKeyRef = rootRef
                    .child("Messages")
                    .child(senderUID)
                    .child(receiverUID)
                    .push();
            String pushID = userMessageKeyRef.getKey();
//Store message in an ordered collection aka a hashmap
            Map messageTextBody = new HashMap();

            messageTextBody.put("message", msg);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", senderUID);

//Store the messageTextBody which is the structure and some details such as from and type of message to the respective key strings.
            Map messageBodyDetails = new HashMap();

            messageBodyDetails.put(messageSenderRef + "/" + pushID, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + pushID, messageTextBody);

//Database will be updated by using pushing the messageBodyDetails into their respective fields.
            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChatActivity.this,"Message Sent!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ChatActivity.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                    enterMSG.setText("");
                }
            });


        }
        else{
            if(!buttonDisplay()){
                sendMSG.setVisibility(View.INVISIBLE);
            }
        }
    }

/*
* This function toggles the visibility of the send button on and off.
* The visibility will be dependent on whether the user has typed something in or not.
* if not, then the button will be invisible, otherwise it will be visible.
*/
    private boolean buttonDisplay(){
        final boolean[] result = {true};
        msg = enterMSG.getText().toString();
        enterMSG.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                msg = enterMSG.getText().toString();
                if (!TextUtils.isEmpty(msg)) {
                    sendMSG.setVisibility(View.VISIBLE);
                }
                else{
                    result[0] = false;
                    sendMSG.setVisibility(View.INVISIBLE);
                }
            }
        });
        return result[0];
    }

}