package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDetail extends AppCompatActivity {

    NoteDatabase database ;
    long id;

    Note note;
            TextView title, number, message, date, time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        id = intent.getLongExtra("ID", 0);
        Log.d("TAG", "onCreate: id: "+ id);

        database= new NoteDatabase(this);
        note = database.getNote(id);
        Toast.makeText(getApplicationContext(),"id: "+ note.getTitle(), Toast.LENGTH_LONG).show();
        Log.d("TAG", "onCreate: note.title()"+ note.getTitle());
        //getSupportActionBar().setTitle(note.getTitle());


        message = findViewById(R.id.message);
        number = findViewById(R.id.phone);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);



        message.setText("Message : "+ note.getMessage());
        number.setText("Text : "+ note.getNumber());
        date.setText("Date : "+ note.getDate());
        time.setText("Time : "+ note.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId() == R.id.edit_note){

            Intent intent = new Intent(this,EditNote.class);
            intent.putExtra("ID",id);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.deleteNote){

            delete();


        }
        return super.onOptionsItemSelected(item);
    }



    private void delete() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(true);
        adb.setMessage("You want to Delete the note?")
                .setTitle("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                        Bundle bundle = new Bundle();
                        bundle.putCharSequence("Status","OFF");
                        myIntent.putExtras(bundle);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int)note.getID(), myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                        AlarmManager alarmManager = (AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);

                        Toast.makeText(NoteDetail.this, "Alarm Cancel!", Toast.LENGTH_LONG).show();
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
}
