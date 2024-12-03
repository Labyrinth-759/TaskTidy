package com.example.tasktidy3D;

public class Task {
    private int id;
    private String name;
    private int priority;
    private String description;

    public Task(int id, String name, int priority, String description) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }
}
