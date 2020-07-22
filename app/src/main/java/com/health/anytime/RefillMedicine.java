package com.health.anytime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RefillMedicine#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RefillMedicine extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Context context;
    private EditText Qty;
    private int medqty;
    private String text;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public RefillMedicine(Context context , String medicine) {
        // Required empty public constructor
        this.context = context;
        this.text = medicine;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_refill_medicine, container, false);
        Button addButton = view.findViewById(R.id. add_button);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Qty = view.findViewById(R.id.qty_edit_text);
                getMed(text);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        int qty = Integer.parseInt(Qty.getText().toString()) + medqty;
                        databaseReference.child("Pharmacy").child(text).child("quantity").setValue(qty);
                        view.setVisibility(View.GONE);
                    }
                }, 300);
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                view.setVisibility(View.GONE);
                return true;
            }
        });

        return view;
    }

    private void getMed(final String title){
        databaseReference.child("Pharmacy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MedicineModel med = snapshot.getValue(MedicineModel.class);
                    if (snapshot.getKey().equals(title)){
                        medqty = med.getQuantity();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}