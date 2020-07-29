package com.health.anytime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmail extends AppCompatActivity {
    EditText email,password,emailcfm;
    ProgressBar progressBar;
    Button change;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeemail);
        email=findViewById(R.id.email);
        emailcfm=findViewById(R.id.newemailcfm);
        password=findViewById(R.id.pw);
        progressBar=findViewById(R.id.progressBar);
        change=findViewById(R.id.change);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals("")){
                    password.setError("Please enter your password");
                }
                if(email.getText().toString().equals("")){
                    email.setError("Please enter your email address");
                }
                if(emailcfm.getText().toString().equals("")){
                    emailcfm.setError("Please renter your email address");
                }
                if (!emailcfm.getText().toString().equals("")&&!email.getText().toString().equals("")&&!password.getText().toString().equals("")){

                    if(emailcfm.getText().toString().equals(email.getText().toString())){
                        progressBar.setVisibility(View.VISIBLE);
                        final FirebaseAuth mAuth;
                        mAuth= FirebaseAuth.getInstance();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(),password.getText().toString() );
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
                                                                mAuth.getCurrentUser().sendEmailVerification();
                                                                Toast.makeText(ChangeEmail.this, "Email Address has been changed", Toast.LENGTH_LONG).show();
                                                                mAuth.signOut();

                                                                Intent intent = new Intent(getApplicationContext(), SignIn.class);
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
                    else{
                        emailcfm.setError("Email does not match");
                    }
                }
            }
        });



    }
}
