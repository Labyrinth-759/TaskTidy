package com.example.tasktidy3D;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.textTaskName.setText(task.getTaskName());
        holder.textTaskPriority.setText("Priority: " + task.getPriority());
        holder.textTaskDescription.setText(task.getDescription());

        if (task.isDone()) {
            holder.buttonDone.setVisibility(View.GONE);
            holder.buttonDone.setEnabled(false);
        } else {
            holder.buttonDone.setVisibility(View.VISIBLE);
            holder.buttonDone.setEnabled(true);
            holder.buttonDone.setOnClickListener(v -> {
                // Move task to completed when the "Done" button is clicked
                if (context instanceof ViewActivity) {
                    ((ViewActivity) context).markTaskAsDoneAndShowCompleted(task);
                } else {
                    Toast.makeText(context, "Context is not ViewActivity", Toast.LENGTH_SHORT).show();
                }
            });
        }

        holder.buttonDelete.setOnClickListener(v -> {
            dbHelper.deleteTask(task.getTaskId());
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
