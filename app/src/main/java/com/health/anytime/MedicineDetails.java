package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);

        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        medicinedetails = findViewById(R.id.textView16);
        medicinetitle = findViewById(R.id.textView6);
        medicinequantity = findViewById(R.id.textView15);
        medicineimg = findViewById(R.id.imageView2);

        fetchMedicinePic(getIntent().getExtras().getString("medicimg"));
        title = getIntent().getExtras().getString("medname");
        medicinetitle.setText(title);
        fetchMedicineData();


        //onclicklistener for FloatingActionButton and edit text
        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton4);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        // fetch patient from firebase
        databaseReference.child("Pharmacy").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get all medicine from firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MedicineModel medicineModel = snapshot.getValue(MedicineModel.class);
                    if (medicineModel.getMedicineTitle().equals(title))
                        medicinedetails.setText(medicineModel.getMedicineDsec());
                        medicinequantity.setText("Quantity : " + medicineModel.getQuantity());
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}