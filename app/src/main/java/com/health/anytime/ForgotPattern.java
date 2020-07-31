package com.health.anytime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPattern extends AppCompatActivity {
    TextView forgotpw;
    EditText password;
    ProgressBar progressBar;
    Button login;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgotpattern);
        password=findViewById(R.id.password);
        progressBar=findViewById(R.id.progressBar_signIn);
        final SharedPreferences.Editor editor = getSharedPreferences("Lock", MODE_PRIVATE).edit();

        login=findViewById(R.id.sendlink);
        forgotpw=findViewById(R.id.textView_forgotPass);
        forgotpw.setPaintFlags(forgotpw.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals("")) {
                    password.setError("Please enter your Password");
                } else {
                    progressBar.setVisibility(View.VISIBLE);


                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), password.getText().toString());

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        editor.putString("Code",null);
                                        editor.apply();
                                        Intent intent =new Intent(ForgotPattern.this,User_home.class);
                                        startActivity(intent);

                                        progressBar.setVisibility(View.GONE);

                                    }
                                    else {

                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(ForgotPattern.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();


                                    }
                                }
                            });



                }
            }
        });




        forgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ForgotPattern.this,ForgetPassword.class);
                startActivity(intent);
            }
        });

    }
}

