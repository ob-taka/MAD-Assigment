package com.example.mad_assigment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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

public class AddPatient extends AppCompatActivity{

    Spinner choose;
    Button create;
    RecyclerView mRecycleView;
    MedicineAdaptor mAdaptor;
    ArrayList<MedicineModel> modelArrayList;
    ArrayList<PatientModel> patientModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        Bundle data = getIntent().getExtras();
        ArrayList<String> addedpatients = data.getStringArrayList("plist");

        // setup rv
        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mAdaptor = new MedicineAdaptor(modelArrayList);
        fetchData();

        mRecycleView.setAdapter((mAdaptor));

        //onclicklistener for FAP and button
        create = (Button) findViewById(R.id.button3);
        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton2);

        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //move to add medicine activity(to be linked later)

                Toast.makeText(getApplicationContext(), "Patient Added!" , Toast.LENGTH_LONG).show();
            }
        });

        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add patient info
            }
        });

        //populate spinner data
        choose = findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, addedpatients);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        choose.setAdapter(spinnerArrayAdapter);

    }



    // this function fetches data from firebase server
    private void fetchData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference UserRef = database.getReference("Medicine");
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,MedicineModel>> genericTypeIndicator =new GenericTypeIndicator<HashMap<String,MedicineModel>>(){};
                HashMap<String,MedicineModel> modelHashMap = dataSnapshot.getValue(genericTypeIndicator);
                modelArrayList.addAll(modelHashMap.values());
                mAdaptor.notifyDataSetChanged();
                Log.d("Debug" , "Loaded list");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("Firebase_bot", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
