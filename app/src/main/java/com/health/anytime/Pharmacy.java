package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Objects;


public class Pharmacy extends AppCompatActivity implements MedicineAdaptor.OnCardListener   {
    ArrayList<MedicineModel> medicineModels = new ArrayList<>();
    ArrayList<String> medicineRefills = new ArrayList<>();
    String doctorId;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    RecyclerView recyclerView;

    @Override
    protected void onStart() {
        super.onStart();
        fetchMedicineData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        doctorId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        MedicineAdaptor adapter = new MedicineAdaptor(this , medicineModels , this);
        recyclerView.setAdapter(adapter);
        //setting diamension of each card view in grid
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new MedicineCardDecorator(largePadding, smallPadding));

    }

    private void fetchMedicineData(){
        // fetch patient from firebase
        databaseReference.child("Pharmacy").child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicineModels.clear();
                recyclerView.removeAllViews();
                //get all medicine from firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MedicineModel medicineModel = snapshot.getValue(MedicineModel.class);
                    medicineModels.add(medicineModel);
                    //get list of medicine below quantity 10
                    if (medicineModel.getQuantity() < 10){
                        medicineRefills.add(medicineModel.getMedicineTitle());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(this, MedicineDetails.class);
        intent.putExtra("medicimg" , medicineModels.get(position).getMedicineImg());
        intent.putExtra("medname" , medicineModels.get(position).getMedicineTitle());
        startActivity(intent);
    }


    public void init_firebase(){
        String[] m = { "Antibiotics" , "Panadol" , "Benzonatate" ,  "Activiated Charcoal"};
        String[] n = { "do not overdose" , "do not overdose" , "never suck or chew on a benzonatate capsule. Swallow the pill whole. Sucking or chewing the capsule may cause your mouth and throat to feel numb or cause other serious side effects." ,  "used to treat a drug overdose or a poisoning."};

        for (int i = 0; i < m.length; i++) {
            MedicineModel med = new MedicineModel(i+1,m[i],"/Medicine/Panadol.jpg", n[i],10);
            databaseReference.child("Pharmacy").child(doctorId).child(m[i]).setValue(med);
        }
    }

}