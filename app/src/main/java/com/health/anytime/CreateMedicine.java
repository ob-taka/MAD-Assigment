package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class CreateMedicine extends AppCompatActivity{
    EditText medicineTitle;
    EditText medicineQty;
    Button uploadButton;
    Button addButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    public Uri mImageUri;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    int numMed = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_medicine);
        medicineTitle = findViewById(R.id.title_edit_text);
        medicineQty = findViewById(R.id.qty_edit_text);
        uploadButton = findViewById(R.id.button_choose_image);
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
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        String title = medicineTitle.getText().toString().trim();
                        uploadFile(mImageUri , title);
                        int qty = Integer.parseInt(medicineQty.getText().toString());
                        MedicineModel medicine = new MedicineModel(numMed+1,title,"/Medicine/"+title+".jpg",qty);
                        databaseReference.child("Pharmacy").child(title).setValue(medicine);
                    }
                },300);



                //to add to user medicine

                Intent nextActivity = new Intent(CreateMedicine.this  , Pharmacy.class );
                startActivity(nextActivity);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
            }
        });
    }

    //count the total number of
    private void countId(){
        // fetch patient from firebase
        databaseReference.child("Pharmacy").addListenerForSingleValueEvent(new ValueEventListener() {
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
            Toast.makeText(this, mImageUri.toString(), Toast.LENGTH_SHORT ).show();
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
                        Toast.makeText(CreateMedicine.this, downloadUrl.toString(), Toast.LENGTH_SHORT ).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

}