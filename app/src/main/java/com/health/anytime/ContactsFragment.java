package com.health.anytime;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ContactsFragment extends Fragment {

    private View contactsView;
    private RecyclerView mRecyclerView;
    private DatabaseReference contactsRef;
    private String uid;


    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid","");
        Log.d("#d",uid);
        contactsRef = FirebaseDatabase.getInstance().getReference("Contacts").child(uid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactsView = inflater.inflate(R.layout.fragment_contacts, container, false);

        mRecyclerView = contactsView.findViewById(R.id.RV_ContactsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return contactsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<PatientModel>()
                .setQuery(contactsRef, PatientModel.class)
                .build();

        //FirebaseRecyclerAdapter<PatientModel, >
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}