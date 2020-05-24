package com.example.mad_assigment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddPatient extends AppCompatActivity{

    Button create;
    EditText search;
    RecyclerView mRecycleView;
    medicineAdaptor mAdaptor;
    ArrayList<medicineModel> medicineList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        mRecycleView = findViewById(R.id.mRV);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mAdaptor = new medicineAdaptor(this , medicineList);
        mRecycleView.setAdapter((mAdaptor));

        search = (EditText) findViewById(R.id.editText2);

        create = (Button) findViewById(R.id.button);

        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //move to add medicine activity(to be linked later)
            }
        });

        final FloatingActionButton addPatient = findViewById(R.id.AddMedicine);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add patient info
            }
        });
    }
}
