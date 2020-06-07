package com.example.mad_assigment;

import android.app.Activity;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class AddMedicinePage extends AppCompatActivity {

    public static EditText searchMed;
    RadioButton before,after;
    TextView dose;
    String medName,medType,medKey;
    ImageButton plus,minus;
    Button breakfast,lunch,dinner,submit;
    Double doseNumber;

    Integer correctMed,breakfastValid,lunchValid,dinnerValid,errors;


    DatabaseReference databaseReference;
    ArrayList<String> med_list;
    ArrayList<String> id_list;
    SearchAdapter searchAdapter;
    RadioGroup radioGroup;
    public static RecyclerView recyclerView;
    TimePickerDialog picker;

//git

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_med);
        radioGroup=findViewById(R.id.radioGroup);
        dose=findViewById(R.id.dose);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        med_list = new ArrayList<>();
        id_list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        searchMed = findViewById(R.id.search_med);
        breakfast=findViewById(R.id.breakfast);
        lunch=findViewById(R.id.lunch);
        dinner=findViewById(R.id.dinner);
        submit=findViewById(R.id.submit);
        before=findViewById(R.id.before);
        after=findViewById(R.id.after);
        Intent intent = getIntent();

        medKey = intent.getStringExtra("medKey");

        final DecimalFormat df = new DecimalFormat("#");


        databaseReference = FirebaseDatabase.getInstance().getReference();
        doseNumber=0.0;
        breakfastValid=0;
        dinnerValid=0;
        lunchValid=0;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.addItemDecoration((new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)));

        //Creating Spinner
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

                    medType="liquid";
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
                    breakfast.setBackground(getResources().getDrawable(R.drawable.btn_selected));
                }
                else{
                    breakfastValid=0;
                    breakfast.setBackground(getResources().getDrawable(R.drawable.unit));
                }

            }
        });
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lunchValid==0){
                    lunchValid=1;
                    lunch.setBackground(getResources().getDrawable(R.drawable.btn_selected));
                }
                else{
                    lunchValid=0;
                    lunch.setBackground(getResources().getDrawable(R.drawable.unit));
                }

            }
        });
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dinnerValid==0){
                    dinnerValid=1;
                    dinner.setBackground(getResources().getDrawable(R.drawable.btn_selected));
                }
                else{
                    dinnerValid=0;
                    dinner.setBackground(getResources().getDrawable(R.drawable.unit));
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
                else if (medType.equals("liquid"))
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
                    else if (medType.equals("liquid"))
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
                //

                checkValid();
                if (errors==0){
                    sendData();
                }
            }
        });

    }

    private void setAdapter(final String searchedString) {
        //Retrieving data in firebase
        databaseReference.child("user_medicien").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                med_list.clear();
                id_list.clear();
                recyclerView.removeAllViews();
                int counter = 0;

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //String med_num=snapshot.getKey();
                    String med_name=snapshot.child("title").getValue().toString();
                    String id_num=snapshot.child("ID").getValue().toString();
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

        databaseReference.child("user_medicien").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String med_name=snapshot.child("title").getValue().toString();
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



        //Validating if all other fields have been filled
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            errorMsg+="Please select before or after food";
            errors=1;
        }
        if(breakfastValid==0 && lunchValid==0 && dinnerValid==0){
            errorMsg+="\n Please select the meal for medicine intake";
            errors=1;

        }
        if (doseNumber==0.0){
            errorMsg+="\n Please select the dosage";
            errors=1;

        }



        Toast toast = Toast.makeText(getApplicationContext(),
                errorMsg,
                Toast.LENGTH_LONG);

        toast.show();



    }
    public void sendData(){
        //Adding data to firebase
        DatabaseReference tempRef=databaseReference.child("med_list");
        tempRef.child(medKey).setValue("");

        if (breakfastValid==1){
            tempRef.child("Breakfast").setValue("True");

        }if (dinnerValid==1){
            tempRef.child("Lunch").setValue("True");

        }if (dinnerValid==1){
            tempRef.child("Dinner").setValue("True");

        }
        if(before.isChecked())
        {
            tempRef.child("Food").setValue("Before");

        }
        else if (after.isChecked())
        {
            tempRef.child("Food").setValue("After");

        }
        tempRef.child("MedicineName").setValue(medName);
        tempRef.child("Dosage").setValue(doseNumber);
        tempRef.child("MedType").setValue(medType);
        tempRef.child("Breakfast").setValue("False");
        tempRef.child("Lunch").setValue("False");
        tempRef.child("Dinner").setValue("False");
        Modle med = new Modle();
        tempRef.child(medKey).child(medName).setValue(med);
    }
}
