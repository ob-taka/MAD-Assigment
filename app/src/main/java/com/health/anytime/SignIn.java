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

import com.bumptech.glide.Glide;
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
    private EditText mEmail, mPassword;
    private Button mLoginBtn, mGoogleSignInBtn;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorage;
    private String email, uid, role , pic;
    private GoogleSignInClient googleSignInClient;
    private int CONSTANT = 0;
    private Boolean STATE = false;
    private Intent patHomeActivity;
    private Intent docHomeActivity;
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
        patHomeActivity = new Intent(SignIn.this, User_home.class);
        docHomeActivity = new Intent(SignIn.this,DoctorHome.class);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mUser = mAuth.getCurrentUser();
        if(mUser != null){
            onAuthSuccess(mUser);
        }

        //Event listener on Sign In button, validation along the way.
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString().trim();
                //saveData();
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
                if(validate) {
                    mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() && mAuth.getCurrentUser().isEmailVerified()) {
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


        mGoogleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, CONSTANT);
            }
        });
    }

    //This method checks for user's role and direct them to their respective pages.
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
                            //Progressbar visibility set to "Off" so that it can start displaying message and move on to user home activity.
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
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    insertGSI_Details(account);
                    //saveData();
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
        String name = account.getDisplayName();
        //Uri pic = account.getPhotoUrl();
        //String fileName = uploadPhoto(pic);

        //Sets information of the user which in this case is default to Patient as this log in is for patients only
        DatabaseReference dr = mDatabase.getReference("User");
        String ukey = mAuth.getCurrentUser().getUid();
        PatientModel user = new PatientModel("default.jpg", name, email, status, role, medid);
        dr.child(ukey).setValue(user);

        //use googleSignInClient.signOut(); when signing out
    }
/*
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


    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid",uid);
        editor.commit();
    }

 */
}

