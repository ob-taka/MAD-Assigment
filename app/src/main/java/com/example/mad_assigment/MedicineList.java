package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MedicineList extends AppCompatActivity{
    Button addMedicine;
    Button submit;
    String patientKey;
    RecyclerView mRecycleView;
    MedicineAdaptor mAdaptor;
    ArrayList<MedicineModel> medicineList;
    ArrayList<MedicineModel> patientMedList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);

        //receive patient key from add patient
        Intent intent = getIntent();
        patientKey = intent.getStringExtra("patientKey");

        // init list and firebase connection
        medicineList = new ArrayList<>();
        patientMedList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        // setup recyclerview
        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdaptor = new MedicineAdaptor(patientMedList);
        mRecycleView.setAdapter((mAdaptor));

        //onclicklistener for buttons

        // button inside recyclerview button : redirects user to add medicine activity
//        addMedicine = findViewById(R.id.add_medicine);
//
//        addMedicine.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
////                Intent nextActivity = new Intent(PatientList.this  , AddPatient.class );
////                startActivity(nextActivity);
//                  overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
//            }
//        });

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // changing status of a patient from false to true
                // to indicate the patient has been added to the doctor's list
                databaseReference.child("Users").child(patientKey).child("status").setValue(true);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchMedicineData();
        fetchpatientMedList();
    }

    /**
     * fetch medicine list from firebase
     * Used as a copy of a list of medicine in firebase
     * Called to fill up list and to reduce the number of requests to firebase
     */
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

    }

    /**
     * Used to update the recyclerview and patient medicineList
     * Contacts firebase for updates
     * Using reference of medicine list from firebase
     * medicine is added into patient medicine list and showed in recyclerview
     */
    private void fetchpatientMedList(){
        // fetch patient medicine list
        databaseReference.child("patientMedicineList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clear rv views and data
                patientMedList.clear();
                mRecycleView.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(patientKey)){
                        GenericTypeIndicator<HashMap<String, Boolean>> to = new
                                GenericTypeIndicator<HashMap<String, Boolean>>() {};
                        HashMap<String, Boolean> map = snapshot.getValue(to);
                        int count = 0;
                        for(boolean ml: map.values()) {
                            if(ml) {
                                patientMedList.add(medicineList.get(count));
                            }
                            count++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * creates and display alert dialog to Add patient activity , as confirmation
     * that the user wants to add the patient.
     * The user can choose to accept or cancel
     * Accept : changes patient status to true and update patient medicine list in firebase
     * Cancel : does nothing
     *
     */
//    private void buildDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(MedicineList.this);
//        builder.setTitle(R.string.addpatient)
//                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        firebaseAddpatient();
//                        Intent nextActivity = new Intent(MedicineList.this  , PatientList.class );
//                        startActivity(nextActivity);
//
//                    }
//                })
//                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
//        builder.create();
//        builder.show();
//
//    }

    /**
     *
     */
//    private void firebaseAddpatient(){
//        // construct medicine list from addmedicine activity
//        HashMap<String, Boolean> patientMedicineList = new HashMap<>();
//        // populate list with medicine in medicineList
//
//        // find patient using name and email
//        for (String i : patientList.keySet()) {
//            PatientModel patient = patientList.get(i);
//            if(patient.getPatientEmail().equals(email.getText().toString().trim()) && !patient.isStatus()) {
//                databaseReference.child("patientMedicineList").child(i).setValue(patientMedicineList);
//                databaseReference.child("Users").child(i).child("Status").setValue(true);
//            }
//        }
//    }


}