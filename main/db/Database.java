package main.db;

import main.db.exception.EntityNotFoundException;
import main.db.exception.InvalidEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

    public static void add(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());

        if (validator != null) {
            validator.validate(e);
        }

        if (e instanceof Trackable) {
            Trackable trackable = (Trackable) e;
            Date now = new Date();
            trackable.setCreationDate(now);
            trackable.setLastModificationDate(now);
        }

        lastId++;
        e.id = lastId;
        entities.add(e.copy());
    }

    public static void update(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());

        if (validator != null) {
            validator.validate(e);
        }

        if (e instanceof Trackable) {
            Trackable trackable = (Trackable) e;
            trackable.setLastModificationDate(new Date());
        }

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                entities.set(i, e.copy());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }

    public static List<Entity> getAll(int entityCode) {
        List<Entity> result = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getEntityCode() == entityCode) {
                result.add(entity.copy());
            }
        }
        return result;
    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity entity : entities) {
            if (entity.id == id) {
                return entity.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException {
        Entity toRemove = null;
        for (Entity entity : entities) {
            if (entity.id == id) {
                toRemove = entity;
                break;
            }
        }
        if (toRemove != null) {
            entities.remove(toRemove);
        } else {
            throw new EntityNotFoundException(id);
        }
    }
}