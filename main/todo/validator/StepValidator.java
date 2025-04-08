package main.todo.validator;

import database.Validator;
import database.Database;
import todo.entity.Step;
import todo.entity.Task;

public class StepValidator implements Validator {
    @Override
    public void validate(Object entity) throws IllegalArgumentException {
        if (!(entity instanceof Step)) {
            throw new IllegalArgumentException("Entity must be of type Step");
        }

        Step step = (Step) entity;

        if (step.getTitle() == null || step.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Step title cannot be empty");
        }

        // Check if referenced task exists
        Task task = (Task) Database.get(step.getTaskRef());
        if (task == null) {
            throw new IllegalArgumentException("Cannot find task with ID=" + step.getTaskRef());
        }
    }
}