package com.example.tasktidy3D;

import static com.example.tasktidy3D.DatabaseHelper.COLUMN_TASK_DRP;
import static com.example.tasktidy3D.DatabaseHelper.COLUMN_TASK_ID;
import static com.example.tasktidy3D.DatabaseHelper.COLUMN_TASK_NAME;
import static com.example.tasktidy3D.DatabaseHelper.COLUMN_TASK_PRIO;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private Button completedTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);

        TextView viewTaskButton = findViewById(R.id.viewtask);
        viewTaskButton.setOnClickListener(v -> {
            // Intent to navigate to ViewActivity
            Intent intent = new Intent(DashboardActivity.this, ViewActivity.class);
            startActivity(intent);
        });

        completedTaskButton = findViewById(R.id.btnCompleted);
        completedTaskButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(DashboardActivity.this, CompletedActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(DashboardActivity.this, "Error opening Completed Activity", Toast.LENGTH_SHORT).show();
            }
        });


    }




    private List<Task> cursorToTaskList(Cursor cursor) {
        List<Task> taskList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String taskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DRP));
                int priority = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_PRIO));
                int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID));

                Task task = new Task(taskId, taskName, taskDescription, priority, false);

                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_about:
                startActivity(new Intent(DashboardActivity.this, AboutActivity.class));
                return true;
            case R.id.menu_developers:
                startActivity(new Intent(DashboardActivity.this, DeveloperActivity.class));
                return true;
            case R.id.menu_exit:
                // Show confirmation dialog
                showExitConfirmationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Method to show the exit confirmation dialog
    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }

}
