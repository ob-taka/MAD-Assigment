package com.health.anytime;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ViewPatient extends AppCompatActivity {
  MAdaptor adaptor;
  DatabaseReference medReference = FirebaseDatabase
    .getInstance()
    .getReference()
    .child("med_list");
  String patientName;
  TextView nameView;
  String patientPic;
  String medKey;
  ImageView patientPicView;
  ArrayList<String> medicineList;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_patient);

    //init data and get data from intent
    final Bundle data = getIntent().getExtras();
    patientName = data.getString("patientname");
    patientPic = data.getString("patientpic");
    medKey = data.getString("medKey");
    medicineList = new ArrayList<>();

    nameView = findViewById(R.id.greating);
    nameView.setText(patientName);
    patientPicView = findViewById(R.id.profile_image);
  }

  @Override
  protected void onStart() {
    super.onStart();
    fetchPatientPic();
    setUpRecyclerView();
  }

  /**
   *fetching medicine data from firebase and set recyclerview
   */
  private void setUpRecyclerView() {
    Query query = medReference.child(medKey).orderByChild("priority");
    FirebaseRecyclerOptions<Modle> options = new FirebaseRecyclerOptions.Builder<Modle>()
      .setQuery(query, Modle.class)
      .build();

    adaptor = new MAdaptor(options);
    RecyclerView recyclerView = findViewById(R.id.mRV);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adaptor);

    adaptor.startListening();// listening for data in firebase

    // when clicked expand the view
    adaptor.setOnItemClickListener(
      new MAdaptor.OnItemClickListener() {

        @Override
        public void onItemClick(DataSnapshot dataSnapshot, int position) {
          Modle modle = adaptor.getItem(position);
          modle.setExpanded(!modle.isExpanded());
          adaptor.notifyItemChanged(position);
        }
      }
    );
  }

  /**
   * fetch image view from firebase Storage (file hosting service)
   */
  private void fetchPatientPic() {
    // finds image and download image from firebase storage by image path and binds it to view holder
    FirebaseStorage storage = FirebaseStorage.getInstance(
      "gs://quickmad-e4016.appspot.com/"
    );
    StorageReference storageRef = storage
      .getReference()
      .child("ProfilePicture/" + patientPic);
    storageRef
      .getDownloadUrl()
      .addOnSuccessListener(
        new OnSuccessListener<Uri>() {

          @Override
          public void onSuccess(Uri uri) {
            Glide.with(ViewPatient.this).load(uri).into(patientPicView); // uses Gilde , a framework to load and download files in android
          }
        }
      );
  }

}
