package main.example;

import main.db.Entity;
import main.db.Validator;
import main.db.exception.InvalidEntityException;

public class HumanValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Human)) {
            throw new IllegalArgumentException("Entity is not a Human");
        }
        Human human = (Human) entity;
        if (human.age < 0) {
            throw new InvalidEntityException("Age must be greater than or equal to zero");
        }
        if (human.name == null || human.name.trim().isEmpty()) {
            throw new InvalidEntityException("Name cannot be null or empty");
        }
    }
}