package com.example.mad_assigment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class User_home extends AppCompatActivity {

<<<<<<< HEAD
    private MAdaptor adaptor;
=======
    RecyclerView mRecycleV;
    MAdaptor mAdaptor;

>>>>>>> af21fcf20a791775bfa02a3eb36e7f19419ed9f2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_home);

        mRecycleV = findViewById(R.id.mRV);
        mRecycleV.setLayoutManager(new LinearLayoutManager(this));

        mAdaptor = new MAdaptor(this, getList());
        mRecycleV.setAdapter((mAdaptor));

    }

    private ArrayList<Modle>getList(){

        ArrayList<Modle> modles= new ArrayList<>();

        Modle m = new Modle();
        m.setTitle("Panadol");
        m.setDescription("Before Food");
        m.setTime("10:00 AM");
        m.setImg(R.drawable.pill);
        modles.add(m);

        m = new Modle();
        m.setTitle("Cough syrup");
        m.setDescription("After Food");
        m.setTime("10:00 AM");
        m.setImg(R.drawable.pill);
        modles.add(m);

        m = new Modle();
        m.setTitle("Antibiotics");
        m.setDescription("After Food");
        m.setTime("10:00 AM");
        m.setImg(R.drawable.pill);
        modles.add(m);

        m = new Modle();
        m.setTitle("Cough syrup");
        m.setDescription("After Food");
        m.setTime("09:00 PM");
        m.setImg(R.drawable.pill);
        modles.add(m);

        m = new Modle();
        m.setTitle("Antibiotics");
        m.setDescription("After Food");
        m.setTime("09:00 PM");
        m.setImg(R.drawable.pill);
        modles.add(m);

        return modles;
    }
}
