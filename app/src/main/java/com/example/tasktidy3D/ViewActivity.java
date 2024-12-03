package com.example.tasktidy3D;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerViewTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        dbHelper = new DatabaseHelper(this);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        // Load tasks from database
        List<Task> taskList = getTasksFromDatabase();

        // Set up RecyclerView
        TaskAdapter adapter = new TaskAdapter(taskList);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.setAdapter(adapter);
    }

    private List<Task> getTasksFromDatabase() {
        List<Task> taskList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllTasks();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int priority = cursor.getInt(2);
            String description = cursor.getString(3);
            taskList.add(new Task(id, name, priority, description));
        }
        cursor.close();
        return taskList;
    }
}
