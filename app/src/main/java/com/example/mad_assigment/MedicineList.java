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
import android.widget.Toast;

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

public class MedicineList extends AppCompatActivity{
    FloatingActionButton addMedicine;
    Button submit;
    String patientKey;
    String medKey;
    MAdaptor adaptor;
    ArrayList<String> medicinepic;
    DatabaseReference databaseReference;
    DatabaseReference medReference = FirebaseDatabase.getInstance().getReference().child("med_list");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);

        //receive patient key and medlist key from add patient
        Intent intent = getIntent();
        patientKey = intent.getStringExtra("patientKey");
        medKey = intent.getStringExtra("patientmlist");
        Toast.makeText(this, medKey , Toast.LENGTH_SHORT).show();
        // init list and firebase connection
        databaseReference = FirebaseDatabase.getInstance().getReference();
        medicinepic = new ArrayList<>();
        setUpRecyclerView();

        //onclicklistener for buttons

        // button inside recyclerview button : redirects user to add medicine activity
        addMedicine = findViewById(R.id.addmed);

        addMedicine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(MedicineList.this  , AddMedicinePage.class );
                nextActivity.putExtra("medKey" , medKey);
                startActivity(nextActivity);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
            }
        });

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // changing status of a patient from false to true
                // to indicate the patient has been added to the doctor's list
                databaseReference.child("User").child(patientKey).child("status").setValue(true);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchpatientMedList();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right); //animation
    }

//    /**
//     * Used to update the recyclerview and patient medicineList
//     * Contacts firebase for updates
//     * Using reference of medicine list from firebase
//     * medicine is added into patient medicine list and showed in recyclerview
//     */
    private void fetchpatientMedList(){
        // fetch patient medicine list
        medReference.child(medKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Modle med = snapshot.getValue(Modle.class);
                    medicinepic.add(med.getImg());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * setup recyclerview
     */
    private void setUpRecyclerView(){
        Query query = medReference.child(medKey).orderByChild("priority");
        FirebaseRecyclerOptions<Modle> options = new FirebaseRecyclerOptions.Builder<Modle>()
                .setQuery(query, Modle.class)
                .build();

        adaptor = new MAdaptor(this , options , medicinepic);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this ));
        recyclerView.setAdapter(adaptor);

        adaptor.setOnItemClickListener(new MAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                Modle modle = adaptor.getItem(position);
                modle.setExpanded(!modle.isExpanded());
                adaptor.notifyItemChanged(position);
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