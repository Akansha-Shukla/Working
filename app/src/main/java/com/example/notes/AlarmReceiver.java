package com.example.notes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    Context context;

    private static final String CHANNEL_ID = "channelID";
    private static final String CHANNEL_NAME = "Notification";
    private static final String CHANNEL_DESC = "Notification tutorial";
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
        String status = (String)bundle.getCharSequence("Status");

        Log.d("TAG", "onStarted Service: Numbers extracted"+ smsNumberToSend + smsTextToSend+status);
        Toast.makeText(context, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
        Toast.makeText(context,"MyAlarmService.onStart() with \n" +
                        "smsNumberToSend = " + smsNumberToSend + "\n" +
                        "smsTextToSend = " + smsTextToSend,
                Toast.LENGTH_LONG).show();

        if(status.equals("ON")) {
            Intent intent2 = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent2, 0);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(smsNumberToSend, null, smsTextToSend, pi, null);
            Log.d("TAG", "SMS SENT Service");
            deliverNotification(context);
        }

    }
    
    private void deliverNotification(Context context) {


        // Build the notification
        Log.d("TAG", "deliverNotification: ");
        NotificationCompat.Builder ntf = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle("Notes")
                .setContentText("SMS Sent Successfully")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);
        ntf.setContentIntent(pendingIntent);
        NotificationManagerCompat mgr = NotificationManagerCompat.from(context);



        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        else {
            ntf.setContentTitle(context.getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.YELLOW, 500, 5000)
                    .setAutoCancel(true);
        }
        mgr.notify(1,ntf.build());

        Log.d("TAG", "delivered Notification: ");
    }



}
