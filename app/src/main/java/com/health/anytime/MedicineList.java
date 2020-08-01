package com.health.anytime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MedicineList extends AppCompatActivity{
    FloatingActionButton addMedicine;
    Button submit;
    String patientKey;
    String medKey;
    String meal;
    String day;
    MAdaptor adaptor;
    ArrayList<String> medicinepic;
    ArrayList<Modle> medicineList;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userRef;

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
        auth = FirebaseAuth.getInstance();
        medicinepic = new ArrayList<>();
        medicineList = new ArrayList<>();
        meal = settime();
        Calendar calendar  = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentdate = calendar.getTime();
        day = dateFormat.format(currentdate);

        userRef = db.collection("Medicines").document(medKey)
                .collection("Day").document(day)
                .collection(meal);
        //onclicklistener for buttons
        // button inside recyclerview button : redirects user to add medicine activity
        addMedicine = findViewById(R.id.addmed);
        addMedicine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(MedicineList.this  , AddMedicinePage.class );
                nextActivity.putExtra("medKey" , medKey);
                nextActivity.putExtra("patientKey" , patientKey);
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
                populateContacts();
                Intent nextActivity = new Intent(MedicineList.this  , PatientList.class );
                startActivity(nextActivity);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpRecyclerView(medKey , userRef);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right); //animation
    }

    /**
     * change the greeting base on the time of the time
     */
    private String  settime() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            return "Breakfast";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            return "Lunch";
        } else if (timeOfDay >= 16 && timeOfDay < 23) {
            return "Dinner";
        }
        return null;
    }

    /**
     * setup recyclerview , fetching medicine date from firestore
     */
    private void setUpRecyclerView(String ID , CollectionReference ref){
        Query query = ref.orderBy("title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Modle> options = new FirestoreRecyclerOptions.Builder<Modle>()
                .setQuery(query, Modle.class)
                .build();

        // passing data into adaptor
        adaptor = new MAdaptor(options);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

        adaptor.startListening();// listening for data in firestore

        // overrides the interface created in the adaptor class to customise the even of the click
        adaptor.setOnItemClickListener(new MAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot dataSnapshot, int position) {
                Modle modle = adaptor.getItem(position);
                modle.setExpanded(!modle.isExpanded());
                adaptor.notifyItemChanged(position);
            }
        });
    }

    /**
     * Add doctor's contact to patient's contact list
    */
    private void populateContacts(){
        databaseReference.child("Contacts").child(patientKey).child(auth.getCurrentUser().getUid()).child("Contacts").setValue("Contact Saved");
        databaseReference.child("Contacts").child(auth.getCurrentUser().getUid()).child(patientKey).child("Contacts").setValue("Contact Saved");
    }

    /**
     * Retrieve uid of doctor using from shared preferences
     */
    private String getDoctorKey(){
        SharedPreferences sharedPreferences = MedicineList.this.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("uid","");
    }

}