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
//        public void initdata(){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        String[] m = {"Panadol" , "Cough Syrup" , "Acetaminophen" ,  "Adderall" ,  "Alprazolam" ,  "Amitriptyline" ,  "Amlodipine" ,  "Amoxicillin" ,  "Ativan" , "Atorvastatin"};
//        String[] mId = {"Medicine_id1","Medicine_id2","Medicine_id3","Medicine_id4","Medicine_id5","Medicine_id6","Medicine_id7","Medicine_id8","Medicine_id9","Medicine_id10"};
//        for (int j = 0; j < m.length; j ++) {
//            DatabaseReference medRef = database.getReference("Medicine");
//            MedicineModel med = new MedicineModel(m[j] , "Before Food" , "10:00 AM" , R.drawable.pill);
//            medRef.child(mId[j]).setValue(med);
//        }
//
//        String[] n = {"Emma" , "Olivia" , "Isabella" };
//        String[] e = {"Emma@" , "Olivia@" , "Isabella@" };
//        for (int i = 0; i < n.length; i++) {
//            DatabaseReference medRef = database.getReference("Users");
//            String key = medRef.push().getKey();
//            PatientModel people = new PatientModel(1 , n[i] , e[i] , false);
//            medRef.child(key).setValue(people);
//            HashMap<String , Boolean> mlist = new HashMap<>();
//            DatabaseReference userRef = database.getReference("patientMedicineList");
//            for (int k = 0; k < mId.length ; k++
//            ) {
//                mlist.put(mId[k] , false);
//            }
//            userRef.child(key).setValue(mlist);
//        }
//    }
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
