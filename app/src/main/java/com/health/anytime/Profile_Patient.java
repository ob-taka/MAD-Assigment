package com.health.anytime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Profile_Patient extends AppCompatActivity {

    TextView Name, Email, Manage;
    ImageView ProfPic;
    ImageButton settings;
    DatabaseReference databaseReference;
    String patientProfilepic;
    // views for button
    String userChoosenTask,uid;
    FirebaseUser user;
    public static Activity fa;

    // view for image view

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 1;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile);
        storage = FirebaseStorage.getInstance();
        fa=this;
        settings=findViewById(R.id.settings);
        user = FirebaseAuth.getInstance().getCurrentUser();
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        storageReference = storage.getReference();
        Name = findViewById(R.id.patient_Name);
        Email = findViewById(R.id.patient_Email);
        ProfPic = findViewById(R.id.patient_Pp);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("644403792200-g475m0097k2o5mrjq0tau3rlau2fftdq.apps.googleusercontent.com")
                .requestEmail()
                .build();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        uid = getIntent().getStringExtra("UID");
        getDetails(uid);
        ProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectImage();



            }
        });


            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(account==null) {
                    Intent intent = new Intent(getApplicationContext(), Settings.class);
                    intent.putExtra("UID", uid);

                    startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), SettingsGoogleSignIn.class);

                        startActivity(intent);                    }
                }


            });

    }


            //This method retrieves the name, email, prof pic url from firebase database. it also sets the profile
            //pic through the fetchPatientPic() method
            private void getDetails(String uid) {
                databaseReference.child("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String patientName = dataSnapshot.child("patientName").getValue().toString();
                        Name.setText(patientName);
                        String patientEmail = dataSnapshot.child("patientEmail").getValue().toString();
                        Email.setText(patientEmail);
                        patientProfilepic = dataSnapshot.child("patientProfilepic").getValue().toString();
                        fetchPatientPic();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Profile_Patient.this, "error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             * fetch image view from firebase Storage (file hosting service)
             * References FirebaseStorage profile pic folder and uses glide to load the image into profpic imageview.
             */
            private void fetchPatientPic() {
                picLocation();
                // finds image and download image from firebase storage by image path and binds it to view holder
                FirebaseStorage storage = FirebaseStorage.getInstance(
                        "gs://quickmad-e4016.appspot.com/"
                );
                StorageReference storageRef = storage
                        .getReference()
                        .child("ProfilePicture/" + patientProfilepic);
                storageRef
                        .getDownloadUrl()
                        .addOnSuccessListener(
                                new OnSuccessListener<Uri>() {

                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(Profile_Patient.this).load(uri).into(ProfPic); // uses Gilde , a framework to load and download files in android
                                    }
                                }
                        );
            }
            private void picLocation(){
                databaseReference.child("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        patientProfilepic = dataSnapshot.child("patientProfilepic").getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Profile_Patient.this, "error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }

            // Select Image method
            private void SelectImage() {

                final CharSequence[] items;
                Log.v("Pic name:",patientProfilepic);
                if(patientProfilepic.equals("default.jpg")){
                 items= new CharSequence[]{"Take Photo", "Choose from Library",
                         "Cancel"};
                }
                else {
                    items= new CharSequence[]{"Take Photo", "Choose from Library","Remove Profile Picture",
                            "Cancel"};
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Patient.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        boolean result = Utility.checkPermission(Profile_Patient.this);
                        if (items[item].equals("Take Photo")) {
                            userChoosenTask = "Take Photo";
                            if (result)
                                cameraIntent();
                        } else if (items[item].equals("Choose from Library")) {
                            userChoosenTask = "Choose from Library";
                            if (result) {
                                // Defining Implicit Intent to mobile gallery
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(
                                        Intent.createChooser(
                                                intent,
                                                "Select Image from here..."),
                                        PICK_IMAGE_REQUEST);
                            }


                        } else if (items[item].equals("Cancel")) {

                            dialog.dismiss();
                        }
                        else if(items[item].equals("Remove Profile Picture")){
                            removeprofpic();
                            picLocation();
                            fetchPatientPic();

                        }
                    }
                });
                builder.show();

            }
            public void removeprofpic(){
                databaseReference.child("User").child(uid).child("patientProfilepic").setValue("default.jpg");
                Toast.makeText(Profile_Patient.this,"Profile picture removed",Toast.LENGTH_LONG).show();



                FirebaseStorage storage = FirebaseStorage.getInstance(
                        "gs://quickmad-e4016.appspot.com/"
                );
                StorageReference storageRef = storage
                        .getReference()
                        .child("ProfilePicture/" + user.getEmail().toString());
                storageRef
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        fetchPatientPic();
                        Log.e("firebasestorage", "onSuccess: deleted file");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred! 
                        Log.e("firebasestorage", "onFailure: did not delete file");
                    }
                });
            }

            public void cameraIntent() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                if (requestCode == Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (userChoosenTask.equals("Take Photo"))
                            cameraIntent();
                        else if (userChoosenTask.equals("Choose from Library")) {
                            // Defining Implicit Intent to mobile gallery
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(
                                    Intent.createChooser(
                                            intent,
                                            "Select Image from here..."),
                                    PICK_IMAGE_REQUEST);
                        }
                    } else {
                        Toast.makeText(Profile_Patient.this, "Permission to access files has been denied.", Toast.LENGTH_SHORT).show();


                    }
                }
            }

            // Override onActivityResult method
            @Override
            protected void onActivityResult(int requestCode,
                                            int resultCode,
                                            Intent data) {

                super.onActivityResult(requestCode,
                        resultCode,
                        data);

                // checking request code and result code
                // if request code is PICK_IMAGE_REQUEST and
                // resultCode is RESULT_OK
                // then set image in the image view
                if (requestCode == PICK_IMAGE_REQUEST
                        && resultCode == RESULT_OK
                        && data != null
                        && data.getData() != null) {

                    // Get the Uri of data
                    filePath = data.getData();
                    try {

                        // Setting image on image view using Bitmap
                        Bitmap bitmap = MediaStore
                                .Images
                                .Media
                                .getBitmap(
                                        getContentResolver(),
                                        filePath);
                        ProfPic.setImageBitmap(bitmap);

                        new AlertDialog.Builder(Profile_Patient.this)
                                .setTitle("Would you like to save this profile picture?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseReference.child("User").child(uid).child("patientProfilepic").setValue(user.getEmail().toString());

                                        uploadImage();

                                    }
                                })

                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        fetchPatientPic();

                                    }
                                })
                                .setIcon(android.R.drawable.ic_menu_save)
                                .show();

                    } catch (IOException e) {
                        // Log the exception
                        e.printStackTrace();
                    }

                } else if (resultCode == Activity.RESULT_OK) {
                    if (requestCode == 0)
                        onCaptureImageResult(data);
                }
            }

            public void onCaptureImageResult(Intent data) {
                final Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ProfPic.setImageBitmap(thumbnail);
                new AlertDialog.Builder(Profile_Patient.this)
                        .setTitle("Would you like to save this profile picture?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child("User").child(uid).child("patientProfilepic").setValue(user.getEmail().toString());

                                uploadCameraImage(thumbnail);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fetchPatientPic();

                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_save)
                        .show();

            }

            public void uploadCameraImage(Bitmap bitmap) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                StorageReference imagesRef = storageReference.child("ProfilePicture/" + user.getEmail().toString());
                // Code for showing progressDialog while uploading
                final ProgressDialog progressDialog
                        = new ProgressDialog(Profile_Patient.this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                imagesRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        // Image uploaded successfully
                        // Dismiss dialog
                        picLocation();

                        progressDialog.dismiss();
                        Toast.makeText(Profile_Patient.this, "Image Saved!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast.makeText(Profile_Patient.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Saved " + (int) progress + "%");
                            }
                        });

            }

            // UploadImage method
            public void uploadImage() {
                if (filePath != null) {

                    // Code for showing progressDialog while uploading
                    final ProgressDialog progressDialog
                            = new ProgressDialog(Profile_Patient.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    // Defining the child of storageReference
                    StorageReference ref = storageReference.child("ProfilePicture/" + user.getEmail().toString());

                    // adding listeners on upload
                    // or failure of image
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Image uploaded successfully
                            // Dismiss dialog
                            progressDialog.dismiss();
                            picLocation();

                            Toast.makeText(Profile_Patient.this, "Image Saved!!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(Profile_Patient.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Saved " + (int) progress + "%");
                        }

                    });


                }
            }




    }