package com.health.anytime;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    FrameLayout pw,email,info,logout,delete;
    FirebaseAuth mAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.settings);
        pw=findViewById(R.id.password);
        email=findViewById(R.id.email);
        info=findViewById(R.id.info);
        delete=findViewById(R.id.delete);
        logout=findViewById(R.id.logout);
        mAuth=FirebaseAuth.getInstance();
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


    }
}
