package com.example.mad_assigment.experiment;

import androidx.annotation.NonNull;

import com.example.mad_assigment.PatientModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientDAL{
    // Data access layer for patient , it contains CRUD methods (Create , Read , Update and Delete)

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static HashMap< String , PatientModel> patientList = new HashMap<>();
//    public static ArrayList<MedicineModel> medicineList = new ArrayList<>();
//    public static HashMap<String, Boolean> patientMedicineList = new HashMap<>();

    public void CreatePatient(PatientModel patient){
        databaseReference.child("Users").push().setValue(patient);
    }

    public void UpdatePatient(String key , PatientModel patient){
        databaseReference.child("Users").child(key).setValue(patient);
    }
    public void DeletePatient(String key){
        databaseReference.child("Users").child(key).removeValue();
    }


}
