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
        deliverNotification(context);

    }
    
    private void deliverNotification(Context context) {


        // Build the notification
        Log.d("TAG", "deliverNotification: ");


        String idChannel = "my_channel_01";
        Intent mainIntent;

        mainIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        // The id of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, null);
        builder.setContentTitle(context.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_date_)
                .setContentIntent(pendingIntent)
                .setContentText("SMS Sent successfully");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, context.getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription("Notification channel");
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            builder.setContentTitle(context.getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.YELLOW, 500, 5000)
                    .setAutoCancel(true);
        }
        mNotificationManager.notify(1, builder.build());

        Log.d("TAG", "delivered Notification: ");
    }



}
