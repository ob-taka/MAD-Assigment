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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPatient extends AppCompatActivity{

    RecyclerView mRecycleView;
    MedicineAdaptor mAdaptor;
    DatabaseReference databaseReference;
    ArrayList<MedicineModel> medicineList;
    ArrayList<MedicineModel> patientMList;
    String patientName;
    TextView nameView;
    String patientKey;
    String patientPic;
    ImageView patientPicView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        final Bundle data = getIntent().getExtras();
        patientName =  data.getString("patientname");
        patientPic = data.getString("patientpic");

        //init data and firebase conncetion
        medicineList = new ArrayList<>();
        patientMList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        nameView = findViewById(R.id.greating);
        nameView.setText(patientName);
        patientPicView = findViewById(R.id.profile_image);

        mRecycleView = findViewById(R.id.mRV);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchMedicineData();
        fetchData();
        fetchMdata();
        fetchPatientPic();
        mAdaptor = new MedicineAdaptor(medicineList);
        mRecycleView.setAdapter((mAdaptor));
    }

    // this function fetches patient data from firebase server
    private void fetchData(){

        databaseReference.child("test").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PatientModel patient = snapshot.getValue(PatientModel.class);

                    if(patient.getPatientName().equals(patientName)){
                        patientKey = snapshot.getKey();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // this method fetches medicine data from firebase server
    private  void fetchMdata(){
        databaseReference.child("patientMedicineList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicineList.clear();
                mRecycleView.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(patientKey)){
                        GenericTypeIndicator<HashMap<String, Boolean>> to = new
                                GenericTypeIndicator<HashMap<String, Boolean>>() {};
                        HashMap<String, Boolean> map = snapshot.getValue(to);
                        int count = 0;
                        for(boolean ml: map.values()) {
                            if(ml) {
                                medicineList.add(patientMList.get(count));
                            }
                            count++;
                        }
                        System.out.println(medicineList);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // add dialog
            }
        });
    }
    //fetch medicine list from firebase
    private void fetchMedicineData(){
        databaseReference.child("Medicine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    patientMList.add(snapshot.getValue(MedicineModel.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
