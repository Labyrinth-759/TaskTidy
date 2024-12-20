package com.example.tasktidy3D;

public class Task {
    private int taskId;
    private String taskName;
    private String description;
    private int priority;
    private boolean isDone;

    public Task(int taskId, String taskName, String description, int priority, boolean isDone) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.priority = priority;
        this.isDone = isDone;
    }


    // Getters
    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isDone() {
        return isDone;
    }


    public void setDone(boolean done) {
        isDone = done;
    }
}
