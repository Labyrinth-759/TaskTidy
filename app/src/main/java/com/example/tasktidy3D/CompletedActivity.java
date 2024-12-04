package com.example.tasktidy3D;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CompletedActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerViewCompletedTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completedtask);

        dbHelper = new DatabaseHelper(this);
        recyclerViewCompletedTasks = findViewById(R.id.recyclerViewCompletedTasks);

        // Load completed tasks from the database
        List<Task> completedTaskList = getCompletedTasksFromDatabase();

        if (completedTaskList.isEmpty()) {
            Toast.makeText(this, "No completed tasks available", Toast.LENGTH_SHORT).show();
        }

        // Set up RecyclerView
        TaskAdapter adapter = new TaskAdapter(completedTaskList, this);
        recyclerViewCompletedTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompletedTasks.setAdapter(adapter);
    }

    private List<Task> getCompletedTasksFromDatabase() {
        List<Task> taskList = new ArrayList<>();

        try {
            Cursor cursor = dbHelper.getCompletedTasks();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String description = cursor.getString(2);
                    int priority = cursor.getInt(3);

                    taskList.add(new Task(id, name, description, priority, true));
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception
            Toast.makeText(this, "Error loading tasks", Toast.LENGTH_SHORT).show();
        }

        return taskList;
    }
}
