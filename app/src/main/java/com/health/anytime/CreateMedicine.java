package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateMedicine extends AppCompatActivity{
    EditText medicineTitle;
    EditText medicineQty;
    EditText medicineImg;
    EditText medicineDesc;
    Button addButton;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    int numMed = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_medicine);

        medicineQty = findViewById(R.id.qty_edit_text);

        //setup onclicklistener on add button
        addButton = findViewById(R.id. add_button);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                countId();
                String title = medicineTitle.getText().toString().trim();
                int qty = Integer.parseInt(medicineQty.getText().toString());
                String img = medicineImg.getText().toString().trim();
                MedicineModel medicine = new MedicineModel(numMed+1,title,img,qty);
                databaseReference.child("Pharmacy").child(title).setValue(medicine);

                //to add to user medicine

                Intent nextActivity = new Intent(CreateMedicine.this  , Pharmacy.class );
                startActivity(nextActivity);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left); // animation
            }
        });
    }

    //count the total number of
    private void countId(){
        // fetch patient from firebase
        databaseReference.child("Pharmacy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numMed = (int)dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}