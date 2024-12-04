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
    private static final int DATABASE_VERSION = 3;

    // Task Table
    private static final String TABLE_TASKS = "Tasks";
    static final String COLUMN_TASK_ID = "task_id";
    static final String COLUMN_TASK_NAME = "task_name";
    static final String COLUMN_TASK_PRIO = "priority";
    static final String COLUMN_TASK_DRP = "description";
    private static final String COLUMN_USER_EMAIL = "user_email";

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

        // Create Tasks Table with user_email
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_TASK_PRIO + " INTEGER, " +
                COLUMN_TASK_DRP + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT)";
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
        if (oldVersion < 3) {
            try {
                Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_TASKS + ")", null);
                boolean columnExists = false;

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String columnName = cursor.getString(cursor.getColumnIndex("name"));
                        if (COLUMN_USER_EMAIL.equals(columnName)) {
                            columnExists = true;
                            break;
                        }
                    } while (cursor.moveToNext());
                    cursor.close();
                }

                if (!columnExists) {
                    db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_USER_EMAIL + " TEXT;");
                }
            } catch (Exception e) {
                Log.e("Database Upgrade", "Error upgrading database to version 3: " + e.getMessage());
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

        // Check if the cursor has any rows, indicating a match for email and password
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


    // Add task to the Tasks table with user email
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

    // Query to get tasks from the Tasks table
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);
    }


    // Method to move task to CompletedTasks table
    public boolean moveToCompleted(int taskId, String taskName, int priority, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert task into CompletedTasks table
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

        // Delete the task from the Tasks table
        int rowsDeleted = db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
        return rowsDeleted > 0;
    }

    public void updateTaskStatus(int taskId, boolean isDone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", isDone ? 1 : 0);  // Assuming "status" is 1 for done, 0 for not
        db.update("tasks", values, "id = ?", new String[]{String.valueOf(taskId)});
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();

        // Check if the task was deleted successfully
        if (rowsDeleted > 0) {
            Log.d("Database", "Task deleted successfully");
        } else {
            Log.d("Database", "Failed to delete task");
        }
    }


    public Cursor getCompletedTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM tasks WHERE status = 1"; // Assuming 1 means completed
        return db.rawQuery(query, null);
    }



}
