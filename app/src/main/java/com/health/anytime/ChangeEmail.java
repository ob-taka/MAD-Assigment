package com.health.anytime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ChangeEmail extends AppCompatActivity {
    EditText email,password,emailcfm;
    ProgressBar progressBar;
    Button change;
    String uid,oldemail;
    StorageReference storageReference;
    FirebaseStorage storage;
    DatabaseReference databaseReference;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeemail);
        email=findViewById(R.id.email);
        emailcfm=findViewById(R.id.newemailcfm);
        password=findViewById(R.id.pw);
        progressBar=findViewById(R.id.progressBar);
        change=findViewById(R.id.change);
        uid = getIntent().getStringExtra("UID");
        storage = FirebaseStorage.getInstance();
        final SharedPreferences.Editor editor = getSharedPreferences("Lock", MODE_PRIVATE).edit();

        storageReference = storage.getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if(validate()){
                        progressBar.setVisibility(View.VISIBLE);
                        final FirebaseAuth mAuth;
                        mAuth= FirebaseAuth.getInstance();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        oldemail=user.getEmail();


                        AuthCredential credential = EmailAuthProvider

                                .getCredential(oldemail,password.getText().toString() );
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {

                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            user.updateEmail(email.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {


                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressBar.setVisibility(View.GONE);
                                                                databaseReference.child("User").child(uid).child("patientEmail").setValue(email.getText().toString());

                                                                databaseReference.child("User").child(uid).child("patientProfilepic").setValue("default.jpg");
                                                                StorageReference storageRef = storage
                                                                        .getReference()
                                                                        .child("ProfilePicture/" + oldemail);
                                                                storageRef
                                                                        .delete()
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.e("firebasestorage", "onSuccess: deleted file");
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception exception) {
                                                                        // Uh-oh, an error occurred!
                                                                        Log.e("firebasestorage", "onFailure: did not delete file");
                                                                    }
                                                                });





                                                                mAuth.getCurrentUser().sendEmailVerification();
                                                                mAuth.signOut();
                                                                Toast.makeText(ChangeEmail.this, "Email Address has been changed", Toast.LENGTH_LONG).show();
                                                                editor.putString("Code",null);
                                                                editor.apply();
                                                                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                                startActivity(intent);

                                                            }
                                                            else{
                                                                Toast.makeText(ChangeEmail.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                progressBar.setVisibility(View.GONE);

                                                            }
                                                        }
                                                    });
                                        }
                                        else{
                                            progressBar.setVisibility(View.GONE);

                                            Toast.makeText(ChangeEmail.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                    }


            }
        });



    }

    public boolean validate(){
        boolean validate=true;
        if(password.getText().toString().equals("")){
            password.setError("Please enter your password");
            validate=false;

        }
        if(email.getText().toString().equals("")){
            email.setError("Please enter your email address");
            validate=false;

        }
        if(emailcfm.getText().toString().equals("")){
            emailcfm.setError("Please renter your email address");
            validate=false;

        }
        if (emailcfm.getText().toString().equals("")&&email.getText().toString().equals("")&&password.getText().toString().equals("")){
            validate=false;




        }
        if(!emailcfm.getText().toString().equals(email.getText().toString())) {

            emailcfm.setError("Email does not match");
            validate=false;

        }
        return validate;

    }
}
