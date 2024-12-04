package com.example.tasktidy3D;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TaskTidyDB";
    private static final int DATABASE_VERSION = 4;

    // Task Table
    private static final String TABLE_TASKS = "Tasks";
    static final String COLUMN_TASK_ID = "task_id";
    static final String COLUMN_TASK_NAME = "task_name";
    static final String COLUMN_TASK_PRIO = "priority";
    static final String COLUMN_TASK_DRP = "description";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_IS_DONE = "is_done";

    // Completed Task Table
    private static final String TABLE_COMPLETED_TASKS = "CompletedTasks";
    public static final String COLUMN_COMPLETED_TASK_ID = "task_id";
    public static final String COLUMN_COMPLETED_TASK_NAME = "task_name";
    public static final String COLUMN_COMPLETED_TASK_PRIO = "priority";
    public static final String COLUMN_COMPLETED_TASK_DRP = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "password TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Tasks Table with user_email and is_done column
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_TASK_PRIO + " INTEGER, " +
                COLUMN_TASK_DRP + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_IS_DONE + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TASKS_TABLE);

        // Create CompletedTasks Table with user_email
        String CREATE_COMPLETED_TASKS_TABLE = "CREATE TABLE " + TABLE_COMPLETED_TASKS + " (" +
                COLUMN_COMPLETED_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COMPLETED_TASK_NAME + " TEXT, " +
                COLUMN_COMPLETED_TASK_PRIO + " INTEGER, " +
                COLUMN_COMPLETED_TASK_DRP + " TEXT)";
        db.execSQL(CREATE_COMPLETED_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_IS_DONE + " INTEGER DEFAULT 0;");
            } catch (Exception e) {
                Log.e("Database Upgrade", "Error upgrading database to version 4: " + e.getMessage());
            }
        }
    }

    public boolean AddUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);

        long result = db.insert("Users", null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ? AND password = ?", new String[]{email, password});

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            if (cursor != null) cursor.close();
            return false;
        }
    }

    public Cursor getUserTasks(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_USER_EMAIL + " = ?", new String[]{userEmail});
    }

    public boolean insertTask(String taskName, int priority, String description, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskName);
        values.put(COLUMN_TASK_PRIO, priority);
        values.put(COLUMN_TASK_DRP, description);
        values.put(COLUMN_USER_EMAIL, userEmail);

        long result = db.insert(TABLE_TASKS, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);
    }

    public boolean moveToCompleted(int taskId, String taskName, int priority, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert the task into CompletedTasks table
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED_TASK_ID, taskId);
        values.put(COLUMN_COMPLETED_TASK_NAME, taskName);
        values.put(COLUMN_COMPLETED_TASK_PRIO, priority);
        values.put(COLUMN_COMPLETED_TASK_DRP, description);

        long result = db.insert(TABLE_COMPLETED_TASKS, null, values);
        if (result == -1) {
            db.close();
            return false;
        }

        // Update the task to be marked as completed in Tasks table
        ContentValues updateValues = new ContentValues();
        updateValues.put(COLUMN_IS_DONE, 1);
        int rowsUpdated = db.update(TABLE_TASKS, updateValues, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});

        // Delete the task from the Tasks table
        int rowsDeleted = db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});

        db.close();
        return rowsUpdated > 0 && rowsDeleted > 0;
    }


    public void markTaskAsDone(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_DONE, 1);

        int rowsUpdated = db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});

        if (rowsUpdated > 0) {
            Log.d("DatabaseHelper", "Task with ID " + taskId + " marked as done.");
        } else {
            Log.e("DatabaseHelper", "Failed to mark task with ID " + taskId + " as done.");
        }

        db.close();
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(TABLE_COMPLETED_TASKS, COLUMN_COMPLETED_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});

        db.close();

        if (rowsDeleted > 0) {
            Log.d("Database", "Task deleted successfully");
        } else {
            Log.d("Database", "Failed to delete task");
        }
    }


    public Cursor getCompletedTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_COMPLETED_TASKS, null);
    }
}
