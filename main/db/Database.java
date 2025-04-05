package main.db;

import main.db.exception.EntityNotFoundException;
import main.db.exception.InvalidEntityException;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int lastId = 0;
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator already exists for entity code: " + entityCode);
        }
        validators.put(entityCode, validator);
    }

    public static void add(Entity e) {
        Validator validator = validators.get(e.getEntityCode());
        if (validator == null) {
            throw new IllegalStateException("No validator registered for entity code: " + e.getEntityCode());
        }
        validator.validate(e);

        lastId++;
        e.id = lastId;
        entities.add(e.copy());
    }

    public static void update(Entity e) {
        Validator validator = validators.get(e.getEntityCode());
        if (validator == null) {
            throw new IllegalStateException("No validator registered for entity code: " + e.getEntityCode());
        }
        validator.validate(e);

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                entities.set(i, e.copy());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }
}