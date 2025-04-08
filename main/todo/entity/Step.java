package main.todo.entity;

import main.db.Entity;

public class Step extends Entity {
    public static final int ENTITY_CODE = 1002;

    public enum Status { NotStarted, Completed }

    private String title;
    private Status status = Status.NotStarted;
    private int taskRef;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public int getTaskRef() { return taskRef; }
    public void setTaskRef(int taskRef) { this.taskRef = taskRef; }

    @Override
    public int getEntityCode() { return ENTITY_CODE; }

    @Override
    public Entity copy() {
        Step copy = new Step();
        copy.id = this.id;
        copy.title = this.title;
        copy.status = this.status;
        copy.taskRef = this.taskRef;
        return copy;
    }
}