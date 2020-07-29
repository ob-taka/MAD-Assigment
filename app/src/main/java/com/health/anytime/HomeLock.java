package com.health.anytime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class HomeLock extends AppCompatActivity {
    TextView forgotpat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences prefs = getSharedPreferences("Lock", MODE_PRIVATE);
        final String code = prefs.getString("Code", "");
        if (code.equals("")) {
            Intent intent = new Intent(HomeLock.this, User_home.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.homelock);
            forgotpat=findViewById(R.id.forgotpat);
            forgotpat.setPaintFlags(forgotpat.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            forgotpat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            final PatternLockView patternLockView = findViewById(R.id.patternView);
            patternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onProgress(List progressPattern) {

                }

                @Override
                public void onComplete(List pattern) {

                    if (PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase(code)) {


                        Intent intent1 = new Intent(HomeLock.this, User_home.class);
                        startActivity(intent1);

                    } else {

                        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);

                        Toast.makeText(HomeLock.this, "Pattern is incorrect", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCleared() {


                }
            });
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
