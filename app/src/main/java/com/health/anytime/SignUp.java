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
    EditText rName, rEmail, rPass, rCfmPass;
    Button rRegisterBtn;
    TextView rLoginBtn;
    ProgressBar rProgressBar;
    FirebaseAuth rAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

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

        //this checks whether user is already logged in
        /*
        if(rAuth.getCurrentUser() != null){
            startActivity(intent);
            finish();
        }
*/
        rRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = rName.getText().toString().trim();
                final String email = rEmail.getText().toString().trim();
                final String pass = rPass.getText().toString().trim();
                final String cfmPass = rCfmPass.getText().toString().trim();
                boolean validate = true;

                //the following checks whether fields are empty, if so, display an error warning
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

                //the following checks for matching passwords and length
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
                //the following code registers the user using the entered email and password
                if(validate==true && !isEmailExist(email)){
                    rAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
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

    //this method formats the email for database insertion
    private String emailFormat(String sEmail){
        //Replacing '@' & '.' to '_' as firebase key does not allow special characters
        String Email = sEmail
                .replace("@","_")
                .replace(".","_");
        return(Email);
    }

    //this method generates and inserts the database details into firebase database
    private void insertDetails(String name, String email){
        String Email = emailFormat(email);
        String Role = "Patient";
        String Pic = "default.jpg";
        String Med = "7d55d1c0-d";
        boolean Status = false;

        //this sets the role of the email/user which in this case is default to Patient as this log in is for patients only
        DatabaseReference dr = firebaseDatabase.getReference("Role").child(Email);
        dr.setValue(Role);

        //this creates a unique generated key in "User" and store the patient's details
        DatabaseReference du = firebaseDatabase.getReference("User");
        String ukey = du.push().getKey();
        PatientModel user = new PatientModel(Pic, name, email, Status, Role, Med);
        du.child(ukey).setValue(user);

    }

    //this method accesses the firebase authentication to check if an email already exists
    private Boolean isEmailExist(String email){
        boolean results = rAuth.fetchSignInMethodsForEmail(email).isSuccessful();
        return results;
    }


}