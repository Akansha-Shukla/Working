package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Add_Note extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    int mHour, mMinute, mYear, mMonth, mDay;
    String setTime = " ", setDate = " ";
    DatePickerDialog picker;
    final Calendar myCalendar = Calendar.getInstance();


    EditText title, number,message ,date, time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__note);



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
                gotoMain();

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


    }

    private void save() {

       NoteDatabase database= new NoteDatabase(this);
        String t, n, m,d, ti;
         t = title.getText().toString();
         n = number.getText().toString();
         m = message.getText().toString();
         d= date.getText().toString();
         ti =time.getText().toString();

        Note note = new Note(t, n, m,d,ti);
        long id = database.addNote(note);

        Toast.makeText(getApplicationContext(), "Your SMS has been scheduled...", Toast.LENGTH_LONG).show();

        Log.d("TAG", "Save ID: " + id);

        long timeInMilliseconds;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date mDate = sdf.parse(d);
          timeInMilliseconds = mDate.getTime();
            Log.d("TAG", "Save time " + timeInMilliseconds);
            sendMessage(id,m,n,timeInMilliseconds);



        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    public void sendMessage(long id,String msg,String num, long time ){


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
       int ID = Integer.valueOf(String.valueOf(id));
        Intent intentBoot = new Intent(getApplicationContext(), AlarmReceiver.class);
        intentBoot.putExtra("id",id);
        intentBoot.putExtra("msg",msg);
        intentBoot.putExtra("num",num);
        Log.d("TAG", "Send Message : ");

        intentBoot.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingAlarm = PendingIntent.getBroadcast(
                this, 0, intentBoot, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingAlarm);
    }


    @Override
    public void onClick(View view) {


        if (view == date) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == time) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            time.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}


