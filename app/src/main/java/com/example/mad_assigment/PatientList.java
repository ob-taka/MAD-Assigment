package com.example.mad_assigment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PatientList extends AppCompatActivity{

    EditText search;
    RecyclerView PRecycleView;
    PatientAdaptor PAdaptor;
//    PatientrecyclerView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        search = (EditText)findViewById(R.id.editText);

        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(PatientList.this  , AddPatient.class );
                startActivity(nextActivity);
            }
        });

        PRecycleView = findViewById(R.id.mRV);
        PRecycleView.setLayoutManager(new LinearLayoutManager(this));

        PAdaptor = new PatientAdaptor(this, fetchData());
        PRecycleView.setAdapter((PAdaptor));
    }

    // this function fetches data from firebase server
    private ArrayList<patientModel> fetchData(){
        ArrayList<patientModel> data = new ArrayList<>();

        data.add(new patientModel( R.drawable.pill , "Gayman"));
        data.add(new patientModel( R.drawable.pill , "Gayman syrup"));
        data.add(new patientModel(R.drawable.pill , "runlingay"));
        data.add(new patientModel(R.drawable.pill , "random" ));
        data.add(new patientModel(R.drawable.pill  , "oxmal" ));

        return data;
    }
}
