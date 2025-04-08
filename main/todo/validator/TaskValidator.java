package main.todo.validator;

import main.db.Validator;
import main.db.Entity;
import main.todo.entity.Task;
import main.db.exception.InvalidEntityException;

public class TaskValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Task)) {
            throw new InvalidEntityException("Entity must be of type Task");
        }

        Task task = (Task) entity;

        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new InvalidEntityException("Task title cannot be empty");
        }

        if (task.getDueDate() == null) {
            throw new InvalidEntityException("Due date is required");
        }

        if (task.getStatus() == null) {
            throw new InvalidEntityException("Status is required");
        }
    }
}