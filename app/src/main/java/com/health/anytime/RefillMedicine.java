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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class RefillMedicine extends Fragment{

    private Context context;
    private EditText Qty;
    private double medqty;
    private String text;
    private String userId;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public RefillMedicine(Context context , String medicine) {
        // Required empty public constructor
        this.context = context;
        this.text = medicine;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
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
                        double qty = Double.parseDouble(Qty.getText().toString()) + medqty;
                        databaseReference.child("Pharmacy").child(userId).child(text).child("quantity").setValue(qty);
                        view.setVisibility(View.GONE);
                        Intent intent = new Intent(context, Pharmacy.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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

    /**
     *
     * @param title -> medicine name
     * Fetches quantity of medicine from firebase
     */
    private void getMed(final String title){
        databaseReference.child("Pharmacy").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medqty = Double.parseDouble(dataSnapshot.child(title).child("quantity").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}