package com.example.mad_assigment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.HashMap;


public class DoctorHome extends AppCompatActivity {

    Button viewpatient;
    Button addpatient;
    ImageView doctorpic;
    String pic;
    HashMap<String,PatientModel> unaddedPatients;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_home);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        viewpatient = findViewById(R.id.button4);
        addpatient = findViewById(R.id.button5);
        unaddedPatients = new HashMap<>();

        Intent intent = getIntent();
        pic = intent.getStringExtra("pic");

        viewpatient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PatientList.class);
                startActivity(intent);
            }
        });

        addpatient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // check if patient data is empty else fetch again
                if  (unaddedPatients.isEmpty()){
                    fetchPatientData();
                    AlertDialog.Builder builder = new AlertDialog.Builder(DoctorHome.this);
                    builder.setTitle("Please wait for HealthAnytime to fetch data")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    builder.create();
                    builder.show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), AddPatient.class);
                    intent.putExtra("keys" , unaddedPatients);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchPatientData();
        fetchDoctorPic();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right); //animation
    }

    /**
     * Fetch patient data from firebase and add patient data as a patientModel class
     * into unaddedPatients to be passed to add patient activity
     */
    private void fetchPatientData(){
        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PatientModel patient = snapshot.getValue(PatientModel.class);
                    if(!patient.isStatus()){
                        unaddedPatients.put(snapshot.getKey() , patient);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * fetch image view from firebase Storage (file hosting service)
     */
    private void fetchDoctorPic() {
        // finds image and download image from firebase storage by image path and binds it to view holder
        FirebaseStorage storage = FirebaseStorage.getInstance(
                "gs://quickmad-e4016.appspot.com/"
        );
        StorageReference storageRef = storage
                .getReference()
                .child("ProfilePicture/" + pic );
        storageRef
                .getDownloadUrl()
                .addOnSuccessListener(
                        new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(DoctorHome.this).load(uri).into(doctorpic); // uses Gilde , a framework to load and download files in android
                            }
                        }
                );
    }



}