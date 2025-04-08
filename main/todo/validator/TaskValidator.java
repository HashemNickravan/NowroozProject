package main.todo.validator;

import database.Validator;
import todo.entity.Task;
import java.util.Date;

public class TaskValidator implements Validator {
    @Override
    public void validate(Object entity) throws IllegalArgumentException {
        if (!(entity instanceof Task)) {
            throw new IllegalArgumentException("Entity must be of type Task");
        }

        Task task = (Task) entity;

        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
    }
}