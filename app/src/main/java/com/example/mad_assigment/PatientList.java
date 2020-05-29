package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.SearchView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientList extends AppCompatActivity{

    EditText search;
    RecyclerView PRecycleView;
    PatientAdaptor PAdaptor;
    ArrayList<String> patientLists;
    ArrayList<String> patientkeyList;
    ArrayList<String> medicinekeyList;
    DatabaseReference databaseReference;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        databaseReference = FirebaseDatabase.getInstance().getReference();



        patientLists = new ArrayList<>();

        PRecycleView = findViewById(R.id.PatientrecyclerView);
        PRecycleView.setLayoutManager(new LinearLayoutManager(this));

        PAdaptor = new PatientAdaptor(this, patientLists);


        PAdaptor = new PatientAdaptor(this, patientLists);
        PRecycleView.setAdapter((PAdaptor));

        search = (EditText) findViewById(R.id.searchpatient);
        addData();
        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(PatientList.this  , AddPatient.class );
                nextActivity.putExtra("plist" , patientkeyList);
                nextActivity.putExtra("nlist" , patientnameList);
                startActivity(nextActivity);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    setPAdaptor(s.toString());
                }

            }
        });
    }

    /*public void fetchPData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();


//         init medicine branch of real time database
//        String[] m = {"Panadol" , "Cough Syrup" , "Acetaminophen" ,  "Adderall" ,  "Alprazolam" ,  "Amitriptyline" ,  "Amlodipine" ,  "Amoxicillin" ,  "Ativan" , "Atorvastatin"};
//        for (String medicine: m) {
//            DatabaseReference medRef = database.getReference("Medicine");
//            String key = medRef.push().getKey();
//            MedicineModel med = new MedicineModel(medicine , "Before Food" , "10:00 AM" , R.drawable.pill);
//            medRef.child(key).setValue(med);
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
//            for (int k = 0; k < m.length ; k++
//            ) {
//                mlist.put(m[k] , false);
//            }
//            userRef.child(key).setValue(mlist);
//        }


    }*/
    private void addData(){
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientLists.clear();
                PRecycleView.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String patientName = snapshot.child("patientName").getValue().toString();
                    patientLists.add(patientName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setPAdaptor(final String searchedString){
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientLists.clear();
                PRecycleView.removeAllViews();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String patientName=snapshot.child("patientName").getValue().toString();
                    if(patientName.toLowerCase().contains(searchedString.toLowerCase())){
                        patientLists.add(patientName);
                    }



                }
                PAdaptor=new PatientAdaptor(PatientList.this,patientLists);
                PRecycleView.setAdapter(PAdaptor);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
