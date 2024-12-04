package com.example.tasktidy3D;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private DatabaseHelper dbHelper;

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textTaskName.setText(task.getTaskName());
        holder.textTaskPriority.setText("Priority: " + task.getPriority());
        holder.textTaskDescription.setText(task.getDescription());

        // Done button action
        holder.buttonDone.setOnClickListener(v -> {
            // Move task to completed tasks
            boolean moved = dbHelper.moveToCompleted(
                    task.getTaskId(),
                    task.getTaskName(),
                    task.getPriority(),
                    task.getDescription()
            );

            if (moved) {
                // Remove from the current task list
                taskList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Task marked as completed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to mark task as completed", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete button action
        holder.buttonDelete.setOnClickListener(v -> {
            // Delete task from database
            dbHelper.deleteTask(task.getTaskId());

            // Remove task from the list and update RecyclerView
            taskList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
        });
    }




    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textTaskName, textTaskPriority, textTaskDescription;
        Button buttonDone, buttonDelete;

        public TaskViewHolder(View itemView) {
            super(itemView);
            textTaskName = itemView.findViewById(R.id.textTaskName);
            textTaskPriority = itemView.findViewById(R.id.textTaskPriority);
            textTaskDescription = itemView.findViewById(R.id.textTaskDescription);
            buttonDone = itemView.findViewById(R.id.buttonDone);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}


