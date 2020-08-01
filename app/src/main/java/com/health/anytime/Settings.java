package com.health.anytime;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    FrameLayout pw,email,logout,delete;
    FirebaseAuth mAuth;
    Switch lock;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        final String uid = getIntent().getStringExtra("UID");

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
                editor.putString("Code",null);
                editor.apply();
                Intent intent=new Intent(Settings.this,SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Settings.this,DeleteAccount.class);

                intent.putExtra("UID", uid);

                startActivity(intent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Settings.this,ChangeEmail.class);
                intent.putExtra("UID", uid);

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
        else{
            lock.setChecked(true);

        }

        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Intent intent=new Intent(Settings.this,SetLock.class);
                    startActivity(intent);
                    if (code.equals("")) {
                        lock.setChecked(false);

                    }
                } else {
                    new AlertDialog.Builder(Settings.this)
                            .setTitle("Remove pattern lock")
                            .setMessage("Are you sure you want to remove the pattern lock?")


                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    editor.putString("Code",null);
                                    editor.apply();
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("No", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    ;

                }
            }
        });

    }


}
