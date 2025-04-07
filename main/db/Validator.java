package main.db;

import main.db.exception.InvalidEntityException;

public interface Validator {
    void validate(Entity entity) throws InvalidEntityException;
}