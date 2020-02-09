package com.example.notes;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AlarmService extends Service {
    String smsNumberToSend, smsTextToSend;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);

        Bundle bundle = intent.getExtras();
        smsNumberToSend = (String) bundle.getCharSequence("SmsNumber");
        smsTextToSend = (String) bundle.getCharSequence("SmsText");

        Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
        Toast.makeText(this,
                "MyAlarmService.onStart() with \n" +
                        "smsNumberToSend = " + smsNumberToSend + "\n" +
                        "smsTextToSend = " + smsTextToSend,
                Toast.LENGTH_LONG).show();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsNumberToSend, null, smsTextToSend, null, null);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }

}