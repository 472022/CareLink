package com.example.carelink;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "users.db";
    private static final int DATABASE_VERSION = 2;  // Incremented version number

    public DBHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE users(name TEXT, age INTEGER, gender TEXT, weight REAL, height REAL, is_logged_in INTEGER DEFAULT 0)");
        MyDB.execSQL("CREATE TABLE login(email TEXT PRIMARY KEY, password TEXT)");
        MyDB.execSQL("CREATE TABLE health_data(date TEXT PRIMARY KEY, calories INTEGER, water INTEGER, steps INTEGER, sleep INTEGER)");

        // Create tables for diet plan and workout plan
        MyDB.execSQL("CREATE TABLE diet_plan(id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, meal_type TEXT, food_items TEXT)");
        MyDB.execSQL("CREATE TABLE workout_plan(id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, exercise_name TEXT, reps INTEGER, sets INTEGER, is_completed INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add the is_logged_in column to the users table
            MyDB.execSQL("ALTER TABLE users ADD COLUMN is_logged_in INTEGER DEFAULT 0");
        }
        // Add other upgrade logic if needed
    }

    // Method to set a user as logged in
    public void setUserLoggedIn(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_logged_in", 1);
        db.update("users", values, "name = ?", new String[]{userName});
    }

    // Method to log out a user
    public void logOutUser(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_logged_in", 0);
        db.update("users", values, "name = ?", new String[]{userName});
    }

    // Method to get the name of the logged-in user
    public String getLoggedInUserName() {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName = null;
        Cursor cursor = db.rawQuery("SELECT name FROM users WHERE is_logged_in = 1", null);
        if (cursor.moveToFirst()) {
            userName = cursor.getString(0);
        }
        cursor.close();
        return userName;
    }

    // Insert user data (excluding email and password)
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

    // Method to insert a diet plan for a specific user and goal
    public Boolean insertDietPlan(String userName, String goal) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Sample diet plans based on goals
        String mealType;
        String foodItems;

        switch (goal.toLowerCase()) {
            case "weight gain":
                mealType = "Breakfast";
                foodItems = "Oats, Milk, Banana";
                break;
            case "weight loss":
                mealType = "Lunch";
                foodItems = "Grilled Chicken, Salad";
                break;
            case "bulk":
                mealType = "Dinner";
                foodItems = "Steak, Sweet Potato";
                break;
            case "hair loss":
                mealType = "Snacks";
                foodItems = "Nuts, Berries";
                break;
            case "boost immunity":
                mealType = "Breakfast";
                foodItems = "Citrus Fruits, Yogurt";
                break;
            default:
                return false; // If goal is not recognized
        }

        contentValues.put("user_name", userName);
        contentValues.put("meal_type", mealType);
        contentValues.put("food_items", foodItems);

        long result = MyDB.insert("diet_plan", null, contentValues);
        return result != -1;
    }

    // Method to insert a workout plan for a specific user and goal
    public Boolean insertWorkoutPlan(String userName, String goal) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Sample workout plans based on goals
        String exerciseName;
        int reps;
        int sets;

        switch (goal.toLowerCase()) {
            case "weight gain":
                exerciseName = "Squats";
                reps = 10;
                sets = 4;
                break;
            case "weight loss":
                exerciseName = "Running";
                reps = 30; // in minutes
                sets = 1;
                break;
            case "bulk":
                exerciseName = "Bench Press";
                reps = 8;
                sets = 4;
                break;
            case "hair loss":
                exerciseName = "Scalp Massage";
                reps = 10; // in minutes
                sets = 1;
                break;
            case "boost immunity":
                exerciseName = "Yoga";
                reps = 15; // in minutes
                sets = 1;
                break;
            default:
                return false; // If goal is not recognized
        }

        contentValues.put("user_name", userName);
        contentValues.put("exercise_name", exerciseName);
        contentValues.put("reps", reps);
        contentValues.put("sets", sets);

        long result = MyDB.insert("workout_plan", null, contentValues);
        return result != -1;
    }
    public int[] getTaskCompletionStats() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to count total tasks
        Cursor totalTasksCursor = db.rawQuery("SELECT COUNT(*) FROM workout_plan", null);
        int totalTasks = 0;
        if (totalTasksCursor.moveToFirst()) {
            totalTasks = totalTasksCursor.getInt(0);
        }
        totalTasksCursor.close();

        // Query to count completed tasks
        Cursor completedTasksCursor = db.rawQuery("SELECT COUNT(*) FROM workout_plan WHERE is_completed = 1", null);
        int completedTasks = 0;
        if (completedTasksCursor.moveToFirst()) {
            completedTasks = completedTasksCursor.getInt(0);
        }
        completedTasksCursor.close();

        return new int[]{totalTasks, completedTasks};
    }

}
