package com.example.mad_assigment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class User_home extends AppCompatActivity {

    RecyclerView mRecycleV;
    MAdaptor mAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_home);

        mRecycleV = findViewById(R.id.mRV);
        mRecycleV.setLayoutManager(new LinearLayoutManager(this));

        mAdaptor = new MAdaptor(this, getList());
        mRecycleV.setAdapter((mAdaptor));

    }

    private ArrayList<Modle>getList(){

        ArrayList<Modle> modles= new ArrayList<>();
        modles.add(new Modle("Panadol", "Before food", "10:00AM", "3 tabs", "Do not over dose", R.drawable.pill));
        modles.add(new Modle("Cough syrup", "Before food", "10:00AM", "3 tabs", "Do not over dose", R.drawable.pill));
        modles.add(new Modle("Antibiotics", "Before food", "10:00AM", "3 tabs", "Do not over dose", R.drawable.pill));
        modles.add(new Modle("Charcoal", "Before food", "10:00AM", "3 tabs", "Do not over dose", R.drawable.pill));

        return modles;
    }
}
