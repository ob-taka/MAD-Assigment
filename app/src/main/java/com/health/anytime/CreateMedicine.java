package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;


public class CreateMedicine extends AppCompatActivity{
    EditText medicineTitle;
    EditText medicineQty;
    EditText medicineDetails;
    Button uploadButton;
    Button addButton;
    TextView fileName;
    private static final int PICK_IMAGE_REQUEST = 1;
    public Uri mImageUri;
    String title , desc;
    double qty;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    int numMed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_medicine);
        medicineTitle = findViewById(R.id.title_edit_text);
        medicineQty = findViewById(R.id.qty_edit_text);
        uploadButton = findViewById(R.id.button_choose_image);
        medicineDetails = findViewById(R.id.desc_edit_text);
        fileName = findViewById(R.id.file_name);
        uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        //setup onclicklistener on add button
        addButton = findViewById(R.id. add_button);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                countId();
                boolean upload = true;
                title = medicineTitle.getText().toString().trim();
                if (medicineQty.getText().toString().equals("")){
                    medicineQty.setError(getString(R.string.input_empty));
                    upload = false;
                }else {
                    medicineDetails.setError(null);
                    qty = Double.parseDouble(medicineQty.getText().toString());
                }
                desc = medicineDetails.getText().toString().trim();
                if (title == null) {
                    medicineTitle.setError(getString(R.string.input_empty));
                    upload = false;
                } else {
                    medicineTitle.setError(null);
                }
                if (desc == null) {
                    medicineDetails.setError(getString(R.string.input_empty));
                    upload = false;
                } else {
                    medicineDetails.setError(null);
                }
                if (mImageUri == null){
                    upload = false;
                    fileName.setText("Needs to Upload File");
                    fileName.setTextColor(Color.rgb(200,0,0));
                }
                if (upload) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            uploadFile(mImageUri , title);
                            MedicineModel medicine = new MedicineModel(numMed+1,title,"/Medicine/"+title+".jpg", desc,qty);
                            databaseReference.child("Pharmacy").child(userId).child(title).setValue(medicine);
                            Intent nextActivity = new Intent(CreateMedicine.this  , Pharmacy.class );
                            nextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(nextActivity);
                            overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
                        }
                    },300);
                }


            }
        });
    }

    //calculates the latest ID
    private void countId(){
        // fetch patient from firebase
        databaseReference.child("Pharmacy").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numMed = (int)dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            fileName.setText(mImageUri.toString());
        }
    }

    private void uploadFile(Uri file , String medname ){
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("Medicine/").child(medname+".jpg");
        mStorageRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
    }
}