package com.health.anytime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class SetLock extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setlock);
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
                if(PatternLockUtils.patternToString(patternLockView, pattern).length()<3){
                    Toast.makeText(SetLock.this, "Please have at least 3 lines in your pattern", Toast.LENGTH_LONG).show();
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);


                }
                else {
                    Intent code = new Intent(SetLock.this, ConfirmLock.class);

                    code.putExtra("code", PatternLockUtils.patternToString(patternLockView, pattern));

                    startActivity(code);
                }


            }

            @Override
            public void onCleared() {


            }
        });

    }








}
