package com.health.anytime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteAccount extends AppCompatActivity {
    EditText pw;
    Button cfm;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    FirebaseFirestore db;
    String email;


    String medid,uid,uid2;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleteaccount);
        mAuth=FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        final Handler handler = new Handler();

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //uid = getIntent().getStringExtra("UID");
        email=user.getEmail();

        Query query = databaseReference.child("User").orderByChild("patientEmail").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    uid2 = childSnapshot.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        pw=findViewById(R.id.password);
        final SharedPreferences.Editor editor = getSharedPreferences("Lock", MODE_PRIVATE).edit();


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
                            .getCredential(email, pw.getText().toString());
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
                                                            User_home.fa.finish();
                                                            Settings.fa.finish();
                                                            Profile_Patient.fa.finish();
                                                            //delete the contact
                                                            deleteContact();

                                                            StorageReference storageRef = storage
                                                                    .getReference()
                                                                    .child("ProfilePicture/" + email);
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
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    final Intent intent=new Intent(DeleteAccount.this,SignIn.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    databaseReference.child("User").child(uid2).removeValue();
                                                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                editor.putString("Code",null);
                                                                                editor.apply();
                                                                                Toast.makeText(DeleteAccount.this, "Account has been deleted", Toast.LENGTH_LONG).show();
                                                                                startActivity(intent);

                                                                            }
                                                                            else {
                                                                                progressBar.setVisibility(View.GONE);
                                                                                Toast.makeText(DeleteAccount.this, task.getException().getMessage(), Toast.LENGTH_LONG).show(); }
                                                                        }});
                                                                }
                                                            }, 1000);

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

    //This will delete the contact from database
    public void deleteContact(){
        databaseReference.child("Contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).exists()){
                    snapshot.getRef().child(user.getUid()).removeValue();
                }
                for(DataSnapshot sc: snapshot.getChildren()){
                    if(sc.getValue().toString()==user.getUid()){
                        sc.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
