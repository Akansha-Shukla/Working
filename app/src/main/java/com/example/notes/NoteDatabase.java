package com.example.notes;

import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.InputType;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME= "Notes_Database1";
    public static final String TABLE_NAME= "Notes_Table1";
    public static final int DATABASE_VERSION= 1;
    public static final String ID= "_ID";
    public static final String TITLE= "Title";
    public static final String NUMBER= "Number";
    public static final String MESSAGE= "Message";
    public static final String DATE= "Date";
    public static final String TIME= "Time";
    public static final String CREATE= "Create table "+ TABLE_NAME + "(" + ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ TITLE +" TEXT,"
            + NUMBER + " INTEGER, "+ MESSAGE + " TEXT, "+ DATE+ " TEXT, "+ TIME + " TEXT);" ;

    public NoteDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


    public long addNote(Note note){

        SQLiteDatabase database= getWritableDatabase();

        ContentValues contentValues = new ContentValues();
       // contentValues.put(ID, note.getID());
        contentValues.put(TITLE, note.getTitle());
        contentValues.put(NUMBER, note.getNumber());
        contentValues.put(MESSAGE, note.getMessage());
        contentValues.put(DATE, note.getDate());
        contentValues.put(TIME, note.getTime());


       long id=  database.insert(TABLE_NAME,null, contentValues);

       Log.d("TAG", "addNote: "+ id);
        database.close();
        return id;
    }


    public Note getNote(long id){


        SQLiteDatabase database = getWritableDatabase();

        String[] columns = { ID, TITLE, NUMBER, MESSAGE, DATE, TIME};
        Cursor cursor = database.query(TABLE_NAME,columns, ID+ "=?",new String[]{String.valueOf(id)},null, null, null);
        Note note = new Note();
        if(cursor!= null) {
            if (cursor.moveToFirst()) {

                long _id = cursor.getLong(cursor.getColumnIndex(ID));
                Log.d("TAG", "getNote(): id: "+ _id);
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                String num = cursor.getString(cursor.getColumnIndex(NUMBER));
                String msg = cursor.getString(cursor.getColumnIndex(MESSAGE));
                String d = cursor.getString(cursor.getColumnIndex(DATE));
                String t = cursor.getString(cursor.getColumnIndex(TIME));
                note = new Note(_id, title, num, msg, d, t);

            }
            cursor.close();

        }
        return note;
    }

    public List<Note> getNotes(){

        SQLiteDatabase database = getWritableDatabase();
        List<Note> list = new ArrayList<Note>();

        String[] columns = { ID, TITLE, NUMBER, MESSAGE, DATE, TIME};
        Cursor cursor = database.query(TABLE_NAME,columns, null,null,null, null, null);

        if(cursor!= null) {
            cursor.moveToFirst();

            while (cursor.moveToNext())   {
                Note note = new Note();
                note.setID(cursor.getLong(cursor.getColumnIndex(ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                note.setNumber(cursor.getString(cursor.getColumnIndex(NUMBER)));
                note.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
                note.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                note.setTime(cursor.getString(cursor.getColumnIndex(TIME)));


                list.add(note);

            }
            cursor.moveToFirst();
            return  list;
        }
        else

    return null;

    }

    void delete(long id){

        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, ID+ "=?",new String[]{String.valueOf(id)});
        database.close();
    }

    long update(Note note){

        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, note.getID());
        contentValues.put(TITLE, note.getTitle());
        contentValues.put(NUMBER, note.getNumber());
        contentValues.put(MESSAGE, note.getMessage());
        contentValues.put(DATE, note.getDate());
        contentValues.put(TIME, note.getTime());
        String[] args = {String.valueOf(note.getID())};
        long id=  database.update(TABLE_NAME, contentValues,ID+"=?",args);

        Log.d("TAG", "update: id "+ id);
        return  id;
    }
}
