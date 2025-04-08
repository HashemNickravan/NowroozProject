package main.db;

public abstract class Entity {
    public int id;
    public abstract int getEntityCode();
    public abstract Entity copy();
}