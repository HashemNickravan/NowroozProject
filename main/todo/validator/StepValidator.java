package main.todo.validator;

import main.db.Entity;
import main.db.Validator;
import main.db.Database;
import main.db.exception.EntityNotFoundException;
import main.todo.entity.Step;
import main.todo.entity.Task;
import main.db.exception.InvalidEntityException;

public class StepValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Step)) {
            throw new InvalidEntityException("Entity must be of type Step");
        }

        Step step = (Step) entity;

        if (step.getTitle() == null || step.getTitle().trim().isEmpty()) {
            throw new InvalidEntityException("Step title cannot be empty");
        }

        try {
            Task task = (Task) Database.get(step.getTaskRef());
            if (task == null || task.getEntityCode() != Task.ENTITY_CODE) {
                throw new InvalidEntityException("Referenced task is invalid");
            }
        } catch (EntityNotFoundException e) {
            throw new InvalidEntityException("Referenced task does not exist");
        }
    }
}