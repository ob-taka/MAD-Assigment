package com.health.anytime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class ConfirmLock extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmlock);
        final SharedPreferences.Editor editor = getSharedPreferences("Lock", MODE_PRIVATE).edit();
        final Intent intent = getIntent();
        final String code = intent.getExtras().getString("code");
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
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);

                    editor.putString("Code",code);
                    editor.apply();
                    Log.v("Code",code);
                    Toast.makeText(ConfirmLock.this, "Pattern lock has been set", Toast.LENGTH_LONG).show();

                    Intent intent1 = new Intent(ConfirmLock.this, Settings.class);
                    startActivity(intent1);

                }
                else {
                    editor.putString("Code",null);
                    editor.apply();
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);

                    Toast.makeText(ConfirmLock.this, "Pattern does not match", Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(ConfirmLock.this, Settings.class);
                    startActivity(intent1);

                }
            }

            @Override
            public void onCleared() {


            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToMainActivity = new Intent(getApplicationContext(), Settings.class);
        goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
        startActivity(goToMainActivity);
    }

}
