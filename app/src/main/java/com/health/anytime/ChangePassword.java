package com.health.anytime;

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

public class ChangePassword extends AppCompatActivity {
    EditText oldpw,newpw,newpwcfm;
    Button confirm;
    ProgressBar progressBar;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        oldpw=findViewById(R.id.oldpw);
        newpw=findViewById(R.id.newpw);
        newpwcfm=findViewById(R.id.newpwcfm);
        confirm=findViewById(R.id.confirm);
        progressBar=findViewById(R.id.progressBar);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if(oldpw.getText().toString().equals("")){
                    oldpw.setError("Please enter your current password");

                }
                if(newpw.getText().toString().equals("")){
                    newpw.setError("Please enter your new password");
                }
                if(newpwcfm.getText().toString().equals("")){
                    newpwcfm.setError("Please renter your new password");
                }
                if (!oldpw.getText().toString().equals("")&&!newpw.getText().toString().equals("")&&!newpwcfm.getText().toString().equals(""))
                {

                    if(newpwcfm.getText().toString().equals(newpw.getText().toString())){

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), oldpw.getText().toString());

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(newpw.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        progressBar.setVisibility(View.GONE);
                                                        oldpw.setText("");
                                                        newpw.setText("");
                                                        newpwcfm.setText("");
                                                        Toast.makeText(ChangePassword.this, "Password has been changed", Toast.LENGTH_LONG).show();


                                                    }
                                                    else {
                                                        progressBar.setVisibility(View.GONE);
                                                        Toast.makeText(ChangePassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });
                                        }
                                        else {

                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(ChangePassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();


                                        }
                                    }
                                });


                    }
                    else{
                        newpwcfm.setError("Password does not match");
                    }
                }
            }
        });

    }
}
