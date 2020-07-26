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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import javax.xml.validation.Validator;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

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
//set adapter
        messageAdapter = new MessageAdapter(msgList);
        messagesList = findViewById(R.id.RV_ChatMessageList);
        linearLayoutManager = new LinearLayoutManager(this);
        messagesList.setLayoutManager(linearLayoutManager);
        messagesList.setAdapter(messageAdapter);
//
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

            Map messageTextBody = new HashMap();

            messageTextBody.put("message", msg);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", senderUID);

            Map messageBodyDetails = new HashMap();

            messageBodyDetails.put(messageSenderRef + "/" + pushID, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + pushID, messageTextBody);

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