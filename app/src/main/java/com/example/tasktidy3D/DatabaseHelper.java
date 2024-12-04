package com.example.tasktidy3D;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TaskTidyDB";
    private static final int DATABASE_VERSION = 2; // Incremented version for schema change

    // Task Table
    private static final String TABLE_TASKS = "Tasks";
    private static final String COLUMN_TASK_ID = "task_id";
    private static final String COLUMN_TASK_NAME = "task_name";
    private static final String COLUMN_TASK_PRIO = "priority";
    private static final String COLUMN_TASK_DRP = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE Users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_TASK_PRIO + " INTEGER, " +
                COLUMN_TASK_DRP + " TEXT)";
        db.execSQL(CREATE_TASKS_TABLE);
    }



    public boolean AddUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);    // Column name for email
        values.put("password", password);  // Column name for password

        long result = db.insert("Users", null, values); // "Users" table should exist
        db.close();
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Insert Task
    public boolean insertTask(String taskName, int priority, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskName);
        values.put(COLUMN_TASK_PRIO, priority);
        values.put(COLUMN_TASK_DRP, description);

        long result = db.insert(TABLE_TASKS, null, values);
        db.close();
        return result != -1;
    }

    // Get All Tasks
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);
    }




}
