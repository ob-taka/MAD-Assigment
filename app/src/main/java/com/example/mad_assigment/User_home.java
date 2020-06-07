package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class User_home extends AppCompatActivity {

    private MAdaptor adaptor; // adaptor refer
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user_medicien"); // Date reference of the database to be use throughout the activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // ensure that no title is displayed
        setContentView(R.layout.activity_user_home);

        createChannel();

        setUpRecyclerView();

    }

    /**
     * Using this method to set up the recycler view using the information fetched form the database
     * adding in OnClickListener to the object to allow the user to interact with the recycler view items to expand of close to show the details of the medicament
     */
    private void setUpRecyclerView(){
        Query query = databaseReference.orderByChild("priority");
        FirebaseRecyclerOptions<Modle> options = new FirebaseRecyclerOptions.Builder<Modle>()
                .setQuery(query, Modle.class)
                .build();
        // passing data into adaptor
        adaptor = new MAdaptor(options);
        RecyclerView recyclerView = findViewById(R.id.mRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

        // overrides the interface created in the adaptor class to customise the even of the click
        adaptor.setOnItemClickListener(new MAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                Modle modle = adaptor.getItem(position);
                modle.setExpanded(!modle.isExpanded());
                adaptor.notifyItemChanged(position);
            }
        });
    }


    /**
     * this help create channel due to android lv 26 requires the app to create a channel inorder to send notification
     */
    private void createChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("channelID", "reminderChannel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("channel for reminder");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * ask the adaptor to start listening for changes in the firebase when the app start or resume
     */
    @Override
    protected void onStart() {
        super.onStart();
        adaptor.startListening();
    }
    /**
     * ask the adaptor to stop listening for changes in the firebase when the app stop of put to the background
     */
    @Override
    protected void onStop() {
        super.onStop();
        adaptor.stopListening();
    }

}
