package main;

import main.db.Database;
import main.db.exception.InvalidEntityException;
import main.example.Human;
import main.example.HumanValidator;

public class Main {
    public static void main(String[] args) throws InvalidEntityException {
        Database.registerValidator(Human.HUMAN_ENTITY_CODE, new HumanValidator());

        Human ali = new Human("Ali", -10);
        Database.add(ali);
    }
}