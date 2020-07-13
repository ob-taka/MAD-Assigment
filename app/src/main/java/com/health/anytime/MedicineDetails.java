package com.health.anytime;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MedicineDetails extends AppCompatActivity{
    TextView medicinetitle;
    TextView medicinedetails;
    ImageView medicineimg;
    String medicine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);

        medicinetitle = findViewById(R.id.textView5);
        medicinedetails = findViewById(R.id.textView6);
        medicineimg = findViewById(R.id.imageView2);

        fetchMedicinePic(getIntent().getExtras().getString("medicimg"));
        medicine = getIntent().getExtras().getString("medicname");
        medicinetitle.setText(medicine);
        medicinedetails.setText(getIntent().getExtras().getString("medicdesc"));
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