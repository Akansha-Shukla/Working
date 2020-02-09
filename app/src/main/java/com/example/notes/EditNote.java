package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EditNote extends AppCompatActivity implements View.OnClickListener{

    long id;
    NoteDatabase database;
    Note note;
    EditText title, message, date, time, number;
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
        Log.d("TAG", "onSave: update id: "+ _id);
        if(_id<0)
            Toast.makeText(getApplicationContext(),"update failed", Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(getApplicationContext(), "updated successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), NoteDetail.class);
            intent.putExtra("ID", note.getID());
            startActivity(intent);
        }




        Log.d("TAG", "Save ID: " + id);


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

                            time.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
