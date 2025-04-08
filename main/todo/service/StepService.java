package main.todo.service;

import main.db.Database;
import main.db.exception.InvalidEntityException;
import main.todo.entity.Step;
import main.todo.entity.Step.Status;

public class StepService {
    public static Step createStep(int taskRef, String title) {
        Step step = new Step();
        step.setTaskRef(taskRef);
        step.setTitle(title);
        step.setStatus(Status.NotStarted);

        try {
            Database.add(step);
            return step;
        } catch (InvalidEntityException e) {
            System.out.println("Cannot save step.");
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}