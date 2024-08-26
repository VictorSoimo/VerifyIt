package com.food.verifyit;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class localDb extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "verifyit.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public localDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    public void onCreate(SQLiteDatabase db) {
        // Creating tables for my database
        db.execSQL("CREATE TABLE users (idNnumber TEXT UNIQUE PRIMARY KEY, username TEXT, email TEXT UNIQUE, password TEXT)");

        db.execSQL("CREATE TABLE drinks (drinkcode TEXT PRIMARY KEY , drinkname TEXT, scanned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, manufacturer TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public Context getContext() {
        return context;
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS drinks");
        onCreate(db);
    }




}






