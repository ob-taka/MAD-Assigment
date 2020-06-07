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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//Will need to change all classes of Patient_Details to PatientModel once merge is completed
public class Profile_Patient extends AppCompatActivity {

    TextView Name, Email, Phone;
    ImageView ProfPic;
    FirebaseUser mAuth;
    DatabaseReference databaseReference;
    Patient_Details patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile);

        Name = findViewById(R.id.patient_Name);
        Email = findViewById(R.id.patient_Email);
        Phone = findViewById(R.id.patient_Phone);
        ProfPic = findViewById(R.id.patient_Pp);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //fetchPatientPic();
/*
        get email and uid from intent
        Log.d("#d",email);
        getDetails(email);
        Name.setText(patient.getName());
        Email.setText(patient.getEmail());
        Phone.setText(patient.getPhone());
        //Do for profile pic

 */

    }

    private void getDetails(String email,String uid) {
        databaseReference.child("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String patientEmail = dataSnapshot.child("patientEmail").getValue().toString();
                String patientName = dataSnapshot.child("patientName").getValue().toString();
                String patientProfilePic = dataSnapshot.child("patientProfilePic").getValue().toString();
                String patientPhone = dataSnapshot.child("patientPhone").getValue().toString();

                patient = new Patient_Details(patientName,patientEmail,patientPhone,patientProfilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile_Patient.this,"error: " + databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * fetch image view from firebase Storage (file hosting service)
     */
//    private void fetchPatientPic(){
//        // finds image and download image from firebase storage by image path and binds it to view holder
//        FirebaseStorage storage = FirebaseStorage.getInstance("gs://quickmad-e4016.appspot.com/");
//        StorageReference storageRef = storage.getReference().child( "ProfilePicture/" + patient.getProfilePicture());
//        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(Profile_Patient.this ).load(uri).into(ProfPic); // uses Gilde , a framework to load and download files in android
//            }
//        });
//    }
}