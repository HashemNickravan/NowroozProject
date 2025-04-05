package main.example;

import main.db.Entity;

public class Human extends Entity {
    public static final int HUMAN_ENTITY_CODE = 14;
    public String name;
    public int age;

    public Human(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public Human copy() {
        Human copy = new Human(this.name, this.age);
        copy.id = this.id;
        return copy;
    }

    @Override
    public int getEntityCode() {
        return HUMAN_ENTITY_CODE;
    }
}