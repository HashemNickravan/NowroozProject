package main.db.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super("Can't find Entity.");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(int id) {
        super("Can't find entity with id: " + id);
    }
}