package com.example.mad_assigment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddPatient extends AppCompatActivity{

    Spinner choose;
    Button create;
    RecyclerView mRecycleView;
    MedicineAdaptor mAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        // setup rv
        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mAdaptor = new MedicineAdaptor(this , fetchData());
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
                this, android.R.layout.simple_spinner_item, fetchPatientData());
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        choose.setAdapter(spinnerArrayAdapter);

    }

    // this function fetches data from firebase server
    private ArrayList<MedicineModel> fetchData(){
        ArrayList<MedicineModel> data = new ArrayList<>();

        data.add(new MedicineModel("Panadol",  R.drawable.pill ));
        data.add(new MedicineModel("Panadol",  R.drawable.pill ));
        data.add(new MedicineModel("Panadol",  R.drawable.pill ));
        data.add(new MedicineModel("Panadol",  R.drawable.pill ));
        data.add(new MedicineModel("Panadol",  R.drawable.pill ));

        return data;
    }

    private ArrayList<String> fetchPatientData(){
        ArrayList<String> data = new ArrayList<>();

        data.add("Gayman" );
        data.add("oxmal" );
        data.add("myman" );
        data.add("dummd" );
        data.add("runlin no cock" );
        data.add("Shrey handsum" );

        return data;
    }
}
