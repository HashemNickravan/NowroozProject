package main.todo.service;

import database.Database;
import todo.entity.Task;
import todo.entity.Task.Status;
import java.util.ArrayList;
import java.util.Date;

public class TaskService {
    public static Task createTask(String title, String description, Date dueDate) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setStatus(Status.NotStarted);

        try {
            Database.save(task, new TaskValidator());
            return task;
        } catch (Exception e) {
            System.out.println("Cannot save task.");
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static void setAsCompleted(int taskId) {
        Task task = (Task) Database.get(taskId);
        if (task != null) {
            task.setStatus(Status.Completed);
            Database.update(task);
        }
    }

}