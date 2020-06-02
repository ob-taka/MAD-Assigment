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
import android.widget.Toast;


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
    // todo : create a firebase class , change ui (research material design)
    EditText search;
    RecyclerView PRecycleView;
    PatientAdaptor PAdaptor;
    ArrayList<String> patientLists;
    ArrayList<String> patientkeyList;
    DatabaseReference databaseReference;
    ArrayList<String> clonePatientList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        patientLists = new ArrayList<>();
        patientkeyList = new ArrayList<>();
        clonePatientList = new ArrayList<>();

        PRecycleView = findViewById(R.id.PatientrecyclerView);
        PRecycleView.setLayoutManager(new LinearLayoutManager(this));

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

        search = (EditText) findViewById(R.id.searchpatient);
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    Search(s.toString());
                }else{
                    PAdaptor = new PatientAdaptor(PatientList.this, clonePatientList);
                    PRecycleView.setAdapter((PAdaptor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchPatientData();
        PAdaptor = new PatientAdaptor(this, patientLists);
        PRecycleView.setAdapter((PAdaptor));
    }

    private void fetchPatientData(){
        // fetch patient from firebase
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientLists.clear();
                PRecycleView.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    patientkeyList.add(snapshot.getKey());
                    String patientName = snapshot.child("patientName").getValue().toString();
                    patientLists.add(patientName);
                    clonePatientList.add(patientName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     *
     * @param searchedString , text user types in edittext to search for patient name
     */
    private void Search(final String searchedString){
        patientLists.clear();
        PRecycleView.removeAllViews();
        for (String patientName:
            clonePatientList ) {
            if(patientName.toLowerCase().contains(searchedString.toLowerCase())){
                patientLists.add(patientName);
            }
        }
        PAdaptor = new PatientAdaptor(this, patientLists);
        PRecycleView.setAdapter((PAdaptor));
    }

}
