package com.food.verifyit;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Drinkscanned {

    private String drinkcode;
    private String drinkname;
    private LocalDateTime timescanned;
    private String manufacturer;
    private localDb drinksDb;
    private Context context;
    private BarcodeRepository barcodeRepository;

    //constructor
    public Drinkscanned(Context context) {
        if(context==null){
            throw new IllegalArgumentException("Context cannot be null");
        }
        drinksDb = new localDb(context);
    }

    public Context getContext() {
        return context;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public LocalDateTime getTimescanned() {
        return timescanned;
    }

    public String getDrinkname() {
        return drinkname;
    }

    public String getDrinkcode() {
        return drinkcode;
    }


    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setDrinkname(String drinkname) {
        this.drinkname = drinkname;
    }

    public void setTimescanned(LocalDateTime timescanned) {
        this.timescanned = timescanned;
    }

    public void setDrinkcode(String drinkcode) {
        this.drinkcode = drinkcode;
    }



    public boolean doesDrinkExist(String drinkcode) {
        boolean exists = false;
        SQLiteDatabase db = drinksDb.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    "drinks",
                    new String[]{"drinkcode"},
                    "drinkcode = ?",
                    new String[]{drinkcode},
                    null, null, null
            );
            exists = cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close(); // Ensure cursor is closed
            }
        }
        return exists;
    }

    public void addDrink() {
        LocalDateTime now = LocalDateTime.now();
        SQLiteDatabase db = drinksDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("drinkcode", getDrinkcode());
        values.put("drinkname", getDrinkname());
        values.put("scanned_at", String.valueOf(now));
        values.put("manufacturer", getManufacturer());
        db.insert("drinks", null, values);
        db.close();
    }


    // Get all Drinks
    public List<Drinkscanned> getAllDrinks() {
        List<Drinkscanned> drinkList = new ArrayList<>();
        SQLiteDatabase db = drinksDb.getReadableDatabase();
        Cursor cursor = db.query(
                "drinks",
                new String[]{ "drinkcode", "drinkname", "scanned_at", "manufacturer"},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Drinkscanned drink = new Drinkscanned(drinksDb.getContext());
                drink.setDrinkcode(cursor.getString(cursor.getColumnIndex("drinkcode")));
                drink.setDrinkname(cursor.getString(cursor.getColumnIndex("drinkname")));
                drink.setTimescanned(LocalDateTime.parse(cursor.getString(cursor.getColumnIndex("scanned_at"))));
                drink.setManufacturer(cursor.getString(cursor.getColumnIndex("manufacturer")));
                drinkList.add(drink);
            }
            cursor.close();
        }
        return drinkList;
    }

    // Update a Drink
    public void updateDrink() {
        SQLiteDatabase db = drinksDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("drinkcode", getDrinkcode());
        values.put("drinkname", getDrinkname());
        values.put("scanned_at", String.valueOf(getTimescanned()));
        values.put("manufacturer", getManufacturer());
        db.update("drinks", values, "drinkcode = ?", new String[]{getDrinkcode()});
        db.close();
    }

    // Delete a Drink
    public void deleteDrink(String drinkcode) {
        SQLiteDatabase db = drinksDb.getWritableDatabase();
        db.delete("drinks", "drinkcode = ?", new String[]{getDrinkcode()});
        db.close();
    }


}

