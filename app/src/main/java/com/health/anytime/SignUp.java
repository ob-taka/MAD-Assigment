package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
//Declaration of attributes
    private EditText rName, rEmail, rPass, rCfmPass;
    private Button rRegisterBtn;
    private TextView rLoginBtn;
    private ProgressBar rProgressBar;
    private FirebaseAuth rAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
//Instantiation of attributes
        rName = findViewById(R.id.editText_regName);
        rEmail = findViewById(R.id.editText_regEmail);
        rPass = findViewById(R.id.editText_regPass);
        rCfmPass = findViewById(R.id.editText_regCfmPass);
        rRegisterBtn = findViewById(R.id.btn_signUp);
        rLoginBtn = findViewById(R.id.textView_regLoginBtn);
        rProgressBar = findViewById(R.id.progressBar_signUp);
        rAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        final Intent intent = new Intent(SignUp.this, SignIn.class);

//The following gets the details from the necessary fields and validates it
        rRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = rName.getText().toString().trim();
                final String email = rEmail.getText().toString().trim();
                final String pass = rPass.getText().toString().trim();
                final String cfmPass = rCfmPass.getText().toString().trim();
                boolean validate = true;

//The following checks whether fields are empty, if so, display an error warning
                if(TextUtils.isEmpty(name)){
                    rName.setError("Please enter your name as in IC.");
                    validate = false;
                }
                if(TextUtils.isEmpty(email)){
                    rEmail.setError("Please enter your email address.");
                    validate = false;
                }
                if(TextUtils.isEmpty(pass)){
                    rPass.setError("Please enter a password.");
                    validate = false;
                }
                if(TextUtils.isEmpty(cfmPass)){
                    rCfmPass.setError("Please enter a password.");
                    validate = false;
                }

//The following checks for matching passwords and length
                if(pass.length()<6 || cfmPass.length()<6){
                    rPass.setError("Please enter a password of at least 6 characters.");
                    rCfmPass.setError("Please enter matching passwords.");
                    validate = false;
                }

                if(!pass.equals(cfmPass)){
                    rPass.setError("Please enter matching passwords.");
                    rCfmPass.setError("Please enter matching passwords.");
                    Toast.makeText(SignUp.this,"Passwords do not match!",Toast.LENGTH_LONG).show();
                    validate = false;
                }

                rProgressBar.setVisibility(View.VISIBLE);
//The following code registers the user using the entered email and password
                if(validate){
                    rAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                rAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SignUp.this,"User has been created successfully! Verification email has been sent. Please verify before logging in. Email might be in junk mail.",Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            task.isCanceled();
                                        }
                                    }
                                });
                                Toast.makeText(SignUp.this,"User has been created successfully!",Toast.LENGTH_LONG).show();
                                insertDetails(name,email);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(SignUp.this,"Registration unsuccessful! " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                rProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignUp.this,"Registration unsuccessful! ",Toast.LENGTH_LONG).show();
                    rProgressBar.setVisibility(View.GONE);
                }

            }
        });

        rLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

//This method generates and inserts the database details into firebase database
    private void insertDetails(String name, String email){
        String Role = "Patient";
        String Pic = "default.jpg";
        String Med = "7d55d1c0-d";
        //Create a unique generated key in "User" and store the patient's details
        DatabaseReference du = firebaseDatabase.getReference("User");
        String ukey = rAuth.getCurrentUser().getUid();
        PatientModel user = new PatientModel(Pic, name, email, false, Role, Med);
        du.child(ukey).setValue(user);
    }

}