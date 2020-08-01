package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class MedicineDetails extends AppCompatActivity{
    TextView medicinetitle;
    TextView medicinedetails;
    TextView medicinequantity;
    ImageView medicineimg;
    String userId;
    String title;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onStart() {
        super.onStart();
        fetchMedicineData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);

        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();//get doctor id from firebase
        medicinetitle = findViewById(R.id.medTitle);
        medicinequantity = findViewById(R.id.medQty);
        medicinedetails = findViewById(R.id.medDesc);
        medicineimg = findViewById(R.id.medImage);

        fetchMedicinePic(getIntent().getExtras().getString("medicimg"));
        title = getIntent().getExtras().getString("medname");
        medicinetitle.setText(title);

        //onclicklistener for FloatingActionButton
        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton4);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create fragment
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RefillMedicine fragment = new RefillMedicine(getApplicationContext() , title );
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void fetchMedicinePic(String url) {
        // finds image and download image from firebase storage by image path and binds it to view holder
        FirebaseStorage storage = FirebaseStorage.getInstance(
                "gs://quickmad-e4016.appspot.com/"
        );
        StorageReference storageRef = storage
                .getReference()
                .child(url);
        storageRef
                .getDownloadUrl()
                .addOnSuccessListener(
                        new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(MedicineDetails.this).load(uri).into(medicineimg); // uses Gilde , a framework to load and download files in android
                            }
                        }
                );
    }

    private void fetchMedicineData(){
        // fetch selected medicine details and quantity from firebase
        databaseReference.child("Pharmacy").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicinedetails.setText(dataSnapshot.child(title).child("medicineDsec").getValue().toString());
                medicinequantity.setText("Quantity : " + dataSnapshot.child(title).child("quantity").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}