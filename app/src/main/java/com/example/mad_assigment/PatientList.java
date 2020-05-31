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

        //initdata();

        patientLists = new ArrayList<>();
        patientkeyList = new ArrayList<>();

        PRecycleView = findViewById(R.id.PatientrecyclerView);
        PRecycleView.setLayoutManager(new LinearLayoutManager(this));
        PAdaptor = new PatientAdaptor(this, patientLists);
        PRecycleView.setAdapter((PAdaptor));

        search = (EditText) findViewById(R.id.searchpatient);
        fetchPatientData();
        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(PatientList.this  , AddPatient.class );
                nextActivity.putExtra("plist" , patientkeyList);
                nextActivity.putExtra("nlist" , patientLists);
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
//    public void initdata(){
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
    private void fetchPatientData(){
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientLists.clear();
                PRecycleView.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    patientkeyList.add(snapshot.getKey());
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
                //[Starts] clears views and data
                patientLists.clear();
                PRecycleView.removeAllViews();
                // [Ends]
                //[Starts] loops through firebase data and find matching search term
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String patientName=snapshot.child("patientName").getValue().toString();
                    if(patientName.toLowerCase().contains(searchedString.toLowerCase())){
                        patientLists.add(patientName);
                    }
                }
                // [Ends]
                // Updates the patient adaptor with data
                PAdaptor=new PatientAdaptor(PatientList.this,patientLists);
                PRecycleView.setAdapter(PAdaptor);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
