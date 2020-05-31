package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddPatient extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner choose;
    Button create;
    RecyclerView mRecycleView;
    MedicineAdaptor mAdaptor;
    ArrayList<MedicineModel> modelArrayList;
    ArrayList<MedicineModel> medicineList;
    ArrayList<String> addedpatients;
    ArrayList<String> patientsname;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        modelArrayList = new ArrayList<>();
        medicineList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        fetchMedicineData();


        final Bundle data = getIntent().getExtras();
        addedpatients = data.getStringArrayList("plist");
        patientsname = data.getStringArrayList("nlist");

        // setup rv
        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdaptor = new MedicineAdaptor(modelArrayList);
        mRecycleView.setAdapter((mAdaptor));

        //onclicklistener for FAP and button
        create = (Button) findViewById(R.id.button3);
        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton2);

        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //move to add medicine activity(to be linked later)

            }
        });

        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add patient info
                Toast.makeText(getApplicationContext(), "Patient Added!" , Toast.LENGTH_LONG).show();
            }
        });


        choose = findViewById(R.id.spinner);
        //populate spinner with data
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, patientsname);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        choose.setAdapter(spinnerArrayAdapter);
        choose.setOnItemSelectedListener(this); //set onItemSelected listener

    }

    // onItemSelected listener to select patient medicine list
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
        // when an patient name is selected , the corresponding patient's prespective list will be displayed in the receycler view .
        databaseReference.child("patientMedicineList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clear rv views and data 
                modelArrayList.clear();
                mRecycleView.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(addedpatients.get(position))){
                        GenericTypeIndicator<HashMap<String, Boolean>> to = new
                                GenericTypeIndicator<HashMap<String, Boolean>>() {};
                        HashMap<String, Boolean> map = snapshot.getValue(to);
                        int count = 0;
                        for(boolean ml: map.values()) {
                            if(ml) {
                                modelArrayList.add(medicineList.get(count));
                            }
                            count++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //fetch medicine list from firebase
    private void fetchMedicineData(){
        databaseReference.child("Medicine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    medicineList.add(snapshot.getValue(MedicineModel.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
