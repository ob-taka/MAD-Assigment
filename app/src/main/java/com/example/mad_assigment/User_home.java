package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class User_home extends AppCompatActivity {

    private MAdaptor adaptor; // adaptor refer
    String receriveIntent;
    private String email;
    private String name;
    private String scheduleID;
    private String patientPic;
    private ArrayList<String> medicinePic;
    private DatabaseReference userReference;// change to recieveintent
    private final DatabaseReference medicineReference = FirebaseDatabase.getInstance().getReference().child("med_list"); // get the reference of the medicine list base on schedule ID from user
    ImageView imageBtn;
    private TextView username, greating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // ensure that no title is displayed
        setContentView(R.layout.activity_user_home);

        imageBtn = findViewById(R.id.imageButton); // when clicked go to patient profile detail
        username = findViewById(R.id.label_Name);
        greating = findViewById(R.id.greating);
        medicinePic = new ArrayList<>();

        receriveIntent = getIntent().getStringExtra("Uid"); // get Uid intent form SignIn
        userReference = FirebaseDatabase.getInstance().getReference().child("User").child(receriveIntent);// get reference of the current using the uid pass in form login

        initUser();
        //fetchMData();
        createChannel();
        setTimeOfDay();


        // set onClickListenr on the image of the user profile to got into their profile page
       /* imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_home.this, Profile_Patient.class);
                intent.putExtra("UID", receriveIntent);
            }
        });*/

    }

    /**
     * fetch the user information form the firebase and get the specific data that was needed
     * to prepare to be display on the screen and pass as a intent to the next activity
     */
    private void initUser(){

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scheduleID = dataSnapshot.child("medid").getValue(String.class);
                name = dataSnapshot.child("patientName").getValue(String.class);
                username.setText(name);
                setUpRecyclerView(scheduleID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    // fetch medicine from med list
    private void fetchMData(){
        // fetch patient from firebase
        medicineReference.child(scheduleID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Modle med = snapshot.getValue(Modle.class);
                    medicinePic.add(med.getImg());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                adaptor.stopListening();
            }
        });
    }

    /**
     * change the greeting base on the time of the time
     */
    private void setTimeOfDay(){
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            greating.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greating.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greating.setText("Good Evening");
        }
    }


    /**
     * Using this method to set up the recycler view using the information fetched form the database
     * adding in OnClickListener to the object to allow the user to interact with the recycler view items to expand of close to show the details of the medicament
     */
    private void setUpRecyclerView(String ID){
        Query query = medicineReference.child(ID).orderByChild("priority");
        FirebaseRecyclerOptions<Modle> options = new FirebaseRecyclerOptions.Builder<Modle>()
                .setQuery(query, Modle.class)
                .build();

        // passing data into adaptor
        adaptor = new MAdaptor(options);
        RecyclerView recyclerView = findViewById(R.id.mRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

        adaptor.startListening();

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
     * fetch image view from firebase Storage (file hosting service)
     */
//    private void fetchPatientPic() {
//        // finds image and download image from firebase storage by image path and binds it to view holder
//        FirebaseStorage storage = FirebaseStorage.getInstance(
//                "gs://quickmad-e4016.appspot.com/"
//        );
//        StorageReference storageRef = storage
//                .getReference()
//                .child("ProfilePicture/" + patientPic);
//        storageRef
//                .getDownloadUrl()
//                .addOnSuccessListener(
//                        new OnSuccessListener<Uri>() {
//
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Glide.with(User_home.this).load(uri).into(imageBtn); // uses Gilde , a framework to load and download files in android
//                            }
//                        }
//                );
//    }

}
