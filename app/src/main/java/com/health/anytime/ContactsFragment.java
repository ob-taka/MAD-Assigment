package com.health.anytime;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactsFragment extends Fragment {

    private View contactsView;
    private RecyclerView mRecyclerView;
    private DatabaseReference contactsRef, usersRef;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private String uid;


    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreferences sharedPreferences = getContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        //uid = sharedPreferences.getString("uid","");

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        contactsRef = FirebaseDatabase.getInstance().getReference("Contacts").child(uid);
        usersRef = FirebaseDatabase.getInstance().getReference("User");
        firebaseStorage = FirebaseStorage.getInstance("gs://quickmad-e4016.appspot.com/");
        storageRef = firebaseStorage.getReference();

        Log.d("#d",uid);
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

        FirebaseRecyclerAdapter<PatientModel, ContactsViewHolder> adapter = new FirebaseRecyclerAdapter<PatientModel, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull PatientModel model) {
                String userIDS = getRef(position).getKey();
                usersRef.child(userIDS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("patientName").getValue().toString();
                        String email = snapshot.child("patientEmail").getValue().toString();
                        String pic = snapshot.child("patientProfilepic").getValue().toString();

                        holder.userName.setText(name);
                        holder.userEmail.setText(email);

                        storageRef = storageRef.child("ProfilePicture/" + pic);
                        storageRef.getDownloadUrl().addOnSuccessListener(
                                new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(ContactsFragment.this).load(uri).into(holder.profPic);
                                    }
                                }
                        );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(),"Error encountered: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }


            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_display_layout, parent, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{
        TextView userName, userEmail;
        CircleImageView profPic;

        public ContactsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            userName = itemView.findViewById(R.id.contacts_name);
            userEmail = itemView.findViewById(R.id.contacts_email);
            profPic = itemView.findViewById(R.id.contacts_profilepic);
        }
    }
}