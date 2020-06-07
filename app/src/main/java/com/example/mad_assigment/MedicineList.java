package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    ArrayList<Modle> medicineList;
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
        // init list and firebase connection
        databaseReference = FirebaseDatabase.getInstance().getReference();
        medicinepic = new ArrayList<>();
        medicineList = new ArrayList<>();

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
        setUpRecyclerView();
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
//    private void fetchpatientMedList(){
//        // fetch patient medicine list
//
//        medReference.child(medKey).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Modle modle = snapshot.getValue(Modle.class);
//                    medicineList.add(modle);
//                    Log.d("#d" , modle.getDescription());
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }

    /**
     * setup recyclerview
     */
    private void setUpRecyclerView(){
        Query query = medReference.child("e6aaa1d7-d").orderByChild("priority");
//        Query query = medReference.child(medKey);
        FirebaseRecyclerOptions<Modle> options = new FirebaseRecyclerOptions.Builder<Modle>()
                .setQuery(query, Modle.class)
                .build();

        adaptor = new MAdaptor( options );
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




}