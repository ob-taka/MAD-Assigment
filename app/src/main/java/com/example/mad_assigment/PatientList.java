package com.example.mad_assigment;

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
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientList extends AppCompatActivity{

    SearchView search;
    RecyclerView PRecycleView;
    PatientAdaptor PAdaptor;
    ArrayList<PatientModel> patientLists;
    ArrayList<String> patientkeyList;
    ArrayList<String> medicinekeyList;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);



        patientLists = new ArrayList<>();

        PRecycleView = findViewById(R.id.PatientrecyclerView);
        PRecycleView.setLayoutManager(new LinearLayoutManager(this));

        PAdaptor = new PatientAdaptor(this, patientLists);

        fetchPData();

        PRecycleView.setAdapter((PAdaptor));

        search = (SearchView) findViewById(R.id.searchpatient);

        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(PatientList.this  , AddPatient.class );
//                nextActivity.putExtra("plist" , );
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

    public void fetchPData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();


//         init medicine branch of real time database
        String[] m = {"Panadol" , "Cough Syrup" , "Acetaminophen" ,  "Adderall" ,  "Alprazolam" ,  "Amitriptyline" ,  "Amlodipine" ,  "Amoxicillin" ,  "Ativan" , "Atorvastatin"};
        for (String medicine:
                m) {
            DatabaseReference medRef = database.getReference("Medicine");
            String key = medRef.push().getKey();
            MedicineModel med = new MedicineModel(medicine , "Before Food" , "10:00 AM" , R.drawable.pill);
            medRef.child(key).setValue(med);
        }
        String[] n = {"Emma" , "Olivia" , "Isabella" ,  "Sophia" ,  "Sophia" ,  "Amelia" ,  "Amlodipine" ,  "Amoxicillin" ,  "Ativan" , "Atorvastatin"};
        String[] e = {"Emma" , "Cough Syrup" , "Acetaminophen" ,  "Adderall" ,  "Alprazolam" ,  "Amitriptyline" ,  "Amlodipine" ,  "Amoxicillin" ,  "Ativan" , "Atorvastatin"};

        for (int i = 0; i < m.length; i++) {
            DatabaseReference medRef = database.getReference("Users");
            String key = medRef.push().getKey();
            PatientModel people = new PatientModel(1 , n[i] , e[i] );
            medRef.child(key).setValue(people);
            medRef.child(key).child("Status").setValue(false);
        }


        DatabaseReference UserRef = database.getReference("Users");
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot patient : dataSnapshot.getChildren()) {
                    PatientModel patientdata = patient.getValue(PatientModel.class);
                    patientLists.add(patientdata);
                    PAdaptor.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("Firebase_bot", "Failed to read value.", databaseError.toException());
            }
        });

    }

}
