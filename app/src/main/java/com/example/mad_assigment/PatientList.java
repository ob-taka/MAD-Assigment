package com.example.mad_assigment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PatientList extends AppCompatActivity{

    SearchView search;
    RecyclerView PRecycleView;
    PatientAdaptor PAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);


        PRecycleView = findViewById(R.id.PatientrecyclerView);
        PRecycleView.setLayoutManager(new LinearLayoutManager(this));

        PAdaptor = new PatientAdaptor(this, fetchData());
        PRecycleView.setAdapter((PAdaptor));

        search = (SearchView) findViewById(R.id.searchpatient);

        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(PatientList.this  , AddPatient.class );
                startActivity(nextActivity);
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                PAdaptor.getFilter().filter(newText);
                return false;
            }
        });


    }

    // this function fetches data from firebase server
    private ArrayList<PatientModel> fetchData(){
        ArrayList<PatientModel> data = new ArrayList<>();

        data.add(new PatientModel( R.drawable.pill , "Gayman" ));
        data.add(new PatientModel( R.drawable.pill , "Gayman syrup"));
        data.add(new PatientModel(R.drawable.pill , "runlingay"));
        data.add(new PatientModel(R.drawable.pill , "random" ));
        data.add(new PatientModel(R.drawable.pill  , "oxmal" ));

        return data;
    }
}
