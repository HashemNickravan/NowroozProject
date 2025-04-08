package main;

import main.db.Database;
import main.db.Entity;
import main.db.Trackable;
import main.db.exception.EntityNotFoundException;
import main.db.exception.InvalidEntityException;
import main.todo.entity.Step;
import main.todo.entity.Task;
import main.todo.service.StepService;
import main.todo.service.TaskService;
import main.todo.validator.StepValidator;
import main.todo.validator.TaskValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        initializeValidators();
        printWelcomeMessage();

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            if (command.equals("exit")) {
                break;
            }

            try {
                processCommand(command);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void initializeValidators() {
        try {
            Database.registerValidator(Task.ENTITY_CODE, new TaskValidator());
            Database.registerValidator(Step.ENTITY_CODE, new StepValidator());
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to initialize validators: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void printWelcomeMessage() {
        System.out.println("=== Todo List Manager ===");
        System.out.println("Available commands:");
        System.out.println("add task       - Add a new task");
        System.out.println("add step       - Add a new step to a task");
        System.out.println("delete         - Delete a task or step");
        System.out.println("update task    - Update a task");
        System.out.println("update step    - Update a step");
        System.out.println("get task-by-id - View a specific task");
        System.out.println("get all-tasks  - List all tasks");
        System.out.println("get incomplete-tasks - List incomplete tasks");
        System.out.println("exit           - Exit the program");
        System.out.println("=========================");
    }

    private static void processCommand(String command) throws ParseException, InvalidEntityException {
        switch (command) {
            case "add task":
                handleAddTask();
                break;
            case "add step":
                handleAddStep();
                break;
            case "delete":
                handleDelete();
                break;
            case "update task":
                handleUpdateTask();
                break;
            case "update step":
                handleUpdateStep();
                break;
            case "get task-by-id":
                handleGetTaskById();
                break;
            case "get all-tasks":
                handleGetAllTasks();
                break;
            case "get incomplete-tasks":
                handleGetIncompleteTasks();
                break;
            default:
                System.out.println("Invalid command! Type 'help' for available commands");
        }
    }

    private static void handleAddTask() throws ParseException {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Due date (yyyy-MM-dd): ");
        Date dueDate = parseDate(scanner.nextLine().trim());

        Task task = TaskService.createTask(title, description, dueDate);
        if (task != null) {
            System.out.println("Task saved successfully.");
            System.out.println("ID: " + task.id);
            System.out.println("Creation Date: " + task.getCreationDate());
        }
    }

    private static void handleAddStep() {
        try {
            System.out.print("TaskID: ");
            int taskId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Title: ");
            String title = scanner.nextLine().trim();

            Step step = StepService.createStep(taskId, title);
            if (step != null) {
                System.out.println("Step saved successfully.");
                System.out.println("ID: " + step.id);
                if (step instanceof Trackable) {
                    Trackable trackableStep = (Trackable) step;
                    System.out.println("Creation Date: " + trackableStep.getCreationDate());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Task ID must be a number");
        }
    }

    private static void handleDelete() {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Entity entity = Database.get(id);
            Database.delete(id);

            if (entity instanceof Task) {
                List<Entity> steps = Database.getAll(Step.ENTITY_CODE);
                for (Entity step : steps) {
                    Step s = (Step) step;
                    if (s.getTaskRef() == id) {
                        Database.delete(s.id);
                    }
                }
            }

            System.out.println("Entity with ID=" + id + " successfully deleted.");
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleUpdateTask() {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Task task = (Task) Database.get(id);

            System.out.print("Field (title/description/dueDate/status): ");
            String field = scanner.nextLine().trim().toLowerCase();

            System.out.print("New Value: ");
            String value = scanner.nextLine().trim();

            String oldValue = "";

            switch (field) {
                case "title":
                    oldValue = task.getTitle();
                    task.setTitle(value);
                    break;
                case "description":
                    oldValue = task.getDescription();
                    task.setDescription(value);
                    break;
                case "duedate":
                    Date newDate = parseDate(value);
                    oldValue = dateFormat.format(task.getDueDate());
                    task.setDueDate(newDate);
                    break;
                case "status":
                    Task.Status newStatus = Task.Status.valueOf(value);
                    oldValue = task.getStatus().toString();
                    task.setStatus(newStatus);

                    if (newStatus == Task.Status.Completed) {
                        updateStepsForTask(task.id, Step.Status.Completed);
                    } else if (newStatus == Task.Status.NotStarted) {
                        updateStepsForTask(task.id, Step.Status.NotStarted);
                    }
                    break;
                default:
                    System.out.println("Error: Invalid field name");
                    return;
            }

            Database.update(task);
            System.out.println("Successfully updated the task.");
            System.out.println("Field: " + field);
            System.out.println("Old Value: " + oldValue);
            System.out.println("New Value: " + value);
            System.out.println("Modification Date: " + task.getLastModificationDate());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid value provided");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: Task not found");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleUpdateStep() {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Step step = (Step) Database.get(id);

            System.out.print("Field (title/status): ");
            String field = scanner.nextLine().trim().toLowerCase();

            System.out.print("New Value: ");
            String value = scanner.nextLine().trim();

            String oldValue = "";

            switch (field) {
                case "title":
                    oldValue = step.getTitle();
                    step.setTitle(value);
                    break;
                case "status":
                    Step.Status newStatus = Step.Status.valueOf(value);
                    oldValue = step.getStatus().toString();
                    step.setStatus(newStatus);
                    updateParentTaskStatus(step.getTaskRef());
                    break;
                default:
                    System.out.println("Error: Invalid field name");
                    return;
            }

            Database.update(step);
            System.out.println("Successfully updated the step.");
            System.out.println("Field: " + field);
            System.out.println("Old Value: " + oldValue);
            System.out.println("New Value: " + value);
            if (step instanceof Trackable) {
                Trackable trackableStep = (Trackable) step;
                System.out.println("Modification Date: " + trackableStep.getLastModificationDate());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid status value. Use: NotStarted, Completed");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: Step not found");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateStepsForTask(int taskId, Step.Status status) throws InvalidEntityException {
        List<Entity> steps = Database.getAll(Step.ENTITY_CODE);
        for (Entity entity : steps) {
            Step step = (Step) entity;
            if (step.getTaskRef() == taskId) {
                step.setStatus(status);
                Database.update(step);
            }
        }
    }

    private static void updateParentTaskStatus(int taskId) throws InvalidEntityException {
        try {
            Task task = (Task) Database.get(taskId);
            List<Entity> steps = Database.getAll(Step.ENTITY_CODE);

            boolean allCompleted = true;
            boolean anyInProgress = false;

            for (Entity entity : steps) {
                Step step = (Step) entity;
                if (step.getTaskRef() == taskId) {
                    if (step.getStatus() != Step.Status.Completed) {
                        allCompleted = false;
                    }
                    if (step.getStatus() == Step.Status.Completed && task.getStatus() == Task.Status.NotStarted) {
                        anyInProgress = true;
                    }
                }
            }

            if (allCompleted) {
                task.setStatus(Task.Status.Completed);
            } else if (anyInProgress) {
                task.setStatus(Task.Status.InProgress);
            }

            Database.update(task);
        } catch (EntityNotFoundException e) {
            throw new InvalidEntityException("Parent task not found");
        }
    }

    private static Date parseDate(String dateStr) throws ParseException {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new ParseException("Invalid date format. Please use yyyy-MM-dd", 0);
        }
    }

    private static void handleGetTaskById() {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Task task = (Task) Database.get(id);
            printTaskDetails(task);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleGetAllTasks() {
        List<Entity> tasks = Database.getAll(Task.ENTITY_CODE);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        Collections.sort(tasks, Comparator.comparing(t -> ((Task) t).getDueDate()));
        System.out.println("\n=== All Tasks ===");
        for (Entity entity : tasks) {
            printTaskSummary((Task) entity);
            System.out.println();
        }
    }

    private static void handleGetIncompleteTasks() {
        List<Entity> tasks = Database.getAll(Task.ENTITY_CODE);
        tasks.removeIf(t -> ((Task) t).getStatus() == Task.Status.Completed);

        if (tasks.isEmpty()) {
            System.out.println("No incomplete tasks found.");
            return;
        }

        Collections.sort(tasks, Comparator.comparing(t -> ((Task) t).getDueDate()));
        System.out.println("\n=== Incomplete Tasks ===");
        for (Entity entity : tasks) {
            printTaskSummary((Task) entity);
            System.out.println();
        }
    }

    private static void printTaskDetails(Task task) {
        System.out.println("\n=== Task Details ===");
        System.out.println("ID: " + task.id);
        System.out.println("Title: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Due Date: " + dateFormat.format(task.getDueDate()));
        System.out.println("Status: " + task.getStatus());
        System.out.println("Created: " + task.getCreationDate());
        System.out.println("Last Modified: " + task.getLastModificationDate());

        List<Entity> steps = Database.getAll(Step.ENTITY_CODE);
        if (!steps.isEmpty()) {
            System.out.println("\nSteps:");
            for (Entity entity : steps) {
                Step step = (Step) entity;
                if (step.getTaskRef() == task.id) {
                    System.out.println("  + " + step.getTitle() + ":");
                    System.out.println("    ID: " + step.id);
                    System.out.println("    Status: " + step.getStatus());
                    if (step instanceof Trackable) {
                        Trackable trackableStep = (Trackable) step;
                        System.out.println("    Created: " + trackableStep.getCreationDate());
                    }
                }
            }
        }
    }

    private static void printTaskSummary(Task task) {
        System.out.println("ID: " + task.id);
        System.out.println("Title: " + task.getTitle());
        System.out.println("Due Date: " + dateFormat.format(task.getDueDate()));
        System.out.println("Status: " + task.getStatus());

        List<Entity> steps = Database.getAll(Step.ENTITY_CODE);
        boolean hasSteps = false;
        for (Entity entity : steps) {
            Step step = (Step) entity;
            if (step.getTaskRef() == task.id) {
                if (!hasSteps) {
                    System.out.println("Steps:");
                    hasSteps = true;
                }
                System.out.println("  + " + step.getTitle() + " (" + step.getStatus() + ")");
            }
        }
    }
}