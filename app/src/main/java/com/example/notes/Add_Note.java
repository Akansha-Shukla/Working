package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Add_Note extends AppCompatActivity implements View.OnClickListener{

    private static Context context;

    public   static Context getAppContext(){
        return context;
    }
    Toolbar toolbar;
    int mHour, mMinute,mSecond, mYear, mMonth, mDay;
    String setTime = " ", setDate = " ";
    DatePickerDialog picker;
    final Calendar c = Calendar.getInstance();
    long id, ID;


    EditText title, number,message ,date, time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__note);
        context = Add_Note.this;


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New Note");
        actionBar.setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title);
        number= findViewById(R.id.number);
        message = findViewById(R.id.message);
        date = findViewById(R.id.date);
        time= findViewById(R.id.time);

        date.setInputType(InputType.TYPE_NULL);
        time.setInputType(InputType.TYPE_NULL);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0)
                    getSupportActionBar().setTitle(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


       date.setOnClickListener(this);
       time.setOnClickListener(this);


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
                save();


                break;
            case R.id.delete:
                 delete();
                 onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoMain() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void delete() {


       NoteDatabase database= new NoteDatabase(getApplicationContext());
        database.delete(id);
        Toast.makeText(getApplicationContext(),"Delete successfully", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putCharSequence("Status", "OFF");
        myIntent.putExtras(bundle);



        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int)id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(Add_Note.this, "Cancel!", Toast.LENGTH_LONG).show();


    }

    private void save() {

       NoteDatabase database= new NoteDatabase(this);
        String t, n, m,d, ti;
         t = title.getText().toString();
         n = number.getText().toString();
         m = message.getText().toString();
         d= date.getText().toString();
         ti =time.getText().toString();
        if(n.isEmpty()||d.isEmpty()||ti.isEmpty()){
            Empty();
        }
        else {

            if(System.currentTimeMillis()-1000 >= c.getTimeInMillis()){
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
                Note note = new Note(t, n, m, d, ti);
                id = database.addNote(note);
                Log.d("TAG", "Save ID: " + id);

                String date_time = d + " " + ti;
                Log.d("TAG", "Save Date-Time: " + date_time);
                sendMessage(note.getID(), m, n, c);
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

    public void sendMessage(long id,String msg,String num, Calendar c){

        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        Log.d("TAG", "Send Message time: " + c.getTimeInMillis());

            Toast.makeText(getApplicationContext(), "Your SMS has been scheduled...", Toast.LENGTH_LONG).show();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("SmsNumber", num);
            bundle.putCharSequence("SmsText", msg);
            bundle.putCharSequence("Status", "ON");
            Log.d("TAG", "Send Message: num, msg " + num + msg);
            myIntent.putExtras(bundle);

            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


                alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), pendingIntent);
            }

            Toast.makeText(Add_Note.this,
                    "Start Alarm with \n" +
                            "smsNumber = " + num + "\n" +
                            "smsText = " + msg,
                    Toast.LENGTH_LONG).show();
            gotoMain();


    }


    @Override
    public void onClick(View view) {


        if (view == date) {

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


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

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            mSecond = c.get(Calendar.SECOND);

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


