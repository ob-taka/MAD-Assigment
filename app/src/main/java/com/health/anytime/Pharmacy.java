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
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.sql.Ref;
import java.util.ArrayList;
import static com.health.anytime.MedicineNotification.CHANNEL_1_ID;


public class Pharmacy extends AppCompatActivity implements MedicineAdaptor.OnCardListener   {
    ArrayList<MedicineModel> medicineModels = new ArrayList<>();
    ArrayList<String> medicineRefills = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    RecyclerView recyclerView;
    FloatingActionButton createMedicine;
    private NotificationManager notificationManager;

    @Override
    protected void onStart() {
        super.onStart();
        fetchMedicineData();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                refillmedicine();
            }
        }, 1000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        recyclerView = findViewById(R.id.recycler_view);
        createMedicine = findViewById(R.id.floatingActionButton4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        MedicineAdaptor adapter = new MedicineAdaptor(this , medicineModels , this);
        recyclerView.setAdapter(adapter);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new MedicineCardDecorator(largePadding, smallPadding));
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        createMedicine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pharmacy.this, CreateMedicine.class);
                startActivity(intent);
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
        intent.putExtra("medicname" , medicineModels.get(position).getMedicineTitle());
        intent.putExtra("medicimg" , medicineModels.get(position).getMedicineImg());
        intent.putExtra("medicdesc" , medicineModels.get(position).getMedid());
        intent.putExtra("medicqty" , medicineModels.get(position).getQuantity());
        startActivity(intent);
    }


    private void refillmedicine(){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ha_icon_background)
                .setContentTitle("Low on supplies")
                .setContentText("Panadol")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

}