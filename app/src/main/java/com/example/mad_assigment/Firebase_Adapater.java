package com.example.mad_assigment;

import android.graphics.ColorSpace;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

public class Firebase_Adapater{
    // Write a message to the database
    private FirebaseDatabase database;
    private static final String TAG = "Firebase_Bot";


    public Firebase_Adapater(){
        this.database = FirebaseDatabase.getInstance();
    }

//    private void writeNewUser(String name, String email , int profile ) {
//        DatabaseReference UserRef = database.getReference("Users");
//        String key = UserRef.push().getKey();
//        PatientModel user = new PatientModel( profile, name, email);
//        UserRef.child(key).setValue(user);
//    }

    private void writeNewMedicine( String title , String description, String time , int img) {
        DatabaseReference medRef = database.getReference("Medicine");
        String key = medRef.push().getKey();
        MedicineModel medicine = new MedicineModel(title , description , time , img);
        medRef.child(key).setValue(medicine);
    }
//    private void writeNewMedicineList() {
//        Modle medicine = new Modle(title , description , time , img);
//        DatabaseReference medRef = database.getReference();
//        medRef.child("Users").child(medicineId).setValue(medicine);
//    }

    public void init_firebase(){
        String[] m = {"Panadol" , "Cough Syrup" , "Acetaminophen" ,  "Adderall" ,  "Alprazolam" ,  "Amitriptyline" ,  "Amlodipine" ,  "Amoxicillin" ,  "Ativan" , "Atorvastatin"};

        for (String medicine:
             m) {
            writeNewMedicine(medicine, "Before Food", "10:00 AM", R.drawable.pill);
        }

        String[] n = {"Emma" , "Olivia" , "Isabella" ,  "Sophia" ,  "Sophia" ,  "Amelia" ,  "Amlodipine" ,  "Amoxicillin" ,  "Ativan" , "Atorvastatin"};
        String[] e = {"Emma" , "Cough Syrup" , "Acetaminophen" ,  "Adderall" ,  "Alprazolam" ,  "Amitriptyline" ,  "Amlodipine" ,  "Amoxicillin" ,  "Ativan" , "Atorvastatin"};

        for (int i = 0; i < m.length; i++) {
            DatabaseReference medRef = database.getReference("Users");
            String key = medRef.push().getKey();
        }
    }
    // test
//    public ArrayList<PatientModel> fetchPData(){
//        DatabaseReference UserRef = database.getReference("User");
//        UserRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("Firebase_bot", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("Firebase_bot", "Failed to read value.", error.toException());
//            }
//        });
//    }
//    public ArrayList<MedicineModel> fetchMData(){
//        DatabaseReference medRef = database.getReference("Medicine");
//        // Read from the database
//        medRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("Firebase_bot", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("Firebase_bot", "Failed to read value.", error.toException());
//            }
//        });
//    }
    // fix

    public void fetchMData(){
        DatabaseReference medRef = database.getReference("Medicine");
        // Read from the database
        medRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Modle value = dataSnapshot.getValue(Modle.class);
                System.out.println(dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Firebase_bot", "Failed to read value.", error.toException());
            }
        });
    }
}
