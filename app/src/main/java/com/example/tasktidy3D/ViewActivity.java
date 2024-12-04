package com.example.tasktidy3D;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private FloatingActionButton fabAddTask;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter adapter;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        fabAddTask = findViewById(R.id.fabAddTask);

        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(ViewActivity.this, AddtaskActivity.class);
            startActivity(intent);
        });

        dbHelper = new DatabaseHelper(this);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        loadTasks();
    }

    private void loadTasks() {
        taskList = getTasksFromDatabase();
        adapter = new TaskAdapter(taskList, this);
        recyclerViewTasks.setAdapter(adapter);
    }

    private List<Task> getTasksFromDatabase() {
        List<Task> tasks = new ArrayList<>();
        // Retrieve tasks from the database
        Cursor cursor = dbHelper.getAllTasks();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String description = cursor.getString(2);
                    int priority = cursor.getInt(3);
                    boolean isDone = cursor.getInt(4) == 1;

                    tasks.add(new Task(id, name, description, priority, isDone));
                }
            } finally {
                cursor.close();
            }
        }
        return tasks;
    }

    // Method to mark task as done and move to completed tasks
    public void markTaskAsDoneAndShowCompleted(Task task) {
        // Move the task to completed tasks and update the database
        boolean result = dbHelper.moveToCompleted(task.getTaskId(), task.getTaskName(), task.getPriority(), task.getDescription());
        if (result) {
            Toast.makeText(this, "Task marked as done", Toast.LENGTH_SHORT).show();
            loadTasks();
            startActivity(new Intent(ViewActivity.this, CompletedActivity.class));
        } else {
            Toast.makeText(this, "Error marking task as completed", Toast.LENGTH_SHORT).show();
        }
    }
}
