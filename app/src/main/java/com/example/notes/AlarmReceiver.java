package com.example.notes;

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
        try {

            Log.d("TAG", "onReceive");
            sendSMS(intent);
        } catch (Exception e) {
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendSMS(Intent intent){

        Bundle bundle = intent.getExtras();
        SmsManager smsManager = SmsManager.getDefault();

        String smsText = bundle.getString("alarm_message");
        String smsNumber = bundle.getString("number");

        smsManager.sendTextMessage(smsNumber, null, smsText, null, null);
        Log.d("TAG", "onCreate: title: SENT SUCCESSFULLY");

        Toast.makeText(context, "SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show();

    }
}
