package com.health.anytime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Calendar;

public class Alarm extends AppCompatActivity {

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        button = findViewById(R.id.btn_setAlarm);

        final NumberPicker b_hour = findViewById(R.id.b_hour);
        final NumberPicker b_min = findViewById(R.id.b_min);
        final NumberPicker l_hour = findViewById(R.id.l_hour);
        final NumberPicker l_min = findViewById(R.id.l_min);
        final NumberPicker d_hour = findViewById(R.id.d_hour);
        final NumberPicker d_min = findViewById(R.id.d_min);

        b_hour.setMaxValue(23);
        b_hour.setMinValue(00);
        b_hour.setValue(9);
        b_min.setMaxValue(59);
        b_min.setMinValue(00);

        l_hour.setMaxValue(23);
        l_hour.setMinValue(0);
        l_hour.setValue(12);
        l_min.setMaxValue(59);
        l_min.setMinValue(00);

        d_hour.setMaxValue(23);
        d_hour.setMinValue(00);
        d_hour.setValue(18);
        d_min.setMaxValue(59);
        d_min.setMinValue(00);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                breakfast_alarm(b_hour.getValue(), b_min.getValue());
                lunch_alarm(l_hour.getValue(), l_min.getValue());
                dinner_alarm(d_hour.getValue(), d_min.getValue());


                dialog("Alarm Successfully added", "We would notify you when it's time");
            }
        });
    }

    public void dialog(String title, String message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_time)
                .setTitle(title)
                .setMessage(message);

        builder.setPositiveButton(
                "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(Alarm.this, User_home.class));
                    }
                }
        );

        builder.setNegativeButton(
                "Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancleAlarm();
                    }
                }
        );

        builder.show();
    }

    private void breakfast_alarm(int hour, int min){
        int id = 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", "Have you ate ?");
        intent.putExtra("des", "Time to take your medicines !!!");
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void lunch_alarm(int hour, int min){
        int id = 2;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", "Have you ate ?");
        intent.putExtra("des", "Time to take your medicines !!!");
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void dinner_alarm(int hour, int min){
        int id = 3;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", "Have you ate ?");
        intent.putExtra("des", "Time to take your medicines !!!");
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    private void cancleAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);

    }

}