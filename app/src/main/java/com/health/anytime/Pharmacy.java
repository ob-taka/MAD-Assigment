package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Pharmacy extends AppCompatActivity{
    ArrayList<MedicineModel> medicineModels = new ArrayList<>();
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

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        MedicineAdaptor adapter = new MedicineAdaptor(this , medicineModels);
        recyclerView.setAdapter(adapter);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new MedicineCardDecorator(largePadding, smallPadding));

        //onclicklistener for FloatingActionButton and edit text
        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton4);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(Pharmacy.this  , CreateMedicine.class );
                startActivity(nextActivity);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
            }
        });
    }

    private void fetchMedicineData(){
        // fetch patient from firebase
        databaseReference.child("Pharmacy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicineModels.clear();
                recyclerView.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MedicineModel medicineModel = snapshot.getValue(MedicineModel.class);
                    medicineModels.add(medicineModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}