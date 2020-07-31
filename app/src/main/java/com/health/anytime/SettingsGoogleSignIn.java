package com.health.anytime;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsGoogleSignIn extends AppCompatActivity {
    FrameLayout logout;
    Switch lock;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("644403792200-g475m0097k2o5mrjq0tau3rlau2fftdq.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        lock=findViewById(R.id.lock);
        logout=findViewById(R.id.logout);
        final SharedPreferences.Editor editor = getSharedPreferences("Lock", MODE_PRIVATE).edit();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut();
                editor.putString("Code",null);
                editor.apply();
                Intent intent=new Intent(SettingsGoogleSignIn.this,SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

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

                    Intent intent=new Intent(SettingsGoogleSignIn.this,SetLock.class);
                    startActivity(intent);
                    if (code.equals("")) {
                        lock.setChecked(false);

                    }
                } else {
                    new AlertDialog.Builder(SettingsGoogleSignIn.this)
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
