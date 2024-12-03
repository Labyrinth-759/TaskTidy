package com.example.tasktidy3D;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        dbHelper = new DatabaseHelper(this);
        TextView taskView = findViewById(R.id.textView3);

        Cursor cursor = dbHelper.getAllTasks();
        StringBuilder tasks = new StringBuilder();
        while (cursor.moveToNext()) {
            tasks.append(cursor.getString(1)).append("\n");
        }
        taskView.setText(tasks.toString());
    }
}
