package com.health.anytime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;


public class DoctorHome extends AppCompatActivity {

    Button viewpatient;
    Button addpatient, opt;
    ImageView doctorpic;
    TextView greetings, docname;
    String pic;
    HashMap<String,PatientModel> unaddedPatients;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_home);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        viewpatient = findViewById(R.id.button4);
        addpatient = findViewById(R.id.button5);
        greetings = findViewById(R.id.greating);
        docname = findViewById(R.id.label_Name);
        doctorpic = findViewById(R.id.doctorpic);
        unaddedPatients = new HashMap<>();
        setTimeOfDay();
        opt = findViewById(R.id.menu_btn);
        registerForContextMenu(opt);

        String uid = getIntent().getStringExtra("Uid");
        Log.d("#d",uid);
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                docname.setText("Doctor " + dataSnapshot.child("patientName").getValue().toString().trim());
                pic = dataSnapshot.child("patientProfilepic").getValue(String.class);
                /**
                 * fetch image view from firebase Storage (file hosting service)
                 */
                FirebaseStorage storage = FirebaseStorage.getInstance(
                        "gs://quickmad-e4016.appspot.com/"
                );
                StorageReference storageRef = storage
                        .getReference()
                        .child("ProfilePicture/" + pic );
                storageRef
                        .getDownloadUrl()
                        .addOnSuccessListener(
                                new OnSuccessListener<Uri>() {

                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // finds image and download image from firebase storage by image path and binds it to view holder
                                        Glide.with(DoctorHome.this).load(uri).into(doctorpic); // uses Gilde , a framework to load and download files in android
                                    }
                                }
                        );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DoctorHome.this,"Error" + databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        /**
         * Fetch patient data from firebase and add patient data as a patientModel class
         * into unaddedPatients to be passed to add patient activity
         */
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PatientModel patient = snapshot.getValue(PatientModel.class);
                    if(!patient.isStatus()){
                        unaddedPatients.put(snapshot.getKey() , patient);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DoctorHome.this,"Error" + databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


        viewpatient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PatientList.class);
                startActivity(intent);
            }
        });

        addpatient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPatient.class);
                intent.putExtra("keys" , unaddedPatients);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right); //animation
    }
    /**
     * change the greeting base on the time of the time
     */
    private void setTimeOfDay(){
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            greetings.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greetings.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greetings.setText("Good Evening");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.options_menu,menu);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()){
            case R.id.chat_button :
                startActivity(new Intent(DoctorHome.this, ChatHome.class));
                break;
            case R.id.logout_button :
                auth.signOut();
                startActivity(new Intent(DoctorHome.this,SignIn.class));
                break;
            default:
                break;

        }
        return super.onContextItemSelected(item);
    }



}
