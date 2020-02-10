package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditNote extends AppCompatActivity implements View.OnClickListener{

    long id;
    NoteDatabase database;
    Note note;
    EditText title, message, date, time, number;
    Calendar c = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();
        id = intent.getLongExtra("ID", 0);
        Log.d("TAG", "onCreate: id: "+ id);

        database= new NoteDatabase(getApplicationContext());
        note = database.getNote(id);
        getSupportActionBar().setTitle(note.getTitle());

        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        number = findViewById(R.id.number);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);


        title.setText(note.getTitle());
        message.setText( note.getMessage());
        number.setText( note.getNumber());
        date.setText( note.getDate());
        time.setText(note.getTime());
        date.setOnClickListener(this);
        time.setOnClickListener(this);
        Log.d("TAG", "onCreate: title: "+ note.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.date_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.save:
                save(id);
                Log.d("TAG", "save clicked: ");



                break;
            case R.id.delete:

                database.delete(id);
                Toast.makeText(getApplicationContext(),"Delete successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    private void save(long id) {

        NoteDatabase database= new NoteDatabase(this);
        Log.d("TAG", "onSAVE");
        String t, n, m,d, ti;
        t = title.getText().toString();
        n = number.getText().toString();
        m = message.getText().toString();
        d= date.getText().toString();
        ti =time.getText().toString();
        Log.d("TAG", "OnSave title: "+ t);
        Note note = new Note(id,t, n, m,d,ti);
        Log.d("TAG", "OnSave id: "+ note.getID());
        long _id = database.update(note);


        String date_time = d+ " " + ti + ":00";
        Log.d("TAG", "Date_time " + date_time);

        Log.d("TAG", "onSave: update id: "+ _id);
        if(_id<0)
            Toast.makeText(getApplicationContext(),"update failed", Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(getApplicationContext(), "updated successfully", Toast.LENGTH_LONG).show();
            Log.d("TAG", "updated");



            sendMessage(id,m,n,c.getTimeInMillis());


            Intent intent = new Intent(getApplicationContext(), NoteDetail.class);
            intent.putExtra("ID", note.getID());
            startActivity(intent);
        }




        Log.d("TAG", "Save ID: " + id);


    }


    public void sendMessage(long id,String msg,String num, long time ){

        Intent myIntent = new Intent(EditNote.this, AlarmReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putCharSequence("SmsNumber", num);
        bundle.putCharSequence("SmsText", msg);
        myIntent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getService(EditNote.this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        alarmManager.set(AlarmManager.RTC,  c.getTimeInMillis(), pendingIntent);

        Toast.makeText(EditNote.this,
                "Start Alarm with \n" +
                        "smsNumber = " + num + "\n" +
                        "smsText = " + msg,
                Toast.LENGTH_LONG).show();


    }

    private void gotoMain() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        if (view == date) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            c.set(Calendar.MONTH, monthOfYear);
                            c.set(Calendar.YEAR, year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == time) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                                time.setText(hourOfDay + ":" + padding(minute)+":00");
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            c.set(Calendar.SECOND, 0);



                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
    private String padding(int time) {
        if(time < 10)
            return "0"+time;
        return String.valueOf(time);

    }
}









/*
package com.example.notes;

        import android.app.Service;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.IBinder;
        import android.telephony.SmsManager;
        import android.util.Log;
        import android.widget.Toast;

        import androidx.annotation.Nullable;

public class AlarmService extends Service {
    String smsNumberToSend, smsTextToSend;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
        Log.d("TAG", "onCreate Service");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
        Log.d("TAG", "onBind Service");
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
        Log.d("TAG", "onDestroy Service");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);

        Log.d("TAG", "onStart Service");
        Bundle bundle = intent.getExtras();
        smsNumberToSend = (String) bundle.getCharSequence("SmsNumber");
        smsTextToSend = (String) bundle.getCharSequence("SmsText");

        Log.d("TAG", "onStarte Service: Numbers extracted");
        Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
        Toast.makeText(this,
                "MyAlarmService.onStart() with \n" +
                        "smsNumberToSend = " + smsNumberToSend + "\n" +
                        "smsTextToSend = " + smsTextToSend,
                Toast.LENGTH_LONG).show();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsNumberToSend, null, smsTextToSend, null, null);
        Log.d("TAG", "SMS SENT Service");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }

}*/
