package com.health.anytime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    FrameLayout pw,email,logout,delete;
    FirebaseAuth mAuth;
    Switch lock;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.settings);
        pw=findViewById(R.id.password);
        email=findViewById(R.id.email);
        lock=findViewById(R.id.lock);
        delete=findViewById(R.id.delete);
        logout=findViewById(R.id.logout);
        mAuth=FirebaseAuth.getInstance();
        final SharedPreferences.Editor editor = getSharedPreferences("Lock", MODE_PRIVATE).edit();
        pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Settings.this,ChangePassword.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(Settings.this,SignIn.class);
                startActivity(intent);
            }
        });
        lock.setTextOff("OFF");
        lock.setTextOn("ON");
        SharedPreferences prefs = getSharedPreferences("Lock", MODE_PRIVATE);
        final String code = prefs.getString("Code", "");
        if (code.equals("")) {
            lock.setChecked(false);

        }

        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent=new Intent(Settings.this,SetLock.class);
                    startActivity(intent);
                } else {
                    editor.putString("Code",null);
                    editor.apply();

                }
            }
        });

    }
}
