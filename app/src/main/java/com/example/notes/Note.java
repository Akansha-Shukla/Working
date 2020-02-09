package com.example.notes;

public class Note {

    long ID;
    String title;
    String message;
    String date;
    String time;
    String number;


    Note(){

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Note(String title, String number, String message, String date, String time) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.number = number;
        this.time = time;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Note(long ID, String title, String number, String message, String date, String time) {
        this.ID = ID;
        this.title = title;
        this.message = message;
        this.date = date;
        this.number= number;
        this.time = time;
    }
}
