package com.example.tasktidy3D;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mobileapp.tasktidy.R;

public class AddtaskActivity extends AppCompatActivity {

    private EditText newtask, priorety, taskdesc;
    private Button addbtn;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        // Initialize UI components
        newtask = findViewById(R.id.enternewtask);
        priorety = findViewById(R.id.setprio);
        taskdesc = findViewById(R.id.taskDescription);
        addbtn = findViewById(R.id.btnTask);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Set onClickListener for the button
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskToDatabase();
            }
        });
    }

    private void addTaskToDatabase() {
        // Get input from EditText fields
        String taskName = newtask.getText().toString().trim();
        String priorityStr = priorety.getText().toString().trim();
        String description = taskdesc.getText().toString().trim();

        // Validate input
        if (taskName.isEmpty() || priorityStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert priority to Integer
        int priority;
        try {
            priority = Integer.parseInt(priorityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Priority must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the logged-in user's email from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userEmail = preferences.getString("user_email", null);

        if (userEmail == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert task into the database with the user's email
        boolean isInserted = dbHelper.insertTask(taskName, priority, description, userEmail);

        if (isInserted) {
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            // Clear input fields after successful insertion
            newtask.setText("");
            priorety.setText("");
            taskdesc.setText("");
        } else {
            Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
        }
    }
}
