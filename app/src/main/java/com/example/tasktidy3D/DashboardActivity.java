package com.example.tasktidy3D;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DatabaseHelper(this);

        Button addButton = findViewById(R.id.floatingActionButton);
        addButton.setOnClickListener(v -> {
            String taskName = "New Task";  // Example static task
            boolean isInserted = dbHelper.insertTask(taskName);
            if (isInserted) {
                Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to Add Task", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
