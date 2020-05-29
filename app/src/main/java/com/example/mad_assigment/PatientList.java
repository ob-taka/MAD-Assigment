package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientList extends AppCompatActivity{

    SearchView search;
    RecyclerView PRecycleView;
    PatientAdaptor PAdaptor;
    ArrayList<PatientModel> patientLists = new ArrayList<>();
    ArrayList<String> patientkeyList = new ArrayList<>();
    ArrayList<String> patientnameList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        PRecycleView = findViewById(R.id.PatientrecyclerView);
        PRecycleView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference UserRef = database.getReference("Users");
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot patient : dataSnapshot.getChildren()) {
                    PatientModel patientdata = patient.getValue(PatientModel.class);
                    Log.d("d" , patientdata.getPatientEmail());
                    patientLists.add(patientdata);
                    if(!patientdata.isStatus()) {
                        patientkeyList.add(patient.getKey());
                        patientnameList.add(patientdata.getPatientName());
                    }
                    PAdaptor.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("Firebase_bot", "Failed to read value.", databaseError.toException());
            }
        });

        PAdaptor = new PatientAdaptor(this, patientLists);
        PRecycleView.setAdapter((PAdaptor));

        search = (SearchView) findViewById(R.id.searchpatient);

        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(PatientList.this  , AddPatient.class );
                nextActivity.putExtra("plist" , patientkeyList);
                nextActivity.putExtra("nlist" , patientnameList);
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
                Log.d("d" , "flitering");

                return false;
            }
        });
    }

    public void fetchPData(){
//         init medicine branch of real time database
//        String[] m = {"Panadol" , "Cough Syrup" , "Acetaminophen" ,  "Adderall" ,  "Alprazolam" ,  "Amitriptyline" ,  "Amlodipine" ,  "Amoxicillin" ,  "Ativan" , "Atorvastatin"};
//        for (String medicine: m) {
//            DatabaseReference medRef = database.getReference("Medicine");
//            String key = medRef.push().getKey();
//            MedicineModel med = new MedicineModel(medicine , "Before Food" , "10:00 AM" , R.drawable.pill);
//            medRef.child(key).setValue(med);
//        }
//
//        String[] n = {"Emma" , "Olivia" , "Isabella" };
//        String[] e = {"Emma@" , "Olivia@" , "Isabella@" };
//        for (int i = 0; i < n.length; i++) {
//            DatabaseReference medRef = database.getReference("Users");
//            String key = medRef.push().getKey();
//            PatientModel people = new PatientModel(1 , n[i] , e[i] , false);
//            medRef.child(key).setValue(people);
//            HashMap<String , Boolean> mlist = new HashMap<>();
//            DatabaseReference userRef = database.getReference("patientMedicineList");
//            for (int k = 0; k < m.length ; k++
//            ) {
//                mlist.put(m[k] , false);
//            }
//            userRef.child(key).setValue(mlist);
//        }

    }
}
