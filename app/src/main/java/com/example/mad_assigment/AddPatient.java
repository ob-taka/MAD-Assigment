package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddPatient extends AppCompatActivity{

    Spinner choose;
    Button create;
    RecyclerView mRecycleView;
    MedicineAdaptor mAdaptor;
    ArrayList<MedicineModel> modelArrayList = new ArrayList<>();
    ArrayList<String> addedpatients;
    ArrayList<String> patientsname;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        final Bundle data = getIntent().getExtras();
        addedpatients = data.getStringArrayList("plist");
        patientsname = data.getStringArrayList("nlist");
        database = FirebaseDatabase.getInstance();

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

        //populate spinner data
        choose = findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, patientsname);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        // dropdown onclick
        choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
                DatabaseReference MRef = database.getReference("patientMedicineList");
                MRef.addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot patientlist : dataSnapshot.getChildren()) {
                            if(patientlist.getKey().equals(addedpatients.get(position))){
                                GenericTypeIndicator<HashMap> medicineList = new GenericTypeIndicator<HashMap>() {};
                                medicineList = (GenericTypeIndicator<HashMap>) patientlist.getValue();
                                System.out.println(medicineList);
                            }
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
        });
        choose.setAdapter(spinnerArrayAdapter);


    }

}
