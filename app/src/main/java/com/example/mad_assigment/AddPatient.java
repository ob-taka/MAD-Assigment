package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AddPatient extends AppCompatActivity{

    EditText name;
    EditText email;
    Button create;
    Button addMedicine;
    RecyclerView mRecycleView;
    MedicineAdaptor mAdaptor;
    ArrayList<MedicineModel> medicineList;
    DatabaseReference databaseReference;
    HashMap<String, PatientModel> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        // init list
        medicineList = new ArrayList<>();
        patientList = new HashMap<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        fetchMedicineData();

        // setup rv
        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdaptor = new MedicineAdaptor(medicineList);
        mRecycleView.setAdapter((mAdaptor));

        //onclicklistener for FAP and button

        // button inside recyclerview button : redirects user to add medicine activity
        addMedicine = findViewById(R.id.add_medicine);

        addMedicine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Intent nextActivity = new Intent(PatientList.this  , AddPatient.class );
//                startActivity(nextActivity);
            }
        });

        // submit button to push patient medicine list to firebase
        create = (Button) findViewById(R.id.button3);

        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // add patient to doctor list
                name = findViewById(R.id.name_edit_text);
                email = findViewById(R.id.email_edit_text);

                //dialog box to show that patient has been added
                buildDialog();

                // reset edittext
//                name.setText("");
//                email.setText("");
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PatientDAL.patientList.put(snapshot.getKey() , snapshot.getValue(PatientModel.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //fetch medicine list from firebase
    private void fetchMedicineData(){
        databaseReference.child("Medicine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    medicineList.add(snapshot.getValue(MedicineModel.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


            // fetch patient medicine list
//        databaseReference.child("patientMedicineList").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //clear rv views and data
//                modelArrayList.clear();
//                mRecycleView.removeAllViews();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (snapshot.getKey().equals(addedpatients.get())){
//                        GenericTypeIndicator<HashMap<String, Boolean>> to = new
//                                GenericTypeIndicator<HashMap<String, Boolean>>() {};
//                        HashMap<String, Boolean> map = snapshot.getValue(to);
//                        int count = 0;
//                        for(boolean ml: map.values()) {
//                            if(ml) {
//                                modelArrayList.add(medicineList.get(count));
//                            }
//                            count++;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }



    private void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPatient.this);
        builder.setTitle(R.string.addpatient)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseAddpatient();
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.create();
        builder.show();

    }

    private void firebaseAddpatient(){
        // construct medicine list from addmedicine activity
        HashMap<String, Boolean> patientMedicineList = new HashMap<>();
        // populate list with medicine in medicineList

        // find patient using name and email
        for (String i : patientList.keySet()) {
            PatientModel patient = patientList.get(i);
            if(patient.getPatientEmail().equals(email.getText().toString().trim()) && !patient.isStatus()) {
                databaseReference.child("patientMedicineList").child(i).setValue(patientMedicineList);
            }
        }

        // refresh patient list

    }


}
