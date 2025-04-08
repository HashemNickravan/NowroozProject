package main.todo.service;

import main.db.Database;
import main.db.exception.EntityNotFoundException;
import main.db.exception.InvalidEntityException;
import main.todo.entity.Task;
import main.todo.entity.Task.Status;
import java.util.Date;

public class TaskService {
    public static Task createTask(String title, String description, Date dueDate) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setStatus(Status.NotStarted);

        try {
            Database.add(task);
            return task;
        } catch (InvalidEntityException e) {
            System.out.println("Cannot save task.");
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static void setAsCompleted(int taskId) {
        try {
            Task task = (Task) Database.get(taskId);
            task.setStatus(Status.Completed);
            Database.update(task);
        } catch (EntityNotFoundException e) {
            System.out.println("Error: Task not found with ID: " + taskId);
        } catch (InvalidEntityException e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
    }
}