package com.example.mad_assigment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ViewPatient extends AppCompatActivity{

    RecyclerView mRecycleView;
    MedicineAdaptor mAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        mRecycleView = findViewById(R.id.mRV);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mAdaptor = new MedicineAdaptor(fetchData());
        mRecycleView.setAdapter((mAdaptor));

    }
    // this function fetches data from firebase server
    private ArrayList<MedicineModel> fetchData(){
        ArrayList<MedicineModel> data = new ArrayList<>();

        data.add(new MedicineModel("Panadol" , R.drawable.pill));
        data.add(new MedicineModel("Cough syrup" , R.drawable.pill));
        data.add(new MedicineModel("Antibiotics" , R.drawable.pill));
        data.add(new MedicineModel("Cough syrup" , R.drawable.pill));
        data.add(new MedicineModel("Antibiotics" , R.drawable.pill));

        return data;
    }
}
