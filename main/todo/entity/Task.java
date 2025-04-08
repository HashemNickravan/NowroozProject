package main.todo.entity;

import main.db.Entity;
import main.db.Trackable;
import java.util.Date;

public class Task extends Entity implements Trackable {
    public static final int ENTITY_CODE = 1001;

    public enum Status { NotStarted, InProgress, Completed }

    private String title;
    private String description;
    private Date dueDate;
    private Status status = Status.NotStarted;
    private Date creationDate;
    private Date lastModificationDate;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public Date getCreationDate() { return creationDate; }
    @Override
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    @Override
    public Date getLastModificationDate() { return lastModificationDate; }
    @Override
    public void setLastModificationDate(Date lastModificationDate) { this.lastModificationDate = lastModificationDate; }

    @Override
    public int getEntityCode() { return ENTITY_CODE; }

    @Override
    public Entity copy() {
        Task copy = new Task();
        copy.id = this.id;
        copy.title = this.title;
        copy.description = this.description;
        if (this.dueDate != null) copy.dueDate = new Date(this.dueDate.getTime());
        copy.status = this.status;
        if (this.creationDate != null) copy.creationDate = new Date(this.creationDate.getTime());
        if (this.lastModificationDate != null) copy.lastModificationDate = new Date(this.lastModificationDate.getTime());
        return copy;
    }
}