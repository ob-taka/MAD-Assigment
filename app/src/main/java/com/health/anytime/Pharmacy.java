package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;

public class Pharmacy extends AppCompatActivity implements MedicineAdaptor.OnCardListener {
    ArrayList<MedicineModel> medicineModels = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    RecyclerView recyclerView;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    boolean clicked = false;

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
        MedicineAdaptor adapter = new MedicineAdaptor(this , medicineModels , this);
        recyclerView.setAdapter(adapter);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new MedicineCardDecorator(largePadding, smallPadding));

        //onclicklistener for FloatingActionButton and edit text
        final FloatingActionButton addPatient = findViewById(R.id.floatingActionButton4);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked){
                    RefillMedicine fragment = new RefillMedicine();
                    fragmentTransaction.add(R.id.container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    clicked = true;
                }


//                // inflate the layout of the popup window
//                LayoutInflater inflater = (LayoutInflater)
//                        getSystemService(LAYOUT_INFLATER_SERVICE);
//                View popupView = inflater.inflate(R.layout.activity_create_medicine, null);
//
//                // create the popup window
//                int width = LinearLayout.LayoutParams.MATCH_PARENT;
//                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                boolean focusable = true; // lets taps outside the popup also dismiss it
//                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//                // show the popup window
//                // which view you pass in doesn't matter, it is only used for the window tolken
//                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//                // dismiss the popup window when touched
//                popupView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        popupWindow.dismiss();
//                        return true;
//                    }
//                });
//                Intent nextActivity = new Intent(Pharmacy.this  , CreateMedicine.class );
//                startActivity(nextActivity);
//                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
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

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(this, MedicineDetails.class);
        intent.putExtra("medicname" , medicineModels.get(position).getMedicineTitle());
        intent.putExtra("medicimg" , medicineModels.get(position).getMedicineImg());
        intent.putExtra("medicdesc" , medicineModels.get(position).getMedid());
        startActivity(intent);
    }
}