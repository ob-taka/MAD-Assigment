package com.health.anytime;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddMedicinePage extends AppCompatActivity {

    public static EditText searchMed;
    TextView dose;
    String medName,medType,medKey;
    ImageButton plus,minus;
    Button breakfast,lunch,dinner,submit;
    Double doseNumber;
    EditText days;
    RadioButton before,after;
    ProgressBar progressBar;

    Integer correctMed,breakfastValid,lunchValid,dinnerValid,errors;
    DatabaseReference databaseReference;
    ArrayList<String> med_list;
    ArrayList<String> id_list;
    SearchAdapter searchAdapter;
    public static RecyclerView recyclerView;
    TimePickerDialog picker;
    private double medqty;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String doctorid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    String patientkey;
    FirebaseFirestore db;
    RadioGroup radioGroup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_med);
        dose=findViewById(R.id.dose);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        med_list = new ArrayList<>();
        id_list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        searchMed = findViewById(R.id.search_med);
        breakfast=findViewById(R.id.breakfast);
        days=findViewById(R.id.days);
        lunch=findViewById(R.id.lunch);
        dinner=findViewById(R.id.dinner);
        submit=findViewById(R.id.submit);
        db = FirebaseFirestore.getInstance();
        radioGroup=findViewById(R.id.radioGroup);
        before=findViewById(R.id.before);
        after=findViewById(R.id.after);
        progressBar=findViewById(R.id.progressBar2);
        Intent intent = getIntent();
        medKey = intent.getStringExtra("medKey");
        patientkey = intent.getStringExtra("patientKey");
        final DecimalFormat df = new DecimalFormat("#");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        doseNumber=0.0;
        breakfastValid=0;
        dinnerValid=0;
        lunchValid=0;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.addItemDecoration((new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)));

        //Creating Spinner , change
        Spinner spinner=findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.medType,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);

                if (selectedItemText.equals("ml")){
                    doseNumber=0.0;

                    medType="ml";
                    String doseString=doseNumber.toString();
                    dose.setText(doseString);

                }
                else if(selectedItemText.equals("tablets")){
                    medType="tablet";
                    doseNumber=0.0;

                    String doseString= df.format(doseNumber);
                    dose.setText(doseString);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Changing colour when selected
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(breakfastValid==0){
                    breakfastValid=1;
                    breakfast.setTextColor(getApplication().getResources().getColor(R.color.green));
                }
                else{
                    breakfastValid=0;
                    breakfast.setTextColor(getApplication().getResources().getColor(R.color.colorPrimary));
                }

            }
        });
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lunchValid==0){
                    lunchValid=1;
                    lunch.setTextColor(getApplication().getResources().getColor(R.color.green));
                }
                else{
                    lunchValid=0;
                    lunch.setTextColor(getApplication().getResources().getColor(R.color.colorPrimary));
                }

            }
        });
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dinnerValid==0){
                    dinnerValid=1;
                    dinner.setTextColor(getApplication().getResources().getColor(R.color.green));
                }
                else{
                    dinnerValid=0;
                    dinner.setTextColor(getApplication().getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        //Changing number in textview based on button pressed
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(medType.equals("tablet")){
                    doseNumber++;
                    String doseString= df.format(doseNumber);
                    dose.setText(doseString);
                }
                else if (medType.equals("ml"))
                {
                    doseNumber+=0.5;
                    String doseString=doseNumber.toString();
                    dose.setText(doseString);

                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doseNumber!=0){


                    if(medType.equals("tablet")){
                        doseNumber--;
                        String doseString= df.format(doseNumber);
                        dose.setText(doseString);
                    }
                    else if (medType.equals("ml"))
                    {
                        doseNumber-=0.5;
                        String doseString=doseNumber.toString();
                        dose.setText(doseString);

                    }
                }
            }
        });

        searchMed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    //Clearing recycler view
                    med_list.clear();
                    id_list.clear();
                    searchAdapter.notifyDataSetChanged();

                    searchMed.clearFocus();


                }
                return false;
            }
        });

        //When text is edited in editText
        searchMed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("Test","1");
                if (!s.toString().isEmpty()) {
                    medName=searchMed.getText().toString();
                    if(searchMed.hasFocus()){
                        setAdapter(s.toString());}

                }

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if all tasks has been selected.
                checkValid();
                if (errors==0){
                    progressBar.setVisibility(View.VISIBLE);
                    // fetch number of stock for selected medicine firebase
                    getMedQty(medName);
                    // delay to provide race condition
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            postmed();
                        }
                    }, 300);

                }
            }
        });

    }

    private void setAdapter(final String searchedString) {
        //Retrieving data in firebase
        databaseReference.child("Pharmacy").child(doctorid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                med_list.clear();
                id_list.clear();
                recyclerView.removeAllViews();
                int counter = 0;

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //String med_num=snapshot.getKey();
                    String med_name=snapshot.child("medicineTitle").getValue().toString();
                    String id_num=snapshot.child("medid").getValue().toString();
                    if(med_name.toLowerCase().contains(searchedString.toLowerCase())){
                        med_list.add(med_name);
                        id_list.add(id_num);
                        counter++;
                    }
                    else if (id_num.contains(searchedString)){
                        med_list.add(med_name);
                        id_list.add(id_num);
                        counter++;
                    }
                    //limit search result
                    if(counter==5){
                        break;
                    }
                }
                searchAdapter=new SearchAdapter(AddMedicinePage.this,med_list,id_list);
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddMedicinePage.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void checkValid(){
        correctMed = 0;
        errors=0;

        databaseReference.child("Pharmacy").child(doctorid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String med_name=snapshot.child("medicineTitle").getValue().toString();
                    Log.v("number",correctMed.toString());
                    //Validating if medicine name entered is same as medicine name in firebase
                    if (med_name.equals(medName)) {
                        correctMed=1;

                    }

                }
                if (searchMed.getText().toString().length()==0){
                    searchMed.setError("Please enter Medicine Name");
                }
                else if (correctMed==0){
                    searchMed.setError("Invalid Medicine Name");
                    errors=1;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String errorMsg = "";

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            errorMsg+="Please select before or after food";
            errors=1;
        }

        //Validating if all other fields have been filled

        if(breakfastValid==0 && lunchValid==0 && dinnerValid==0){
            errorMsg+="\n Please select the meal for medicine intake";
            errors=1;
        }
        if (doseNumber==0.0){
            errorMsg+="\n Please select the dosage";
            errors=1;
        }
        if(days.getText().toString().equals("")){
            days.setError("Please enter the number of days");
            errors=1;

        }
        Toast toast = Toast.makeText(getApplicationContext(),
                errorMsg,
                Toast.LENGTH_LONG);
        toast.show();


    }
    public void postmed(){
        int daysno=Integer.parseInt(days.getText().toString());

        String food = "";
        String desc="";
        if(medName.equals("Activated Charcoal")){
            desc="used to help treat a drug overdose or a poisoning";
        }
        else if(medName.equals("Panadol")){
            desc="Help with cought and flu";
        }
        else if(medName.equals("Benzonatate")){
            desc="used to relieve cough";
        }
        else if(medName.equals("Antibiotics")){
            desc="used to treat or prevent some types of bacterial infection";
        }

        if(before.isChecked())
        {
            food="Before Food";
        }
        else if (after.isChecked()) {
            food = "After Food";
        }
        // checking if there is enough stock
        if ( medqty > (daysno * (dinnerValid + lunchValid + breakfastValid) * doseNumber)) {
            medqty -= (daysno * (dinnerValid + lunchValid + breakfastValid) * doseNumber);
            //Deduct quantity of medicine from firebase
            databaseReference.child("Pharmacy").child(doctorid).child(medName).child("quantity").setValue(medqty);
            for(int i=0;i<daysno;i++) {

                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, i);
                Date date = c.getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String formattedDate = df.format(date);

                Map<String, Object> med = new HashMap<>();
                med.put("Name", medName);
                med.put("Dosage", doseNumber);
                med.put("Time",food);
                med.put("DetailDes",desc);
                med.put("Unit",medType);


                if(breakfastValid==1) {
                    db.collection("Medicines").document(medKey).collection("Day").document(formattedDate).collection("Breakfast").document(medName)
                            .set(med).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }

                if(lunchValid==1) {
                    db.collection("Medicines").document(medKey).collection("Day").document(formattedDate).collection("Lunch").document(medName)
                            .set(med).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }

                if(dinnerValid==1) {
                    db.collection("Medicines").document(medKey).collection("Day").document(formattedDate).collection("Dinner").document(medName)
                            .set(med).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }

            }

            Intent intent = new Intent(getApplicationContext(), MedicineList.class);
            intent.putExtra("patientmlist" , medKey);
            intent.putExtra("patientKey" ,patientkey );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else {
            buildDialog();
        }
        progressBar.setVisibility(View.GONE);

    }

    private void getMedQty(final String title){
        databaseReference.child("Pharmacy").child(doctorid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MedicineModel med = snapshot.getValue(MedicineModel.class);
                    if (snapshot.getKey().equals(title)){
                        medqty = med.getQuantity();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Used to alert user that patient email entered does not exist
     */
    private void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMedicinePage.this);
        builder.setTitle("this medicine does not have enough stock")
                .setPositiveButton("Refill", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), Pharmacy.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
        ;
        builder.create();
        builder.show();
    }
}
