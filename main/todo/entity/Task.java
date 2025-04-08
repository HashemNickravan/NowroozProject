package main.todo.entity;

import java.util.Date;
import database.Entity;
import todo.Trackable;

public class Task extends Entity implements Trackable {
    public enum Status {
        NotStarted, InProgress, Completed
    }

    private String title;
    private String description;
    private Date dueDate;
    private Status status;

    public Task() {
        this.status = Status.NotStarted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}