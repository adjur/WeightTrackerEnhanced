package com.andidj.weighttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "weight_tracker.db";
    public static final int DB_VERSION = 1;

    // Users table
    public static final String T_USERS = "users";
    public static final String U_ID = "user_id";
    public static final String U_USERNAME = "username";
    public static final String U_PASSWORD = "password";

    // Weights table
    public static final String T_WEIGHTS = "weights";
    public static final String W_ID = "weight_id";
    public static final String W_DATE = "date";
    public static final String W_WEIGHT = "weight";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_USERS + " (" +
                U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                U_USERNAME + " TEXT UNIQUE NOT NULL, " +
                U_PASSWORD + " TEXT NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE " + T_WEIGHTS + " (" +
                W_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                W_DATE + " TEXT NOT NULL, " +
                W_WEIGHT + " REAL NOT NULL" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + T_WEIGHTS);
        onCreate(db);
    }

    // -------- USERS --------

    public boolean createUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(U_USERNAME, username);
        cv.put(U_PASSWORD, password);
        long result = db.insert(T_USERS, null, cv);
        return result != -1;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT 1 FROM " + T_USERS + " WHERE " + U_USERNAME + "=?",
                new String[]{username}
        );
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public boolean validateLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT 1 FROM " + T_USERS + " WHERE " + U_USERNAME + "=? AND " + U_PASSWORD + "=?",
                new String[]{username, password}
        );
        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }

    // -------- WEIGHTS CRUD --------

    public long addWeight(String dateYYYYMMDD, double weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(W_DATE, dateYYYYMMDD);
        cv.put(W_WEIGHT, weight);
        return db.insert(T_WEIGHTS, null, cv);
    }

    public Cursor getAllWeights() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "SELECT " + W_ID + ", " + W_DATE + ", " + W_WEIGHT +
                        " FROM " + T_WEIGHTS +
                        " ORDER BY " + W_DATE + " DESC, " + W_ID + " DESC",
                null
        );
    }

    public boolean updateWeight(long weightId, String dateYYYYMMDD, double weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(W_DATE, dateYYYYMMDD);
        cv.put(W_WEIGHT, weight);
        int rows = db.update(T_WEIGHTS, cv, W_ID + "=?", new String[]{String.valueOf(weightId)});
        return rows > 0;
    }

    public boolean deleteWeight(long weightId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(T_WEIGHTS, W_ID + "=?", new String[]{String.valueOf(weightId)});
        return rows > 0;
    }
}