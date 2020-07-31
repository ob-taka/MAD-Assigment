package com.health.anytime;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignIn extends AppCompatActivity {
//Declaration of attributes
    private EditText mEmail, mPassword;
    private TextView forgetPw;
    private AppCompatButton mSignUpBtn;
    private Button mLoginBtn, mGoogleSignInBtn;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorage;
    private String email, uid, role;
    private GoogleSignInClient googleSignInClient;
    private int CONSTANT = 0;
    private Intent patHomeActivity;
    private Intent docHomeActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
//Instantiation of attributes
        mEmail = findViewById(R.id.editText_email);
        mPassword = findViewById(R.id.editText_password);
        mLoginBtn = findViewById(R.id.btn_signIn);
        mProgressBar = findViewById(R.id.progressBar_signIn);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mGoogleSignInBtn = findViewById(R.id.btn_gsi);
        forgetPw = findViewById(R.id.textView_forgotPass);
        mSignUpBtn = findViewById(R.id.ACB_loginSUBtn);
        //Configuring google sign in (GSO) and GSO object
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        patHomeActivity = new Intent(SignIn.this, User_home.class);
        docHomeActivity = new Intent(SignIn.this,DoctorHome.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

//Logs existing user in without having to key in necessary details
        FirebaseUser mUser = mAuth.getCurrentUser();
        if(mUser != null){
            onAuthSuccess(mUser);
        }

//Event listener on Sign In button, validates fields and signs the user in
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString().trim();

                String pw = mPassword.getText().toString().trim();
                boolean validate = true;
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required!");
                    validate = false;
                }
                if (TextUtils.isEmpty(pw)) {
                    mPassword.setError("Password is required!");
                    validate = false;
                }
                mProgressBar.setVisibility(View.VISIBLE);

                if(validate) {
                    mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Calls function to redirect user to necessary activities
                                onAuthSuccess(task.getResult().getUser());
                            } else {
                                Toast.makeText(SignIn.this, "Login Unsuccessful, " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignIn.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        forgetPw.setPaintFlags(forgetPw.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        forgetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, ForgetPassword.class));

            }
        });
//Opens the google sign in activity created by google
        mGoogleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, CONSTANT);
            }
        });

//Redirects user to sign up page if they need to sign up
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });
    }

//This method checks for user's role and direct them to their respective pages.
//Progressbar visibility set to "Off" so that it can start displaying message and move on to user home activity.
    private void onAuthSuccess(FirebaseUser user) {
        if (user != null) {
            uid = user.getUid();
            Log.d("#d",uid);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    role = dataSnapshot.child(uid).child("role").getValue().toString();
                    Log.d("#d",role);
                    switch (role){
                        case ("Doctor"):
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(SignIn.this, "Succesfully signed in as Doctor",Toast.LENGTH_LONG).show();
                            startActivity(docHomeActivity);
                            overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
                            break;
                        case ("Patient"):
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(SignIn.this, "Succesfully signed in as Patient",Toast.LENGTH_LONG).show();
                            startActivity(patHomeActivity);
                            overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
                            break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SignIn.this, "Error" + databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    //This gets a result from the google sign in activity
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONSTANT) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

//This method handles the sign in by inserting details of the user's google account into firebase database
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    insertGSI_Details(account);
                    startActivity(patHomeActivity);
                }
            });

        }
        catch (ApiException e) {
            //This will log the error that is returned from the api exception
            Log.d("#Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(SignIn.this, "Google sign in failed. Try again later.", Toast.LENGTH_LONG).show();
        }
    }

//This method gets the details of the user from his google sign in and updates the database and storage in firebase
    private void insertGSI_Details(GoogleSignInAccount account){
        String medid = "7d55d1c0-d";
        String role = "Patient";
        email = account.getEmail();
        //Sets information of the user which in this case is default to Patient as this log in is for patients only
        DatabaseReference dr = mDatabase.getReference("User");
        String ukey = mAuth.getCurrentUser().getUid();
        PatientModel user = new PatientModel("default.jpg", account.getDisplayName(), email, false, role, medid);
        dr.child(ukey).setValue(user);
    }
}

