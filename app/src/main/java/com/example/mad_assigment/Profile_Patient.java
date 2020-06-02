package com.example.mad_assigment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile_Patient extends AppCompatActivity {

    TextView Name, Email, Phone;
    ImageView ProfPic;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Name = findViewById(R.id.patient_Name);
        Email = findViewById(R.id.patient_Email);
        Phone = findViewById(R.id.patient_Phone);
        ProfPic = findViewById(R.id.patient_Pp);

    }
}
