package com.health.anytime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
    private Spinner medicine;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    List<String> spinnerArray =  new ArrayList<String>();

    public RefillMedicine(Context context) {
        // Required empty public constructor
        this.context = context;
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
        // you need to have a list of data that you want the spinner to display

        getMedName();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context , R.layout.support_simple_spinner_dropdown_item , spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicine = (Spinner) view.findViewById(R.id.spinner);
        medicine.setAdapter(adapter);
        medicine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = parent.getItemAtPosition(position).toString();
                Log.d("NigGA", text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button addButton = view.findViewById(R.id. add_button);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Qty = view.findViewById(R.id.qty_edit_text);
                int qty = Integer.parseInt(Qty.getText().toString());
                getMed(text);
                databaseReference.child("Pharmacy").child(text).child("quantity").setValue(qty+medqty);

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

    private void getMedName(){
        // fetch patient from firebase
        databaseReference.child("Pharmacy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    spinnerArray.add(snapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMed(final String title){
        // fetch patient from firebase
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