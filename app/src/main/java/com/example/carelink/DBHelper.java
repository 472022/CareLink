package com.example.carelink;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "users.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE users(name TEXT, age INTEGER, gender TEXT, weight REAL, height REAL)");
        MyDB.execSQL("CREATE TABLE login(email TEXT PRIMARY KEY, password TEXT)");

        // Create table for health data (calories, water, steps, sleep)
        MyDB.execSQL("CREATE TABLE health_data(date TEXT PRIMARY KEY, calories INTEGER, water INTEGER, steps INTEGER, sleep INTEGER)");

        // Example of inserting a demo record into the users table
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "John Doe");
        contentValues.put("age", 30);
        contentValues.put("gender", "Male");
        contentValues.put("weight", 70.5);
        contentValues.put("height", 175.0);

        long result = MyDB.insert("users", null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
        MyDB.execSQL("DROP TABLE IF EXISTS login");
        MyDB.execSQL("DROP TABLE IF EXISTS health_data");
        onCreate(MyDB);
    }

    // Method to insert user data (excluding email and password)
    public Boolean insertUserData(String name, int age, String gender, double weight, double height) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("age", age);
        contentValues.put("gender", gender);
        contentValues.put("weight", weight);
        contentValues.put("height", height);
        long result = MyDB.insert("users", null, contentValues);
        return result != -1;
    }

    // Method to register user login (storing email and password)
    public Boolean registerUser(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDB.insert("login", null, contentValues);
        return result != -1;
    }

    // Method to check user login credentials
    public Boolean checkUser(String email, String password) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM login WHERE email = ? AND password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    // Example method to retrieve all user data
    public Cursor getUserData() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.rawQuery("SELECT * FROM users", null);
    }

    // Method to insert health data
    public Boolean insertHealthData(String date, int calories, int water, int steps, int sleep) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("calories", calories);
        contentValues.put("water", water);
        contentValues.put("steps", steps);
        contentValues.put("sleep", sleep);
        long result = MyDB.insert("health_data", null, contentValues);
        return result != -1;
    }

    // Method to update health data for a specific date
    public Boolean updateHealthData(String date, int calories, int water, int steps, int sleep) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("calories", calories);
        contentValues.put("water", water);
        contentValues.put("steps", steps);
        contentValues.put("sleep", sleep);
        long result = MyDB.update("health_data", contentValues, "date = ?", new String[]{date});
        return result != -1;
    }

    // Method to retrieve health data for a specific date
    public Cursor getHealthData(String date) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.rawQuery("SELECT * FROM health_data WHERE date = ?", new String[]{date});
    }

    // Method to retrieve all health data
    public Cursor getAllHealthData() {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.rawQuery("SELECT * FROM health_data", null);
    }
}
