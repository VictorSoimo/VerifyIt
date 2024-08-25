package com.food.verifyit;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;

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

    //constructor
    public Drinkscanned(Context context) {
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


    public void addDrink() {
        SQLiteDatabase db = drinksDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("code", getDrinkcode());
        values.put("name", getDrinkname());
        values.put("scanned_at", String.valueOf(getTimescanned()));
        values.put("manufacturer", getManufacturer());
        db.insert("drinks", null, values);
        db.close();
    }
    private void showManufacturerDetails(Drinkscanned drink) {
        AlertDialog.Builder builder = new AlertDialog.Builder(drink.getContext());
        builder.setTitle("Manufacturer Details");

        String message = "Product code: " + drink.getDrinkcode() + "\n" +
                "Manufacturer: " + drink.getManufacturer() + "\n" +
                "Product: " + drink.getDrinkname();

        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    // Get all Drinks
    public List<Drinkscanned> getAllDrinks() {
        List<Drinkscanned> drinkList = new ArrayList<>();
        SQLiteDatabase db = drinksDb.getReadableDatabase();
        Cursor cursor = db.query(
                "drinks",
                new String[]{"id", "code", "name", "scanned_at", "manufacturer"},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Drinkscanned drink = new Drinkscanned(drinksDb.getContext());
                drink.setDrinkcode(cursor.getString(cursor.getColumnIndex("code")));
                drink.setDrinkname(cursor.getString(cursor.getColumnIndex("name")));
                drink.setTimescanned(LocalDateTime.now());
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
        values.put("code", getDrinkcode());
        values.put("name", getDrinkname());
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

