package com.example.mad_assigment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    ProgressBar mProgressBar;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    String email, uid, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        mEmail = findViewById(R.id.editText_email);
        mPassword = findViewById(R.id.editText_password);
        mLoginBtn = findViewById(R.id.btn_signIn);
        mProgressBar = findViewById(R.id.progressBar_signIn);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Event listener on Sign In button, validation along the way.
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString().trim();
                String pw = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required!");
                }
                if (TextUtils.isEmpty(pw)) {
                    mEmail.setError("Password is required!");
                }
                mProgressBar.setVisibility(View.VISIBLE);

                //Authenticate User
                mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser(),email);
                        }
                        else {
                            Toast.makeText(SignIn.this, "Login Unsuccessful, " + task.getException(), Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
    //add validation to keep user signed in.

    private void onAuthSuccess(FirebaseUser user, final String email) {
        final String fEmail = email
                .replace("@","_")
                .replace(".","_");

        if (user != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Role");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    role = dataSnapshot.child(fEmail).getValue().toString();
                    Log.d("#d",role);
                    returnKey();

                    if(role.equals("Doctor")){
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(SignIn.this, "Succesfully signed in as Doctor",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignIn.this,User_home.class);
                        intent.putExtra("Uid",uid)
                                .putExtra("Role","Doctor");
                        startActivity(intent);
                    }
                    else if (role.equals("Patient")){
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(SignIn.this, "Succesfully signed in as Patient",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignIn.this,User_home.class);
                        intent.putExtra("Uid",uid)
                                .putExtra("Role","Patient");
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SignIn.this, "Error" + databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //Method to find email in Child "Users" of database and return the key
    private void returnKey(){
        ref = FirebaseDatabase.getInstance().getReference().child("User");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if(ds.child("patientEmail").getValue().toString().equals(email)){
                        uid = ds.getKey();
                    }
                    //need to add statement to check for doctor uid
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignIn.this, "Error" + databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}

