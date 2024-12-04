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
    private TaskAdapter adapter;
    private List<Task> completedTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completedtask);

        dbHelper = new DatabaseHelper(this);
        recyclerViewCompletedTasks = findViewById(R.id.recyclerViewCompletedTasks);

        recyclerViewCompletedTasks.setLayoutManager(new LinearLayoutManager(this));

        loadCompletedTasks();
    }

    private void loadCompletedTasks() {
        completedTaskList = getCompletedTasksFromDatabase();

        if (completedTaskList.isEmpty()) {
            Toast.makeText(this, "No completed tasks available", Toast.LENGTH_SHORT).show();
        }

        adapter = new TaskAdapter(completedTaskList, this);
        recyclerViewCompletedTasks.setAdapter(adapter);
    }

    private List<Task> getCompletedTasksFromDatabase() {
        List<Task> taskList = new ArrayList<>();
        Cursor cursor = dbHelper.getCompletedTasks();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String description = cursor.getString(2);
                    int priority = cursor.getInt(3);

                    taskList.add(new Task(id, name, description, priority, true));
                }
            } finally {
                cursor.close();
            }
        }
        return taskList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCompletedTasks();
    }

}
