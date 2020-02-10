package com.example.notes;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;


            Log.d("TAG", "onReceive");
            sendSMS(intent);

    }

    private void sendSMS(Intent intent){

        Bundle bundle = intent.getExtras();
        String smsNumberToSend = (String) bundle.getCharSequence("SmsNumber");
        String smsTextToSend = (String) bundle.getCharSequence("SmsText");

        Log.d("TAG", "onStarted Service: Numbers extracted"+ smsNumberToSend + smsTextToSend);
        Toast.makeText(context, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
        Toast.makeText(context,"MyAlarmService.onStart() with \n" +
                        "smsNumberToSend = " + smsNumberToSend + "\n" +
                        "smsTextToSend = " + smsTextToSend,
                Toast.LENGTH_LONG).show();

        Intent intent2=new Intent(context,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(context, 0, intent2,0);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsNumberToSend, null, smsTextToSend, pi, null);
        Log.d("TAG", "SMS SENT Service");

    }
}
