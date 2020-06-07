package com.example.mad_assigment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPatient extends AppCompatActivity{

    MAdaptor adaptor;
    DatabaseReference medReference = FirebaseDatabase.getInstance().getReference().child("med_list");
    ArrayList<MedicineModel> medicineList;
    ArrayList<MedicineModel> patientMList;
    String patientName;
    TextView nameView;
    String patientPic;
    String medKey;
    ImageView patientPicView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        final Bundle data = getIntent().getExtras();
        patientName =  data.getString("patientname");
        patientPic = data.getString("patientpic");
        medKey = data.getString("medKey");

        //init data
        medicineList = new ArrayList<>();
        patientMList = new ArrayList<>();

<<<<<<< HEAD
        nameView = findViewById(R.id.greating);
=======
        //set patient name and image
        nameView = findViewById(R.id.patientName);
>>>>>>> 3fa462f1cfd49ce9f23d4773ddde60a683c001ff
        nameView.setText(patientName);
        patientPicView = findViewById(R.id.profile_image);



    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchPatientPic();
        setUpRecyclerView();
    }

//    // this function fetches patient data from firebase server
//    private void fetchData(){
//
//        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    PatientModel patient = snapshot.getValue(PatientModel.class);
//
//                    if(patient.getPatientName().equals(patientName)){
//                        patientKey = snapshot.getKey();
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//    // this method fetches medicine data from firebase server
//    private  void fetchMdata(){
//        databaseReference.child("medicine_list").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                medicineList.clear();
//                mRecycleView.removeAllViews();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (snapshot.getKey().equals(patientKey)){
//                        GenericTypeIndicator<HashMap<String, Boolean>> to = new
//                                GenericTypeIndicator<HashMap<String, Boolean>>() {};
//                        HashMap<String, Boolean> map = snapshot.getValue(to);
//                        int count = 0;
//                        for(boolean ml: map.values()) {
//                            if(ml) {
//                                medicineList.add(patientMList.get(count));
//                            }
//                            count++;
//                        }
//                        System.out.println(medicineList);
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // add dialog
//            }
//        });
//    }
//    //fetch medicine list from firebase
//    private void fetchMedicineData(){
//        databaseReference.child("Medicine").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    patientMList.add(snapshot.getValue(MedicineModel.class));
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void setUpRecyclerView(){
        Query query = medReference.orderByChild("priority");
        FirebaseRecyclerOptions<Modle> options = new FirebaseRecyclerOptions.Builder<Modle>()
                .setQuery(query, Modle.class)
                .build();

        adaptor = new MAdaptor(options);
        RecyclerView recyclerView = findViewById(R.id.mRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

        adaptor.setOnItemClickListener(new MAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                Modle modle = adaptor.getItem(position);
                modle.setExpanded(!modle.isExpanded());
                adaptor.notifyItemChanged(position);
            }
        });
    }

    /**
     * fetch image view from firebase Storage (file hosting service)
     */
    private void fetchPatientPic(){
        // finds image and download image from firebase storage by image path and binds it to view holder
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://quickmad-e4016.appspot.com/");
        StorageReference storageRef = storage.getReference().child( "ProfilePicture/" + patientPic);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ViewPatient.this ).load(uri).into(patientPicView); // uses Gilde , a framework to load and download files in android
            }
        });
    }

}
