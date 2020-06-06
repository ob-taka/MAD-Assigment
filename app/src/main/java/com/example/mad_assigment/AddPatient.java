package com.example.mad_assigment;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cdflynn.android.library.checkview.CheckView;

public class AddPatient extends AppCompatActivity{

    EditText name;
    EditText email;
    Button create;
    String key;
    CheckView success;
    HashMap<String,PatientModel> patientList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        // set illustration src to image view (found in drawable)
        ImageView iv = (ImageView)findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.addpatient);

        //init list and database
        Intent intent = getIntent();
        patientList = (HashMap<String, PatientModel>) intent.getSerializableExtra("keys");

        // submit button to push patient medicine list to firebase
        create = (Button) findViewById(R.id.button7);
        name = findViewById(R.id.name_edit_text);
        email = findViewById(R.id.email_edit_text);
        success = findViewById(R.id.check);
        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // add patient to doctor list
                // checks if patient email match with the user input from edittext
                if (checkPatient()){
                    final Intent nextActivity = new Intent(AddPatient.this  , MedicineList.class );
                    nextActivity.putExtra("pname",name.getText().toString().trim());
                    nextActivity.putExtra("pemail",email.getText().toString().trim());
                    nextActivity.putExtra("patientKey" , key);

                    success.check();// check animation

                    // delay moving to medicineList
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            clearText();
                            startActivity(nextActivity);
                            overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
                        }
                    }, 1000);

                }else {
                    buildDialog();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        clearText();
        success.uncheck(); // uncheck animation
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right); //animation
    }

    /**
     * Used to check if patient user enter exist in firebase
     * Takes in input from user using edittext
     * @return true if patient email match user input else false
     *
     */
    private Boolean checkPatient(){
        for (Map.Entry patientEntry : patientList.entrySet()){
            key = (String) patientEntry.getKey();
            PatientModel patient = (PatientModel) patientEntry.getValue();
            if(patient.getPatientEmail().equals(email.getText().toString().trim())){
                return true;
            }
        }
        return false;
    }

    /**
     * Used to alert user that patient entered does not exist
     */
    private void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPatient.this);
        builder.setTitle("Patient Email not found!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();

    }

    /**
     * Used to clear text in edit text
     */
    private void clearText(){
        // reset edittext
        name.setText("");
        email.setText("");
    }
}
