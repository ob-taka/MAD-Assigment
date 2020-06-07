package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profile_Patient extends AppCompatActivity {

    TextView Name, Email;
    ImageView ProfPic;
    DatabaseReference databaseReference;
    String patientProfilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile);

        Name = findViewById(R.id.patient_Name);
        Email = findViewById(R.id.patient_Email);
        ProfPic = findViewById(R.id.patient_Pp);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String uid = getIntent().getStringExtra("UID");
        getDetails(uid);


    }
    //This method retrieves the name, email, prof pic url from firebase database. it also sets the profile
    //pic through the fetchPatientPic() method
    private void getDetails(String uid) {
        databaseReference.child("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String patientName = dataSnapshot.child("patientName").getValue().toString();
                Name.setText(patientName);
                String patientEmail = dataSnapshot.child("patientEmail").getValue().toString();
                Email.setText(patientEmail);
                patientProfilepic = dataSnapshot.child("patientProfilepic").getValue().toString();
                fetchPatientPic();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile_Patient.this,"error: " + databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * fetch image view from firebase Storage (file hosting service)
     * References FirebaseStorage profile pic folder and uses glide to load the image into profpic imageview.
     */
    private void fetchPatientPic() {
        // finds image and download image from firebase storage by image path and binds it to view holder
        FirebaseStorage storage = FirebaseStorage.getInstance(
                "gs://quickmad-e4016.appspot.com/"
        );
        StorageReference storageRef = storage
                .getReference()
                .child("ProfilePicture/" + patientProfilepic );
        storageRef
                .getDownloadUrl()
                .addOnSuccessListener(
                        new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(Profile_Patient.this).load(uri).into(ProfPic); // uses Gilde , a framework to load and download files in android
                            }
                        }
                );
    }
}