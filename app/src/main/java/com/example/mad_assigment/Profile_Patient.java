package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_Patient extends AppCompatActivity {

    TextView Name, Email, Phone;
    ImageView ProfPic;
    FirebaseUser mAuth;
    DatabaseReference databaseReference;

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

        String email = mAuth.getEmail();
        getDetails(email);
    }

    private Patient_Details getDetails(final String email) {
        final Patient_Details[] patient = {new Patient_Details()};
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    patient[0] = snapshot.getValue(Patient_Details.class);

                    if (patient[0].getEmail().equals(email)) {

                        //for testing
                        String key = snapshot.getKey();
                        Toast.makeText(Profile_Patient.this, key, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return patient[0];
    }


}