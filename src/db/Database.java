package db;

import db.exception.EntityNotFoundException;
import java.util.ArrayList;

public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();

    private Database() {}

    public static void add(Entity e) {
        e.id = entities.size() + 1;
        entities.add(e);
    }

    public static Entity get(int id) {
        for (Entity entity : entities) {
            if (entity.id == id) return entity;
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) {
        Entity entity = get(id);
        entities.remove(entity);
    }

    public static void update(Entity e) {
        Entity existing = get(e.id);
        int index = entities.indexOf(existing);
        entities.set(index, e);
    }
}