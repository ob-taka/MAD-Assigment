package com.health.anytime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MedicineDetails extends AppCompatActivity{
    TextView medicinetitle;
    TextView medicinedetails;
    ImageView medicineimg;
    String medicine;
    int qty;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);

        medicinetitle = findViewById(R.id.textView5);
        medicinedetails = findViewById(R.id.textView6);
        medicineimg = findViewById(R.id.imageView2);

        fetchMedicinePic(getIntent().getExtras().getString("medicimg"));
        medicine = getIntent().getExtras().getString("medicname");
        qty = getIntent().getExtras().getInt("medicqty");
        medicinetitle.setText(medicine);
        medicinedetails.setText(getIntent().getExtras().getString("medicdesc"));

        //onclicklistener for FloatingActionButton and edit text
        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton4);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RefillMedicine fragment = new RefillMedicine(getApplicationContext() , medicine );
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

}