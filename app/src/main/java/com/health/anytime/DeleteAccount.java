package com.health.anytime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class DeleteAccount extends AppCompatActivity {
    EditText pw;
    Button cfm;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleteaccount);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        pw=findViewById(R.id.password);
        mAuth= FirebaseAuth.getInstance();

        cfm=findViewById(R.id.confirmtxt);
        progressBar=findViewById(R.id.progressBar);
        cfm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pw.getText().toString().equals("")){
                    pw.setError("Please enter a password");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), pw.getText().toString());

                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {

                                        LayoutInflater li = LayoutInflater.from(getApplicationContext());
                                        View promptsView = li.inflate(R.layout.deletebox, null);

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                DeleteAccount.this);

                                        // set alert_dialog.xml to alertdialog builder
                                        alertDialogBuilder.setView(promptsView);

                                        final EditText userInput = promptsView.findViewById(R.id.cfmtxt);

                                        // set dialog message
                                        alertDialogBuilder
                                                .setCancelable(false)
                                                .setPositiveButton("Delete account", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        if (userInput.getText().toString().equals("confirm")){
                                                            user.delete()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {



                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(DeleteAccount.this, "Account has been deleted", Toast.LENGTH_LONG).show();
                                                                                Intent intent=new Intent(DeleteAccount.this,SignIn.class);
                                                                                startActivity(intent);

                                                                            } else {
                                                                                progressBar.setVisibility(View.GONE);
                                                                                Toast.makeText(DeleteAccount.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                                                            }
                                                                        }
                                                                    });


                                                        }
                                                        else{
                                                            progressBar.setVisibility(View.GONE);

                                                            Toast.makeText(DeleteAccount.this,"Incorrect input", Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                progressBar.setVisibility(View.GONE);

                                                            }
                                                        });

                                        // create alert dialog

                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                        // show it
                                        alertDialog.show();



                                    }
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(DeleteAccount.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    }

                                }
                            });

                }
            }
        });

    }

}
