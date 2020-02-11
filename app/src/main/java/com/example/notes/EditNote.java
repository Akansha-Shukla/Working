package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
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
    Add_Note instance ;
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

                delete();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,NoteDetail.class);
        intent.putExtra("ID",note.getID());
        startActivity(intent);

    }

    private void save(long id) {

        NoteDatabase database= new NoteDatabase(this);
        Log.d("TAG", "onSAVE");
        String t, n, m,d, ti;
        long _id;
        t = title.getText().toString();
        n = number.getText().toString();
        m = message.getText().toString();
        d= date.getText().toString();
        ti =time.getText().toString();
        Log.d("TAG", "OnSave title: "+ t);
        Note note = new Note(id,t, n, m,d,ti);
        if(n.isEmpty()||d.isEmpty()||ti.isEmpty()){
            Empty();
        }
        else {
            Calendar check = Calendar.getInstance();
            if(check.getTimeInMillis() > c.getTimeInMillis()){

                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setCancelable(true);
                adb.setMessage("Chosen time has passed already.")
                        .setTitle("Choose different Time")
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = adb.create();
                alert.show();

            }
            else {
                _id = database.update(note);
                if (_id < 0)
                    Toast.makeText(getApplicationContext(), "update failed", Toast.LENGTH_LONG).show();
                else {
                    sendMessage(note.getID(), m, n, c);

                }
                Log.d("TAG", "Save ID: " + id);
            }
        }

    }

    private void Empty() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(true);
        adb.setMessage("Mandatory Fields can't be empty")
                .setTitle("Required")
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = adb.create();
        alert.show();
    }


    private void delete() {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(true);
        adb.setMessage("You want to Delete the note?")
                .setTitle("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



//                        note.setStatus("OFF");
                        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                        Bundle bundle = new Bundle();
                        bundle.putCharSequence("Status","OFF");
                        myIntent.putExtras(bundle);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int)note.getID(), myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                        AlarmManager alarmManager = (AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);

                        Toast.makeText(getApplicationContext(), "Alarm Cancel!", Toast.LENGTH_LONG).show();
                        database.delete(id);

                        Toast.makeText(getApplicationContext(),"Delete successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        database.delete(id);
                        Toast.makeText(getApplicationContext(),"Delete successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = adb.create();
        alert.show();
    }
    public void sendMessage(long id,String msg,String num, Calendar c ){

        Log.d("TAG", "sendMessage: ");


            Toast.makeText(getApplicationContext(), "updated successfully", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);

            Bundle bundle = new Bundle();
            bundle.putCharSequence("SmsNumber", num);
            bundle.putCharSequence("SmsText", msg);
            bundle.putCharSequence("Status", "ON");
            myIntent.putExtras(bundle);

            Log.d("TAG", "sendMessage: Intent created");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) note.getID(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), pendingIntent);

            Toast.makeText(EditNote.this,
                    "Start Alarm with \n" +
                            "smsNumber = " + num + "\n" +
                            "smsText = " + msg,
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), NoteDetail.class);
            intent.putExtra("ID", note.getID());
            startActivity(intent);


    }

    private void gotoMain() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        if (view == date) {

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
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
        if (view == time) {

            // Get Current Time

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
