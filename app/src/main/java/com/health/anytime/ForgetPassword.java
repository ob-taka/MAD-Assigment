package com.health.anytime;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText email;
    Button link;
    ProgressBar progressBar;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgetpassword);
        mAuth= FirebaseAuth.getInstance();
        email=findViewById(R.id.editText_email);
        progressBar=findViewById(R.id.progressBar_signIn);
        link=findViewById(R.id.sendlink);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String resetemail=email.getText().toString();
                if (TextUtils.isEmpty(resetemail)){
                    Toast.makeText(ForgetPassword.this,"Please enter an email address",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);

                }

                else{
                    mAuth.sendPasswordResetEmail(resetemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgetPassword.this,"Link has been sent to email address",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                email.setText("");

                            }
                            else{
                                Toast.makeText(ForgetPassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    });
                }
            }
        });
    }

}
