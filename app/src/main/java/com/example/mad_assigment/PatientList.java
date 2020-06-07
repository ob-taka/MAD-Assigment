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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ProgressBar;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientList extends AppCompatActivity{
    EditText search;
    RecyclerView PRecycleView;
    PatientAdaptor PAdaptor;
    ArrayList<PatientModel> patientLists;
    HashMap<String,PatientModel> unaddedPatients;
    ArrayList<String> patientpic;
    DatabaseReference databaseReference;
    ArrayList<PatientModel> clonePatientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        //init Lists and Firebase connection
        databaseReference = FirebaseDatabase.getInstance().getReference();
        patientLists = new ArrayList<>();
        patientpic = new ArrayList<>();
        unaddedPatients = new HashMap<>();
        clonePatientList = new ArrayList<>();

        //setup recyclerview
        PRecycleView = findViewById(R.id.PatientrecyclerView);
        PRecycleView.setLayoutManager(new LinearLayoutManager(this));
        PAdaptor = new PatientAdaptor(this, patientLists , patientpic);
        PRecycleView.setAdapter((PAdaptor));

        //onclicklistener for FloatingActionButton and edit text
        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(PatientList.this  , AddPatient.class );
                nextActivity.putExtra("keys" , unaddedPatients);
                startActivity(nextActivity);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
            }
        });

        // used to search for patient based on user input in edittext
        search = (EditText) findViewById(R.id.searchpatient);
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    Search(s.toString());
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
        search.setText("");
        clonePatientList.clear();
        //initdata();
        fetchPatientData();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right); //animation
    }

    /**
     * Fetch patient data from firebase and add patient data as a patientModel class into patientList and clonepatientList
     * clonepatientList is used a copy of the full patient list in firebase , it is also used to filter in search method
     */
    private void fetchPatientData(){
        // fetch patient from firebase
        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientLists.clear();
                PRecycleView.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PatientModel patient = snapshot.getValue(PatientModel.class);
                    if (patient.isStatus() && patient.getRole().equals("Patient")){
                        patientpic.add(patient.getPatientProfilepic());
                        patientLists.add(patient);
                        clonePatientList.add(patient);
                    }else if(!patient.isStatus()){
                        unaddedPatients.put(snapshot.getKey() , patient);
                    }

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
     * This method filter clonepatientList to find a similar name based on user input
     */
    private void Search(final String searchedString){
        patientLists.clear();
        PRecycleView.removeAllViews();
        for (PatientModel patient:
            clonePatientList ) {
            if(patient.getPatientName().toLowerCase().contains(searchedString.toLowerCase())){
                patientLists.add(patient);
            }
        }
        PAdaptor = new PatientAdaptor(this, patientLists , patientpic);
        PRecycleView.setAdapter((PAdaptor));
    }


    // input dummy data
    public void initdata(){
        //add med to firebase
        String[] m = {"Panadol" , "Cough Syrup" , "Acetaminophen" ,  "Adderall" ,  "Alprazolam" ,  "Amitriptyline" ,  "Amlodipine" ,  "Amoxicillin" ,  "Ativan" , "Atorvastatin"};
//        for (int j = 0; j < m.length; j ++) {
//            DatabaseReference medRef = databaseReference.getReference("Medicine");
//            Modle med = new Modle(m[j], "Before Food" , "10:00 AM" , );
//            medRef.child(m[j]).setValue(med);
//        }
        // add med list to firebase

        String[] n = {"Emma" , "Olivia" , "Isabella" , "Tyler" };
        String[] e = {"Emma@gmail.com" , "Olivia@gmail.com" , "Isabella@gmail.com" , "Tyler@gmail.com"};
        for (int i = 0; i < n.length; i++) {
            DatabaseReference medRef = databaseReference.child("User");
            String key = medRef.push().getKey();
            PatientModel people = new PatientModel(key+".jpg", n[i] , e[i] , false , "Patient" , "");
            medRef.child(key).setValue(people);
        }
    }
}
