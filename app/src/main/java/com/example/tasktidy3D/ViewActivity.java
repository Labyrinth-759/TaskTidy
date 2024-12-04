package com.example.tasktidy3D;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerViewTasks;
    private ActivityResultLauncher<Intent> addTaskLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        dbHelper = new DatabaseHelper(this);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        // Set RecyclerView LayoutManager
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        // Set up ActivityResultLauncher for AddTaskActivity
        addTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Reload tasks after a new task is added
                List<Task> taskList = getTasksFromDatabase();
                TaskAdapter adapter = new TaskAdapter(taskList, this);
                recyclerViewTasks.setAdapter(adapter);
            }
        });

        // Set up FloatingActionButton for adding new task
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> {
            // Launch AddTaskActivity to add a new task
            Intent intent = new Intent(ViewActivity.this, AddtaskActivity.class);
            addTaskLauncher.launch(intent);
        });

        // Load tasks from database and display in RecyclerView
        List<Task> taskList = getTasksFromDatabase();
        TaskAdapter adapter = new TaskAdapter(taskList, this);
        recyclerViewTasks.setAdapter(adapter);
    }

    private List<Task> getTasksFromDatabase() {
        List<Task> taskList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllTasks();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int priority = cursor.getInt(3);
            boolean isDone = cursor.getInt(4) == 1;

            taskList.add(new Task(id, name, description, priority, isDone));
        }
        cursor.close();
        return taskList;
    }

}

