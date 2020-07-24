package com.health.anytime;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    EditText mEmail, mPassword;
    Button mLoginBtn, mGoogleSignInBtn;
    ProgressBar mProgressBar;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    StorageReference mStorage;
    String email, uid, role , pic;
    GoogleSignInClient googleSignInClient;
    int CONSTANT = 0;
    Boolean STATE = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        mEmail = findViewById(R.id.editText_email);
        mPassword = findViewById(R.id.editText_password);
        mLoginBtn = findViewById(R.id.btn_signIn);
        mProgressBar = findViewById(R.id.progressBar_signIn);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mGoogleSignInBtn = findViewById(R.id.btn_gsi);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("644403792200-g475m0097k2o5mrjq0tau3rlau2fftdq.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        intent = new Intent(SignIn.this, User_home.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if user has been previously signed in using google sign in or normal sign in
        ///Will need to add code for normal sign in

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null || mAuth.getCurrentUser() != null) {
            if(mAuth.getCurrentUser() != null) {
                email = mAuth.getCurrentUser().getEmail();
            }
            else{
                email = account.getEmail();
            }
            Log.d("#D", "Passed this state " + email);
            ExampleThread thread = new ExampleThread();
            thread.start();
        }
        ExampleThread1 thread2 = new ExampleThread1();
        thread2.start();

        //Event listener on Sign In button, validation along the way.
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString().trim();
                returnKey();
                saveData();
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

                //Authenticate User
                if(validate && !isEmailExist(email)) {
                    mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                onAuthSuccess(task.getResult().getUser(), email);
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

        mGoogleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, CONSTANT);
            }
        });
    }
    //need to add validation to keep user signed in. (Stage 2)

    //This method checks for user's role and direct them to their respective pages.
    private void onAuthSuccess(FirebaseUser user, final String email) {
        //Replacing '@' & '.' to '_' as firebase key does not allow special characters
        final String fEmail = email
                .replace("@","_")
                .replace(".","_");
        //returnKey();
        //Log.d("#D",uid);
        if (user != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Role");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    role = dataSnapshot.child(fEmail).getValue().toString();
                    //role = Objects.requireNonNull(dataSnapshot.child(fEmail).getValue()).toString();
                    Log.d("#d",role);
                    if(role.equals("Doctor")){
                        //Progressbar visibility set to "Off" so that it can start displaying message and move on to user home activity.
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(SignIn.this, "Succesfully signed in as Doctor",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignIn.this,DoctorHome.class);
                        intent.putExtra("Uid",uid)
                                .putExtra("Role","Doctor").putExtra("pic" , pic);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
                    }
                    else if (role.equals("Patient")){
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(SignIn.this, "Succesfully signed in as Patient",Toast.LENGTH_LONG).show();
                        Log.d("#d",uid);
                        //Intent intent2prof = new Intent(SignIn.this,Profile_Patient.class);
                        //intent2prof.putExtra("Uid",uid);
                        Intent intent = new Intent(SignIn.this,User_home.class);
                        intent.putExtra("Uid",uid)
                                .putExtra("Role","Patient");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SignIn.this, "Error" + databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    class ExampleThread extends Thread{
        @Override
        public void run() {
            returnKey();
        }
    }
    class ExampleThread1 extends Thread{
        @Override
        public void run() {
            while(true){
                if(STATE==true){
                    startActivity(intent);

                    break;
                }
            }
            saveData();
        }
    }

    //This Method searches the email in Child "User" of database and return the key
    private void returnKey(){
        DatabaseReference ref = mDatabase.getReference("User");
         ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if(ds.child("patientEmail").getValue().toString().toLowerCase().equals(email)){
                        uid = ds.getKey();
                        STATE = true;
                        intent.putExtra("Uid", uid)
                                .putExtra("Role", "Patient");
                        Log.d("#D","got the uid" + uid + email);
                        //pic = ds.child("patientProfilepic").getValue().toString();
                        role = ds.child("role").getValue().toString();
                        Log.d("#d",role);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignIn.this, "Error" + databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
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
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            insertGSI_Details(account);
            returnKey();
            saveData();
            AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Intent intent = new Intent(SignIn.this, User_home.class);
                    intent.putExtra("Uid",uid)
                            .putExtra("Role","Patient");
                    startActivity(intent);
                }
            });

        }
        catch (ApiException e) {
            // This will log the error that is returned from the api exception
            Log.d("#Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(SignIn.this, "Google sign in failed. Try again later.", Toast.LENGTH_LONG).show();
        }
    }

    //This method gets the details of the user from his google sign in and updates the database and storage in firebase
    private void insertGSI_Details(GoogleSignInAccount account){
        String medid = "7d55d1c0-d";
        String role = "Patient";
        boolean status = false;
        email = account.getEmail();
        String dbEmail = emailFormat(email);
        String name = account.getDisplayName();
        Uri pic = account.getPhotoUrl();
        String fileName = uploadPhoto(pic);
        Log.d("#D", email + " " + dbEmail + " " + name + " " + fileName);
        //***IMPT WLL NEED TO CONSOLIDATE WITH SIGN UP
        //this sets the role of the email/user which in this case is default to Patient as this log in is for patients only
        DatabaseReference dr = mDatabase.getReference("Role").child(dbEmail);
        dr.setValue(role);
        Log.d("#D","Passed this stage");
        //this creates a unique generated key in "User" and store the patient's details
        DatabaseReference du = mDatabase.getReference("User");
        String ukey = du.push().getKey();
        Log.d("#D","Passed this stage");
        PatientModel user = new PatientModel(fileName, name, email, status, role, medid);
        du.child(ukey).setValue(user);
        Log.d("#D","Passed this stage");


        //use googleSignInClient.signOut(); when signing out
    }

    //This method simply gets the type of the file for e.g. .jpg or .png
    private String getFileType(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //This method uploads the google account's image into firebase storage.
    //It also returns the file name of the picture
    private String uploadPhoto(Uri pic){
        String picName = "default.jpg";
        if(pic != null) {
            picName = System.currentTimeMillis() + "." + getFileType(pic);
            StorageReference storageReference = mStorage.child("ProfilePicture/" + picName);
            storageReference.putFile(pic);
        }

        return picName;
    }

    //***IMPT WLL NEED TO CONSOLIDATE WITH SIGN UP
    //this method formats the email for database insertion
    private String emailFormat(String sEmail){
        //Replacing '@' & '.' to '_' as firebase key does not allow special characters
        String Email = sEmail
                .replace("@","_")
                .replace(".","_");
        return(Email);
    }

    //***IMPT WLL NEED TO CONSOLIDATE WITH SIGN UP
    //this method accesses the firebase authentication to check if an email already exists
    private Boolean isEmailExist(String email){
        boolean results = mAuth.fetchSignInMethodsForEmail(email).isSuccessful();
        return results;
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid",uid);
        editor.commit();
    }
}

