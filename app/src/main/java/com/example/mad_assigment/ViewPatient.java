package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPatient extends AppCompatActivity{

    RecyclerView mRecycleView;
    MedicineAdaptor mAdaptor;
    DatabaseReference databaseReference;
    ArrayList<MedicineModel> medicineList;
    ArrayList<MedicineModel> patientMList;
    String patientName;
    TextView nameView;
    String patientKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        final Bundle data = getIntent().getExtras();
        patientName =  data.getString("patientname");

        medicineList = new ArrayList<>();
        patientMList = new ArrayList<>();

        nameView = findViewById(R.id.patientName);
        nameView.setText(patientName);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        
        fetchMedicineData();
        fetchData();
        fetchMdata();

        mRecycleView = findViewById(R.id.mRV);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mAdaptor = new MedicineAdaptor(medicineList);
        mRecycleView.setAdapter((mAdaptor));

    }
    // this function fetches data from firebase server
    private void fetchData(){

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PatientModel patient = snapshot.getValue(PatientModel.class);

                    if(patient.getPatientName().equals(patientName)){
                        patientKey = snapshot.getKey();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private  void fetchMdata(){
        databaseReference.child("patientMedicineList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicineList.clear();
                mRecycleView.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(patientKey)){
                        GenericTypeIndicator<HashMap<String, Boolean>> to = new
                                GenericTypeIndicator<HashMap<String, Boolean>>() {};
                        HashMap<String, Boolean> map = snapshot.getValue(to);
                        int count = 0;
                        for(boolean ml: map.values()) {
                            if(ml) {
                                medicineList.add(patientMList.get(count));
                            }
                            count++;
                        }
                        System.out.println(medicineList);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // add dialog
            }
        });
    }
    //fetch medicine list from firebase
    private void fetchMedicineData(){
        databaseReference.child("Medicine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    patientMList.add(snapshot.getValue(MedicineModel.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
