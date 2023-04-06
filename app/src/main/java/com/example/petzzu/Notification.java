package com.example.petzzu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Notification extends AppCompatActivity implements View.OnClickListener{


    private TimePicker tp;
    private Button btn_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        btn_set=findViewById(R.id.set_alarm);
        tp=findViewById(R.id.time);
        btn_set.setOnClickListener(this);

        Button ext=(Button) findViewById(R.id.exit);
        ext.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }


    @Override
    public void onClick(View view) {
        Calendar cal=Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                tp.getHour(),
                tp.getMinute(),0
                );
        Alarm_set(cal.getTimeInMillis());
    }

    private void Alarm_set(long timeInMillis) {
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,Alarm.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this,"Alarm is Set",Toast.LENGTH_LONG).show();
    }
}