package main.todo.service;

import database.Database;
import todo.entity.Step;
import todo.entity.Step.Status;
import todo.entity.Task;
import java.util.Date;

public class StepService {
    public static Step createStep(int taskRef, String title) {
        Step step = new Step();
        step.setTaskRef(taskRef);
        step.setTitle(title);
        step.setStatus(Status.NotStarted);

        try {
            Database.save(step, new StepValidator());
            return step;
        } catch (Exception e) {
            System.out.println("Cannot save step.");
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Other methods will be added here
}